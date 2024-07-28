// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.infrastructure.resources;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import de.egladil.web.auth_admin_api.domain.auth.dto.MessagePayload;
import de.egladil.web.auth_admin_api.domain.mailversand.api.MailversandauftragDetailsResponseDto;
import de.egladil.web.auth_admin_api.domain.mailversand.api.MailversandauftragOverview;
import de.egladil.web.auth_admin_api.domain.mailversand.api.MailversandauftragRequestDto;
import de.egladil.web.auth_admin_api.domain.mailversand.api.MailversandgruppeDetailsResponseDto;
import de.egladil.web.auth_admin_api.domain.mailversand.api.SingleUuidDto;
import de.egladil.web.auth_admin_api.domain.mailversand.api.VersandauftragService;
import de.egladil.web.auth_admin_api.domain.validation.ValidationErrorResponseDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * VersandauftraegeResource
 */
@RequestScoped
@Path("auth-admin-api/versandauftraege")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
@Tag(name = "VersandauftraegeResource")
public class VersandauftraegeResource {

	@Inject
	VersandauftragService versandauftragService;

	@GET
	@RolesAllowed({ "AUTH_ADMIN" })
	@Operation(
		operationId = "loadVersandauftraege", summary = "Gibt alle gespeicherten Mailversandaufträge zurück.")
	@APIResponse(
		name = "OKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MailversandauftragOverview.class, type = SchemaType.ARRAY)))
	@APIResponse(
		name = "NotAuthorized",
		responseCode = "401",
		content = @Content(
			mediaType = "application/json"))
	@APIResponse(
		name = "Forbidden",
		description = "kann auch vorkommen, wenn mod_security zuschlägt",
		responseCode = "403",
		content = @Content(
			mediaType = "application/json"))
	@APIResponse(
		name = "ServerError",
		description = "server error",
		responseCode = "500", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public Response loadVersandauftraege() {

		List<MailversandauftragOverview> responsePayload = versandauftragService.versandauftraegeLaden();
		return Response.status(200).entity(responsePayload).build();
	}

	@GET
	@Path("{uuid}")
	@RolesAllowed({ "AUTH_ADMIN" })
	@Operation(
		operationId = "getDetailsVersandauftrag",
		summary = "Läd die Details des durch die uuid definierten Mailversandauftrags")
	@Parameters({
		@Parameter(
			in = ParameterIn.PATH, name = "uuid", description = "UUID des Mailversandauftrags",
			example = "a4c4d45e-4a81-4bde-a6a3-54464801716d", required = true)
	})
	@APIResponse(
		name = "OKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MailversandauftragDetailsResponseDto.class)))
	@APIResponse(
		name = "BadRequest",
		responseCode = "400",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(type = SchemaType.ARRAY, implementation = ValidationErrorResponseDto.class)))
	@APIResponse(
		name = "NotAuthorized",
		responseCode = "401",
		content = @Content(
			mediaType = "application/json"))
	@APIResponse(
		name = "Forbidden",
		description = "kann auch vorkommen, wenn mod_security zuschlägt",
		responseCode = "403",
		content = @Content(
			mediaType = "application/json"))
	@APIResponse(
		name = "ServerError",
		description = "server error",
		responseCode = "500", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public Response getDetailsVersandauftrag(@Pattern(regexp = "^[abcdef\\d\\-]*$") @Size(max = 36) @PathParam(
		value = "uuid") @Pattern(
			regexp = "^[abcdef\\d\\-]*$", message = "uuid enthält ungültige Zeichen") @Size(
				max = 36, message = "uuid zu lang (max. 36 Zeichen)") final String uuid) {

		MailversandauftragDetailsResponseDto responsePayload = versandauftragService.detailsMailversandauftragLaden(uuid);

		return Response.status(200).entity(responsePayload).build();
	}

	@GET
	@Path("gruppen/{uuid}")
	@RolesAllowed({ "AUTH_ADMIN" })
	@Operation(
		operationId = "getDetailsMailversandgruppe",
		summary = "Läd die Details der durch die uuid definierten Mainversandgruppe")
	@Parameters({
		@Parameter(
			in = ParameterIn.PATH, name = "uuid", description = "UUID der Mailversandgruppe",
			example = "a4c4d45e-4a81-4bde-a6a3-54464801716d", required = true)
	})
	@APIResponse(
		name = "OKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MailversandgruppeDetailsResponseDto.class)))
	@APIResponse(
		name = "BadRequest",
		responseCode = "400",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(type = SchemaType.ARRAY, implementation = ValidationErrorResponseDto.class)))
	@APIResponse(
		name = "NotAuthorized",
		responseCode = "401",
		content = @Content(
			mediaType = "application/json"))
	@APIResponse(
		name = "Forbidden",
		description = "kann auch vorkommen, wenn mod_security zuschlägt",
		responseCode = "403",
		content = @Content(
			mediaType = "application/json"))
	@APIResponse(
		name = "ServerError",
		description = "server error",
		responseCode = "500", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public Response getDetailsMailversandgruppe(@Pattern(
		regexp = "^[abcdef\\d\\-]*$") @Size(max = 36) @PathParam(
			value = "uuid") @Pattern(
				regexp = "^[abcdef\\d\\-]*$", message = "uuid enthält ungültige Zeichen") @Size(
					max = 36, message = "uuid zu lang (max. 36 Zeichen)") final String uuid) {

		MailversandgruppeDetailsResponseDto responsePayload = versandauftragService.detailsMailversandgruppeLaden(uuid);

		return Response.status(200).entity(responsePayload).build();
	}

	@POST
	@RolesAllowed({ "AUTH_ADMIN" })
	@Operation(
		operationId = "scheduleInfomailversand", summary = "Legt einen Mailversandauftrag an.")
	@APIResponse(
		name = "OKResponse",
		responseCode = "201",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MailversandauftragOverview.class)))
	@APIResponse(
		name = "BadRequest",
		responseCode = "400",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(type = SchemaType.ARRAY, implementation = ValidationErrorResponseDto.class)))
	@APIResponse(
		name = "NotAuthorized",
		responseCode = "401",
		content = @Content(
			mediaType = "application/json"))
	@APIResponse(
		name = "Forbidden",
		description = "kann auch vorkommen, wenn mod_security zuschlägt",
		responseCode = "403",
		content = @Content(
			mediaType = "application/json"))
	@APIResponse(
		name = "PrecoditionFailed",
		description = "eine Voraussetzung ist nicht erfüllt. Beispielsweise gibt es keinen Infomailtext mit der gegebenen UUID",
		responseCode = "412", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "ServerError",
		description = "server error",
		responseCode = "500", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public Response scheduleInfomailversand(@Valid final MailversandauftragRequestDto requestPayload) {

		MailversandauftragOverview responsePayload = versandauftragService.versandauftragAnlegen(requestPayload);
		return Response.status(201).entity(responsePayload).build();
	}

	@DELETE
	@Path("{uuid}")
	@RolesAllowed({ "AUTH_ADMIN" })
	@Operation(
		operationId = "versandauftragLoeschen",
		summary = "Löscht den durch die uuid definierten Mailversandauftrag")
	@Parameters({
		@Parameter(
			in = ParameterIn.PATH, name = "uuid", description = "UUID des Mailversandauftrags, der gelöscht werden soll",
			example = "a4c4d45e-4a81-4bde-a6a3-54464801716d", required = true)
	})
	@APIResponse(
		name = "OKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = SingleUuidDto.class)))
	@APIResponse(
		name = "BadRequest",
		responseCode = "400",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(type = SchemaType.ARRAY, implementation = ValidationErrorResponseDto.class)))
	@APIResponse(
		name = "NotAuthorized",
		responseCode = "401",
		content = @Content(
			mediaType = "application/json"))
	@APIResponse(
		name = "Forbidden",
		description = "kann auch vorkommen, wenn mod_security zuschlägt",
		responseCode = "403",
		content = @Content(
			mediaType = "application/json"))
	@APIResponse(
		name = "ServerError",
		description = "server error",
		responseCode = "500", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public Response versandauftragLoeschen(@Pattern(regexp = "^[abcdef\\d\\-]*$") @Size(max = 36) @PathParam(
		value = "uuid") @Pattern(
			regexp = "^[abcdef\\d\\-]*$", message = "uuid enthält ungültige Zeichen") @Size(
				max = 36, message = "uuid zu lang (max. 36 Zeichen)") final String uuid) {

		SingleUuidDto responsePayload = versandauftragService.versandauftragLoeschen(uuid);
		return Response.status(200).entity(responsePayload).build();
	}

	@PUT
	@Path("{uuid}/cancellation")
	@RolesAllowed({ "AUTH_ADMIN" })
	@Operation(
		operationId = "versandauftragAbbrechen",
		summary = "Bricht den durch die uuid definierten Mailversandauftrag ab")
	@Parameters({
		@Parameter(
			in = ParameterIn.PATH, name = "uuid", description = "UUID des Mailversandauftrags, der abgebrochen werden soll",
			example = "a4c4d45e-4a81-4bde-a6a3-54464801716d", required = true)
	})
	@APIResponse(
		name = "OKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = SingleUuidDto.class)))
	@APIResponse(
		name = "BadRequest",
		responseCode = "400",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(type = SchemaType.ARRAY, implementation = ValidationErrorResponseDto.class)))
	@APIResponse(
		name = "NotAuthorized",
		responseCode = "401",
		content = @Content(
			mediaType = "application/json"))
	@APIResponse(
		name = "Forbidden",
		description = "kann auch vorkommen, wenn mod_security zuschlägt",
		responseCode = "403",
		content = @Content(
			mediaType = "application/json"))
	@APIResponse(
		name = "ServerError",
		description = "server error",
		responseCode = "500", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public Response versandauftragAbbrechen(@Pattern(regexp = "^[abcdef\\d\\-]*$") @Size(max = 36) @PathParam(
		value = "uuid") @Pattern(
			regexp = "^[abcdef\\d\\-]*$", message = "uuid enthält ungültige Zeichen") @Size(
				max = 36, message = "uuid zu lang (max. 36 Zeichen)") final String uuid) {

		SingleUuidDto responsePayload = versandauftragService.mailversandAbbrechen(uuid);
		return Response.status(200).entity(responsePayload).build();
	}
}
