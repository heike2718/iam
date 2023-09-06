// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_server.event;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.web.commons_validation.payload.MessagePayload;
import de.egladil.web.commons_validation.payload.ResponsePayload;
import de.egladil.web.profil_server.domain.StoredEvent;
import de.egladil.web.profil_server.error.ProfilserverRuntimeException;
import de.egladil.web.profil_server.error.PropagationFailedException;
import de.egladil.web.profil_server.restclient.MkGatewayRestClient;

/**
 * ProfilEventHandler
 */
@ApplicationScoped
public class ProfilEventHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ProfilEventHandler.class);

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	@ConfigProperty(name = "mkv-app.client-id")
	private String clientId;

	@ConfigProperty(name = "syncInfrastructureAvailable", defaultValue = "true")
	String syncInfrastructureAvailable;

	@Inject
	EventRepository eventRepository;

	@Inject
	@RestClient
	MkGatewayRestClient mkGateway;

	public void handleSynchron(final ProfilEvent event) {

		this.handleEvent(event);
	}

	public void handleEvent(@Observes final ProfilEvent event) {

		try {

			String body = new ObjectMapper().writeValueAsString(event);

			LOG.debug("Event body = " + body);

			StoredEvent storedEvent = StoredEvent.createEvent(event.occuredOn(), event.typeName(), body);

			this.eventRepository.appendEvent(storedEvent);

			if (event.propagateToListeners()) {

				sendEventToListeners(event);
			}
		} catch (JsonProcessingException e) {

			throw new ProfilserverRuntimeException("konnte event nicht serialisieren: " + e.getMessage(), e);

		}

	}

	/**
	 * @param event
	 */
	private void sendEventToListeners(final ProfilEvent event) {

		if (!event.propagateToListeners()) {

			return;
		}

		if (!isSyncInfrastructureAvailable()) {

			LOG.info("Infrastruktur nicht verfuegbar: Ändern oder Löschen von {} wird nicht propagiert", event);

			return;
		}

		if (ProfilEvent.TYPE_USER_CHANGED.equals(event.typeName())) {

			propagateUserChanged(event);

		}

		if (ProfilEvent.TYPE_USER_DELETED.equals(event.typeName())) {

			propagateUserDeleted(event);
		}
	}

	private void propagateUserDeleted(final ProfilEvent event) {

		LOG.info("Sende Löschevent an mk-gateway");

		Response mkGatewayResponse = null;

		try {

			String syncToken = getSyncToken(event);

			if (syncToken == null) {

				LOG.error("Datensynchronisation hat keine Freigabe: syncToken ist null");
				throw new PropagationFailedException(applicationMessages.getString("deleteUser.propagation.failure"));
			}

			UserDeleted userDeleted = (UserDeleted) event;

			DeleteUserCommand command = DeleteUserCommand.fromEvent(userDeleted).withSyncToken(syncToken);

			mkGatewayResponse = mkGateway.propagateUserDeleted(command);

			LOG.debug("Antwort: " + mkGatewayResponse.getStatus());

			if (mkGatewayResponse.getStatus() != 200) {

				LOG.error("Status {} vom mk-gateway beim senden des Events {} ", mkGatewayResponse.getStatus(), userDeleted);
				throw new PropagationFailedException(applicationMessages.getString("deleteUser.propagation.failure"));
			}

		} catch (Exception e) {

			LOG.error("Konnte delete-event nicht propagieren: {} - {}", event, e.getMessage(), e);
			throw new PropagationFailedException(applicationMessages.getString("deleteUser.propagation.failure"));
		} finally {

			if (mkGatewayResponse != null) {

				mkGatewayResponse.close();
			}
		}
	}

	private void propagateUserChanged(final ProfilEvent event) {

		Response mkGatewayResponse = null;

		try {

			String syncToken = getSyncToken(event);

			if (syncToken == null) {

				LOG.error("Datensynchronisation hat keine Freigabe: syncToken ist null");
				throw new PropagationFailedException(applicationMessages.getString("changeUser.propagation.failure"));
			}

			UserChanged userChanged = (UserChanged) event;

			ChangeUserCommand command = new ChangeUserCommand(syncToken, userChanged);

			mkGatewayResponse = mkGateway.propagateUserChanged(command);

			if (mkGatewayResponse.getStatus() != 200) {

				LOG.error("Status {} vom mk-gateway beim senden des Events {} ", mkGatewayResponse.getStatus(), userChanged);
				throw new PropagationFailedException(applicationMessages.getString("changeUser.propagation.failure"));
			}

		} catch (Exception e) {

			LOG.error("Konnte change-event nicht propagieren: {} - {}", event, e.getMessage(), e);
			throw new PropagationFailedException(applicationMessages.getString("changeUser.propagation.failure"));
		} finally {

			if (mkGatewayResponse != null) {

				mkGatewayResponse.close();
			}
		}
	}

	/**
	 * @param event
	 */
	private String getSyncToken(final ProfilEvent event) {

		String nonce = UUID.randomUUID().toString();

		SyncHandshake handshake = new SyncHandshake(clientId, nonce);

		Response mkGatewayResponse = null;

		try {

			mkGatewayResponse = mkGateway.getSyncToken(handshake);

			ResponsePayload responsePayload = mkGatewayResponse.readEntity(ResponsePayload.class);

			MessagePayload messagePayload = responsePayload.getMessage();

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
