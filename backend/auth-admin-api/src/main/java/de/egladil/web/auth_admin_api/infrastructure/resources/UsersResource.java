// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.infrastructure.resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import de.egladil.web.auth_admin_api.domain.auth.dto.MessagePayload;
import de.egladil.web.auth_admin_api.domain.users.UserSearchDto;
import de.egladil.web.auth_admin_api.domain.users.UserSearchResult;
import de.egladil.web.auth_admin_api.domain.users.UserService;
import de.egladil.web.auth_admin_api.domain.validation.ValidationErrorResponseDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * UsersResource
 */
@RequestScoped
@Path("auth-admin-api/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
@Tag(name = "Users")
public class UsersResource {

	@Inject
	UserService userService;

	@POST
	@RolesAllowed({ "AUTH_ADMIN" })
	@Operation(
		operationId = "findUsers", summary = "Gibt alle User zurück, die auf die gegebene Suchanfrage passen.")
	@APIResponse(
		name = "OKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = UserSearchResult.class)))
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
	public Response findUsers(@Valid final UserSearchDto userSerachDto) {

		UserSearchResult responsePayload = userService.findUsers(userSerachDto);
		// TODO output-validierung
		return Response.ok(responsePayload).build();
	}

}
