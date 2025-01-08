// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.endpoints;

import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

import de.egladil.web.authprovider.event.AuthproviderEventHandler;
import de.egladil.web.authprovider.event.BotAttackEvent;
import de.egladil.web.authprovider.event.BotAttackEventPayload;
import de.egladil.web.authprovider.payload.ChangeTempPasswordPayload;
import de.egladil.web.authprovider.payload.MessagePayload;
import de.egladil.web.authprovider.payload.OrderTempPasswordPayload;
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
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

/**
 * TempPasswordResource
 */
@Path("api/temppwd")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TempPasswordResource {

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	@Inject
	CreateTempPasswordService createTempPasswordService;

	@Inject
	ChangeTempPasswordService changeTempPasswordService;

	@Inject
	AuthproviderEventHandler eventHandler;

	@POST
	public Response orderTempPassword(@Valid final OrderTempPasswordPayload payload, @Context final UriInfo uriInfo) {

		String kleber = payload.getKleber();

		if (StringUtils.isNotBlank(kleber)) {

			BotAttackEventPayload eventPayload = new BotAttackEventPayload()
				.withPath(uriInfo.getPath())
				.withKleber(kleber)
				.withLoginName(payload.getEmail());

			this.eventHandler.handleEvent(new BotAttackEvent(eventPayload));
		}

		createTempPasswordService.orderTempPassword(payload.getEmail());

		return Response
			.ok(new TempPasswordV2ResponseDto().withMessage(applicationMessages.getString("TempPassword.ordered.success"))).build();

	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeTempPassword(@Valid final ChangeTempPasswordPayload payload, @Context final UriInfo uriInfo) {

		String kleber = payload.getKleber();

		if (StringUtils.isNotBlank(kleber)) {

			BotAttackEventPayload eventPayload = new BotAttackEventPayload()
				.withPath(uriInfo.getPath())
				.withKleber(kleber)
				.withLoginName(payload.getEmail())
				.withPasswort(payload.getZweiPassworte().getPasswort());

			this.eventHandler.handleEvent(new BotAttackEvent(eventPayload));

			return Response.status(401).entity(MessagePayload
				.error(applicationMessages.getString("general.notAuthenticated"))).build();
		}

		ResponsePayload responsePayload = changeTempPasswordService.changeTempPassword(payload);

		if (responsePayload.isOk()) {

			return Response.ok().entity(responsePayload).build();
		}

		return Response.status(412).entity(responsePayload.getMessage()).build();
	}
}
