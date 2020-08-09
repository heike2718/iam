// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.endpoints;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.domain.Client;
import de.egladil.web.authprovider.error.AuthRuntimeException;
import de.egladil.web.authprovider.error.ClientAccessTokenNotFoundException;
import de.egladil.web.authprovider.error.ClientAccessTokenRuntimeException;
import de.egladil.web.authprovider.payload.ChangeTempPasswordPayload;
import de.egladil.web.authprovider.payload.OrderTempPasswordPayload;
import de.egladil.web.authprovider.service.ClientService;
import de.egladil.web.authprovider.service.temppwd.ChangeTempPasswordService;
import de.egladil.web.authprovider.service.temppwd.CreateTempPasswordService;
import de.egladil.web.commons_validation.ValidationDelegate;
import de.egladil.web.commons_validation.payload.MessagePayload;
import de.egladil.web.commons_validation.payload.ResponsePayload;

/**
 * TempPasswordResource
 */
@RequestScoped
@Path("/temppwd")
@Consumes(MediaType.APPLICATION_JSON)
public class TempPasswordResource {

	private ValidationDelegate validationDelegate = new ValidationDelegate();

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	private static final Logger LOG = LoggerFactory.getLogger(TempPasswordResource.class);

	@Inject
	CreateTempPasswordService createTempPasswordService;

	@Inject
	ChangeTempPasswordService changeTempPasswordService;

	@Inject
	ClientService clientService;

	@Context
	private UriInfo uriInfo;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response orderTempPassword(final OrderTempPasswordPayload payload) {

		validationDelegate.check(payload, OrderTempPasswordPayload.class);

		Client client = null;

		try {

			if (payload.getClientCredentials() != null) {

				client = clientService.findAndCheckClient(payload.getClientCredentials());
			}
		} catch (ClientAccessTokenNotFoundException e) {

			LOG.info(e.getMessage());

			if (payload.getClientCredentials() != null) {

				throw new ClientAccessTokenRuntimeException(payload.getClientCredentials());
			}

			throw new AuthRuntimeException(e.getMessage());

		} catch (AuthRuntimeException e) {

			String pathIfo = "POST " + uriInfo.getBaseUri().toString() + "/temppwd";

			LOG.error("{} mit fehlerhaften ClientCredentials aufgerufen: {}", pathIfo, payload.toString());

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
	public Response changeTempPassword(final ChangeTempPasswordPayload payload) {

		validationDelegate.check(payload, ChangeTempPasswordPayload.class);

		ResponsePayload responsePayload = changeTempPasswordService.changeTempPassword(payload);

		if (responsePayload.isOk()) {

			return Response.ok().entity(responsePayload).build();
		}

		return Response.status(412).entity(responsePayload).build();
	}
}
