// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.payload.profile;

import de.egladil.web.auth_validations.annotations.PasswortLogin;
import de.egladil.web.auth_validations.annotations.ValidPasswords;
import de.egladil.web.auth_validations.dto.ZweiPassworte;
import de.egladil.web.auth_validations.utils.SecUtils;
import jakarta.validation.constraints.NotNull;

/**
 * ProfilePasswordPayload
 */
public class ProfilePasswordPayload {

	@NotNull
	@PasswortLogin
	private String passwort;

	@NotNull
	@ValidPasswords
	private ZweiPassworte twoPasswords;

	/**
	 * Entfernt alle sensiblen Infos: also password und passwordWdh.
	 */
	public void clean() {

		if (twoPasswords != null) {

			twoPasswords.clean();
			twoPasswords = null;
		}

		if (passwort != null) {

			passwort = SecUtils.wipe(passwort);
		}
	}

	public ZweiPassworte getTwoPasswords() {

		return twoPasswords;
	}

	public void setTwoPasswords(final ZweiPassworte twoPasswords) {

		this.twoPasswords = twoPasswords;
	}

	public String getPasswort() {

		return passwort;
	}

	public void setPasswort(final String passwort) {

		this.passwort = passwort;
	}

}
