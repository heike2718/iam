// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.restclient;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import de.egladil.web.authprovider.event.CreateUserCommand;
import de.egladil.web.authprovider.event.DeleteUserCommand;
import de.egladil.web.authprovider.event.SyncHandshake;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * MkGatewayRestClient
 */
@RegisterRestClient(configKey = "mk-gateway")
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface MkGatewayRestClient {

	@Path("sync/veranstalter")
	@DELETE
	@Retry(maxRetries = 3, delay = 1000)
	Response propagateUserDeleted(DeleteUserCommand command);

	@Path("sync/veranstalter")
	@POST
	@Retry(maxRetries = 3, delay = 1000)
	Response propagateUserCreated(CreateUserCommand command);

	@Path("sync/ack")
	@POST
	@Retry(maxRetries = 3, delay = 1000)
	Response getSyncToken(SyncHandshake handshake);
}
