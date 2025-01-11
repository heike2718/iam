// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.benutzer;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.web.bv_admin.domain.validation.StringLatinConstants;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * BenutzerTrefferlisteItem
 */
@Schema(description = "Ein Elememt der Trefferliste. Es sind die Attribute, nach denen auch gesucht werden kann.")
public class BenutzerTrefferlisteItem {

	@JsonProperty
	@Schema(description = "UUID des Users", example = "732b2ed8-b9b7-4800-8685-38fd09d330cf")
	@Pattern(regexp = "^[abcdef\\d\\-]*$")
	@Size(max = 36)
	private String uuid;

	@JsonProperty
	@Schema(description = "Vorname eines Users", example = "Rainer")
	@Pattern(regexp = StringLatinConstants.WHITELIST_REGEXP)
	@Size(max = 100)
	private String vorname;

	@JsonProperty
	@Schema(description = " Nachname eines Users", example = "Honig")
	@Pattern(regexp = StringLatinConstants.WHITELIST_REGEXP)
	@Size(max = 100)
	private String nachname;

	@JsonProperty
	@Schema(description = "email eines Users", example = "test@provider777.com")
	@Pattern(regexp = "^[a-zA-Z0-9\\.!#$%&'*+/=\\?\\^_`{|}~\\-@]*$")
	@Size(max = 255)
	private String email;

	@JsonProperty
	@Schema(description = "Rollen eines Users.", example = "STANDARD")
	@Pattern(regexp = "^[A-Z_,]*$")
	@Size(max = 150)
	private String rollen;

	@JsonProperty
	@Schema(description = "Flag ,ob der user aktiviert ist", example = "false")
	private boolean aktiviert;

	@JsonProperty
	@Schema(description = "Datum, an dem die Daten das letzte Mal geändert wurden - meist letztes Login", example = "2019-09-14 18:40:06")
	@Pattern(regexp = "^[\\d.\\-:]*$")
	@Size(max = 19)
	private String aenderungsdatum;

	public String getUuid() {

		return uuid;
	}

	public void setUuid(final String uuid) {

		this.uuid = uuid;
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

	public String getEmail() {

		return email;
	}

	public void setEmail(final String email) {

		this.email = email;
	}

	public String getRollen() {

		return rollen;
	}

	public void setRollen(final String rollen) {

		this.rollen = rollen;
	}

	public boolean isAktiviert() {

		return aktiviert;
	}

	public void setAktiviert(final boolean aktiviert) {

		this.aktiviert = aktiviert;
	}

	public String getAenderungsdatum() {

		return aenderungsdatum;
	}

	public void setAenderungsdatum(final String dateModified) {

		this.aenderungsdatum = dateModified;
	}

}
