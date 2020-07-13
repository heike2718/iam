// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.error;

/**
 * ClientAuthException
 */
public class ClientAuthException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public ClientAuthException(final String message) {

		super(message);
	}

}
