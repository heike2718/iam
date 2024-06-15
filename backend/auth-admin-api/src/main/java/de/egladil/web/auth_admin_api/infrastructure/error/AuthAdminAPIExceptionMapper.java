// =====================================================
// Project: aith-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.infrastructure.error;

import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.auth_admin_api.domain.auth.dto.MessagePayload;
import de.egladil.web.auth_admin_api.domain.exceptions.AuthAdminAPIRuntimeException;
import de.egladil.web.auth_admin_api.domain.exceptions.AuthException;
import de.egladil.web.auth_admin_api.domain.exceptions.SessionExpiredException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * AuthAdminAPIExceptionMapper
 */
@Provider
public class AuthAdminAPIExceptionMapper implements ExceptionMapper<Throwable> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthAdminAPIExceptionMapper.class);

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	@Override
	public Response toResponse(final Throwable exception) {

		LOGGER.debug(exception.getMessage(), exception);

		if (exception instanceof WebApplicationException) {

			WebApplicationException ex = (WebApplicationException) exception;

			if (ex.getResponse().getStatus() == 403) {

				LOGGER.warn(
					"403: könnte auch eine fehlende mod-security-Konfiguration sein, wenn es ein PUT oder DELETE-Request war");
				return Response.status(403).entity(MessagePayload.error("Diese Aktion ist nicht erlaubt")).build();
			}

			return ex.getResponse();
		}

		if (exception instanceof AuthException) {

			LOGGER.warn(exception.getMessage());
			return Response.status(Status.UNAUTHORIZED).build();
		}

		if (exception instanceof SessionExpiredException) {

			return Response.status(440).entity(MessagePayload.warn(exception.getMessage()))
				.build();
		}

		LOGGER.error(exception.getMessage(), exception);

		if (exception instanceof AuthAdminAPIRuntimeException) {

			return Response.status(500).entity(MessagePayload.error(exception.getMessage()))
				.build();
		}

		return Response.status(500).entity(MessagePayload.error(applicationMessages.getString("general.internalServerError")))
			.build();
	}

}
