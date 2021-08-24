// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.event;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.error.PropagationFailedException;
import de.egladil.web.authprovider.restclient.MkGatewayRestClient;
import de.egladil.web.authprovider.service.AuthMailService;
import de.egladil.web.authprovider.service.mail.MinikaengurukontenInfoStrategie;
import de.egladil.web.authprovider.service.mail.MinikaengurukontenInfoStrategie.MinikaengurukontenMailKontext;
import de.egladil.web.commons_mailer.DefaultEmailDaten;
import de.egladil.web.commons_validation.payload.MessagePayload;
import de.egladil.web.commons_validation.payload.ResponsePayload;

/**
 * AuthproviderEventHandler
 */
@ApplicationScoped
public class AuthproviderEventHandler {

	private static final Logger LOG = LoggerFactory.getLogger(AuthproviderEventHandler.class);

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	@ConfigProperty(name = "mkv-app.client-id")
	private String mkvAppClientId;

	@ConfigProperty(name = "stage")
	String stage;

	@ConfigProperty(name = "syncInfrastructureAvailable", defaultValue = "true")
	String syncInfrastructureAvailable;

	@Inject
	EventRepository eventRepository;

	@Inject
	AuthMailService mailService;

	@Inject
	@RestClient
	MkGatewayRestClient mkGateway;

	public static AuthproviderEventHandler createForTest(final EventRepository eventRepository, final AuthMailService mailService, final MkGatewayRestClient mkGateway) {

		AuthproviderEventHandler result = new AuthproviderEventHandler();
		result.mkvAppClientId = "bajlsdl";
		result.stage = "DEV";
		result.eventRepository = eventRepository;
		result.mailService = mailService;
		result.mkGateway = mkGateway;
		return result;
	}

	public void handleEvent(@Observes final AuthproviderEvent event) {

		if (event != null) {

			if (event.writeToEventStore()) {

				String body = event.serializePayload();

				LOG.debug("Event body = " + body);

				StoredEvent storedEvent = StoredEvent.createEvent(event.occuredOn(), event.eventType().getLabel(), body);

				this.eventRepository.appendEvent(storedEvent);
			}
		}

		if (event.propagateToListeners()) {

			sendEventToListeners(event);
		}

	}

	/**
	 * @param event
	 */
	private void sendEventToListeners(final AuthproviderEvent event) {

		switch (event.eventType()) {

		case LOGINVERSUCH_INAKTIVER_USER:
			this.sendeInfoAnMichQuietly(MinikaengurukontenMailKontext.LOGIN_INAKTIV, event.payload());
			break;

		case REGISTRATION_CONFIRMATION_EXPIRED:
			this.sendeInfoAnMichQuietly(MinikaengurukontenMailKontext.CONFIRMATION_EXPIRED, event.payload());
			this.propagateUserDeleted(event.payload());
			break;

		case USER_CREATED:
			this.sendeInfoAnMichQuietly(MinikaengurukontenMailKontext.USER_CREATED, event.payload());
			this.propagateUserCreated(event.payload());
			break;

		default:
			break;
		}

	}

	private void sendeInfoAnMichQuietly(final MinikaengurukontenMailKontext kontext, final Object payload) {

		try {

			ResourceOwnerEventPayload resourceOwner = (ResourceOwnerEventPayload) payload;

			DefaultEmailDaten maildaten = new MinikaengurukontenInfoStrategie(resourceOwner, kontext, stage)
				.createEmailDaten(kontext.name());

			if (maildaten != null) {

				mailService.sendMail(maildaten);
			}
		} catch (ClassCastException e) {

			LOG.error("Fehler beim Propagieren eines AuthproviderEvents: " + e.getMessage(), e);
		} catch (Exception e) {

			LOG.error("Infomail an mich konnte nicht gesendet werden: " + e.getMessage(), e);
		}
	}

	/**
	 * @param event
	 */
	private void propagateUserDeleted(final Object payload) {

		Response mkGatewayResponse = null;
		DeleteUserCommand command = null;
		ResourceOwnerEventPayload resourceOwner = null;

		try {

			resourceOwner = (ResourceOwnerEventPayload) payload;

			if (!isSyncInfrastructureAvailable()) {

				LOG.warn("Infrastruktur nicht verfuegbar: Löschen von {} wird nicht propagiert", resourceOwner);

				return;
			}

			LOG.debug("sende UserDeleted für {} an mk-gateway", resourceOwner);

			String syncToken = getSyncToken(resourceOwner.getUuid());

			if (syncToken == null) {

				LOG.error("Datensynchronisation hat keine Freigabe: syncToken ist null");
				this.sendeInfoAnMichQuietly(MinikaengurukontenMailKontext.SYNC_FAILED, resourceOwner);
				throw new PropagationFailedException(applicationMessages.getString("createUser.propagation.failure"));
			}

			LOG.debug("sync ack erhalten");

			command = DeleteUserCommand.create(resourceOwner.getUuid()).withSyncToken(syncToken);

			mkGatewayResponse = mkGateway.propagateUserDeleted(command);

			LOG.debug("Antwort: " + mkGatewayResponse.getStatus());

			if (mkGatewayResponse.getStatus() != 200) {

				LOG.error("Status {} vom mk-gateway beim Senden des DeleteUserCommands {} ", mkGatewayResponse.getStatus(),
					resourceOwner.getUuid());
				this.sendeInfoAnMichQuietly(MinikaengurukontenMailKontext.SYNC_FAILED, resourceOwner);
				throw new PropagationFailedException(applicationMessages.getString("deleteUser.propagation.failure"));
			}

		} catch (Exception e) {

			LOG.error("Konnte delete-event nicht propagieren: {} - {}", command, e.getMessage(), e);

			this.sendeInfoAnMichQuietly(MinikaengurukontenMailKontext.SYNC_FAILED, resourceOwner);

			throw new PropagationFailedException(applicationMessages.getString("deleteUser.propagation.failure"));
		} finally {

			if (mkGatewayResponse != null) {

				mkGatewayResponse.close();
			}
		}
	}

