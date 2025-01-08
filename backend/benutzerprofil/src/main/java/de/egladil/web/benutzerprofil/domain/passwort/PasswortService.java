// =====================================================
// Project: benutzerprofil
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.benutzerprofil.domain.passwort;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.auth_validations.dto.OAuthClientCredentials;
import de.egladil.web.benutzerprofil.domain.auth.dto.MessagePayload;
import de.egladil.web.benutzerprofil.domain.auth.dto.ResponsePayload;
import de.egladil.web.benutzerprofil.domain.exceptions.CommunicationException;
import de.egladil.web.benutzerprofil.domain.exceptions.ConcurrentModificationException;
import de.egladil.web.benutzerprofil.domain.exceptions.DuplicateEntityException;
import de.egladil.web.benutzerprofil.domain.exceptions.LogmessagePrefixes;
import de.egladil.web.benutzerprofil.domain.exceptions.ProfilAPIRuntimeException;
import de.egladil.web.benutzerprofil.infrastructure.restclient.AuthproviderRestClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

/**
 * PasswortService
 */
@ApplicationScoped
public class PasswortService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PasswortService.class);

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

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

		try (Response response = authproviderRestClient.changePassword(payload);) {

			ResponsePayload responsePayload = response.readEntity(ResponsePayload.class);
			@SuppressWarnings("unchecked")
			Map<String, String> dataMap = (Map<String, String>) responsePayload.getData();
			String nonce = dataMap.get("nonce");

			if (!expectedNonce.equals(nonce)) {

				LOGGER.warn(LogmessagePrefixes.BOT + "angefragter Entdpoint hat das nonce geändert: expected={}, actual={}",
					expectedNonce, nonce);

				throw new ProfilAPIRuntimeException("Der authprovider konnte nicht erreicht werden.");

			}

			MessagePayload messagePayload = responsePayload.getMessage();
			return messagePayload;

		} catch (CommunicationException e) {
			// diese wird vom AuthproviderResponseExceptionMapper geworfen und enthält das, was der BenutzerverwaltungExceptionMapper dann
			// umwandeln kann.

			throw e.getExceptionToPropagate();
		} catch (ProcessingException e) {

			LOGGER.error("ProcessingException bei der Kommunikation mit dem authprovider: {}", e.getMessage(), e);
			throw new ProfilAPIRuntimeException(
				"Fehler bei Kommunikation mit authprovider. Evtl. Konfiguration der route pruefen. Laeuft der authprovider noch?");
		} catch (ClientWebApplicationException e) {

			if (e.getResponse().getStatus() == 409) {

				throw new ConcurrentModificationException(applicationMessages.getString("conflict.notFound"));
			}

			if (e.getResponse().getStatus() == 412) {

				throw new DuplicateEntityException(applicationMessages.getString("conflict.duplicate"));
			}
			throw e;
		} catch (Exception e) {

			LOGGER.error("unerwarteter Fehler bei der Kommunikation mit dem authprovider: {}", e.getMessage(), e);
			throw new ProfilAPIRuntimeException(
				"unerwarteter Fehler bei Kommunikation mit authprovider");
		} finally {

			credentials.clean();
		}
	}
}
