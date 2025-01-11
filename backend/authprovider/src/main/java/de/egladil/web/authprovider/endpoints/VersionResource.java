// =====================================================
// Project: mkadmin-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.endpoints;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.payload.MessagePayload;
import de.egladil.web.authprovider.payload.ResponsePayload;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * VersionResource
 */
@RequestScoped
@Path("api/version")
@Produces(MediaType.APPLICATION_JSON)
public class VersionResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(VersionResource.class);

	@ConfigProperty(name = "quarkus.application.version", defaultValue = "")
	String version;

	@ConfigProperty(name = "env")
	String env;

	@ConfigProperty(name = "stage")
	String stage;

	@GET
	public Response getVersion() {

		String message = "AuthproviderApplication running version " + version + " on stage " + stage + " and env " + env;

		LOGGER.info(message);
		return Response.ok(ResponsePayload.messageOnly(MessagePayload.info(message))).build();

	}

}
