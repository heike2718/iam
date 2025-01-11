// =====================================================
// Project: benutzerprofil
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.benutzerprofil.infrastructure.resources;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import de.egladil.web.benutzerprofil.domain.benutzer.BenutzerDto;
import de.egladil.web.benutzerprofil.domain.benutzer.BenutzerService;
import de.egladil.web.benutzerprofil.domain.benutzer.ChangeBenutzerdatenResponseDto;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * BenutzerResource
 */
@RequestScoped
@Path("api/benutzer")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
@Tag(name = "BenutzerResource")
public class BenutzerResource {

	@Inject
	BenutzerService benutzerService;

	@GET
	@Authenticated
	public Response benutzerdatenLaden() {

		BenutzerDto benutzerDto = benutzerService.ladeBenuterDaten();

		return Response.ok(benutzerDto).build();
	}

	@PUT
	@Authenticated
	public Response benutzerdatenAendern(@Valid
	final BenutzerDto benutzerDto) {

		ChangeBenutzerdatenResponseDto result = benutzerService.benutzerdatenAendern(benutzerDto);

		return Response.ok(result).build();
	}

	@DELETE
	@Authenticated
	public Response kontoLoeschen() {

		return benutzerService.kontoLoeschen();
	}

}
