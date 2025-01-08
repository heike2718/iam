// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.benutzerprofil.domain.benutzer;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.web.auth_validations.IValidationMessages;
import de.egladil.web.auth_validations.annotations.InputSecured;
import de.egladil.web.auth_validations.annotations.LoginName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * BenutzerDto
 */
@Schema(description = "änderbare Daten eines Users")
public class BenutzerDto {

	@JsonProperty
	@LoginName
	@NotBlank(message = "Der Login-Name ist erforderlich.")
	@Size(max = 255, message = "Der Login-Name ist zu lang (maximal {max} Zeichen).")
	private String loginName;

	@JsonProperty
	@Email
	@NotBlank(message = "Die Mailadresse ist erforderlich.")
	@Size(max = 255, message = "Die Maildresse ist zu lang (maximal {max} Zeichen).")
	private String email;

	@JsonProperty
	@InputSecured(message = "Der Vorname enthält ungültige Zeichen. " + IValidationMessages.INPUT_SECURED_ERLAUBTE_ZEICHEN)
	@NotBlank(message = "Der Vorname ist erforderlich.")
	@Size(max = 100, message = "Der Vorname ist zu lang (maximal {max} Zeichen).")
	private String vorname;

	@JsonProperty
	@InputSecured(message = "Der Nachname enthält ungültige Zeichen. " + IValidationMessages.INPUT_SECURED_ERLAUBTE_ZEICHEN)
	@NotBlank(message = "Der Nachname ist erforderlich.")
	@Size(max = 100, message = "Der Nachname ist zu lang (maximal {max} Zeichen).")
	private String nachname;

	/**
	 *
	 */
	public BenutzerDto() {

	}

	public String getLoginName() {

		return loginName;
	}

	public void setLoginName(final String loginName) {

		this.loginName = loginName;
	}

	public String getEmail() {

		return email;
	}

	public void setEmail(final String email) {

		this.email = email;
	}

	public String getVorname() {

		return vorname;
	}

	public void setVorname(final String vorname) {

		this.vorname = vorname;
	}

	public String getNachname() {

		return nachname;
	}

	public void setNachname(final String nachname) {

		this.nachname = nachname;
	}

	private boolean hasName() {

		return StringUtils.isNotBlank(vorname) || StringUtils.isNotBlank(nachname);
	}

	@JsonIgnore
	public String getFullName() {

		if (!hasName()) {

			return "";
		}
		return vorname + " " + nachname;
	}

	@Override
	public String toString() {

		return "BenutzerDto [vorname=" + vorname + ", nachname=" + nachname + ", loginName=" + loginName + ", email=" + email + "]";
	}

}
