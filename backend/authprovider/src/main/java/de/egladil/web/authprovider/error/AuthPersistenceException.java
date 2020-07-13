//=====================================================
// Project: authprovider
// (c) Heike Winkelvoß
//=====================================================
package de.egladil.web.authprovider.error;

/**
 * AuthPersistenceException
 */
public class AuthPersistenceException extends AuthRuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 * @param cause
	 */
	public AuthPersistenceException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public AuthPersistenceException(final String message) {
		super(message);
	}

}
