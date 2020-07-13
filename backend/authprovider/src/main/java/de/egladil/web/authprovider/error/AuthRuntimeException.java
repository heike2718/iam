// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.error;

/**
 * AuthRuntimeException
 */
public class AuthRuntimeException extends RuntimeException {

	/* serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * Erzeugt eine Instanz von AuthRuntimeException
	 */
	public AuthRuntimeException(final String message, final Throwable cause) {

		super(message, cause);
	}

	/**
	 * Erzeugt eine Instanz von AuthRuntimeException
	 */
	public AuthRuntimeException(final String message) {

		super(message);
	}

}
