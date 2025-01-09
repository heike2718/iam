// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.benutzer;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UpdateBenutzerResponseDto
 */
@Schema(description = "Ergebnis nach dem Ändern des Benutzers. Attribut benutzer wird null sein, wenn statusCode != 200")
public class UpdateBenutzerResponseDto {

	@JsonProperty
	private String uuid;

	@JsonProperty
	private BenutzerTrefferlisteItem benuzer;

	public BenutzerTrefferlisteItem getBenuzer() {

		return benuzer;
	}

	public void setBenuzer(final BenutzerTrefferlisteItem benuzer) {

		this.benuzer = benuzer;
	}

	public String getUuid() {

		return uuid;
	}

	public void setUuid(final String uuid) {

		this.uuid = uuid;
	}

}
