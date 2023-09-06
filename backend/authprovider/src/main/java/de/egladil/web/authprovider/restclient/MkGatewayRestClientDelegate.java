// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.restclient;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import de.egladil.web.authprovider.event.CreateUserCommand;
import de.egladil.web.authprovider.event.DeleteUserCommand;
import de.egladil.web.authprovider.event.SyncHandshake;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

/**
 * MkGatewayRestClientDelegate
 */
@ApplicationScoped
public class MkGatewayRestClientDelegate {

	@Inject
	@RestClient
	MkGatewayRestClient mkGateway;

	/**
	 * @param  handshake
	 * @return
	 */
	public Response getSyncToken(final SyncHandshake handshake) {

		return mkGateway.getSyncToken(handshake);
	}

	/**
	 * @param  command
	 * @return
	 */
	public Response propagateUserCreated(final CreateUserCommand command) {

		return mkGateway.propagateUserCreated(command);
	}

	/**
	 * @param  command
	 * @return
	 */
	public Response propagateUserDeleted(final DeleteUserCommand command) {

		return mkGateway.propagateUserDeleted(command);
	}
}
