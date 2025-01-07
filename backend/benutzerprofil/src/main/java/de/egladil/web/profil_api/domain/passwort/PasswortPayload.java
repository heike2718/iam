// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_api.domain.passwort;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.web.auth_validations.annotations.PasswortLogin;
import de.egladil.web.auth_validations.dto.ZweiPassworte;
import de.egladil.web.auth_validations.utils.SecUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * PasswortPayload
 */
public class PasswortPayload {

	@NotBlank(message = "Das aktuelle Passwort ist erforderlich.")
	@PasswortLogin(message = "Das aktuelle Passwort enthält ungültige Zeichen.")
	@Size(max = 100, message = "Das aktuelle Passwort ist zu lang (max. {max} Zeichen.")
	private String passwort;

	@NotNull(message = "Die neuen Passwörter sind erforderlich.")
	@Valid
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
