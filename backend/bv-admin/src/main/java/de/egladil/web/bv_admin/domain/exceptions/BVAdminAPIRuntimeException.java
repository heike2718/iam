// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.exceptions;

/**
 * BVAdminAPIRuntimeException
 */
public class BVAdminAPIRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BVAdminAPIRuntimeException(final String message, final Throwable cause) {

		super(message, cause);

	}

	public BVAdminAPIRuntimeException(final String message) {

		super(message);

	}

}
