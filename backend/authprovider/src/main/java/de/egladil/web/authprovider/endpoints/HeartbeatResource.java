// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.endpoints;

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

import de.egladil.web.authprovider.error.LogmessagePrefixes;
import de.egladil.web.authprovider.payload.MessagePayload;
import de.egladil.web.authprovider.payload.ResponsePayload;
import de.egladil.web.authprovider.payload.SignUpLogInResponseData;
import de.egladil.web.authprovider.service.HeartbeatService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * HeartbeatResource
 */
@RequestScoped
@Path("api/heartbeats")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HeartbeatResource {

	private static final Logger LOG = LoggerFactory.getLogger(HeartbeatResource.class);

	@Inject
	HeartbeatService heartbeatService;

	@ConfigProperty(name = "heartbeat.id")
	String expectedHeartbeatId;

	@GET
	@Operation(operationId = "check liveliness", summary = "ping, ob autprovider da ist.")
	@Parameters({
		@Parameter(in = ParameterIn.HEADER, name = "X-HEARTBEAT-ID", description = "ein secret"), })
	@APIResponse(name = "OKResponse", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePayload.class)))
	@APIResponse(name = "NotAuthorized", responseCode = "401", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(name = "ServerError", description = "server error", responseCode = "500", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePayload.class)))
	public Response check(@HeaderParam("X-HEARTBEAT-ID")
	final String heartbeatId) {

		if (!expectedHeartbeatId.equals(heartbeatId)) {

			LOG.warn(LogmessagePrefixes.BOT + "Aufruf mit fehlerhaftem X-HEARTBEAT-ID-Header value " + heartbeatId);
			return Response.status(401).entity(MessagePayload.error("keine Berechtigung für diese Resource")).build();
		}
		ResponsePayload responsePayload = heartbeatService.update();

		if (responsePayload.isOk()) {

			return Response.ok().entity(responsePayload).build();
		}
		return Response.serverError().entity(responsePayload).build();
	}
}
