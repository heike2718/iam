// =====================================================
// Project: profil-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_api.domain.benutzer;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.profil_api.domain.auth.dto.MessagePayload;
import de.egladil.web.profil_api.domain.auth.dto.OAuthClientCredentials;
import de.egladil.web.profil_api.domain.auth.dto.ResponsePayload;
import de.egladil.web.profil_api.domain.event.ProfilEventHandler;
import de.egladil.web.profil_api.domain.event.UserChanged;
import de.egladil.web.profil_api.domain.event.UserDeleted;
import de.egladil.web.profil_api.domain.exceptions.ConflictException;
import de.egladil.web.profil_api.domain.exceptions.ProfilserverRuntimeException;
import de.egladil.web.profil_api.infrastructure.restclient.AuthproviderRestClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

/**
 * BenutzerService
 */
@ApplicationScoped
public class BenutzerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BenutzerService.class);

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	@ConfigProperty(name = "public-client-id")
	String clientId;

	@ConfigProperty(name = "public-client-secret")
	String clientSecret;

	@Context
	SecurityContext securityContext;

	@Inject
	Event<UserChanged> userChangedEvent;

	@Inject
	Event<UserDeleted> userDeletedEvent;

	@Inject
	ProfilEventHandler eventHandler;

	@Inject
	@RestClient
	AuthproviderRestClient profilRestClient;

	public BenutzerDto ladeBenuterDaten() {

		String uuid = securityContext.getUserPrincipal().getName();

		OAuthClientCredentials credentials = OAuthClientCredentials.create(clientId,
			clientSecret, null);

		SelectProfilePayload selectPayload = SelectProfilePayload.create(credentials, uuid);

		Response response = null;

		try {

			response = profilRestClient.getUserProfile(selectPayload);
			ResponsePayload payload = response.readEntity(ResponsePayload.class);
			MessagePayload messagePayload = payload.getMessage();

			if (messagePayload.isOk()) {

				@SuppressWarnings("unchecked")
				Map<String, String> data = (Map<String, String>) payload.getData();

				BenutzerDto benutzer = new BenutzerDto();
				benutzer.setEmail(data.get("email"));
				benutzer.setLoginName(data.get("loginName"));
				benutzer.setNachname(data.get("nachname"));
				benutzer.setVorname(data.get("vorname"));

				return benutzer;
			}

			return createAnonumousBenutzer();
		} catch (WebApplicationException e) {

			response = e.getResponse();

			int status = response.getStatus();

			if (status == 401) {

				LOGGER.error("Authentisierungsfehler für Client {} gegenüber dem authprovider",
					StringUtils.abbreviate(clientId, 11));
				throw new ProfilserverRuntimeException("Authentisierungsfehler für Client");
			}

			if (status == 404) {

				LOGGER.error("Nach gueltigem Login wird USER mit uuid={} nicht gefunden.", StringUtils.abbreviate(uuid, 11));
				throw new ConflictException(applicationMessages.getString("conflict"));
			}

			LOGGER.error("Statuscode {} beim Holen des Users", status);

			throw new ProfilserverRuntimeException("Unerwarteter Response-Status beim Holen des Users.");

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

	/**
	 * @return
	 */
	private BenutzerDto createAnonumousBenutzer() {

		BenutzerDto benutzer = new BenutzerDto();
		benutzer.setEmail("");
		benutzer.setLoginName("");
		benutzer.setNachname("Anonym");
		benutzer.setVorname("Gast");

		return benutzer;
	}

}
