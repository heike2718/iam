// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.error;

/**
 * SessionExpiredException
 */
public class SessionExpiredException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public SessionExpiredException(final String message) {

		super(message);
	}

}
