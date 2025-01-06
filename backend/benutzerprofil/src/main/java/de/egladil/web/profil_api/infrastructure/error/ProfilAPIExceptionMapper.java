// =====================================================
// Project: profil-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_api.infrastructure.error;

import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.profil_api.domain.auth.dto.MessagePayload;
import de.egladil.web.profil_api.domain.exceptions.AuthRuntimeException;
import de.egladil.web.profil_api.domain.exceptions.ConcurrentModificationException;
import de.egladil.web.profil_api.domain.exceptions.ConflictException;
import de.egladil.web.profil_api.domain.exceptions.ProfilAPIRuntimeException;
import de.egladil.web.profil_api.domain.exceptions.SessionExpiredException;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * ProfilAPIExceptionMapper
 */
@Provider
public class ProfilAPIExceptionMapper implements ExceptionMapper<Throwable> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProfilAPIExceptionMapper.class);

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	@Inject
	ContainerRequestContext containerRequestContext;

	@Override
	public Response toResponse(final Throwable exception) {

		LOGGER.debug(exception.getMessage(), exception);

		String path = containerRequestContext.getUriInfo().getPath();
		String method = containerRequestContext.getMethod();

		LOGGER.info("====> method={}, path={}", method, path);

		if (exception instanceof WebApplicationException) {

			WebApplicationException ex = (WebApplicationException) exception;

			LOGGER.error("WebApplicationException: http-status={}", ex.getResponse().getStatus());

			if (ex.getResponse().getStatus() == 403) {

				LOGGER.warn(
					"403: koennte auch eine fehlende mod-security-Konfiguration sein, wenn es ein PUT oder DELETE-Request war");
				return Response.status(403).entity(MessagePayload.error("Diese Aktion ist nicht erlaubt")).build();
			}

			return ex.getResponse();
		}

		if (exception instanceof AuthRuntimeException) {

			LOGGER.warn(exception.getMessage());
			return Response.status(Status.UNAUTHORIZED).build();
		}

		if (exception instanceof SessionExpiredException) {

			return Response.status(440).entity(MessagePayload.warn(exception.getMessage()))
				.build();
		}

		if (exception instanceof ConflictException) {

			return Response.status(409).entity(MessagePayload.warn(exception.getMessage()))
				.build();
		}

		if (exception instanceof ConcurrentModificationException) {

			return Response.status(412).entity(MessagePayload.warn(
				"Ihr Benutzerkonto wurde zwischenzeitlich anscheinend gelöscht. Bitte kontaktieren Sie die Mailadresse im Impressum."))
				.build();
		}

		LOGGER.error(exception.getMessage(), exception);

		if (exception instanceof ProfilAPIRuntimeException) {

			return Response.status(500).entity(MessagePayload.error(exception.getMessage()))
				.build();
		}

		return Response.status(500).entity(MessagePayload.error(applicationMessages.getString("general.internalServerError")))
			.build();
	}

}
