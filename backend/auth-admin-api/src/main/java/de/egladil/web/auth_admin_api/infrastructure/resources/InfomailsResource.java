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
import de.egladil.web.auth_admin_api.domain.infomails.InfomailRequestDto;
import de.egladil.web.auth_admin_api.domain.infomails.InfomailResponseDto;
import de.egladil.web.auth_admin_api.domain.infomails.InfomailvorlagenService;
import de.egladil.web.auth_admin_api.domain.infomails.UpdateInfomailResponseDto;
import de.egladil.web.auth_admin_api.domain.validation.ValidationErrorResponseDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * InfomailsResource
 */
@RequestScoped
@Path("auth-admin-api/infomails")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
@Tag(name = "InfomailsResource")
public class InfomailsResource {

	@Inject
	InfomailvorlagenService infomailService;

	@GET
	@RolesAllowed({ "AUTH_ADMIN" })
	@Operation(
		operationId = "loadInfomails", summary = "Gibt alle gespeicherten Infomailtexte zurück.")
	@APIResponse(
		name = "OKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = InfomailResponseDto.class, type = SchemaType.ARRAY)))
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
	public Response loadInfomails() {

		List<InfomailResponseDto> responsePayload = infomailService.loadInfomailTexte();
		return Response.ok(responsePayload).build();
	}

	@POST
	@RolesAllowed({ "AUTH_ADMIN" })
	@Operation(
		operationId = "infomailAnlegen", summary = "Legt eine Infomail an.")
	@APIResponse(
		name = "OKResponse",
		responseCode = "201",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = InfomailResponseDto.class)))
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
	public Response infomailAnlegen(@Valid final InfomailRequestDto infomailRequestDto) {

		InfomailResponseDto responsePayload = infomailService.infomailAnlegen(infomailRequestDto);
		return Response.status(201).entity(responsePayload).build();
	}

	@PUT
	@Path("{uuid}")
	@RolesAllowed({ "AUTH_ADMIN" })
	@Operation(
		operationId = "infomailAendern", summary = "Ändert eine Infomail an.")
	@Parameters({
		@Parameter(
			in = ParameterIn.PATH, name = "uuid", description = "UUID der Infomail, die geändert werden soll",
			example = "a4c4d45e-4a81-4bde-a6a3-54464801716d", required = true)
	})
	@APIResponse(
		name = "OKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = UpdateInfomailResponseDto.class)))
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
		name = "NotFound",
		description = "wenn es die Entity mit der uuid gar nicht gibt",
		responseCode = "404",
		content = @Content(
			mediaType = "application/json"))
	@APIResponse(
		name = "ServerError",
		description = "server error",
		responseCode = "500", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public Response infomailAendern(@Pattern(regexp = "^[abcdef\\d\\-]*$") @Size(max = 36) @PathParam(
		value = "uuid") final String uuid, @Valid final InfomailRequestDto infomailRequestDto) {

		UpdateInfomailResponseDto responsePayload = infomailService.infomailAendern(uuid, infomailRequestDto);
		return Response.ok(responsePayload).build();
	}
}
