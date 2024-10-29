// =====================================================
// Project: commons-validation
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_validations.dto;

import de.egladil.web.auth_validations.annotations.PasswortNeu;
import de.egladil.web.auth_validations.utils.SecUtils;
import jakarta.validation.constraints.NotNull;

/**
 * TwoStringsPayload
 */
public class ZweiPassworte {

	@NotNull(message = "passwort ist erforderlich")
	@PasswortNeu(message = "passwort ist nicht regelkonform")
	private String passwort;

	@NotNull(message = "passwortWdh ist erforderlich")
	@PasswortNeu(message = "passwortWdh ist nicht regelkonform")
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
