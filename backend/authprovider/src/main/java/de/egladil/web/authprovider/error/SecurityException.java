// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.error;

/**
 * SecurityException
 */
public class SecurityException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	public SecurityException() {

	}

	/**
	 * @param message
	 */
	public SecurityException(final String message) {

		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SecurityException(final String message, final Throwable cause) {

		super(message, cause);
	}

}
