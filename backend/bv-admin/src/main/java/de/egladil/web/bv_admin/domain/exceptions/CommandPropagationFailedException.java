// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.exceptions;

/**
 * CommandPropagationFailedException
 */
public class CommandPropagationFailedException extends RuntimeException {

	private static final long serialVersionUID = 8814673262453282669L;

	public CommandPropagationFailedException(final String message, final Throwable cause) {

		super(message, cause);

	}

	public CommandPropagationFailedException(final String message) {

		super(message);

	}

}
