// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.endpoints;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.auth_validations.annotations.InputSecured;
import de.egladil.web.auth_validations.annotations.UuidString;
import de.egladil.web.auth_validations.dto.OAuthClientCredentials;
import de.egladil.web.authprovider.api.ClientInformation;
import de.egladil.web.authprovider.error.AuthException;
import de.egladil.web.authprovider.error.ClientAccessTokenNotFoundException;
import de.egladil.web.authprovider.error.ClientAccessTokenRuntimeException;
import de.egladil.web.authprovider.error.LogmessagePrefixes;
import de.egladil.web.authprovider.payload.ClientCredentials;
import de.egladil.web.authprovider.payload.MessagePayload;
import de.egladil.web.authprovider.payload.OAuthAccessTokenPayload;
import de.egladil.web.authprovider.payload.ResponsePayload;
import de.egladil.web.authprovider.service.ClientService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * ClientResource stellt REST-Endpoints für die Clients zur Verfügung, mit denen sie sich die nicht geheimen Infos
 * abholen können, die über sie in der DB stehen. Zur Authentisierung dient die clientId und eine redirect-URL.
 */
@RequestScoped
@Path("api/clients")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClientResource {

	private static final Logger LOG = LoggerFactory.getLogger(ClientResource.class);

	@Inject
	ClientService clientService;

	@GET
	// @formatter:off
	public Response getClient(
		@NotBlank
		@UuidString
		@Size(max = 50) @QueryParam("accessToken") final String accessToken,
		@NotBlank
		@URL @QueryParam("redirectUrl") final String redirectUrl,
		@InputSecured @Size(max = 150) @QueryParam("state") final String state) {
		// @formatter:on

		LOG.debug("accessToken={}", accessToken);
		final ClientCredentials clientCredentials = ClientCredentials.createWithState(accessToken, redirectUrl, state);

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

	@GET
	@Path("v2")
	// @formatter:off
	public Response getClientV2(
		@NotBlank
		@UuidString
		@Size(max = 50) @QueryParam("accessToken") final String accessToken,
		@NotBlank
		@URL @QueryParam("redirectUrl") final String redirectUrl,
		@InputSecured @Size(max = 150) @QueryParam("state") final String state) {
		// @formatter:on

		LOG.debug("accessToken={}", accessToken);
		final ClientCredentials clientCredentials = ClientCredentials.createWithState(accessToken, redirectUrl, state);

		try {

			ClientInformation data = clientService.getClientInformation(clientCredentials);

			LOG.debug("Client {} hat Daten geholt", data.getName());
			return Response.ok().entity(data).build();
		} catch (ClientAccessTokenNotFoundException e) {

			LOG.warn(LogmessagePrefixes.BOT + ":Keine Daten mit accessToken {} vorhanden", accessToken);

			throw new ClientAccessTokenRuntimeException(clientCredentials);

		}
	}

	/**
	 * Authentisiert den Client und erzeugt ein OAuthAccessTokenPayload, welches ein Client-AccessToken enthält.
	 *
	 * @param clientCredentials
	 * @return Response mit OAuthAccessTokenPayload-Data
	 */
	@POST
	@Path("client/accesstoken")
	public Response authenticateClient(@Valid
	final OAuthClientCredentials clientCredentials) {

		try {

			OAuthAccessTokenPayload payload = clientService.createClientAccessToken(clientCredentials);

			if (clientCredentials.getNonce() != null) {

				payload.setNonce(clientCredentials.getNonce());
			}

			ResponsePayload responsePayload = new ResponsePayload(MessagePayload.ok(), payload);

			return Response.ok(responsePayload).build();
		} catch (ClientAccessTokenRuntimeException e) {

			throw new AuthException(
				"Der Client mit ID=" + StringUtils.abbreviate(clientCredentials.getClientId(), 15) + " hat keine Berechtigung");
		}
	}
}
