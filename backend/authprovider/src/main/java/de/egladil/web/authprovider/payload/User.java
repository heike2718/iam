// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.web.authprovider.domain.ResourceOwner;

/**
 * User enthält die öffentlichen Daten eines ResourceOwners.
 */
public class User {

	@JsonProperty
	private String loginName;

	@JsonProperty
	private String email;

	@JsonProperty
	private String vorname;

	@JsonProperty
	private String nachname;

	@JsonProperty
	private String nonce;

	public static User fromResourceOwner(final ResourceOwner resourceOwner) {

		User result = new User();

		result.loginName = resourceOwner.getLoginName();
		result.email = resourceOwner.getEmail();
		result.vorname = resourceOwner.getVorname();
		result.nachname = resourceOwner.getNachname();

		return result;
	}

	/**
	 *
	 */
	public User() {

	}

	public String getLoginName() {

		return loginName;
	}

	public String getEmail() {

		return email;
	}

	public String getVorname() {

		return vorname;
	}

	public String getNachname() {

		return nachname;
	}

	public void setNonce(final String nonce) {

		this.nonce = nonce;
	}

}
