// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.payload;

import javax.json.bind.annotation.JsonbProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import de.egladil.web.commons_validation.annotations.Honeypot;

/**
 * OrderTempPasswordCredentials
 */
public class OrderTempPasswordPayload {

	@NotNull
	@Email
	@Size(min = 1, max = 255)
	private String email;

	@JsonbProperty
	private ClientCredentials clientCredentials;

	@Honeypot(message = "")
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

	public ClientCredentials getClientCredentials() {

		return clientCredentials;
	}

	public void setClientCredentials(final ClientCredentials clientCredentials) {

		this.clientCredentials = clientCredentials;
	}

	@Override
	public String toString() {

		return "OrderTempPasswordPayload [email=" + email + ", clientCredentials=" + clientCredentials + "]";
	}

}
