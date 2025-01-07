// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.endpoints;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import de.egladil.web.authprovider.utils.AuthTimeUtils;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * DevelopmentResource stellt REST-Endpoints zum Spielen und Dinge ausprobieren zur Verfügung. Die werden irgendwann
 * umziehen.
 */
@RequestScoped
@Path("/dev")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DevelopmentResource {

	@GET
	public Response sayHello() {

		final Map<String, String> json = new HashMap<>();
		json.put("greetings",
			"Also Hallochen vom authprovider am  "
				+ DateTimeFormatter.ofPattern(AuthTimeUtils.DEFAULT_DATE_TIME_FORMAT).format(AuthTimeUtils.now()));

		return Response.ok(json).build();
	}
}
