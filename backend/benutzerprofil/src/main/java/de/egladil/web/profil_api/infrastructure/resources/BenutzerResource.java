// =====================================================
// Project: profil-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_api.infrastructure.resources;

import de.egladil.web.profil_api.domain.benutzer.BenutzerDto;
import de.egladil.web.profil_api.domain.benutzer.BenutzerService;
import de.egladil.web.profil_api.domain.benutzer.ChangeBenutzerdatenResponseDto;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

/**
 * BenutzerResource
 */
@Path("api/benutzer")
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
	public Response benutzerdatenAendern(@Valid final BenutzerDto benutzerDto) {

		ChangeBenutzerdatenResponseDto result = benutzerService.benutzerdatenAendern(benutzerDto);

		return Response.ok(result).build();
	}

	@DELETE
	@Authenticated
	public Response kontoLoeschen() {

		return benutzerService.kontoLoeschen();
	}

}
