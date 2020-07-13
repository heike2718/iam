//=====================================================
// Project: authprovider
// (c) Heike Winkelvoß
//=====================================================
package de.egladil.web.authprovider.error;

/**
 * AccountDeactivatedException
 */
public class AccountDeactivatedException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public AccountDeactivatedException(final String message) {
		super(message);
	}

}
