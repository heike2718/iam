// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.infrastructure.resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import de.egladil.web.bv_admin.domain.auth.dto.MessagePayload;
import de.egladil.web.bv_admin.domain.mailversand.api.MailService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * MailingResource
 */
@Path("api/mails")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
@Tag(name = "MailingResource")
public class MailingResource {

	@Inject
	MailService mailservice;

	@GET
	@Path("testmail")
	@RolesAllowed({ "AUTH_ADMIN" })
	@Operation(operationId = "sendTestMail", summary = "Endpoint zum Testen der Mailerkonfiguration.")
	@APIResponse(name = "OKResponse", responseCode = "202")
	@APIResponse(name = "NotAuthorized", responseCode = "401", content = @Content(mediaType = "application/json"))
	@APIResponse(name = "Forbidden", description = "kann auch vorkommen, wenn mod_security zuschlägt", responseCode = "403", content = @Content(mediaType = "application/json"))
	@APIResponse(name = "ServerError", description = "server error", responseCode = "500", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessagePayload.class)))
	public Response sendTestMail(@QueryParam(value = "to")
	final String empfaenger) {

		this.mailservice.sendATestMail(empfaenger);
		return Response.status(202).build();
	}
}
