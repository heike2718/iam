// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.event;

import java.util.Map;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.error.AuthRuntimeException;
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

	@ConfigProperty(name = "mkv-app.client-id")
	private String mkvAppClientId;

	@ConfigProperty(name = "stage")
	String stage;

	@Inject
	EventRepository eventRepository;

	@Inject
	AuthMailService mailService;

	@Inject
	@RestClient
	MkGatewayRestClient mkGateway;

	public void handleEvent(@Observes final AuthproviderEvent event) {

		if (event.writeToEventStore()) {

			String body = event.serializePayload();

			LOG.debug("Event body = " + body);

			StoredEvent storedEvent = StoredEvent.createEvent(event.occuredOn(), event.eventType().getLabel(), body);

			this.eventRepository.appendEvent(storedEvent);
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
			break;

		default:
			break;
		}

	}

	private void sendeInfoAnMichQuietly(final MinikaengurukontenMailKontext kontext, final Object payload) {

		try {

			ResourceOwnerEventPayload resourceOwner = (ResourceOwnerEventPayload) payload;

			DefaultEmailDaten maildaten = new MinikaengurukontenInfoStrategie(resourceOwner, kontext, stage).createEmailDaten();

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

		try {

			ResourceOwnerEventPayload resourceOwner = (ResourceOwnerEventPayload) payload;

			LOG.info("sende Löschevent für {} an mk-gateway", resourceOwner);

			String syncToken = getSyncToken(resourceOwner.getUuid());

			if (syncToken == null) {

				LOG.error("Datensynchronisation hat keine Freigabe: syncToken ist null");
				return;
			}

			LOG.info("sync ack erhalten");

			Response mkGatewayResponse = null;

			try {

				DeleteUserCommand command = DeleteUserCommand.create(resourceOwner.getUuid()).withSyncToken(syncToken);

				mkGatewayResponse = mkGateway.propagateUserDeleted(command);

				LOG.info("Antwort: " + mkGatewayResponse.getStatus());

				if (mkGatewayResponse.getStatus() != 200) {

					LOG.error("Status {} vom mk-gateway beim Senden des DeleteUserCommands {} ", mkGatewayResponse.getStatus(),
						resourceOwner.getUuid());
				}

			} catch (Exception e) {

				LOG.error("Konnte delete-event nicht propagieren: {} - {}", resourceOwner.getUuid(), e.getMessage(), e);
			} finally {

				if (mkGatewayResponse != null) {

					mkGatewayResponse.close();
				}
			}

		} catch (ClassCastException e) {

			LOG.error("Fehler beim Propagieren eines AuthproviderEvents: " + e.getMessage(), e);
		}
	}

	/**
	 * @param event
	 */
	private String getSyncToken(final String uuid) {

		String nonce = UUID.randomUUID().toString();

		SyncHandshake handshake = new SyncHandshake(mkvAppClientId, nonce);

		Response mkGatewayResponse = null;

		try {

			mkGatewayResponse = mkGateway.getSyncToken(handshake);

			ResponsePayload responsePayload = mkGatewayResponse.readEntity(ResponsePayload.class);

			MessagePayload messagePayload = responsePayload.getMessage();

			if (messagePayload.isOk()) {

				try {

					@SuppressWarnings("unchecked")
					Map<String, Object> data = (Map<String, Object>) responsePayload.getData();

					HandshakeAck ack = HandshakeAck.fromResponse(data);

					if (!nonce.equals(ack.nonce())) {

						LOG.error("Nonce wurde geändert");
						return null;
					}

					return ack.syncToken();

				} catch (ClassCastException e) {

					LOG.error(e.getMessage());
					throw new AuthRuntimeException("Konnte ResponsePayload vom mk-gateway nicht verarbeiten");
				}

			}

			return null;
		} catch (Exception e) {

			LOG.error("Keine Freigabe fürs Senden des DeletUserCommands {} - {}", uuid, e.getMessage(), e);
			return null;
		} finally {

			if (mkGatewayResponse != null) {

				mkGatewayResponse.close();
			}
		}
	}

}
