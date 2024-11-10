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
import de.egladil.web.authprovider.payload.ResponsePayload;
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
				.entity(ResponsePayload.messageOnly(MessagePayload.error("Netter Versuch, klappt aber nicht."))).build();
		}

		if (exception instanceof SessionExpiredException) {

			ResponsePayload payload = ResponsePayload.messageOnly(MessagePayload.warn(exceptionMessage));
			return Response.status(908)
				.header("X-Auth-Error", "SessionExpired")
				.entity(payload)
				.build();
		}

		if (exception instanceof NotFoundException) {

			ResponsePayload payload = ResponsePayload.messageOnly(MessagePayload.error("Not Found"));
			return Response.status(404)
				.entity(payload)
				.build();
		}

		if (exception instanceof AccountDeactivatedException) {

			ResponsePayload payload = ResponsePayload.messageOnly(MessagePayload.error(exceptionMessage));
			return Response.status(401)
				.entity(payload)
				.build();
		}

		if (exception instanceof ClientAccessTokenRuntimeException) {

			ClientAccessTokenRuntimeException ex = (ClientAccessTokenRuntimeException) exception;

			if (ex.getClientCredentials() != null && ex.getClientCredentials().getRedirectUrl() != null) {

				LOGGER.warn("Das client access token {} ist unterwegs verlorengegangen. redirectUrl={}",
					ex.getClientCredentials().getAccessToken(), ex.getClientCredentials().getRedirectUrl());

				ResponsePayload payload = new ResponsePayload(MessagePayload.error("Ups, da ist etwas schiefgegangen"),
					ex.getClientCredentials().getRedirectUrl());
				return Response.status(904)
					.entity(payload)
					.build();
			} else {

				LOGGER.error(ex.getMessage());
				ResponsePayload payload = ResponsePayload.messageOnly(MessagePayload.error("Fehler beim Holen der ClientDaten"));
				return Response.serverError()
					.header("X-Auth-Error", "Serverfehler")
					.entity(payload)
					.build();
			}
		}

		if (exception instanceof AuthException) {

			ResponsePayload payload = ResponsePayload.messageOnly(MessagePayload.error(exceptionMessage));
			LOGGER.warn("{}: {}", exception.getClass().getSimpleName(), exceptionMessage);
			return Response.status(401)
				.entity(payload)
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

			return Response.status(400)
				.header("X-Auth-Error", "ungültige Eingaben")
				.entity(ResponsePayload.messageOnly(MessagePayload.error(exception.getMessage())))
				.build();
		}

		if (exception instanceof InvalidRedirectUrl || exception instanceof AuthRuntimeException) {

			// wurde schon geloggt
			return Response.serverError()
				.header("X-Auth-Error", "Serverfehler")
				.entity(ResponsePayload.messageOnly(MessagePayload
					.error(generalError)))
				.build();
		}

		if (exception instanceof AuthPersistenceException || exception instanceof ClientAuthException) {

			// wurde schon geloggt.

			ResponsePayload payload = ResponsePayload.messageOnly(MessagePayload.error(generalError));
			return Response.serverError()
				.header("X-Auth-Error", generalError)
				.entity(payload)
				.build();
		}

		if (exception instanceof PropagationFailedException) {

			// wurde schon geloggt
			ResponsePayload payload = ResponsePayload.messageOnly(MessagePayload.error(exceptionMessage));
			return Response.serverError()
				.header("X-Auth-Error", exceptionMessage)
				.entity(payload)
				.build();

		}

		LOGGER.error(exceptionMessage, exception);

		ResponsePayload payload = ResponsePayload.messageOnly(MessagePayload.error(generalError));
		return Response.serverError()
			.header("X-Auth-Error", "Serverfehler")
			.entity(payload)
			.build();
	}

	private Response handleConcurrentUpdateException(final ConcurrentUpdateException e) {

		ResponsePayload payload = ResponsePayload.messageOnly(MessagePayload
			.error("Es ist ein Fehler aufgetreten. Bitte Klicken Sie 'abbrechen' und wiederholen Sie Ihre Änderungen."));
		return Response.status(409)
			.header("X-Auth-Error", "concurrent update please reload")
			.entity(payload)
			.build();
	}

	private Response handleDuplicateEntityException(final DuplicateEntityException e) {

		ResponsePayload payload = ResponsePayload.messageOnly(MessagePayload.warn(e.getMessage()));
		return Response.status(412)
			.header("X-Auth-Error", "resource exists")
			.entity(payload)
			.build();
	}
}
