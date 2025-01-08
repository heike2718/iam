// =====================================================
// Projekt: mk-commons
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.error;

import de.egladil.web.authprovider.payload.DuplicateAttributeType;

/**
 * DuplicateEntityException
 */
public class DuplicateEntityException extends RuntimeException {

	/* serialVersionUID */
	private static final long serialVersionUID = 1L;

	private final int defaultStatuscode = 412;

	private DuplicateAttributeType duplicateEntityType;

	/**
	 * Erzeugt eine Instanz von DuplicateEntityException
	 */
	public DuplicateEntityException(final String arg0, final Throwable arg1) {

		super(arg0, arg1);
	}

	/**
	 * Erzeugt eine Instanz von DuplicateEntityException
	 */
	public DuplicateEntityException(final String arg0) {

		super(arg0);
	}

	public int getDefaultStatuscode() {

		return defaultStatuscode;
	}

	public DuplicateAttributeType getDuplicateEntityType() {

		return duplicateEntityType;
	}

	public void setDuplicateEntityType(final DuplicateAttributeType duplicateEntityType) {

		this.duplicateEntityType = duplicateEntityType;
	}

}
