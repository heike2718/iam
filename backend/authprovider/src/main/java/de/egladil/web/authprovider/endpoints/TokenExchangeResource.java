// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.endpoints;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.auth_validations.annotations.UuidString;
import de.egladil.web.auth_validations.dto.ExchangeTokenResponse;
import de.egladil.web.auth_validations.dto.OAuthClientCredentials;
import de.egladil.web.authprovider.domain.Client;
import de.egladil.web.authprovider.payload.MessagePayload;
import de.egladil.web.authprovider.payload.ResponsePayload;
import de.egladil.web.authprovider.service.AuthJWTService;
import de.egladil.web.authprovider.service.ClientService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * TokenExchangeResource
 */
@RequestScoped
@Path("api/token")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TokenExchangeResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(TokenExchangeResource.class);

	@Inject
	AuthJWTService authJWTService;

	@Inject
	ClientService clientService;

	/**
	 * Tauscht das OneTimeToken gegen das generierte JWT um.
	 *
	 * @param oneTimeToken
	 * @param clientCredentials OAuthClientCredentials
	 * @return ExchangeTokenResponse mit dem JWT und dem gespiegelten nonce. Das JWT enthält die Daten des
	 * ResourceOwners: also UUID, Rolle, FullName und Email.
	 */
	@PUT
	@Path("/exchange/{oneTimeToken}")
	public Response exchangeOneTimeTokenWithJwt(@PathParam(value = "oneTimeToken")
	@UuidString
	final String oneTimeToken, final OAuthClientCredentials clientCredentials) {

		try {

			Client client = this.clientService.authorizeClient(clientCredentials);

			String jwt = this.authJWTService.exchangeTheOneTimeToken(oneTimeToken, client);

			ResponsePayload responsePayload = new ResponsePayload(MessagePayload.ok(),
				ExchangeTokenResponse.create(jwt, clientCredentials.getNonce()));

			LOGGER.info("OTT exchanged:{}", responsePayload.getMessage().getLevel());

			return Response.ok(responsePayload).build();
		} finally {

			clientCredentials.clean();
		}
	}
}
