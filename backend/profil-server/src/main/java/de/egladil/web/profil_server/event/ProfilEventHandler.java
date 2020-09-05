// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_server.event;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.web.profil_server.domain.StoredEvent;
import de.egladil.web.profil_server.error.ProfilserverRuntimeException;
import de.egladil.web.profil_server.restclient.MkGatewayRestClient;

/**
 * ProfilEventHandler
 */
@ApplicationScoped
public class ProfilEventHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ProfilEventHandler.class);

	@ConfigProperty(name = "mkv-app.client-id")
	private String clientId;

	@Inject
	EventRepository eventRepository;

	@Inject
	@RestClient
	MkGatewayRestClient mkGateway;

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

		if (!(event instanceof UserChanged)) {

			return;
		}

		try {

			UserChanged userChanged = (UserChanged) event;

			ChangeUserCommand command = new ChangeUserCommand(clientId, userChanged);

			Response mkGatewayResponse = mkGateway.propagateUserChanged(command);

			if (mkGatewayResponse.getStatus() != 200) {

				LOG.error("Status {} vom mk-gateway beim senden des Events {} ", mkGatewayResponse.getStatus(), userChanged);
			}

		} catch (Exception e) {

			LOG.error("Konnte change-event nicht propagieren: {} - {}", event, e.getMessage(), e);
		}
	}
}
