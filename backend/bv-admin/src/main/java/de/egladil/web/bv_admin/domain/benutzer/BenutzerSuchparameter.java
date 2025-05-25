// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.benutzer;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.web.bv_admin.domain.SortDirection;
import de.egladil.web.bv_admin.domain.validation.StringLatinConstants;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BenutzerSuchparameter
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "BenutzerSuchparameter. Gesucht wird mit und-Verknüpfung und like")
public class BenutzerSuchparameter {

	@JsonProperty
	@Schema(description = "Teil der UUID eines Users", example = "732b2ed8")
	@Pattern(regexp = "^[abcdef\\d\\-]*$")
	@Size(max = 36)
	private String uuid;

	@JsonProperty
	@Schema(description = "Teil des Vornamens eines Users", example = "Rainer")
	@Pattern(regexp = StringLatinConstants.WHITELIST_REGEXP)
	@Size(max = 100)
	private String vorname;

	@JsonProperty
	@Schema(description = "Teil des Nachnamens eines Users", example = "Honig")
	@Pattern(regexp = StringLatinConstants.WHITELIST_REGEXP)
	@Size(max = 100)
	private String nachname;

	@JsonProperty
	@Schema(description = "Teil der email eines Users", example = "egladil")
	@Pattern(regexp = "^[a-zA-Z0-9\\.!#$%&'*+/=\\?\\^_`{|}~\\-@]*$")
	@Size(max = 255)
	private String email;

	@JsonProperty
	@Schema(description = "Teil der Rolle eines Users.", example = "STANDARD")
	@Pattern(regexp = "^[A-Za-z_,]*$")
	@Size(max = 150)
	private String rolle;

	@JsonProperty
	@Schema(description = "Teil des Datums, an dem die Daten das letzte Mal geändert wurden - meist letztes Login Format '2019-09-14 18:40:06'", example = "2021")
	@Pattern(regexp = "^[\\d\\-.:]*$")
	@Size(max = 19)
	private String aenderungsdatum;

	@JsonProperty
	@Schema(description = "Name des Feldes, nach dem sortiert werden soll. Der Name muss sich auf die enum UsersSortColumn abbilden lassen", example = "vorname")
	@Pattern(regexp = "^[A-Za-zäöüßÄÖÜ\\-_ ]*$")
	@Size(max = 100)
	private String sortByLabelname;

	@JsonProperty
	@Schema(description = "Pagination: welche Seite wird abgefragt", defaultValue = "0", example = "2")
	private int pageIndex = 0;

	@JsonProperty
	@Schema(description = "Pagination: wievile Treffer werden abgefragt. Im Frontend haben wir 100, 150 und 200. Default ist trotzdem 25.", defaultValue = "25", example = "25")
	private int pageSize = 25;

	@JsonProperty
	@Schema(description = "Sortierreihenfolge (asc oder desc)", example = "asc")
	private SortDirection sortDirection;
}
