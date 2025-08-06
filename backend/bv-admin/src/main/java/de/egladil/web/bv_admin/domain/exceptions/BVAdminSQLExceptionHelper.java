// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.exceptions;

import org.hibernate.exception.ConstraintViolationException;

/**
 * BVAdminSQLExceptionHelper
 */
public class BVAdminSQLExceptionHelper {

	/**
	 * Versucht, eine ConstraintViolationException zu finden.
	 *
	 * @param th
	 * @param errorMessage String das, was in der Message der BVAdminAPIRuntimeException stehen soll, wenn keine
	 * ConstraintViolationException gefunden werden konnte.
	 * @return ConstraintViolationException
	 * @throws BVAdminAPIRuntimeException
	 */
	public static ConstraintViolationException unwrappConstraintViolationException(final Throwable th, final String errorMessage)
		throws BVAdminAPIRuntimeException {

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

		throw new BVAdminAPIRuntimeException(errorMessage, th);
	}
}
