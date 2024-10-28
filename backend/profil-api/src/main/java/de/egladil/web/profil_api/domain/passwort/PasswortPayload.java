// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_api.domain.passwort;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.web.profil_api.domain.auth.util.SecUtils;
import de.egladil.web.profil_api.domain.validation.annotations.PasswortLogin;
import de.egladil.web.profil_api.domain.validation.annotations.ValidPasswords;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * PasswortPayload
 */
public class PasswortPayload {

	@NotBlank
	@PasswortLogin
	@Size(max = 100, message = "maximal 100 Zeichen lang")
	private String passwort;

	@NotNull
	@ValidPasswords
	@JsonProperty(value = "twoPasswords")
	private ZweiPassworte zweiPassworte;

	/**
	 * Entfernt alle sensiblen Infos: also password und passwordWdh.
	 */
	public void clean() {

		if (zweiPassworte != null) {

			zweiPassworte.clean();
			zweiPassworte = null;
		}

		if (passwort != null) {

			passwort = SecUtils.wipe(passwort);
			passwort = null;
		}
	}

	public ZweiPassworte getZweiPassworte() {

		return zweiPassworte;
	}

	public void setZweiPassworte(final ZweiPassworte twoPasswords) {

		this.zweiPassworte = twoPasswords;
	}

	public String getPasswort() {

		return passwort;
	}

	public void setPasswort(final String passwort) {

		this.passwort = passwort;
	}

}
