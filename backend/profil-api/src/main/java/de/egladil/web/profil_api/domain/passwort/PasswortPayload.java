// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_api.domain.passwort;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.web.auth_validations.annotations.PasswortLogin;
import de.egladil.web.auth_validations.annotations.ValidPasswords;
import de.egladil.web.auth_validations.dto.ZweiPassworte;
import de.egladil.web.auth_validations.utils.SecUtils;
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
	@JsonProperty
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

	public void setZweiPassworte(final ZweiPassworte zweiPassworte) {

		this.zweiPassworte = zweiPassworte;
	}

	public String getPasswort() {

		return passwort;
	}

	public void setPasswort(final String passwort) {

		this.passwort = passwort;
	}

}
