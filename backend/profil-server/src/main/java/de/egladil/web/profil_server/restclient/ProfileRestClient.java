// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_server.restclient;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import de.egladil.web.profil_server.payload.ChangeProfileDataPayload;
import de.egladil.web.profil_server.payload.ChangeProfilePasswordPayload;
import de.egladil.web.profil_server.payload.SelectProfilePayload;

/**
 * ProfileRestClient die Base-URI ist [auth-url]/profiles
 */
@RegisterRestClient
@Path("profiles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ProfileRestClient {

	@POST
	@Path("/profile")
	Response getUserProfile(final SelectProfilePayload selectProfilePayload);

	@DELETE
	@Path("/profile")
	Response deleteProfile(final SelectProfilePayload selectProfilePayload);

	@PUT
	@Path("/profile/password")
	public Response changePassword(final ChangeProfilePasswordPayload payload);

	@PUT
	@Path("/profile/data")
	public Response changeData(final ChangeProfileDataPayload payload);
}
