// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.validation;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ValidationErrorResponseDto
 */
public class ValidationErrorResponseDto {

	@JsonProperty
	String fieldName;

	@JsonProperty
	String message;

	ValidationErrorResponseDto() {

		super();

	}

	public ValidationErrorResponseDto(final String fieldName, final String message) {

		this.fieldName = fieldName;
		this.message = message;
	}

	public String getFieldName() {

		return fieldName;
	}

	public String getMessage() {

		return message;
	}

}
