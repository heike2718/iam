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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BenutzerTrefferlisteItem
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

	@JsonProperty
	private CryptoAlgorithm cryptoAlgorithm;

}
