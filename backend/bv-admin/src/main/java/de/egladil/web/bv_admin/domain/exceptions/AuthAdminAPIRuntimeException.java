// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.exceptions;

/**
 * AuthAdminAPIRuntimeException
 */
public class AuthAdminAPIRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AuthAdminAPIRuntimeException(final String message, final Throwable cause) {

		super(message, cause);

	}

	public AuthAdminAPIRuntimeException(final String message) {

		super(message);

	}

}
