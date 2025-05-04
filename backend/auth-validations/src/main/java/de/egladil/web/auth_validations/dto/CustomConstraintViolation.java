// =====================================================
// Project: auth-validations
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_validations.dto;

/**
 * CustomConstraintViolation
 */
public class CustomConstraintViolation {

	private String field;

	private String message;

	public String getField() {

		return field;
	}

	public void setField(final String field) {

		this.field = field;
	}

	public String getMessage() {

		return message;
	}

	public void setMessage(final String message) {

		this.message = message;
	}
}
