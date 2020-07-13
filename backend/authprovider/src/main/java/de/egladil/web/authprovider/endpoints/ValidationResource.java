// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.endpoints;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.authprovider.payload.StringPayload;
import de.egladil.web.authprovider.service.ResourceOwnerService;
import de.egladil.web.commons_validation.payload.MessagePayload;
import de.egladil.web.commons_validation.payload.ResponsePayload;

/**
 * ValidationResource
 */
@RequestScoped
@Path("/validators")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ValidationResource {

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	@Inject
	ResourceOwnerService resourceOwnerService;

	@POST
	@Path("loginname")
	public Response checkLoginnameExists(final StringPayload loginName) {

		Optional<ResourceOwner> optUserWithLoginName = this.resourceOwnerService.findResourceOwnerByLoginName(loginName.getInput());

		if (optUserWithLoginName.isPresent()) {

			return Response
				.ok(ResponsePayload.messageOnly(MessagePayload.error(applicationMessages.getString("loginName.vergeben")))).build();
		}
		return Response.ok(ResponsePayload.messageOnly(MessagePayload.info("keine Einwände"))).build();

	}

	// Zu unicher
	// @POST
	// @Path("email")
	public Response checkEmailExists(final StringPayload email) {

		Optional<ResourceOwner> optUserWithEmail = this.resourceOwnerService.findResourceOwnerByEmail(email.getInput());

		if (optUserWithEmail.isPresent()) {

			return Response
				.ok(ResponsePayload.messageOnly(MessagePayload.error(applicationMessages.getString("email.vergeben")))).build();
		}
		return Response.ok(ResponsePayload.messageOnly(MessagePayload.info("keine Einwände"))).build();

	}
}
