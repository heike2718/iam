// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.restclient;

import java.time.temporal.ChronoUnit;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

import de.egladil.web.authprovider.event.ChangeUserCommand;
import de.egladil.web.authprovider.event.CreateUserCommand;
import de.egladil.web.authprovider.event.DeleteUserCommand;
import de.egladil.web.authprovider.event.SyncHandshake;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * MkGatewayRestClient
 */
@RegisterRestClient(configKey = "mkgateway")
@Path("sync")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface MkGatewayRestClient {

	@Path("veranstalter")
	@DELETE
	@Retry(maxRetries = 3, delay = 1000, abortOn = ClientWebApplicationException.class)
	@Timeout(value = 10, unit = ChronoUnit.SECONDS)
	Response propagateUserDeleted(DeleteUserCommand command);

	@Path("veranstalter")
	@POST
	@Retry(maxRetries = 3, delay = 1000, abortOn = ClientWebApplicationException.class)
	@Timeout(value = 10, unit = ChronoUnit.SECONDS)
	Response propagateUserCreated(CreateUserCommand command);

	@Path("veranstalter")
	@PUT
	@Retry(maxRetries = 3, delay = 1000, abortOn = ClientWebApplicationException.class)
	@Timeout(value = 10, unit = ChronoUnit.SECONDS)
	Response propagateUserChanged(ChangeUserCommand command);

	@Path("ack")
	@POST
	@Retry(maxRetries = 3, delay = 1000, abortOn = ClientWebApplicationException.class)
	@Timeout(value = 10, unit = ChronoUnit.SECONDS)
	Response getSyncToken(SyncHandshake handshake);
}
