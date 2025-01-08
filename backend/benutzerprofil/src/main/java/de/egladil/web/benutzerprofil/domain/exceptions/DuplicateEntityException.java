// =====================================================
// Project: benutzerprofil
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.benutzerprofil.domain.exceptions;

import de.egladil.web.benutzerprofil.domain.benutzer.DuplicateAttributeType;

/**
 * DuplicateEntityException
 */
public class DuplicateEntityException extends RuntimeException {

	private static final long serialVersionUID = -4363667040261045259L;

	private final int defaultStatuscode = 412;

	private DuplicateAttributeType duplicateAttributeType;

	public DuplicateEntityException(final String message, final Throwable cause) {

		super(message, cause);

	}

	public DuplicateEntityException(final String message) {

		super(message);

	}

	public DuplicateEntityException(final DuplicateAttributeType duppplicateAttributeType) {

		super();
		this.duplicateAttributeType = duppplicateAttributeType;
	}

	public DuplicateAttributeType getDuplicateAttributeType() {

		return duplicateAttributeType;
	}

	public int getDefaultStatuscode() {

		return defaultStatuscode;
	}

}
