// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * OrderTempPasswordPayload
 */
public class OrderTempPasswordPayload {

	@NotNull
	@Email
	@Size(min = 1, max = 255)
	private String email;

	// keine Annotation, damit das geloggt wird
	private String kleber;

	public String getEmail() {

		return email;
	}

	public void setEmail(final String email) {

		this.email = email;
	}

	public String getKleber() {

		return kleber;
	}

	public void setKleber(final String kleber) {

		this.kleber = kleber;
	}

}
