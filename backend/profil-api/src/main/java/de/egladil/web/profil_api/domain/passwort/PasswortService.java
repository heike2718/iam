// =====================================================
// Project: profil-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_api.domain.passwort;

import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.profil_api.domain.auth.dto.MessagePayload;
import de.egladil.web.profil_api.domain.auth.dto.OAuthClientCredentials;
import de.egladil.web.profil_api.domain.auth.dto.ResponsePayload;
import de.egladil.web.profil_api.domain.exceptions.ConflictException;
import de.egladil.web.profil_api.domain.exceptions.LogmessagePrefixes;
import de.egladil.web.profil_api.domain.exceptions.ProfilserverRuntimeException;
import de.egladil.web.profil_api.infrastructure.restclient.AuthproviderRestClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

/**
 * PasswortService
 */
@ApplicationScoped
public class PasswortService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PasswortService.class);

	@ConfigProperty(name = "public-client-id")
	String clientId;

	@ConfigProperty(name = "public-client-secret")
	String clientSecret;

	@Context
	SecurityContext securityContext;

	@Inject
	@RestClient
	AuthproviderRestClient authproviderRestClient;

	/**
	 * Sendet die Payload an die authprovider-API und bekommt von dort eine Message zurück.
	 *
	 * @param  payload
	 * @return         MessagePayload
	 */
	public MessagePayload passwortAendern(final PasswortPayload passwortPayload) {

		String uuid = securityContext.getUserPrincipal().getName();
		String expectedNonce = UUID.randomUUID().toString();

		OAuthClientCredentials credentials = OAuthClientCredentials.create(clientId,
			clientSecret, expectedNonce);

		ChangeProfilePasswordPayload payload = ChangeProfilePasswordPayload.create(credentials, passwortPayload, uuid);

		Response response = null;

		try {

			response = authproviderRestClient.changePassword(payload);

			ResponsePayload responsePayload = response.readEntity(ResponsePayload.class);
			@SuppressWarnings("unchecked")
			Map<String, String> dataMap = (Map<String, String>) responsePayload.getData();
			String nonce = dataMap.get("nonce");

			if (!expectedNonce.equals(nonce)) {

				LOGGER.warn(LogmessagePrefixes.BOT + "angefragter Entdpoint hat das nonce geändert: expected={}, actual={}",
					expectedNonce, nonce);

				throw new ProfilserverRuntimeException("Der authprovider konnte nicht erreicht werden.");

			}

			@SuppressWarnings("unchecked")
			Map<String, String> messageMap = (Map<String, String>) responsePayload.getMessage();

			return MessagePayload.info("Passwort wurde geändert");

		} catch (WebApplicationException e) {

			response = e.getResponse();

			int status = response.getStatus();

			if (status == 401) {

				LOGGER.error("Authentisierungsfehler für Client {} gegenüber dem authprovider",
					StringUtils.abbreviate(clientId, 11));
				throw new ProfilserverRuntimeException("Authentisierungsfehler für Client");
			}

			if (status == 404) {

				return MessagePayload.error("Ihr Benutzerkonto existiert seit Kurzem nicht mehr");
			}

			if (status == 412) {

				MessagePayload messagePayload = response.readEntity(MessagePayload.class);
				String message = messagePayload.getMessage();
				LOGGER.warn("Konflikt beim Aendern der Daten des Users {}: {}", StringUtils.abbreviate(uuid, 11), message);
				throw new ConflictException(message);
			}

			LOGGER.error("Statuscode {} beim Aendern des USERS {}", status, StringUtils.abbreviate(uuid, 11));

			throw new ProfilserverRuntimeException("Unerwarteter Response-Status " + status + " beim Aendern des Users.");

		} catch (Exception e) {

			LOGGER.error(e.getMessage(), e);

			throw new ProfilserverRuntimeException("Fehler bei Anfrage des authproviders: " + e.getMessage(), e);

		} finally {

			credentials.clean();

			if (response != null) {

				response.close();
			}
		}
	}
}
