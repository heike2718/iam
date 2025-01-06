// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.payload;

import de.egladil.web.auth_validations.annotations.UuidString;
import de.egladil.web.auth_validations.annotations.ValidPasswords;
import de.egladil.web.auth_validations.dto.ZweiPassworte;
import de.egladil.web.auth_validations.utils.SecUtils;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * TempPasswordCredentials
 */
public class ChangeTempPasswordPayload {

	@NotNull(message = "tokenId ist erforderlich")
	@UuidString
	@Size(min = 1, max = 40)
	private String tokenId;

	@NotNull(message = "email ist erforderlich")
	@Email(message = "keine gültige email")
	@Size(min = 1, max = 255)
	private String email;

	@NotNull(message = "tempPassword ist erforderlich")
	@UuidString
	@Size(min = 1, max = 40)
	private String tempPassword;

	// keine Annotation, damit das geloggt wird
	private String kleber;

	@NotNull(message = "zweiPassworte ist erforderlich")
	@ValidPasswords
	private ZweiPassworte zweiPassworte;

	/**
	 * Entfernt alle sensiblen Infos: also password, passwordWdh und tempPassword.
	 */
	public void clean() {

		if (zweiPassworte != null) {

			zweiPassworte.clean();
		}
		tempPassword = SecUtils.wipe(tempPassword);
	}

	@Override
	public String toString() {

		return "ChangeTempPasswordPayload [tokenId=" + tokenId + ", email=" + email + "]";
	}

	public String getEmail() {

		return email;
	}

	public void setEmail(final String email) {

		this.email = email;
	}

	public String getTempPassword() {

		return tempPassword;
	}

	public void setTempPassword(final String tempPassword) {

		this.tempPassword = tempPassword;
	}

	public String getTokenId() {

		return tokenId;
	}

	public void setTokenId(final String tokenId) {

		this.tokenId = tokenId;
	}

	public ZweiPassworte getZweiPassworte() {

		return zweiPassworte;
	}

	public void setZweiPassworte(final ZweiPassworte zweiPassworte) {

		this.zweiPassworte = zweiPassworte;
	}

	public String getKleber() {

		return kleber;
	}

	public void setKleber(final String kleber) {

		this.kleber = kleber;
	}
}
