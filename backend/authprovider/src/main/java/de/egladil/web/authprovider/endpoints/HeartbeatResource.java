// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.endpoints;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.error.LogmessagePrefixes;
import de.egladil.web.authprovider.payload.MessagePayload;
import de.egladil.web.authprovider.payload.ResponsePayload;
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
	public Response check(@HeaderParam("X-HEARTBEAT-ID") final String heartbeatId) {

		if (!expectedHeartbeatId.equals(heartbeatId)) {

			LOG.warn(LogmessagePrefixes.BOT + "Aufruf mit fehlerhaftem X-HEARTBEAT-ID-Header value " + heartbeatId);
			return Response.status(401)
				.entity(MessagePayload.error("keine Berechtigung für diese Resource")).build();
		}
		ResponsePayload responsePayload = heartbeatService.update();

		if (responsePayload.isOk()) {

			return Response.ok().entity(responsePayload).build();
		}
		return Response.serverError().entity(responsePayload).build();
	}
}
