// =====================================================
// Projekt: authenticationprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.endpoints;

import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import de.egladil.web.authprovider.entities.ResourceOwner;
import de.egladil.web.authprovider.event.AuthproviderEventHandler;
import de.egladil.web.authprovider.event.BotAttackEvent;
import de.egladil.web.authprovider.event.BotAttackEventPayload;
import de.egladil.web.authprovider.payload.LoginCredentials;
import de.egladil.web.authprovider.payload.MessagePayload;
import de.egladil.web.authprovider.payload.OAuthAccessTokenPayload;
import de.egladil.web.authprovider.payload.ResponsePayload;
import de.egladil.web.authprovider.payload.SignUpLogInResponseData;
import de.egladil.web.authprovider.service.AuthJWTService;
import de.egladil.web.authprovider.service.AuthenticationService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

/**
 * AuthenticationResource stellt REST-Endpoints zur Authentisierung von ResourceOwnern zur Verfügung.
 */
@RequestScoped
@Path("api/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationResource {

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	@Inject
	AuthenticationService authenticationService;

	@Inject
	AuthJWTService authJWTService;

	@Inject
	AuthproviderEventHandler eventHandler;

	/**
	 * Authentifiziert den Benutzer mit Loginname / Email und Passwort. Gibt als SignUpLogInResponseData.idToken ein
	 * oneTimeToken zurück, mit dem der Server des anfragenden Clients das JWT abholen kann.
	 *
	 * @param credentials
	 * @return Resonse mit SignUpLogInResponseData
	 */
	@POST
	@Path("sessions/auth-token-grant")
	@Operation(operationId = "authenticateUserWithTokenExchangeTypeAuthTokenGrant", summary = "Authentifiziert den Benutzer	")
	@Parameters({
		@Parameter(in = ParameterIn.QUERY, name = "accessToken", description = "Authentifiziert den Benutzer mit Loginname / Email und Passwort. Gibt als SignUpLogInResponseData.idToken ein oneTimeToken zurück, mit dem der Server des anfragenden Clients das JWT abholen kann."), })
	@APIResponse(name = "OKResponse", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignUpLogInResponseData.class)))
	@APIResponse(name = "BadRequestResponse", responseCode = "400", description = "fehlgeschlagene Input-Validierung", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(name = "NotAuthorized", responseCode = "401", description = "Client credentials stimmen nicht", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(name = "ServerError", description = "server error", responseCode = "500", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePayload.class)))
	public Response authenticateUserWithTokenExchangeTypeAuthTokenGrant(@Valid
	final LoginCredentials credentials, @Context
	final UriInfo uriInfo) {

		try {

			String kleber = credentials.getAuthorizationCredentials().getKleber();

			if (StringUtils.isNotBlank(kleber)) {

				BotAttackEventPayload payload = new BotAttackEventPayload().withPath(uriInfo.getPath()).withKleber(kleber)
					.withLoginName(credentials.getAuthorizationCredentials().getLoginName())
					.withPasswort(credentials.getAuthorizationCredentials().getPasswort())
					.withRedirectUrl(credentials.getClientCredentials().getRedirectUrl());

				this.eventHandler.handleEvent(new BotAttackEvent(payload));

				return Response.status(401).entity(MessagePayload.error(applicationMessages.getString("general.notAuthenticated")))
					.build();
			}

			ResourceOwner resourceOwner = authenticationService
				.authenticateResourceOwner(credentials.getAuthorizationCredentials());

			SignUpLogInResponseData data = authJWTService.createAndStoreAuthorization(resourceOwner,
				credentials.getClientCredentials(), null);

			return Response.ok(data).build();

		} finally {

			credentials.getAuthorizationCredentials().clean();
		}
	}
}
