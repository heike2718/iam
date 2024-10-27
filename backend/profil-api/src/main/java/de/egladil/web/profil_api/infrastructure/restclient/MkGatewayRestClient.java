// =====================================================
// Project: profil-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_api.infrastructure.restclient;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import de.egladil.web.profil_api.domain.event.ChangeUserCommand;
import de.egladil.web.profil_api.domain.event.DeleteUserCommand;
import de.egladil.web.profil_api.domain.event.SyncHandshake;
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
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface MkGatewayRestClient {

	@Path("veranstalter")
	@PUT
	Response propagateUserChanged(ChangeUserCommand command);

	@Path("veranstalter")
	@DELETE
	Response propagateUserDeleted(DeleteUserCommand command);

	@Path("ack")
	@POST
	Response getSyncToken(SyncHandshake handshake);
}
