// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.endpoints;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.auth_validations.dto.NoncePayload;
import de.egladil.web.auth_validations.dto.OAuthClientCredentials;
import de.egladil.web.authprovider.entities.ResourceOwner;
import de.egladil.web.authprovider.error.AuthException;
import de.egladil.web.authprovider.payload.MessagePayload;
import de.egladil.web.authprovider.payload.ResponsePayload;
import de.egladil.web.authprovider.payload.TempPasswordV2ResponseDto;
import de.egladil.web.authprovider.payload.User;
import de.egladil.web.authprovider.payload.profile.ChangeProfileDataPayload;
import de.egladil.web.authprovider.payload.profile.ChangeProfilePasswordPayload;
import de.egladil.web.authprovider.payload.profile.SelectProfilePayload;
import de.egladil.web.authprovider.service.ClientService;
import de.egladil.web.authprovider.service.ResourceOwnerService;
import de.egladil.web.authprovider.service.profile.ChangeDataService;
import de.egladil.web.authprovider.service.profile.ChangePasswordService;
import de.egladil.web.authprovider.service.profile.DeleteAccountService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * BenutzerprofilResource ist der Endpoint, der vom profileservice verwendet wird, um die Daten eines Users zu ändern.
 */
@RequestScoped
@Path("api/profiles")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BenutzerprofilResource {

	private static final Logger LOG = LoggerFactory.getLogger(BenutzerprofilResource.class);

	@ConfigProperty(name = "profilapp.client-id")
	String permittedClientId;

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	@Inject
	ClientService clientService;

	@Inject
	ChangePasswordService changePasswordService;

	@Inject
	ResourceOwnerService resourceOwnerService;

	@Inject
	ChangeDataService changeDataService;

	@Inject
	DeleteAccountService deleteAccountService;

	/**
	 * @param uuid
	 * @param payload
	 * @return Response mit data User
	 */
	@PUT
	@Path("profile/password")
	@Operation(operationId = "changePassword", summary = "ändert das Passwort.")
	@APIResponse(name = "OKResponse", responseCode = "200", description = "data des ResponsePayload ist ein NoncePayload", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePayload.class)))
	@APIResponse(name = "BadRequestResponse", responseCode = "400", description = "fehlgeschlagene Input-Validierung", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(name = "NotAuthorized", responseCode = "401", description = "Benutzer konnte nicht authentifiziert werden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(name = "ServerError", description = "server error", responseCode = "500", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePayload.class)))
	public Response changePassword(@Valid
	final ChangeProfilePasswordPayload payload) {

		try {

			verifyClientId(payload.getClientCredentials());

			clientService.authorizeClient(payload.getClientCredentials());

			ResponsePayload responsePayload = changePasswordService.changePassword(payload.getUuid(), payload.getPasswordPayload());

			if (responsePayload.isOk()) {

				responsePayload.setData(NoncePayload.create(payload.getClientCredentials().getNonce()));

				LOG.info("Passwort geändert für UUID={}", payload.getUuid().substring(0, 8));
				return Response.ok(responsePayload).build();
			}

			responsePayload.setData(NoncePayload.create(payload.getClientCredentials().getNonce()));
			return Response.ok().entity(responsePayload).build();
		} finally {

			payload.clean();
		}
	}

	/**
	 * Falls Mailadresse vergeben, dann 910, Falls Loginname vergeben, dann 911, falls beides vergeben, dann 912
	 *
	 * @param payload
	 * @return
	 */
	@PUT
	@Path("profile/data")
	@Operation(operationId = "changeData", summary = "ändert Daten des Benutzers")
	@APIResponse(name = "OKResponse", responseCode = "200", description = "data des ResponsePayload ist ein User", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePayload.class)))
	@APIResponse(name = "BadRequestResponse", responseCode = "400", description = "fehlgeschlagene Input-Validierung", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(name = "NotAuthorized", responseCode = "401", description = "Benutzer konnte nicht authentifiziert werden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(name = "ServerError", description = "server error", responseCode = "500", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePayload.class)))
	public Response changeData(@Valid
	final ChangeProfileDataPayload payload) {

		try {

			verifyClientId(payload.getClientCredentials());

			clientService.authorizeClient(payload.getClientCredentials());

			User user = changeDataService.changeData(payload.getUuid(), payload.getProfileData());
			user.setNonce(payload.getClientCredentials().getNonce());

			ResponsePayload responsePayload = new ResponsePayload(
				MessagePayload.info(applicationMessages.getString("BenutzerprofilResource.data.success")), user);

			responsePayload.setData(user);

			return Response.ok(responsePayload).build();
		} finally {

			payload.clean();
		}
	}

	/**
	 * Gibt den User zurück. Die UUID ist im Container ist nicht mehr klar, was ich mit der USER_ID- Property im
	 * ContainerRequestContext vorhatte.
	 *
	 * @param crc
	 * @param userId
	 * @return
	 */
	@POST
	@Path("profile")
	@Operation(operationId = "getUserProfile", summary = "gibt die Daten des Benutzers zurück")
	@APIResponse(name = "OKResponse", responseCode = "200", description = "data des ResponsePayload ist ein User", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePayload.class)))
	@APIResponse(name = "BadRequestResponse", responseCode = "400", description = "fehlgeschlagene Input-Validierung", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(name = "NotAuthorized", responseCode = "401", description = "Benutzer konnte nicht authentifiziert werden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(name = "ServerError", description = "server error", responseCode = "500", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePayload.class)))
	public Response getUserProfile(@Valid
	final SelectProfilePayload selectProfilePayload) {

		try {

			verifyClientId(selectProfilePayload.getClientCredentials());

			clientService.authorizeClient(selectProfilePayload.getClientCredentials());

			Optional<ResourceOwner> optRO = this.resourceOwnerService.findByUUID(selectProfilePayload.getUuid());

			if (optRO.isPresent()) {

				User user = User.fromResourceOwner(optRO.get());
				ResponsePayload responsePayload = new ResponsePayload(MessagePayload.info("OK"), user);

				return Response.ok(responsePayload).build();
			}

			throw new NotFoundException();
		} finally {

			selectProfilePayload.clean();
		}
	}

	/**
	 * Gibt den User zurück. Die UUID ist im Container ist nicht mehr klar, was ich mit der USER_ID- Property im
	 * ContainerRequestContext vorhatte.
	 *
	 * @param crc
	 * @param userId
	 * @return
	 */
	@DELETE
	@Path("profile")
	@Operation(operationId = "deleteUser", summary = "löscht das Benutzerkonto")
	@APIResponse(name = "OKResponse", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePayload.class)))
	@APIResponse(name = "BadRequestResponse", responseCode = "400", description = "fehlgeschlagene Input-Validierung", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(name = "NotAuthorized", responseCode = "401", description = "Benutzer konnte nicht authentifiziert werden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(name = "ServerError", description = "server error", responseCode = "500", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePayload.class)))
	public Response deleteUser(@Valid
	final SelectProfilePayload selectProfilePayload) {

		try {

			verifyClientId(selectProfilePayload.getClientCredentials());

			clientService.authorizeClient(selectProfilePayload.getClientCredentials());

			return deleteAccountService.deleteAccount(selectProfilePayload);

		} finally {

			selectProfilePayload.clean();
		}
	}

	void verifyClientId(@Valid
	final OAuthClientCredentials clientCredentials) {

		if (!permittedClientId.equals(clientCredentials.getClientId())) {

			LOG.warn("Nicht erlaubter Zugriff auf Profil-Resource mit ClientId "
				+ StringUtils.abbreviate(clientCredentials.getClientId(), 8));

			throw new AuthException("Keine Berechtigung für diese Resource");
		}
	}

}
