// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.endpoints;

import java.util.Locale;
import java.util.ResourceBundle;

import de.egladil.web.authprovider.payload.ChangeTempPasswordPayload;
import de.egladil.web.authprovider.payload.OrderTempPasswordPayloadV2;
import de.egladil.web.authprovider.payload.ResponsePayload;
import de.egladil.web.authprovider.payload.TempPasswordV2ResponseDto;
import de.egladil.web.authprovider.service.temppwd.ChangeTempPasswordService;
import de.egladil.web.authprovider.service.temppwd.CreateTempPasswordService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * TempPasswordResourceV2
 */
@Path("/temppwd/v2")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TempPasswordResourceV2 {

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	@Inject
	CreateTempPasswordService createTempPasswordService;

	@Inject
	ChangeTempPasswordService changeTempPasswordService;

	@POST
	public Response orderTempPassword(@Valid final OrderTempPasswordPayloadV2 payload) {

		createTempPasswordService.orderTempPasswordV2(payload.getEmail());

		return Response
			.ok(new TempPasswordV2ResponseDto().withMessage(applicationMessages.getString("TempPassword.ordered.success"))).build();

	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeTempPassword(@Valid final ChangeTempPasswordPayload payload) {

		ResponsePayload responsePayload = changeTempPasswordService.changeTempPassword(payload);

		if (responsePayload.isOk()) {

			return Response.ok().entity(responsePayload).build();
		}

		return Response.status(412).entity(responsePayload.getMessage()).build();
	}
}
