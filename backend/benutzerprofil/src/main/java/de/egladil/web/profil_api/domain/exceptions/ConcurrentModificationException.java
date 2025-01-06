// =====================================================
// Project: profil-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_api.domain.exceptions;

/**
 * ConcurrentModificationException
 */
public class ConcurrentModificationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ConcurrentModificationException(final String message) {

		super(message);

	}

}
