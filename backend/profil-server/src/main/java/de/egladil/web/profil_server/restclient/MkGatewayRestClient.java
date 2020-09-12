// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_server.restclient;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import de.egladil.web.profil_server.event.ChangeUserCommand;
import de.egladil.web.profil_server.event.DeleteUserCommand;
import de.egladil.web.profil_server.event.SyncHandshake;

/**
 * MkGatewayRestClient
 */
@RegisterRestClient
@Path("/sync")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface MkGatewayRestClient {

	@Path("/veranstalter")
	@POST
	Response propagateUserChanged(ChangeUserCommand command);

	@Path("/veranstalter")
	@DELETE
	Response propagateUserDeleted(DeleteUserCommand command);

	@Path("/ack")
	@POST
	Response getSyncToken(SyncHandshake handshake);

}
