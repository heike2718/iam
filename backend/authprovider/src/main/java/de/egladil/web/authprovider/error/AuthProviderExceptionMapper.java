// =====================================================
// Projekt: authenticationprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.auth_validations.exceptions.InvalidInputException;
import de.egladil.web.authprovider.dao.impl.PersistenceExceptionMapper;
import de.egladil.web.authprovider.payload.MessagePayload;
import de.egladil.web.commons_net.exception.SessionExpiredException;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.RollbackException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.NoContentException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * AuthProviderExceptionMapper
 */
@Provider
public class AuthProviderExceptionMapper implements ExceptionMapper<Exception> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthProviderExceptionMapper.class);

	@Override
	public Response toResponse(final Exception exception) {

		String generalError = "Es ist ein Serverfehler aufgetreten. Bitte senden Sie eine Mail an info@egladil.de";

		LOGGER.debug("Exception {} gefangen", exception.getClass().getName(), exception);

		if (exception instanceof NoContentException) {

			return Response.status(204).build();
		}

		String exceptionMessage = exception.getMessage();

		if (exception instanceof SecurityException) {

			if (exceptionMessage != null) {

				LOGGER.warn(LogmessagePrefixes.BOT + exceptionMessage, exception);
			}
			return Response.status(Status.FORBIDDEN)
				.entity(MessagePayload.error("Netter Versuch, klappt aber nicht.")).build();
		}

		if (exception instanceof SessionExpiredException) {

			return Response.status(908)
				.header("X-Auth-Error", "SessionExpired")
				.entity(MessagePayload.warn(exceptionMessage))
				.build();
		}

		if (exception instanceof NotFoundException) {

			return Response.status(404)
				.entity(MessagePayload.error("Not Found"))
				.build();
		}

		if (exception instanceof AccountDeactivatedException) {

			return Response.status(401)
				.entity(MessagePayload.error(exceptionMessage))
				.build();
		}

		if (exception instanceof ClientAccessTokenRuntimeException) {

			ClientAccessTokenRuntimeException ex = (ClientAccessTokenRuntimeException) exception;

			if (ex.getClientCredentials() != null && ex.getClientCredentials().getRedirectUrl() != null) {

				LOGGER.warn("Das client access token {} ist unterwegs verlorengegangen. redirectUrl={}",
					ex.getClientCredentials().getAccessToken(), ex.getClientCredentials().getRedirectUrl());

				return Response.status(904)
					.entity(MessagePayload
						.error("Ups, da ist etwas schiefgegangen: redirectUrl=" + ex.getClientCredentials().getRedirectUrl()))
					.build();
			} else {

				LOGGER.error(ex.getMessage());
				return Response.serverError()
					.header("X-Auth-Error", "Serverfehler")
					.entity(MessagePayload.error("Fehler beim Holen der ClientDaten"))
					.build();
			}
		}

		if (exception instanceof AuthException) {

			LOGGER.warn("{}: {}", exception.getClass().getSimpleName(), exceptionMessage);
			return Response.status(401)
				.entity(MessagePayload.error(exceptionMessage))
				.build();
		}

		if (exception instanceof RollbackException || exception instanceof PersistenceException) {

			RuntimeException cause = PersistenceExceptionMapper.mapException(exception);

			if (cause instanceof ConcurrentUpdateException) {

				return handleConcurrentUpdateException((ConcurrentUpdateException) cause);
			}

			if (cause instanceof DuplicateEntityException) {

				return handleDuplicateEntityException((DuplicateEntityException) cause);
			}

		}

		if (exception instanceof ConcurrentUpdateException) {

			return handleConcurrentUpdateException((ConcurrentUpdateException) exception);
		}

		if (exception instanceof DuplicateEntityException) {

			return handleDuplicateEntityException((DuplicateEntityException) exception);
		}

		if (exception instanceof InvalidInputException) {

			InvalidInputException invalidInputException = (InvalidInputException) exception;
			// wurde schon geloggt.
			return Response.status(400)
				.header("X-Auth-Error", "ungültige Eingaben")
				.entity(MessagePayload.error(invalidInputException.getMessage()))
				.build();
		}

		if (exception instanceof InvalidRedirectUrl || exception instanceof AuthRuntimeException) {

			// wurde schon geloggt
			return Response.serverError()
				.header("X-Auth-Error", "Serverfehler")
				.entity(MessagePayload.error(generalError))
				.build();
		}

		if (exception instanceof AuthPersistenceException || exception instanceof ClientAuthException) {

			// wurde schon geloggt.

			return Response.serverError()
				.header("X-Auth-Error", generalError)
				.entity(MessagePayload.error(generalError))
				.build();
		}

		if (exception instanceof PropagationFailedException) {

			// wurde schon geloggt
			return Response.serverError()
				.header("X-Auth-Error", exceptionMessage)
				.entity(MessagePayload.error(exceptionMessage))
				.build();

		}

		LOGGER.error(exceptionMessage, exception);

		return Response.serverError()
			.header("X-Auth-Error", "Serverfehler")
			.entity(MessagePayload.error(generalError))
			.build();
	}

	private Response handleConcurrentUpdateException(final ConcurrentUpdateException e) {

		return Response.status(409)
			.header("X-Auth-Error", "concurrent update please reload")
			.entity(MessagePayload
				.error("Es ist ein Fehler aufgetreten. Bitte Klicken Sie 'abbrechen' und wiederholen Sie Ihre Änderungen."))
			.build();
	}

	private Response handleDuplicateEntityException(final DuplicateEntityException e) {

		LOGGER.error("fangen die DuplicateEntityException mit message={}", e.getMessage());

		return Response.status(412)
			.header("X-Auth-Error", "resource exists")
			.entity(MessagePayload.warn(e.getMessage()))
			.build();
	}
}
