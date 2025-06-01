// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.endpoints;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.auth_validations.annotations.ClientId;
import de.egladil.web.auth_validations.annotations.UuidString;
import de.egladil.web.auth_validations.dto.OAuthClientCredentials;
import de.egladil.web.auth_validations.exceptions.InvalidInputException;
import de.egladil.web.authprovider.entities.Client;
import de.egladil.web.authprovider.entities.ResourceOwner;
import de.egladil.web.authprovider.error.ClientAccessTokenNotFoundException;
import de.egladil.web.authprovider.error.MailversandException;
import de.egladil.web.authprovider.event.AuthproviderEventHandler;
import de.egladil.web.authprovider.event.BotAttackEvent;
import de.egladil.web.authprovider.event.BotAttackEventPayload;
import de.egladil.web.authprovider.payload.BannedEmailsResponseDto;
import de.egladil.web.authprovider.payload.ClientCredentials;
import de.egladil.web.authprovider.payload.MessagePayload;
import de.egladil.web.authprovider.payload.ResponsePayload;
import de.egladil.web.authprovider.payload.SignUpCredentials;
import de.egladil.web.authprovider.payload.SignUpLogInResponseData;
import de.egladil.web.authprovider.service.AuthJWTService;
import de.egladil.web.authprovider.service.ClientService;
import de.egladil.web.authprovider.service.RegistrationService;
import de.egladil.web.authprovider.service.ResourceOwnerService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;

/**
 * UserResource stellt REST-Endpoints für die Verwaltung von ResourceOwnern nur Verfügung.
 */
@RequestScoped
@Path("api/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

	private static final Logger LOG = LoggerFactory.getLogger(UserResource.class);

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	@ConfigProperty(name = "admin.clientIds")
	String adminClientIds;

	@ConfigProperty(name = "signup.response.uri")
	String signUpResponseUri;

	@Context
	ContainerRequestContext requestContext;

	@Inject
	ClientService clientService;

	@Inject
	RegistrationService registrationService;

	@Inject
	ResourceOwnerService resourceOwnerService;

	@Inject
	AuthJWTService authJWTService;

	@Context
	SecurityContext securityContext;

	@Inject
	AuthproviderEventHandler eventHandler;

	@GET
	@Path("/banned-emails")
	@Operation(operationId = "getBannedUsers", summary = "Gibt eine Liste mit UUIDs zurück, die vom Mailversand ausgeschlossen wurden.")
	@Parameters({ @Parameter(in = ParameterIn.HEADER, name = "X-CLIENT-ID", description = "ID des anfragenden Clients"),
		@Parameter(in = ParameterIn.HEADER, name = "X-CLIENT-SECRET", description = "Decret des anfragenden Clients"),
		@Parameter(in = ParameterIn.HEADER, name = "X-NONCE", description = "nonce, das ungeändert wieder zurückgegeben wird."), })
	@APIResponse(name = "OKResponse", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String[].class)))
	@APIResponse(name = "BadRequestResponse", responseCode = "400", description = "fehlgeschlagene Input-Validierung", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(name = "NotAuthorized", responseCode = "401", description = "fehlgeschlagene Autorisierung des Clients", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(name = "ServerError", description = "server error", responseCode = "500", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BannedEmailsResponseDto.class)))
	public Response getBannedEmails(@HeaderParam(value = "X-CLIENT-ID")
	@NotBlank
	@ClientId
	@Size(max = 50)
	String clientId, @HeaderParam(value = "X-CLIENT-SECRET")
	@NotBlank
	@ClientId
	@Size(max = 50)
	String clientSecretString, @HeaderParam(value = "X-NONCE")
	@UuidString
	@Size(max = 36)
	String nonce) {

		clientService.authorizeClient(OAuthClientCredentials.create(clientId, clientSecretString, nonce));
		List<String> usersBannedForMails = resourceOwnerService.getBannedEmails();

		BannedEmailsResponseDto responsePayload = new BannedEmailsResponseDto();
		responsePayload.setBannedUUIDs(usersBannedForMails);
		responsePayload.setNonce(nonce);

		return Response.ok(responsePayload).build();
	}

	/**
	 * Endpoint, der einen neuen ResourceOwner anlegt. Der Response enthält im body ein JWT.
	 *
	 * @param signUpCredentials SignUpCredentials
	 * @return Response
	 */
	@POST
	@Path("/signup")
	@Operation(operationId = "signUp", summary = "Legt ein neues Benutzerkonto an")
	@APIResponse(name = "OKResponse", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignUpLogInResponseData.class)))
	@APIResponse(name = "BadRequestResponse", responseCode = "400", description = "fehlgeschlagene Input-Validierung", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(name = "ServerError", description = "server error", responseCode = "500", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePayload.class)))
	public Response signUp(@Valid
	final SignUpCredentials signUpCredentials, @Context
	final UriInfo uriInfo) {

		String kleber = signUpCredentials.getKleber();

		if (StringUtils.isNotBlank(kleber)) {

			BotAttackEventPayload payload = new BotAttackEventPayload().withPath(uriInfo.getPath()).withKleber(kleber)
				.withLoginName(signUpCredentials.getEmail()).withPasswort(signUpCredentials.getZweiPassworte().getPasswort())
				.withRedirectUrl(signUpCredentials.getClientCredentials().getRedirectUrl());

			this.eventHandler.handleEvent(new BotAttackEvent(payload));

			return Response.status(401).entity(MessagePayload.error(applicationMessages.getString("general.badRequest"))).build();
		}

		try {

			Client client = clientService.findAndCheckClient(signUpCredentials.getClientCredentials());

			ResourceOwner resourceOwner = registrationService.createNewResourceOwner(client, signUpCredentials, uriInfo);

			SignUpLogInResponseData data = authJWTService.createAndStoreAuthorization(resourceOwner,
				signUpCredentials.getClientCredentials(), signUpCredentials.getNonce());

			URI uri = new URI(signUpResponseUri + data.getIdToken());

			return Response.created(uri).entity(data).build();

		} catch (MailversandException e) {

			this.resourceOwnerService.deleteResourceOwnerQuietly(signUpCredentials.getEmail());
			throw new InvalidInputException(applicationMessages.getString("email.invalid"));
		} catch (URISyntaxException | ClientAccessTokenNotFoundException e) {

			LOG.error(e.getMessage());
			return Response.serverError().build();
		} finally {

			signUpCredentials.clean();
		}
	}
}
