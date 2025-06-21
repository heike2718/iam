// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.endpoints;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.log.LogDelegate;
import de.egladil.web.authprovider.log.LogEntry;
import de.egladil.web.authprovider.payload.ResponsePayload;
import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * LogResource
 */
@RequestScoped
@Path("api/log")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LogResource {

	private static final Logger LOG = LoggerFactory.getLogger(LogResource.class);

	private static final String CLIENT_ID = "auth-app";

	@POST
	@Operation(operationId = "logError", summary = "Schreibt etwas ins server.log.")
	@APIResponse(name = "OKResponse", responseCode = "200")
	@APIResponse(name = "ServerError", description = "server error", responseCode = "500", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePayload.class)))
	public Response logError(@Valid
	final LogEntry logEntry) {

		new LogDelegate().log(logEntry, LOG, CLIENT_ID);

		return Response.ok().build();
	}
}
