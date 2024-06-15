// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.validation;

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
