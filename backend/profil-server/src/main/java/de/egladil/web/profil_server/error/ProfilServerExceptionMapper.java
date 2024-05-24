// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_server.error;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.commons_net.exception.SessionExpiredException;
import de.egladil.web.commons_net.utils.CommonHttpUtils;
import de.egladil.web.commons_validation.payload.MessagePayload;
import de.egladil.web.commons_validation.payload.ResponsePayload;
import de.egladil.web.profil_server.ProfilServerApp;
import de.egladil.web.profil_server.domain.UserSession;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.NoContentException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * ProfilServerExceptionMapper
 * TODO: InvalidationCookie wieder einkommentieren!!!!
 */
@Provider
public class ProfilServerExceptionMapper implements ExceptionMapper<Exception> {

	/**
	 *
	 */
	private static final String GENERAL_SERVERERROR = "Es ist ein Fehler aufgetreten. Bitte senden Sie eine Mail an info@egladil.de";

	private static final Logger LOG = LoggerFactory.getLogger(ProfilServerExceptionMapper.class);

	@Context
	SecurityContext securityContext;

	@Override
	public Response toResponse(final Exception exception) {

		if (exception instanceof NoContentException) {

			return Response.status(204).build();
		}

		String exceptionMessage = exception.getMessage();

		if (exception instanceof AuthException) {

			ResponsePayload payload = ResponsePayload.messageOnly(MessagePayload.error(exceptionMessage));
			LOG.warn(exceptionMessage);
			return Response.status(401)
				.cookie(CommonHttpUtils.createSessionInvalidatedCookie(ProfilServerApp.CLIENT_COOKIE_PREFIX))
				.entity(payload)
				.build();
		}

		if (exception instanceof SessionExpiredException) {

			ResponsePayload payload = ResponsePayload.messageOnly(MessagePayload.error(exceptionMessage));
			LOG.warn(exceptionMessage);

			return Response.status(908)
				.entity(payload)
				.cookie(CommonHttpUtils.createSessionInvalidatedCookie(ProfilServerApp.CLIENT_COOKIE_PREFIX))
				.build();
		}

		if (exception instanceof ForbiddenException) {

			LOG.warn(exceptionMessage, exception);

			ResponsePayload payload = ResponsePayload.messageOnly(MessagePayload.error("Zugang für alle User gesperrt"));
			return Response.status(Response.Status.FORBIDDEN).entity(payload).build();
		}

		if (exception instanceof PropagationFailedException) {

			LOG.info("caught :)");
			ResponsePayload payload = ResponsePayload.messageOnly(MessagePayload.error(exceptionMessage));
			return Response.serverError()
				.header("X-Auth-Error", exceptionMessage)
				.entity(payload)
				.build();
		}

		if (exception instanceof ProfilserverRuntimeException || exception instanceof ClientAuthException) {

			// wurde schon geloggt
		} else {

			if (securityContext != null && securityContext.getUserPrincipal() instanceof UserSession) {

				UserSession userSession = (UserSession) securityContext.getUserPrincipal();
				LOG.error("{} - {}: {}", StringUtils.abbreviate(userSession.getIdReference(), 11),
					StringUtils.abbreviate(userSession.getUuid(), 11), exceptionMessage, exception);
			} else {

				LOG.error(exceptionMessage, exception);
			}
		}

		ResponsePayload payload = ResponsePayload.messageOnly(MessagePayload.error(
			GENERAL_SERVERERROR));

		return Response.status(Status.INTERNAL_SERVER_ERROR).header("X-Profilserver-Error", payload.getMessage().getMessage())
			.entity(payload).build();
	}

}
