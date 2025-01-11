// =====================================================
// Project: benutzerprofil
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.benutzerprofil.infrastructure.resources;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import de.egladil.web.benutzerprofil.domain.auth.dto.MessagePayload;
import de.egladil.web.benutzerprofil.domain.passwort.PasswortPayload;
import de.egladil.web.benutzerprofil.domain.passwort.PasswortService;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * PasswortResource
 */
@RequestScoped
@Path("api/passwort")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
@Tag(name = "PasswortResource")
public class PasswortResource {

	@Inject
	PasswortService passwortService;

	@PUT
	@Authenticated
	public Response passwortAendern(@Valid
	final PasswortPayload payload) {

		MessagePayload messagePayload = passwortService.passwortAendern(payload);

		return Response.ok(messagePayload).build();
	}

}
