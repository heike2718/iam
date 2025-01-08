// =====================================================
// Project: benutzerprofil
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.benutzerprofil.infrastructure.error;

import java.util.Locale;
import java.util.ResourceBundle;

import org.jboss.resteasy.reactive.ClientWebApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.benutzerprofil.domain.auth.dto.MessagePayload;
import de.egladil.web.benutzerprofil.domain.auth.dto.ResponsePayload;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * ClientWebApplicationExceptionMapper
 */
@Provider
public class ClientWebApplicationExceptionMapper implements ExceptionMapper<ClientWebApplicationException> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientWebApplicationExceptionMapper.class);

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	@Override
	public Response toResponse(final ClientWebApplicationException exception) {

		Response response = exception.getResponse();
		LOGGER.error("Status={}: message={}", response.getStatus(), exception.getMessage());

		MessagePayload messagePayload = extractMessagePayload(response);

		if (messagePayload == null) {

			messagePayload = MessagePayload.error(applicationMessages.getString("general.internalServerError"));
		}
		return Response.status(500).entity(messagePayload).build();
	}

	MessagePayload extractMessagePayload(final Response response) {

		Object entity = response.getEntity();

		if (entity == null) {

			return null;
		}

		if (entity instanceof MessagePayload) {

			return (MessagePayload) entity;
		}

		if (entity instanceof ResponsePayload) {

			return ((ResponsePayload) entity).getMessage();
		}

		LOGGER.warn("authprovider gibt kein ResponsePayload und kein MessagePayload zurück, sondern ein {}!!!",
			entity.getClass().getSimpleName());
		return MessagePayload.error(applicationMessages.getString("general.internalServerError"));

	}
}
