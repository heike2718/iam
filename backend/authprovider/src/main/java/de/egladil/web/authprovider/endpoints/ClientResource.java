// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.endpoints;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
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
import de.egladil.web.authprovider.payload.SignUpLogInResponseData;
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
	@Path("v2")
	@Operation(operationId = "getClientInformation", summary = "Läd die Infos über den Client für die Loginmaske. ")
	@Parameters({
		@Parameter(in = ParameterIn.QUERY, name = "accessToken", description = "Ein zuvor über POST authprovider/api/clients/client/accesstoken angefordertes OTT.", required = true),
		@Parameter(in = ParameterIn.QUERY, name = "redirectUrl", description = "Die Redirect-URL für den anfragenden Client. Ist das Secret in der Kommunikation.", required = true),
		@Parameter(in = ParameterIn.QUERY, name = "state", description = "hat seine Bedeutung irgendwie verloren, hängt aber in allen Client-Anwendungen drin."),})
	@APIResponse(name = "OKResponse", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientInformation.class)))
	@APIResponse(name = "BadRequestResponse", responseCode = "400", description = "fehlgeschlagene Input-Validierung", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(name = "SessionExpired", responseCode = "908", description = "Das accessToken hat eine Zeitbegrenzung und die ist abgelaufen.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePayload.class)))
	@APIResponse(name = "ServerError", description = "server error", responseCode = "500", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePayload.class)))
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
	@Operation(operationId = "authenticateClient", summary = "Authentifiziert den Client")
	@Parameters({
		@Parameter(in = ParameterIn.QUERY, name = "accessToken", description = "Ein zuvor über POST authprovider/api/clients/client/accesstoken angefordertes OTT."), })
	@APIResponse(name = "OKResponse", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OAuthAccessTokenPayload.class)))
	@APIResponse(name = "BadRequestResponse", responseCode = "400", description = "fehlgeschlagene Input-Validierung", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(name = "NotAuthorized", responseCode = "401", description = "Client credentials stimmen nicht", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(name = "ServerError", description = "server error", responseCode = "500", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePayload.class)))
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
