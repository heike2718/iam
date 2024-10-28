// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_api.domain.benutzer;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.web.profil_api.domain.validation.annotations.InputSecured;
import de.egladil.web.profil_api.domain.validation.annotations.LoginName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * BenutzerDto
 */
@Schema(description = "änderbare Daten eines Users")
public class BenutzerDto {

	@JsonProperty
	@LoginName
	@NotNull
	@Size(min = 1, max = 255)
	private String loginName;

	@JsonProperty
	@Email
	@NotNull
	@Size(min = 1, max = 255)
	private String email;

	@JsonProperty
	@InputSecured
	@NotNull
	@Size(min = 1, max = 100)
	private String vorname;

	@JsonProperty
	@InputSecured
	@NotNull
	@Size(min = 1, max = 100)
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