	/**
	 * @param event
	 */
	private void propagateUserCreated(final Object payload) {

		Response mkGatewayResponse = null;
		CreateUserCommand command = null;
		ResourceOwnerEventPayload resourceOwner = null;

		try {

			resourceOwner = (ResourceOwnerEventPayload) payload;

			if (!isSyncInfrastructureAvailable()) {

				LOG.warn("Infrastruktur nicht verfuegbar: Anlegen von {} wird nicht propagiert", resourceOwner);

				return;
			}

			LOG.info("sende UserCreated für {} an mk-gateway", resourceOwner);

			String syncToken = getSyncToken(resourceOwner.getUuid());

			if (syncToken == null) {

				LOG.error("Datensynchronisation hat keine Freigabe: syncToken ist null");
				this.sendeInfoAnMichQuietly(MinikaengurukontenMailKontext.SYNC_FAILED, resourceOwner);
				throw new PropagationFailedException(applicationMessages.getString("createUser.propagation.failure"));

			}

			LOG.debug("sync ack erhalten");

			String fullName = StringUtils.isAllBlank(new String[] { resourceOwner.getVorname(), resourceOwner.getNachname() })
				? null
				: resourceOwner.getVorname() + " " + resourceOwner.getNachname();

			command = new CreateUserCommand()
				.withEmail(resourceOwner.getEmail())
				.withFullName(fullName)
				.withNonce(resourceOwner.getNonce())
				.withSyncToken(syncToken)
				.withUuid(resourceOwner.getUuid())
				.withClientId(resourceOwner.getClientId());

			mkGatewayResponse = mkGateway.propagateUserCreated(command);

			LOG.debug("CreateUserCommand=" + command + ", httpStatusCode=" + mkGatewayResponse.getStatus());

			if (mkGatewayResponse.getStatus() != 200) {

				LOG.error("Status {} vom mk-gateway beim Senden des CreateUserCommand {} ", mkGatewayResponse.getStatus(),
					command);
				this.sendeInfoAnMichQuietly(MinikaengurukontenMailKontext.SYNC_FAILED, resourceOwner);
				throw new PropagationFailedException(applicationMessages.getString("createUser.propagation.failure"));
			}

		} catch (PropagationFailedException e) {

			throw e;
		} catch (WebApplicationException e) {

			Response exceptionResponse = e.getResponse();
			LOG.error("WebApplicationException beim Propagieren des CreateUserEvents: status={} - {}",
				exceptionResponse.getStatus(), e.getMessage(), e);

			this.sendeInfoAnMichQuietly(MinikaengurukontenMailKontext.SYNC_FAILED, resourceOwner);

			throw new PropagationFailedException(applicationMessages.getString("createUser.propagation.failure"));

		} catch (Exception e) {

			LOG.error("Konnte create-event nicht propagieren: {} - {}", command, e.getMessage(), e);

			this.sendeInfoAnMichQuietly(MinikaengurukontenMailKontext.SYNC_FAILED, resourceOwner);

			throw new PropagationFailedException(applicationMessages.getString("createUser.propagation.failure"));
		} finally {

			if (mkGatewayResponse != null) {

				mkGatewayResponse.close();
			}
		}
	}

	/**
	 * @param event
	 */
	private String getSyncToken(final String uuid) {

		String nonce = UUID.randomUUID().toString();

		SyncHandshake handshake = new SyncHandshake(mkvAppClientId, nonce);

		LOG.debug("mkvAppClientId={}", mkvAppClientId);

		Response mkGatewayResponse = null;

		try {

			mkGatewayResponse = mkGateway.getSyncToken(handshake);

			LOG.debug("mkGatewayResponse.status={}", mkGatewayResponse.getStatus());

			ResponsePayload responsePayload = mkGatewayResponse.readEntity(ResponsePayload.class);

			MessagePayload messagePayload = responsePayload.getMessage();

			LOG.debug("Result={}", messagePayload.toString());

			if (messagePayload.isOk()) {

				@SuppressWarnings("unchecked")
				Map<String, Object> data = (Map<String, Object>) responsePayload.getData();

				HandshakeAck ack = HandshakeAck.fromResponse(data);

				if (!nonce.equals(ack.nonce())) {

					LOG.error("Nonce wurde geändert");
					return null;
				}

				return ack.syncToken();
			}
			return null;
		} finally {

			if (mkGatewayResponse != null) {

				mkGatewayResponse.close();
			}
		}
	}

	private boolean isSyncInfrastructureAvailable() {

		return Boolean.valueOf(syncInfrastructureAvailable);
	}

}
