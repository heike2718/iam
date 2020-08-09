// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.migration;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.egladil.web.authprovider.migration.payload.AuthenticationPayload;
import de.egladil.web.authprovider.migration.service.MigrationService;
import de.egladil.web.commons_validation.payload.ResponsePayload;

/**
 * MigrationResource
 */
@Path("/migration")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.TEXT_PLAIN)
public class MigrationResource {

	@ConfigProperty(name = "mk.migration.secret")
	String expectedSecret;

	@Inject
	MigrationService migrationService;

	/**
	 * Es kommt hier immer der status 200 zurück. Client muss MessagePayload auswerten!
	 *
	 * @param  requestPayload
	 * @return                Response
	 */
	@POST
	@Path("/benutzer")
	public Response importierenFuerMinikaenguru(final AuthenticationPayload requestPayload) {

		if (!expectedSecret.equals(requestPayload.getSecret())) {

			throw new SecurityException("benutzer importieren, ohne das secret zu kennen, ist verboten");
		}

		ResponsePayload responsePayload = this.migrationService.importiereBenutzer(requestPayload);
		return Response.ok(responsePayload).build();
	}
}
