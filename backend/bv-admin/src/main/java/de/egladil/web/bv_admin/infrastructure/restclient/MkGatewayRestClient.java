// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.infrastructure.restclient;

import java.time.temporal.ChronoUnit;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import de.egladil.web.bv_admin.domain.benutzer.DeleteUserCommand;
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
@RegisterRestClient(configKey = "mkgateway")
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface MkGatewayRestClient {

	@Path("sync/veranstalter")
	@DELETE
	@Retry(maxRetries = 3, delay = 1000)
	@Timeout(value = 10, unit = ChronoUnit.SECONDS)
	Response propagateUserDeleted(DeleteUserCommand command);

	@Path("sync/ack")
	@POST
	@Retry(maxRetries = 3, delay = 1000)
	@Timeout(value = 10, unit = ChronoUnit.SECONDS)
	Response getSyncToken(SyncHandshake handshake);
}
