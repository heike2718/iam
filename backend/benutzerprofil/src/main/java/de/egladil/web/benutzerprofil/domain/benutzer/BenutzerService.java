// =====================================================
// Project: benutzerprofil
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.benutzerprofil.domain.benutzer;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
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
import de.egladil.web.benutzerprofil.domain.exceptions.BenutzerprofilRuntimeException;
import de.egladil.web.benutzerprofil.infrastructure.restclient.AuthproviderRestClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ProcessingException;
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

	@ConfigProperty(name = "mock.kontoloeschung")
	boolean mockDelete;

	@Context
	SecurityContext securityContext;

	@Inject
	@RestClient
	AuthproviderRestClient authproviderRestClient;

	public BenutzerDto ladeBenuterDaten() {

		String uuid = securityContext.getUserPrincipal().getName();

		OAuthClientCredentials credentials = OAuthClientCredentials.create(clientId, clientSecret, null);

		SelectProfilePayload selectPayload = SelectProfilePayload.create(credentials, uuid);

		try (Response response = authproviderRestClient.getUserProfile(selectPayload);) {

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

			LOGGER.error(
				"kein OK vom authprovider: USER {} existiert nicht mehr? Oder stimmt path beim AuthprovderRestClient.getUserProfile nicht?",
				StringUtils.abbreviate(uuid, 11));
			throw new BenutzerprofilRuntimeException("unerwarteter Fehler bei Kommunikation mit authprovider");

		} catch (CommunicationException e) {
			// diese wird vom AuthproviderResponseExceptionMapper geworfen und enthält das, was der
			// BenutzerverwaltungExceptionMapper dann
			// umwandeln kann.

			throw e.getExceptionToPropagate();
		} catch (ProcessingException e) {

			LOGGER.error("ProcessingException bei der Kommunikation mit dem authprovider: {}", e.getMessage(), e);
			throw new BenutzerprofilRuntimeException(
				"Fehler bei Kommunikation mit authprovider. Evtl. Konfiguration der route pruefen. Laeuft der authprovider noch?");
		} catch (ClientWebApplicationException e) {

			throw e;
		} catch (Exception e) {

			LOGGER.error("unerwarteter Fehler bei der Kommunikation mit dem authprovider: {}", e.getMessage(), e);
			throw new BenutzerprofilRuntimeException("unerwarteter Fehler bei Kommunikation mit authprovider");
		} finally {

			credentials.clean();
		}
	}

	/**
	 * Ändert die Benutzerdaten und gibt die geänderten zurück.
	 *
	 * @param benutzerDto BenutzerDto
	 * @return BenutzerDto
	 */
	@Transactional
	public ChangeBenutzerdatenResponseDto benutzerdatenAendern(final BenutzerDto benutzerDto) {

		String uuid = securityContext.getUserPrincipal().getName();

		BenutzerDto existing = ladeBenuterDaten();

		boolean isSecurityRelevant = this.isSecurityRelevant(existing, benutzerDto);

		String expectedNonce = UUID.randomUUID().toString();
		OAuthClientCredentials credentials = OAuthClientCredentials.create(clientId, clientSecret, expectedNonce);

		ChangeProfileDataPayload payload = ChangeProfileDataPayload.create(credentials, benutzerDto, uuid);

		try (Response response = authproviderRestClient.changeData(payload);) {

			LOGGER.debug("Response-Status={}", response.getStatus());

			ResponsePayload responsePayload = response.readEntity(ResponsePayload.class);
			@SuppressWarnings("unchecked")
			Map<String, String> dataMap = (Map<String, String>) responsePayload.getData();
			String nonce = dataMap.get("nonce");

			if (!expectedNonce.equals(nonce)) {

				LOGGER.warn(LogmessagePrefixes.BOT + "angefragter Endpoint hat das nonce geändert: expected={}, actual={}",
					expectedNonce, nonce);

				throw new BenutzerprofilRuntimeException("Der authprovider konnte nicht erreicht werden.");

			}

			BenutzerDto result = new BenutzerDto();
			result.setEmail(dataMap.get("email"));
			result.setLoginName(dataMap.get("loginName"));
			result.setNachname(dataMap.get("nachname"));
			result.setVorname(dataMap.get("vorname"));

			return new ChangeBenutzerdatenResponseDto().withBenutzer(benutzerDto).withSecurityEvent(isSecurityRelevant);

		} catch (CommunicationException e) {
			// diese wird vom AuthproviderResponseExceptionMapper geworfen und enthält das, was der
			// BenutzerverwaltungExceptionMapper dann
			// umwandeln kann.

			throw e.getExceptionToPropagate();
		} catch (ProcessingException e) {

			LOGGER.error("ProcessingException bei der Kommunikation mit dem authprovider: {}", e.getMessage(), e);
			throw new BenutzerprofilRuntimeException(
				"Fehler bei Kommunikation mit authprovider. Evtl. Konfiguration der route pruefen. Laeuft der authprovider noch?");
		} catch (ClientWebApplicationException e) {

			if (e.getResponse().getStatus() == 409) {

				throw new ConcurrentModificationException(applicationMessages.getString("conflict.notFound"));
			}

			if (e.getResponse().getStatus() >= 900) {

				switch (e.getResponse().getStatus()) {

				case 910:
					throw new DuplicateEntityException(DuplicateAttributeType.EMAIL);

				case 911:
					throw new DuplicateEntityException(DuplicateAttributeType.LOGINNAME);

				case 912:
					throw new DuplicateEntityException(DuplicateAttributeType.EMAIL_AND_LOGINNAME);

				default:
					throw e;
				}
			}
			throw e;
		} catch (Exception e) {

			LOGGER.error("unerwarteter Fehler bei der Kommunikation mit dem authprovider: {}", e.getMessage(), e);
			throw new BenutzerprofilRuntimeException("unerwarteter Fehler bei Kommunikation mit authprovider");
		} finally {

			credentials.clean();
		}
	}

	/**
	 * @return
	 */
	public Response kontoLoeschen() {

		String uuid = securityContext.getUserPrincipal().getName();

		String expectedNonce = UUID.randomUUID().toString();
		OAuthClientCredentials credentials = OAuthClientCredentials.create(clientId, clientSecret, expectedNonce);

		SelectProfilePayload selectPayload = SelectProfilePayload.create(credentials, uuid);

		if (mockDelete) {

			return this.mockTheKontoloeschung();
		}

		try (Response response = authproviderRestClient.deleteProfile(selectPayload)) {

			LOGGER.debug("Response-Status={}", response.getStatus());

			ResponsePayload responsePayload = response.readEntity(ResponsePayload.class);
			String nonce = responsePayload.getData() == null ? null : responsePayload.getData().toString();

			if (!expectedNonce.equals(nonce)) {

				LOGGER.warn(LogmessagePrefixes.BOT + "angefragter Endpoint hat das nonce geändert: expected={}, actual={}",
					expectedNonce, nonce);

				throw new BenutzerprofilRuntimeException(
					"Statt authprovider wurde offenbar ein evil endpoint erreicht, der das nonce gefresen hat.");

			}

			MessagePayload messagePayload = MessagePayload.info("Ihr Benutzerkonto wurde erfolgreich gelöscht.");
			return Response.ok(messagePayload).build();

		} catch (CommunicationException e) {
			// diese wird vom AuthproviderResponseExceptionMapper geworfen und enthält das, was der
			// BenutzerverwaltungExceptionMapper dann
			// umwandeln kann.

			throw e.getExceptionToPropagate();
		} catch (ProcessingException e) {

			LOGGER.error("ProcessingException bei der Kommunikation mit dem authprovider: {}", e.getMessage(), e);
			throw new BenutzerprofilRuntimeException(
				"Fehler bei Kommunikation mit authprovider. Evtl. Konfiguration der route pruefen. Laeuft der authprovider noch?");
		} catch (Exception e) {

			LOGGER.error("unerwarteter Fehler bei der Kommunikation mit dem authprovider: {}", e.getMessage(), e);
			throw new BenutzerprofilRuntimeException("unerwarteter Fehler bei Kommunikation mit authprovider");
		} finally {

			credentials.clean();
		}
	}

	private Response mockTheKontoloeschung() {

		MessagePayload messagePayload = MessagePayload.info("Ihr Benutzerkonto wurde erfolgreich gelöscht.");
		return Response.ok(messagePayload).build();
	}

	boolean isSecurityRelevant(final BenutzerDto alteDaten, final BenutzerDto neueDaten) {

		if (!alteDaten.getEmail().equalsIgnoreCase(neueDaten.getEmail())) {

			return true;
		}

		if (!alteDaten.getLoginName().equalsIgnoreCase(neueDaten.getLoginName())) {

			return true;
		}

		return false;
	}
}
