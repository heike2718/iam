// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.events;

import java.util.Map;
import java.util.UUID;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.auth_admin_api.domain.auth.dto.MessagePayload;
import de.egladil.web.auth_admin_api.domain.auth.dto.ResponsePayload;
import de.egladil.web.auth_admin_api.domain.benutzer.DeleteUserCommand;
import de.egladil.web.auth_admin_api.domain.exceptions.CommandPropagationFailedException;
import de.egladil.web.auth_admin_api.infrastructure.restclient.HandshakeAck;
import de.egladil.web.auth_admin_api.infrastructure.restclient.MkGatewayRestClient;
import de.egladil.web.auth_admin_api.infrastructure.restclient.SyncHandshake;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

/**
 * PropagateEventService
 */
@ApplicationScoped
public class PropagateEventService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PropagateEventService.class);

	@ConfigProperty(name = "sync.infrastructure.available")
	boolean syncInfrastructureAvailable;

	@ConfigProperty(name = "mkv-app.client-id")
	private String mkGatewayClientId;

	@ConfigProperty(name = "stage")
	private String stage;

	@Inject
	@RestClient
	MkGatewayRestClient mkGatewayRestClient;

	/**
	 * Propagiert das Löschen eines Benutzers an mk-gateway, damit der zugehörige Veranstalter gelöscht wird.
	 *
	 * @param  uuid
	 *                                           String die UUID des USERS
	 * @throws CommandPropagationFailedException
	 *                                           wenn das nicht geklappt hat.
	 */
	public void propagateDeleteUserToMkGateway(final String uuid) throws CommandPropagationFailedException {

		if (!syncInfrastructureAvailable) {

			LOGGER.warn("Sync mit mk-gateway ist deaktiviert. Loeschen des USERS {} wird nicht propagiert", uuid);
			return;
		}

		String syncToken = getSyncToken();

		if (syncToken == null) {

			String message = "Datensynchronisation mit mk-gateway hat keine Freigabe: syncToken ist null";
			LOGGER.error(message);
			// this.sendeInfoAnMichQuietly(MinikaengurukontenMailKontext.SYNC_FAILED, resourceOwner);
			throw new CommandPropagationFailedException(message);
		}

		LOGGER.debug("sync ack erhalten: {}", syncToken);

		try (Response mkGatewayResponse = mkGatewayRestClient
			.propagateUserDeleted(DeleteUserCommand.create(uuid).withSyncToken(syncToken))) {

			if (mkGatewayResponse.getStatus() != 200) {

				LOGGER.error("Status {} vom mk-gateway beim Senden des DeleteUserCommands {} ", mkGatewayResponse.getStatus(),
					uuid);
				// this.sendeInfoAnMichQuietly(MinikaengurukontenMailKontext.SYNC_FAILED, resourceOwner);
				String message = "Datensynchronisation mit mk-gateway fehlgeschlagen";
				throw new CommandPropagationFailedException(message);
			}

		} catch (WebApplicationException e) {

			LOGGER.error("response.status={} - {}", e.getResponse().getStatus(), e.getMessage(), e);
			// this.sendeInfoAnMichQuietly(MinikaengurukontenMailKontext.SYNC_FAILED, resourceOwner);
			throw new CommandPropagationFailedException("WebapplicationException vom mk-gateway: " + e.getMessage(), e);

		} catch (ProcessingException e) {

			LOGGER.error("endpoint mk-gateway ist nicht erreichbar");
			// this.sendeInfoAnMichQuietly(MinikaengurukontenMailKontext.SYNC_FAILED, resourceOwner);
			throw new CommandPropagationFailedException("Der Endpoint mk-gateway ist nicht erreichbar. ");

		}
	}

	/**
	 * @param event
	 */
	private String getSyncToken() {

		String nonce = UUID.randomUUID().toString();

		SyncHandshake handshake = new SyncHandshake(mkGatewayClientId, nonce);

		LOGGER.debug("mkGatewayClientId={}", mkGatewayClientId);

		try (Response mkGatewayResponse = mkGatewayRestClient.getSyncToken(handshake)) {

			LOGGER.info("sync: mkGatewayResponse.status={}", mkGatewayResponse.getStatus());

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

}
