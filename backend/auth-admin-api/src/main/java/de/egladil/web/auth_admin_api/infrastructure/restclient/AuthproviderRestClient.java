// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.infrastructure.restclient;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import de.egladil.web.auth_admin_api.domain.auth.dto.OAuthClientCredentials;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * AuthproviderRestClient
 */
/**
 * InitAccessTokenRestClient
 */
@RegisterRestClient(configKey = "authprovider")
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AuthproviderRestClient {

	@POST
	@Path("clients/client/accesstoken")
	Response authenticateClient(OAuthClientCredentials clientSecrets);

	@PUT
	@Path("token/exchange/{oneTimeToken}")
	public Response exchangeOneTimeTokenWithJwt(@PathParam(
		value = "oneTimeToken") final String oneTimeToken, final OAuthClientCredentials clientCredentials);

}
