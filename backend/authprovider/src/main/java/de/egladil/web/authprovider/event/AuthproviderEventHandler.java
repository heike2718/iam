// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.event;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.error.PropagationFailedException;
import de.egladil.web.authprovider.payload.MessagePayload;
import de.egladil.web.authprovider.payload.ResponsePayload;
import de.egladil.web.authprovider.restclient.MkGatewayRestClient;
import de.egladil.web.authprovider.service.AuthMailService;
import de.egladil.web.authprovider.service.mail.MinikaengurukontenInfoStrategie;
import de.egladil.web.authprovider.service.mail.MinikaengurukontenInfoStrategie.MinikaengurukontenMailKontext;
import de.egladil.web.commons_mailer.DefaultEmailDaten;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

/**
 * AuthproviderEventHandler
 */
@ApplicationScoped
public class AuthproviderEventHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthproviderEventHandler.class);

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	@Inject
	@RestClient
	MkGatewayRestClient mkGateway;

	@ConfigProperty(name = "mkv-app.client-id")
	private String mkvAppClientId;

	@ConfigProperty(name = "stage")
	String stage;

	@ConfigProperty(name = "sync.infrastructure.available")
	String syncInfrastructureAvailable;

	@Inject
	EventRepository eventRepository;

	@Inject
	AuthMailService mailService;

	public void handleEvent(@Observes final AuthproviderEvent event) {

		if (event != null) {

			if (event.writeToEventStore()) {

				String body = event.serializePayload();

				LOGGER.debug("Event body = " + body);

				StoredEvent storedEvent = StoredEvent.createEvent(event.occuredOn(), event.eventType().getLabel(), body);

				this.eventRepository.appendEvent(storedEvent);
			}
		}

		if (event.propagateToListeners()) {

			if (!isSyncInfrastructureAvailable()) {

				LOGGER.warn("Infrastruktur zum Synchronisieren nicht verfuegbar: Abbruch");

				return;
			}

			String syncToken = null;

			if (event.eventType() != AuthproviderEventType.LOGINVERSUCH_INAKTIVER_USER) {

				syncToken = this.getSyncToken();
			}

			sendEventToListeners(event, syncToken);
		}

	}

	/**
	 * @param event
	 * @param syncToken
	 */
	private void sendEventToListeners(final AuthproviderEvent event, final String syncToken) {

		switch (event.eventType()) {

		case LOGINVERSUCH_INAKTIVER_USER:
			this.sendeInfoAnMichQuietly(MinikaengurukontenMailKontext.LOGIN_INAKTIV, event.payload());
			break;

		case REGISTRATION_CONFIRMATION_EXPIRED:
			this.sendeInfoAnMichQuietly(MinikaengurukontenMailKontext.CONFIRMATION_EXPIRED, event.payload());
			this.propagateUserDeleted(event.payload(), syncToken);
			break;

		case USER_CREATED:
			this.sendeInfoAnMichQuietly(MinikaengurukontenMailKontext.USER_CREATED, event.payload());
			this.propagateUserCreated(event.payload(), syncToken);
			break;

		case USER_CHANGED:
			this.sendeInfoAnMichQuietly(MinikaengurukontenMailKontext.USER_CHANGED, event.payload());
			this.propagateUserChanged(event, syncToken);
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

			LOGGER.error("Fehler beim Propagieren eines AuthproviderEvents: " + e.getMessage(), e);
		} catch (Exception e) {

			LOGGER.error("Infomail an mich konnte nicht gesendet werden: " + e.getMessage(), e);
		}
	}

	/**
	 * @param event
	 */
	private void propagateUserDeleted(final Object payload, final String syncToken) {

		DeleteUserCommand command = null;
		ResourceOwnerEventPayload resourceOwner = null;

		resourceOwner = (ResourceOwnerEventPayload) payload;

		LOGGER.debug("sende UserDeleted für {} an mk-gateway", resourceOwner);

		if (syncToken == null) {

			LOGGER.error("Datensynchronisation hat keine Freigabe: syncToken ist null");
			this.sendeInfoAnMichQuietly(MinikaengurukontenMailKontext.SYNC_FAILED, resourceOwner);
			throw new PropagationFailedException(applicationMessages.getString("createUser.propagation.failure"));
		}

		LOGGER.debug("sync ack erhalten");

		command = DeleteUserCommand.create(resourceOwner.getUuid()).withSyncToken(syncToken);

		try (Response mkGatewayResponse = mkGateway.propagateUserDeleted(command)) {

			LOGGER.debug("Antwort: " + mkGatewayResponse.getStatus());

			if (mkGatewayResponse.getStatus() != 200) {

				LOGGER.error("Status {} vom mk-gateway beim Senden des DeleteUserCommands {} ", mkGatewayResponse.getStatus(),
					resourceOwner.getUuid());
				this.sendeInfoAnMichQuietly(MinikaengurukontenMailKontext.SYNC_FAILED, resourceOwner);
				throw new PropagationFailedException(applicationMessages.getString("deleteUser.propagation.failure"));
			}

		} catch (Exception e) {

			LOGGER.error("Konnte delete-event nicht propagieren: {} - {}", command, e.getMessage(), e);

			this.sendeInfoAnMichQuietly(MinikaengurukontenMailKontext.SYNC_FAILED, resourceOwner);

			throw new PropagationFailedException(applicationMessages.getString("deleteUser.propagation.failure"));
		}
	}

	/**
	 * @param event
	 */
	private void propagateUserCreated(final Object payload, final String syncToken) {

		CreateUserCommand command = null;
		ResourceOwnerEventPayload resourceOwner = null;

		resourceOwner = (ResourceOwnerEventPayload) payload;

		LOGGER.info("sende UserCreated für {} an mk-gateway", resourceOwner);

		if (syncToken == null) {

			LOGGER.error("Datensynchronisation hat keine Freigabe: syncToken ist null");
			this.sendeInfoAnMichQuietly(MinikaengurukontenMailKontext.SYNC_FAILED, resourceOwner);
			throw new PropagationFailedException(applicationMessages.getString("createUser.propagation.failure"));

		}

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

		try (Response mkGatewayResponse = mkGateway.propagateUserCreated(command)) {

			LOGGER.debug("CreateUserCommand=" + command + ", httpStatusCode=" + mkGatewayResponse.getStatus());

			if (mkGatewayResponse.getStatus() != 200) {

				LOGGER.error("Status {} vom mk-gateway beim Senden des CreateUserCommand {} ", mkGatewayResponse.getStatus(),
					command);
				this.sendeInfoAnMichQuietly(MinikaengurukontenMailKontext.SYNC_FAILED, resourceOwner);
				throw new PropagationFailedException(applicationMessages.getString("createUser.propagation.failure"));
			}

		} catch (ProcessingException e) {

			LOGGER.error("ProcessingException beim Propagieren des CreateUserEvents ans mk-gateway: {}",
				e.getMessage(), e);

			this.sendeInfoAnMichQuietly(MinikaengurukontenMailKontext.SYNC_FAILED, resourceOwner);

			throw new PropagationFailedException(applicationMessages.getString("createUser.propagation.failure"));
		} catch (WebApplicationException e) {

			Response exceptionResponse = e.getResponse();
			LOGGER.error("WebApplicationException beim Propagieren des CreateUserEvents: status={} - {}",
				exceptionResponse.getStatus(), e.getMessage(), e);

			this.sendeInfoAnMichQuietly(MinikaengurukontenMailKontext.SYNC_FAILED, resourceOwner);

			throw new PropagationFailedException(applicationMessages.getString("createUser.propagation.failure"));

		} catch (Exception e) {

			LOGGER.error("Konnte create-event nicht propagieren: {} - {}", command, e.getMessage(), e);

			this.sendeInfoAnMichQuietly(MinikaengurukontenMailKontext.SYNC_FAILED, resourceOwner);

			throw new PropagationFailedException(applicationMessages.getString("createUser.propagation.failure"));
		}
	}

	/**
	 * @param event
	 */
	private void propagateUserChanged(final Object payload, final String syncToken) {

		ChangeUserCommand command = null;
		ResourceOwnerEventPayload resourceOwner = null;

		resourceOwner = (ResourceOwnerEventPayload) payload;

		LOGGER.info("sende UserChanged für {} an mk-gateway", resourceOwner);

		if (syncToken == null) {

			LOGGER.error("Datensynchronisation hat keine Freigabe: syncToken ist null");
			this.sendeInfoAnMichQuietly(MinikaengurukontenMailKontext.SYNC_FAILED, resourceOwner);
			throw new PropagationFailedException(applicationMessages.getString("createUser.propagation.failure"));

		}

		command = new ChangeUserCommand()
			.withEmail(resourceOwner.getEmail())
			.withNachname(resourceOwner.getNachname())
			.withSyncToken(syncToken)
			.withUuid(resourceOwner.getUuid())
			.withVorname(resourceOwner.getVorname());

		try (Response mkGatewayResponse = mkGateway.propagateUserChanged(command)) {

			LOGGER.debug("CreateUserCommand=" + command + ", httpStatusCode=" + mkGatewayResponse.getStatus());

			if (mkGatewayResponse.getStatus() != 200) {

				LOGGER.error("Status {} vom mk-gateway beim Senden des CreateUserCommand {} ", mkGatewayResponse.getStatus(),
					command);
				this.sendeInfoAnMichQuietly(MinikaengurukontenMailKontext.SYNC_FAILED, resourceOwner);
				throw new PropagationFailedException(applicationMessages.getString("createUser.propagation.failure"));
			}

		} catch (ProcessingException e) {

			LOGGER.error("ProcessingException beim Propagieren des CreateUserEvents ans mk-gateway: {}",
				e.getMessage(), e);

			this.sendeInfoAnMichQuietly(MinikaengurukontenMailKontext.SYNC_FAILED, resourceOwner);

			throw new PropagationFailedException(applicationMessages.getString("createUser.propagation.failure"));
		} catch (WebApplicationException e) {

			Response exceptionResponse = e.getResponse();
			LOGGER.error("WebApplicationException beim Propagieren des CreateUserEvents: status={} - {}",
				exceptionResponse.getStatus(), e.getMessage(), e);

			this.sendeInfoAnMichQuietly(MinikaengurukontenMailKontext.SYNC_FAILED, resourceOwner);

			throw new PropagationFailedException(applicationMessages.getString("createUser.propagation.failure"));

		} catch (Exception e) {

			LOGGER.error("Konnte create-event nicht propagieren: {} - {}", command, e.getMessage(), e);

			this.sendeInfoAnMichQuietly(MinikaengurukontenMailKontext.SYNC_FAILED, resourceOwner);

			throw new PropagationFailedException(applicationMessages.getString("createUser.propagation.failure"));
		}
	}

	/**
	 * @param event
	 */
	private String getSyncToken() {

		String nonce = UUID.randomUUID().toString();

		SyncHandshake handshake = new SyncHandshake(mkvAppClientId, nonce);

		LOGGER.debug("mkvAppClientId={}", mkvAppClientId);

		try (Response mkGatewayResponse = mkGateway.getSyncToken(handshake)) {

			LOGGER.debug("mkGatewayResponse.status={}", mkGatewayResponse.getStatus());

			ResponsePayload responsePayload = mkGatewayResponse.readEntity(ResponsePayload.class);

			MessagePayload messagePayload = responsePayload.getMessage();

			LOGGER.debug("Result={}", messagePayload.toString());

			if (messagePayload.isOk()) {

				@SuppressWarnings("unchecked")
				Map<String, Object> data = (Map<String, Object>) responsePayload.getData();

				HandshakeAck ack = HandshakeAck.fromResponse(data);

				if (!"dev".equals(stage) && !nonce.equals(ack.nonce())) {

					LOGGER.error("Nonce wurde geändert");
					return null;
				}

				return ack.syncToken();
			}

			LOGGER.error("MessagePayload={}", messagePayload.toString());
			return null;
		}
	}

	private boolean isSyncInfrastructureAvailable() {

		return Boolean.valueOf(syncInfrastructureAvailable);
	}

}
