// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.endpoints;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.commons_validation.LogDelegate;
import de.egladil.web.commons_validation.payload.LogEntry;

/**
 * LogResource
 */
@RequestScoped
@Path("/log")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LogResource {

	private static final Logger LOG = LoggerFactory.getLogger(LogResource.class);

	private static final String CLIENT_ID = "auth-app";

	@POST
	public Response logError(final LogEntry logEntry) {

		new LogDelegate().log(logEntry, LOG, CLIENT_ID);

		return Response.ok().build();
	}
}
