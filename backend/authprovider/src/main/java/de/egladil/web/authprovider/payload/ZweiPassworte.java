// =====================================================
// Project: commons-validation
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.payload;

import de.egladil.web.authprovider.validation.annotations.PasswortNeu;
import de.egladil.web.authprovider.validation.annotations.ValidPasswords;
import de.egladil.web.commons_validation.SecUtils;
import jakarta.validation.constraints.NotNull;

/**
 * TwoStringsPayload
 */
@ValidPasswords
public class ZweiPassworte {

	@NotNull
	@PasswortNeu
	private String passwort;

	@NotNull
	@PasswortNeu
	private String passwortWdh;

	/**
	 *
	 */
	public ZweiPassworte() {

	}

	/**
	 * @param passwort
	 * @param passwortWdh
	 */
	public ZweiPassworte(final String passwort, final String passwortWdh) {

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
