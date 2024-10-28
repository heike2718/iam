// =====================================================
// Project: profil-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_api.domain.benutzer;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
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
import jakarta.transaction.Transactional;
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
	@RestClient
	AuthproviderRestClient authproviderRestClient;

	// @Inject
	// UserDao userDao;

	public BenutzerDto ladeBenuterDaten() {

		String uuid = securityContext.getUserPrincipal().getName();

		OAuthClientCredentials credentials = OAuthClientCredentials.create(clientId,
			clientSecret, null);

		SelectProfilePayload selectPayload = SelectProfilePayload.create(credentials, uuid);

		Response response = null;

		try {

			response = authproviderRestClient.getUserProfile(selectPayload);
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
	 * Ändert die Benutzerdaten und gibt die geänderten zurück.
	 *
	 * @param  benutzerDto
	 *                     BenutzerDto
	 * @return             BenutzerDto
	 */
	public BenutzerDto benutzerdatenAendern(final BenutzerDto benutzerDto) {

		String uuid = securityContext.getUserPrincipal().getName();
		this.checkConflict(benutzerDto, uuid);
		return this.doChange(benutzerDto, uuid);

	}

	@Transactional
	BenutzerDto doChange(final BenutzerDto benutzerDto, final String uuid) {

		String expectedNonce = UUID.randomUUID().toString();
		OAuthClientCredentials credentials = OAuthClientCredentials.create(clientId,
			clientSecret, expectedNonce);

		ChangeProfileDataPayload payload = ChangeProfileDataPayload.create(credentials, benutzerDto, uuid);

		Response response = null;

		try {

			response = authproviderRestClient.changeData(payload);

			LOGGER.debug("Response-Status={}", response.getStatus());

			ResponsePayload responsePayload = response.readEntity(ResponsePayload.class);
			@SuppressWarnings("unchecked")
			Map<String, String> dataMap = (Map<String, String>) responsePayload.getData();
			String nonce = dataMap.get("nonce");

			if (!expectedNonce.equals(nonce)) {

				LOGGER.warn(LogmessagePrefixes.BOT + "angefragter Entdpoint hat das nonce geändert: expected={}, actual={}",
					expectedNonce, nonce);

				throw new ProfilserverRuntimeException("Der authprovider konnte nicht erreicht werden.");

			}

			BenutzerDto result = new BenutzerDto();
			result.setEmail(dataMap.get("email"));
			result.setLoginName(dataMap.get("loginName"));
			result.setNachname(dataMap.get("nachname"));
			result.setVorname(dataMap.get("vorname"));
			return result;

		} catch (WebApplicationException e) {

			response = e.getResponse();

			int status = response.getStatus();

			if (status == 401) {

				LOGGER.error("Authentisierungsfehler für Client {} gegenüber dem authprovider",
					StringUtils.abbreviate(clientId, 11));
				throw new ProfilserverRuntimeException("Authentisierungsfehler für Client");
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

	void checkConflict(final BenutzerDto benutzerDto, final String uuid) throws ConflictException {

		// List<UserEntity> gleicherLoginName = this.userDao.findOtherUsersWithSameLoginName(uuid, benutzerDto.getLoginName());
		// List<UserEntity> gleicheMailadresse = gleicherLoginName;
		//
		// if (!benutzerDto.getEmail().equalsIgnoreCase(benutzerDto.getLoginName())) {
		//
		// gleicheMailadresse = this.userDao.findOtherUsersWithSameEmail(uuid, benutzerDto.getEmail());
		// }
		//
		// if (gleicheMailadresse.size() > 0 && gleicherLoginName.size() > 0) {
		//
		// throw new ConflictException(applicationMessages.getString("conflict.sameEmailAndLoginName"));
		//
		// } else if (gleicheMailadresse.size() > 0) {
		//
		// throw new ConflictException(applicationMessages.getString("conflict.sameEmail"));
		//
		// } else if (gleicherLoginName.size() > 0) {
		//
		// throw new ConflictException(applicationMessages.getString("conflict.sameLoginName"));
		// }
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
