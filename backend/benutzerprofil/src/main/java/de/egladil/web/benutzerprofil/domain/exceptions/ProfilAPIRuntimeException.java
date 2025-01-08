// =====================================================
// Project: benutzerprofil
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.benutzerprofil.domain.exceptions;

/**
 * ProfilAPIRuntimeException
 */
// FIXME umbenennen
public class ProfilAPIRuntimeException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 * @param cause
	 */
	public ProfilAPIRuntimeException(final String message, final Throwable cause) {

		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ProfilAPIRuntimeException(final String message) {

		super(message);
	}

}
