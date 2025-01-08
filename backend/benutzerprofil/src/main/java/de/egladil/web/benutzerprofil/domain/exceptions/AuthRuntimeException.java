// =====================================================
// Project: benutzerprofil
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.benutzerprofil.domain.exceptions;

/**
 * AuthRuntimeException
 */
public class AuthRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public AuthRuntimeException(final String message) {

		super(message);
	}

	public AuthRuntimeException(final String message, final Throwable cause) {

		super(message, cause);

	}

}
