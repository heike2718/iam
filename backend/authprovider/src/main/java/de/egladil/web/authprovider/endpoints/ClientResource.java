// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.endpoints;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.api.ClientInformation;
import de.egladil.web.authprovider.error.AuthException;
import de.egladil.web.authprovider.error.ClientAccessTokenNotFoundException;
import de.egladil.web.authprovider.error.ClientAccessTokenRuntimeException;
import de.egladil.web.authprovider.error.LogmessagePrefixes;
import de.egladil.web.authprovider.payload.ClientCredentials;
import de.egladil.web.authprovider.payload.OAuthAccessTokenPayload;
import de.egladil.web.authprovider.service.ClientService;
import de.egladil.web.commons_validation.ValidationDelegate;
import de.egladil.web.commons_validation.exception.InvalidInputException;
import de.egladil.web.commons_validation.payload.MessagePayload;
import de.egladil.web.commons_validation.payload.OAuthClientCredentials;
import de.egladil.web.commons_validation.payload.ResponsePayload;

/**
 * ClientResource stellt REST-Endpoints für die Clients zur Verfügung, mit denen sie sich die nicht geheimen Infos
 * abholen können, die über sie in der DB stehen. Zur Authentisierung dient die clientId und eine redirect-URL.
 */
@RequestScoped
@Path("/clients")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClientResource {

	private static final Logger LOG = LoggerFactory.getLogger(ClientResource.class);

	@Inject
	ClientService clientService;

	private final ValidationDelegate validationDelegate = new ValidationDelegate();

	@GET
	// @formatter:off
	public Response getClient(
		@QueryParam("accessToken") final String accessToken,
		@QueryParam("redirectUrl") final String redirectUrl,
		@QueryParam("state") final String state) {
		// @formatter:on

		LOG.debug("accessToken={}", accessToken);
		final ClientCredentials clientCredentials = ClientCredentials.createWithState(accessToken, redirectUrl, state);

		validationDelegate.check(clientCredentials, ClientCredentials.class);

		try {

			ClientInformation data = clientService.getClientInformation(clientCredentials);

			LOG.debug("Client {} hat Daten geholt", data.getName());

			ResponsePayload payload = new ResponsePayload(MessagePayload.info("client OK"), data);
			return Response.ok().entity(payload).build();
		} catch (ClientAccessTokenNotFoundException e) {

			LOG.warn(LogmessagePrefixes.BOT + ":Keine Daten mit accessToken {} vorhanden", accessToken);

			throw new ClientAccessTokenRuntimeException(clientCredentials);

		}
	}

	/**
	 * Authentisiert den Client und erzeugt ein OAuthAccessTokenPayload, welches ein Client-AccessToken enthält.
	 *
	 * @param  clientCredentials
	 * @return                   Response mit OAuthAccessTokenPayload-Data
	 */
	@POST
	@Path("/client/accesstoken")
	public Response authenticateClient(final OAuthClientCredentials clientCredentials) {

		try {

			validationDelegate.check(clientCredentials, OAuthClientCredentials.class);

			OAuthAccessTokenPayload payload = clientService.createClientAccessToken(clientCredentials);

			if (clientCredentials.getNonce() != null) {

				payload.setNonce(clientCredentials.getNonce());
			}

			ResponsePayload responsePayload = new ResponsePayload(MessagePayload.ok(), payload);

			return Response.ok(responsePayload).build();
		} catch (InvalidInputException e) {

			throw new InvalidInputException(ResponsePayload.messageOnly(MessagePayload.error("ungültige eingaben")));
		} catch (ClientAccessTokenRuntimeException e) {

			throw new AuthException(
				"Der Client mit ID=" + StringUtils.abbreviate(clientCredentials.getClientId(), 15) + " hat keine Berechtigung");
		}
	}
}
