// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.endpoints;

import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.domain.Client;
import de.egladil.web.authprovider.error.AuthRuntimeException;
import de.egladil.web.authprovider.error.ClientAccessTokenNotFoundException;
import de.egladil.web.authprovider.error.ClientAccessTokenRuntimeException;
import de.egladil.web.authprovider.payload.ChangeTempPasswordPayload;
import de.egladil.web.authprovider.payload.MessagePayload;
import de.egladil.web.authprovider.payload.OrderTempPasswordPayload;
import de.egladil.web.authprovider.payload.ResponsePayload;
import de.egladil.web.authprovider.service.ClientService;
import de.egladil.web.authprovider.service.temppwd.ChangeTempPasswordService;
import de.egladil.web.authprovider.service.temppwd.CreateTempPasswordService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * TempPasswordResource
 */
@RequestScoped
@Path("/temppwd")
@Consumes(MediaType.APPLICATION_JSON)
public class TempPasswordResource {

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	private static final Logger LOG = LoggerFactory.getLogger(TempPasswordResource.class);

	@Inject
	CreateTempPasswordService createTempPasswordService;

	@Inject
	ChangeTempPasswordService changeTempPasswordService;

	@Inject
	ClientService clientService;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response orderTempPassword(@Valid final OrderTempPasswordPayload payload) {

		Client client = null;

		try {

			if (payload.getClientCredentials() != null) {

				client = clientService.findAndCheckClient(payload.getClientCredentials());
			}
		} catch (ClientAccessTokenNotFoundException e) {

			LOG.warn(e.getMessage());

			if (payload.getClientCredentials() != null) {

				throw new ClientAccessTokenRuntimeException(payload.getClientCredentials());
			}

			throw new AuthRuntimeException(e.getMessage());

		} catch (AuthRuntimeException e) {

			LOG.error("fehlerhaften ClientCredentials aufgerufen: {}", payload.toString());

			ResponsePayload responsePayload = ResponsePayload.messageOnly(
				MessagePayload.error(applicationMessages.getString("general.internalServerError")));

			return Response.status(401)
				.entity(responsePayload)
				.build();
		}

		try {

			createTempPasswordService.orderTempPassword(payload.getEmail(), client);

			ResponsePayload responsePayload = ResponsePayload.messageOnly(
				MessagePayload
					.info(applicationMessages.getString("TempPassword.ordered.success")));

			return Response.ok().entity(responsePayload).build();
		} catch (NotFoundException e) {

			ResponsePayload responsePayload = ResponsePayload.messageOnly(
				MessagePayload
					.error(applicationMessages.getString("TempPassword.ordered.incorrectEmail")));
			return Response.status(404).entity(responsePayload).build();
		}
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeTempPassword(@Valid final ChangeTempPasswordPayload payload) {

		ResponsePayload responsePayload = changeTempPasswordService.changeTempPassword(payload);

		if (responsePayload.isOk()) {

			return Response.ok().entity(responsePayload).build();
		}

		return Response.status(412).entity(responsePayload).build();
	}
}
