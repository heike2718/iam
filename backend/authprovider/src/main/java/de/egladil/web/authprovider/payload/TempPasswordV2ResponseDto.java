// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TempPasswordV2ResponseDto
 */
public class TempPasswordV2ResponseDto {

	@JsonProperty
	private String message;

	public String getMessage() {

		return message;
	}

	public TempPasswordV2ResponseDto withMessage(final String message) {

		this.message = message;
		return this;
	}

}
