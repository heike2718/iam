// =====================================================
// Project: commons-validation
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_validations.dto;

import de.egladil.web.auth_validations.annotations.PasswortNeu;
import de.egladil.web.auth_validations.annotations.ValidPasswords;
import de.egladil.web.auth_validations.utils.SecUtils;
import jakarta.validation.constraints.NotNull;

/**
 * TwoStringsPayload
 */
@ValidPasswords
public class ZweiPassworte {

	@NotNull(message = "Das (neue) Passwort ist erforderlich.")
	@PasswortNeu(message = "Das (neue) Passwort ist nicht regelkonform.")
	private String passwort;

	@NotNull(message = "Das wiederholte Passwort ist erforderlich.")
	@PasswortNeu(message = "Das wiederholte Passwort ist nicht regelkonform.")
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
