// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_server.payload;

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
}
