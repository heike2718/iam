// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.exceptions;

import org.hibernate.exception.ConstraintViolationException;

/**
 * AuthAdminSQLExceptionHelper
 */
public class AuthAdminSQLExceptionHelper {

	/**
	 * Versucht, eine ConstraintViolationException zu finden.
	 *
	 * @param  th
	 * @param  errorMessage
	 *                                      String das, was in der Message der AuthAdminAPIRuntimeException stehen soll, wenn keine
	 *                                      ConstraintViolationException gefunden werden konnte.
	 * @return                              ConstraintViolationException
	 * @throws AuthAdminAPIRuntimeException
	 */
	public static ConstraintViolationException unwrappConstraintViolationException(final Throwable th, final String errorMessage) throws AuthAdminAPIRuntimeException {

		if (th instanceof ConstraintViolationException) {

			return (ConstraintViolationException) th;
		}

		Throwable throwable = th;

		Throwable cause = th.getCause();

		while (cause != null && cause != throwable) {

			if (cause instanceof ConstraintViolationException) {

				return (ConstraintViolationException) cause;
			}
			throwable = cause;
			cause = throwable.getCause();
		}

		throw new AuthAdminAPIRuntimeException(errorMessage, th);
	}
}
