// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import de.egladil.web.commons_validation.SecUtils;
import de.egladil.web.commons_validation.annotations.Honeypot;
import de.egladil.web.commons_validation.annotations.LoginName;
import de.egladil.web.commons_validation.annotations.Passwort;

/**
 * AuthorizationCredentials sind die Daten, mit denen ein Benutzer eine Änderung an seinem Profil autoriisieren kann.
 */
public class AuthorizationCredentials {

	@NotNull
	@LoginName
	@Size(max = 255)
	private String loginName;

	@NotNull
	@Passwort
	private String passwort;

	@Honeypot(message = "")
	private String kleber;

	/**
	 *
	 */
	public AuthorizationCredentials() {

	}

	/**
	 * @param loginName
	 * @param passwort
	 */
	public AuthorizationCredentials(@NotNull @Size(max = 255) final String loginName, @NotNull final String passwort) {

		this.loginName = loginName;
		this.passwort = passwort;
	}

	public String getLoginName() {

		return loginName;
	}

	public void setLoginName(final String loginName) {

		this.loginName = loginName;
	}

	public String getPasswort() {

		return passwort;
	}

	public void setPasswort(final String passwort) {

		this.passwort = passwort;
	}

	public String getKleber() {

		return kleber;
	}

	public void setKleber(final String kleber) {

		this.kleber = kleber;
	}

	public void clean() {

		if (passwort != null) {

			this.passwort = SecUtils.wipe(passwort);
		}
	}

	@Override
	public String toString() {

		return "AuthorizationCredentials [loginName=" + loginName + "]";
	}

}
