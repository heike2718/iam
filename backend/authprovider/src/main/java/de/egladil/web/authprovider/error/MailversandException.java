// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.error;

/**
 * MailversandException
 */
public class MailversandException extends RuntimeException {

	private static final long serialVersionUID = -2832990161555202805L;

	public MailversandException(final String message, final Throwable cause) {

		super(message, cause);

	}

	public MailversandException(final String message) {

		super(message);

	}

}
