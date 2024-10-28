// =====================================================
// Project: commons-validation
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_api.domain.passwort;

import de.egladil.web.profil_api.domain.auth.util.SecUtils;
import de.egladil.web.profil_api.domain.validation.annotations.Passwort;
import de.egladil.web.profil_api.domain.validation.annotations.ValidPasswords;
import jakarta.validation.constraints.NotNull;

/**
 * TwoStringsPayload
 */
@ValidPasswords
public class TwoPasswords {

	@NotNull
	@Passwort
	private String passwort;

	@NotNull
	@Passwort
	private String passwortWdh;

	/**
	 *
	 */
	public TwoPasswords() {

	}

	/**
	 * @param passwort
	 * @param passwortWdh
	 */
	public TwoPasswords(final String passwort, final String passwortWdh) {

		super();
		this.passwort = passwort;
		this.passwortWdh = passwortWdh;
	}

	public String getPasswort() {

		return passwort;
	}

	public String getPasswortWdh() {

		return passwortWdh;
	}

	public void setPasswort(final String passwort) {

		this.passwort = passwort;
	}

	public void setPasswortWdh(final String passwortWdh) {

		this.passwortWdh = passwortWdh;
	}

	/**
	 * Entfernt alle sensiblen Infos: also password, passwordWdh.
	 */
	public void clean() {

		passwort = SecUtils.wipe(passwort);
		passwortWdh = SecUtils.wipe(passwortWdh);
	}

}
