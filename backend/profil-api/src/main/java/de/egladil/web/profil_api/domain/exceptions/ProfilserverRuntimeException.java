// =====================================================
// Project: profil-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_api.domain.exceptions;

/**
 * ProfilserverRuntimeException
 */
// FIXME umbenennen
public class ProfilserverRuntimeException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 * @param cause
	 */
	public ProfilserverRuntimeException(final String message, final Throwable cause) {

		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ProfilserverRuntimeException(final String message) {

		super(message);
	}

}
