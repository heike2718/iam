// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.endpoints;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import de.egladil.web.authprovider.domain.Client;
import de.egladil.web.authprovider.service.AuthorizationService;
import de.egladil.web.authprovider.service.ClientService;
import de.egladil.web.commons_validation.annotations.UuidString;
import de.egladil.web.commons_validation.payload.ExchangeTokenResponse;
import de.egladil.web.commons_validation.payload.MessagePayload;
import de.egladil.web.commons_validation.payload.OAuthClientCredentials;
import de.egladil.web.commons_validation.payload.ResponsePayload;

/**
 * TokenExchangeResource
 */
@RequestScoped
@Path("/token")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TokenExchangeResource {

	@Inject
	AuthorizationService authorizationService;

	@Inject
	ClientService clientService;

	/**
	 * Tauscht das OneTimeToken gegen das generierte JWT um.
	 *
	 * @param  oneTimeToken
	 * @param  clientCredentials
	 *                           OAuthClientCredentials
	 * @return                   ExchangeTokenResponse mit dem JWT und dem gespiegelten nonce. Das JWT enthält die Daten des
	 *                           ResourceOwners: also
	 *                           UUID, Rolle, FullName und Email.
	 */
	@PUT
	@Path("/exchange/{oneTimeToken}")
	public Response exchangeOneTimeTokenWithJwt(@PathParam(
		value = "oneTimeToken") @UuidString final String oneTimeToken, final OAuthClientCredentials clientCredentials) {

		try {

			Client client = this.clientService.authorizeClient(clientCredentials);

			String jwt = this.authorizationService.exchangeTheOneTimeToken(oneTimeToken, client);

			ResponsePayload responsePayload = new ResponsePayload(MessagePayload.ok(),
				ExchangeTokenResponse.create(jwt, clientCredentials.getNonce()));

			return Response.ok(responsePayload).build();
		} finally {

			clientCredentials.clean();
		}
	}
}
