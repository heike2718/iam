// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.benutzerprofil.domain.exceptions;

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
