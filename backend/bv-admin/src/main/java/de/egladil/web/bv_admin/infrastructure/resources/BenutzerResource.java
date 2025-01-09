// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.infrastructure.resources;

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

import de.egladil.web.bv_admin.domain.auth.dto.MessagePayload;
import de.egladil.web.bv_admin.domain.benutzer.Aktivierungsstatus;
import de.egladil.web.bv_admin.domain.benutzer.BenutzerSearchResult;
import de.egladil.web.bv_admin.domain.benutzer.BenutzerService;
import de.egladil.web.bv_admin.domain.benutzer.BenutzerSuchparameter;
import de.egladil.web.bv_admin.domain.benutzer.DeleteBenutzerResponseDto;
import de.egladil.web.bv_admin.domain.benutzer.UpdateBenutzerResponseDto;
import de.egladil.web.bv_admin.domain.validation.ValidationErrorResponseDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * BenutzerResource
 */
@RequestScoped
@Path("api/benutzer")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
@Tag(name = "BenutzerResource")
public class BenutzerResource {

	@Inject
	BenutzerService benutzerService;

	@POST
	@RolesAllowed({ "AUTH_ADMIN" })
	@Operation(
		operationId = "findUsers", summary = "Gibt alle Benutzer zurück, die auf die gegebene Suchanfrage passen.")
	@APIResponse(
		name = "OKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = BenutzerSearchResult.class)))
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
	public Response findUsers(@Valid final BenutzerSuchparameter userSerachDto) {

		BenutzerSearchResult responsePayload = benutzerService.findUsers(userSerachDto);
		return Response.ok(responsePayload).build();
	}

	@PUT
	@Path("{uuid}")
	@RolesAllowed({ "AUTH_ADMIN" })
	@Operation(
		operationId = "aktivierungsstatusAendern", summary = "Setzt für den gegebenen Benutzer den neuen Aktivierungsstatus")
	@Parameters({
		@Parameter(
			in = ParameterIn.PATH, name = "uuid", description = "UUID des Benutzers, der geändert werden soll",
			example = "a4c4d45e-4a81-4bde-a6a3-54464801716d", required = true)
	})
	@APIResponse(
		name = "OKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = UpdateBenutzerResponseDto.class)))
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
			mediaType = "application/json", schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "Forbidden",
		description = "kann auch vorkommen, wenn mod_security zuschlägt",
		responseCode = "403",
		content = @Content(
			mediaType = "application/json", schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "NotFound",
		description = "kann auch vorkommen, wenn mod_security zuschlägt",
		responseCode = "404")
	@APIResponse(
		name = "ServerError",
		description = "server error",
		responseCode = "500", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public Response aktivierungsstatusAendern(@PathParam(value = "uuid") @Pattern(
		regexp = "^[abcdef\\d\\-]*$", message = "uuid enthält ungültige Zeichen") @Size(
			max = 36, message = "uuid zu lang (max. 36 Zeichen)") final String uuid, final Aktivierungsstatus aktivierungsstatus) {

		UpdateBenutzerResponseDto responsePayload = benutzerService.updateAktivierungsstatus(uuid, aktivierungsstatus);

		return Response.ok(responsePayload).build();
	}

	@DELETE
	@Path("{uuid}")
	@RolesAllowed({ "AUTH_ADMIN" })
	@Operation(
		operationId = "benutzerLoeschen",
		summary = "Löscht das durch die uuid definierte Benutzerkonto vollständig aus allen Anwendungen.")
	@Parameters({
		@Parameter(
			in = ParameterIn.PATH, name = "uuid", description = "UUID des Benutzers, der gelöscht werden soll",
			example = "a4c4d45e-4a81-4bde-a6a3-54464801716d", required = true)
	})
	@APIResponse(
		name = "OKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = DeleteBenutzerResponseDto.class)))
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
	public Response benutzerLoeschen(@Pattern(regexp = "^[abcdef\\d\\-]*$") @Size(max = 36) @PathParam(value = "uuid") @Pattern(
		regexp = "^[abcdef\\d\\-]*$", message = "uuid enthält ungültige Zeichen") @Size(
			max = 36, message = "uuid zu lang (max. 36 Zeichen)") final String uuid) {

		DeleteBenutzerResponseDto responsePayload = benutzerService.deleteUser(uuid);
		return Response.ok().entity(responsePayload).build();
	}

	@PUT
	@Path("loeschung")
	@RolesAllowed({ "AUTH_ADMIN" })
	@Operation(
		operationId = "batchDelete",
		summary = "Löscht die durch die uuids definierten Benutzerkonten vollständig aus allen Anwendungen.")
	@Parameters({
		@Parameter(
			in = ParameterIn.PATH, name = "uuid", description = "UUID des Benutzers, der gelöscht werden soll",
			example = "a4c4d45e-4a81-4bde-a6a3-54464801716d", required = true)
	})
	@APIResponse(
		name = "OKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = DeleteBenutzerResponseDto.class)))
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
	public Response batchDelete(final List<String> uuids) {

		return Response.status(500).entity(MessagePayload.error("noch nicht implementiert")).build();
	}

}
