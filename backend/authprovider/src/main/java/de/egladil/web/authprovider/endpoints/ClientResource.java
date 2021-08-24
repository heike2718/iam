// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.endpoints;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.api.ClientInformation;
import de.egladil.web.authprovider.error.AuthException;
import de.egladil.web.authprovider.error.AuthRuntimeException;
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

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

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
	@Deprecated
	public JsonObject authenticateClientWithJsonObject(final OAuthClientCredentials clientCredentials) {

		try {

			validationDelegate.check(clientCredentials, OAuthClientCredentials.class);

			OAuthAccessTokenPayload payload = clientService.createClientAccessToken(clientCredentials);

			// {"message":{"level":"INFO","message":"OK"},"data":{"accessToken":"1592c584643346a0a163607a5d8fc9ec","expiresAt":1563087362150,
			// "nonce":"aiugdisg"}}
			JsonObject data = null;

			if (clientCredentials.getNonce() != null) {

				data = Json.createObjectBuilder().add("accessToken", payload.getAccessToken())
					.add("expiresAt", payload.getExpiresAt())
					.add("nonce", clientCredentials.getNonce())
					.build();
			} else {

				data = Json.createObjectBuilder().add("accessToken", payload.getAccessToken())
					.add("expiresAt", payload.getExpiresAt())
					.build();
			}

			JsonObject message = Json.createObjectBuilder().add("level", "INFO").add("message", "OK").build();

			JsonObject result = Json.createObjectBuilder().add("message", message).add("data", data).build();

			return result;
		} catch (InvalidInputException e) {

			JsonObject message = Json.createObjectBuilder().add("level", "ERROR").add("message", "ungültige Eingaben").build();
			return Json.createObjectBuilder().add("message", message).build();
		} catch (ClientAccessTokenRuntimeException e) {

			JsonObject message = Json.createObjectBuilder().add("level", "ERROR").add("message", "Client hat keine Berechtigung")
				.build();
			return Json.createObjectBuilder().add("message", message).build();
		} catch (AuthRuntimeException e) {

			LOG.error(e.getMessage(), e);
			JsonObject message = Json.createObjectBuilder().add("level", "ERROR")
				.add("message", applicationMessages.getString("general.internalServerError"))
				.build();
			return Json.createObjectBuilder().add("message", message).build();
		}
	}

	/**
	 * Authentisiert den Client und erzeugt ein OAuthAccessTokenPayload, welches ein Client-AccessToken enthält.
	 *
	 * @param  clientCredentials
	 * @return                   Response mit OAuthAccessTokenPayload-Data
	 */
	@POST
	@Path("/client/accesstoken/responsepayload")
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
