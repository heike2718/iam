// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.error;

/**
 * ClientAccessTokenNotFoundException
 */
public class ClientAccessTokenNotFoundException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 * @param cause
	 */
	public ClientAccessTokenNotFoundException(final String message, final Throwable cause) {

		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ClientAccessTokenNotFoundException(final String message) {

		super(message);
	}

}
