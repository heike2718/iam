// =====================================================
// Project: profil-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_api.domain.exceptions;

/**
 * ConflictException
 */
public class ConflictException extends RuntimeException {

	private static final long serialVersionUID = -4363667040261045259L;

	public ConflictException(final String message, final Throwable cause) {

		super(message, cause);

	}

	public ConflictException(final String message) {

		super(message);

	}

}
