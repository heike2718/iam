// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.payload.profile;

import de.egladil.web.auth_validations.annotations.InputSecured;
import de.egladil.web.auth_validations.annotations.LoginName;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * ChangeProfileDataPayload
 */
public class ProfileDataPayload {

	@NotNull
	@Email
	@Size(min = 1, max = 255)
	private String email;

	@NotNull
	@LoginName
	@Size(max = 255)
	private String loginName;

	@InputSecured
	@Size(min = 1, max = 100)
	@Column(name = "VORNAME")
	private String vorname;

	@InputSecured
	@Size(min = 1, max = 100)
	@Column(name = "NACHNAME")
	private String nachname;

	public String getEmail() {

		return email;
	}

	public void setEmail(final String email) {

		this.email = email;
	}

	public String getLoginName() {

		return loginName;
	}

	public void setLoginName(final String loginName) {

		this.loginName = loginName;
	}

	public String getVorname() {

		return vorname;
	}

	public void setVorname(final String vorname) {

		this.vorname = vorname;
	}

	public String getNachname() {

		return nachname;
	}

	public void setNachname(final String nachname) {

		this.nachname = nachname;
	}

	@Override
	public String toString() {

		return "ChangeProfileDataPayload [email=" + email + ", loginName=" + loginName + ", vorname=" + vorname + ", nachname="
			+ nachname
			+ "]";
	}

}
