// =====================================================
// Project: benutzerprofil
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.benutzerprofil.infrastructure.restclient;

import java.time.temporal.ChronoUnit;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

import de.egladil.web.auth_validations.dto.OAuthClientCredentials;
import de.egladil.web.benutzerprofil.domain.benutzer.ChangeProfileDataPayload;
import de.egladil.web.benutzerprofil.domain.benutzer.SelectProfilePayload;
import de.egladil.web.benutzerprofil.domain.passwort.ChangeProfilePasswordPayload;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
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
@RegisterRestClient(configKey = "authprovider")
@Path("api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AuthproviderRestClient {

	@POST
	@Path("clients/client/accesstoken")
	@Retry(maxRetries = 3, delay = 1000)
	@Timeout(value = 10, unit = ChronoUnit.SECONDS)
	Response authenticateClient(OAuthClientCredentials clientSecrets);

	@PUT
	@Path("token/exchange/{oneTimeToken}")
	@Retry(maxRetries = 3, delay = 1000)
	@Timeout(value = 10, unit = ChronoUnit.SECONDS)
	public Response exchangeOneTimeTokenWithJwt(@PathParam(value = "oneTimeToken")
	final String oneTimeToken, final OAuthClientCredentials clientCredentials);

	@POST // wegen sensibler Daten, die nur im Body übertragen werden können. Wäre sonst GET
	@Path("profiles/profile")
	@Retry(maxRetries = 3, delay = 1000)
	@Timeout(value = 10, unit = ChronoUnit.SECONDS)
	Response getUserProfile(final SelectProfilePayload selectProfilePayload);

	@DELETE
	@Path("profiles/profile")
	@Retry(maxRetries = 3, delay = 1000)
	@Timeout(value = 10, unit = ChronoUnit.SECONDS)
	Response deleteProfile(final SelectProfilePayload selectProfilePayload);

	@PUT
	@Path("profiles/profile/password")
	@Retry(maxRetries = 3, delay = 1000, abortOn = ClientWebApplicationException.class)
	@Timeout(value = 10, unit = ChronoUnit.SECONDS)
	public Response changePassword(final ChangeProfilePasswordPayload payload);

	@PUT
	@Path("profiles/profile/data")
	@Retry(maxRetries = 3, delay = 1000, abortOn = ClientWebApplicationException.class)
	@Timeout(value = 10, unit = ChronoUnit.SECONDS)
	public Response changeData(final ChangeProfileDataPayload payload);
}
