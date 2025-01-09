// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.exceptions;

/**
 * MailversandException
 */
public class MailversandException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MailversandException(final String message, final Throwable cause) {

		super(message, cause);

	}

	public MailversandException(final String message) {

		super(message);

	}

}
