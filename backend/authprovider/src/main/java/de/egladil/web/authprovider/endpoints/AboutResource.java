// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.endpoints;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import de.egladil.web.authprovider.about.AboutDto;
import de.egladil.web.authprovider.about.AboutService;
import de.egladil.web.authprovider.payload.ResponsePayload;
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
	@Operation(operationId = "getAboutInfo", summary = "Über authprovider. Zum Smoketest sinnvoll.")
	@APIResponse(name = "OKResponse", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AboutDto.class)))
	@APIResponse(name = "ServerError", description = "server error", responseCode = "500", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePayload.class)))
	public Response getAboutInfo() {

		return Response.ok(aboutService.getAboutDto()).build();
	}
}
