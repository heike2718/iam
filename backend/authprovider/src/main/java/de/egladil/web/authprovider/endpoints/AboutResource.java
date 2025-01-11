// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.endpoints;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import de.egladil.web.authprovider.about.AboutService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * AboutResource
 */
@RequestScoped
@Path("api/about")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
@Tag(name = "AboutResource")
public class AboutResource {

	@Inject
	AboutService aboutService;

	@GET
	public Response getAboutInfo() {

		return Response.ok(aboutService.getAboutDto()).build();
	}
}
