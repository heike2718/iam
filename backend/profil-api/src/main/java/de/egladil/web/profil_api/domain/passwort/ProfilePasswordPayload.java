// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_api.domain.passwort;

import de.egladil.web.profil_api.domain.auth.util.SecUtils;
import de.egladil.web.profil_api.domain.validation.annotations.Passwort;
import de.egladil.web.profil_api.domain.validation.annotations.ValidPasswords;
import jakarta.validation.constraints.NotNull;

/**
 * ProfilePasswordPayload
 */
public class ProfilePasswordPayload {

	@NotNull
	@Passwort
	private String passwort;

	@NotNull
	@ValidPasswords
	private TwoPasswords twoPasswords;

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
			passwort = null;
		}
	}

	public TwoPasswords getTwoPasswords() {

		return twoPasswords;
	}

	public void setTwoPasswords(final TwoPasswords twoPasswords) {

		this.twoPasswords = twoPasswords;
	}

	public String getPasswort() {

		return passwort;
	}

	public void setPasswort(final String passwort) {

		this.passwort = passwort;
	}

}
