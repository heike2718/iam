// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * StringPayload
 */
public class StringPayload {

	@NotBlank
	@Size(max = 255)
	private String input;

	/**
	 *
	 */
	public StringPayload() {

	}

	public String getInput() {

		return input;
	}

	public void setInput(final String input) {

		this.input = input;
	}
}
