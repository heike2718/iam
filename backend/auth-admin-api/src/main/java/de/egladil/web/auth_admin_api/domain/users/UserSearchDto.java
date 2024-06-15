// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.users;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.web.auth_admin_api.domain.SortDirection;
import de.egladil.web.auth_admin_api.domain.validation.StringLatinConstants;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * UserSearchDto
 */
@Schema(description = "Suchparameter. Gesucht wird mit und-Verknüpfung und like")
public class UserSearchDto {

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
	@Pattern(regexp = "^[A-Z_,]*$")
	@Size(max = 150)
	private String rollen;

	@JsonProperty
	@Schema(description = "Flag ,ob der user aktiviert ist", example = "false")
	private Boolean aktiviert;

	@JsonProperty
	@Schema(
		description = "Teil des Datums, an dem die Daten das letzte Mal geändert wurden - meist letztes Login Format '2019-09-14 18:40:06'",
		example = "2021")
	@Pattern(regexp = "^[\\d\\-.:]*$")
	@Size(max = 19)
	private String dateModified;

	@JsonProperty
	@Schema(
		description = "Name des Feldes, nach dem sortiert werden soll. Der Name muss sich auf die enum UserSortColumn abbilden lassen",
		example = "vorname")
	@Pattern(regexp = "^[A-Za-zäöüßÄÖÜ\\-_ ]*$")
	@Size(max = 100)
	private String sortByLabelname;

	@JsonProperty
	@Schema(
		description = "Pagination: welche Seite wird abgefragt",
		defaultValue = "1",
		example = "2")
	private int pageIndex = 1;

	@JsonProperty
	@Schema(
		description = "Pagination: wievile Treffer werden abgefragt",
		defaultValue = "25",
		example = "25")
	private int pageSize = 25;

	@JsonProperty
	@Schema(description = "Sortierreihenfolge (asc oder desc)", example = "asc")
	private SortDirection sortDirection;

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

	public String getDateModified() {

		return dateModified;
	}

	public void setDateModified(final String dateModified) {

		this.dateModified = dateModified;
	}

	public String getSortByLabelname() {

		return sortByLabelname;
	}

	public void setSortByLabelname(final String sortByLabelname) {

		this.sortByLabelname = sortByLabelname;
	}

	public int getPageIndex() {

		return pageIndex;
	}

	public void setPageIndex(final int pageIndex) {

		this.pageIndex = pageIndex;
	}

	public int getPageSize() {

		return pageSize;
	}

	public void setPageSize(final int pageSize) {

		this.pageSize = pageSize;
	}

	public SortDirection getSortDirection() {

		return sortDirection;
	}

	public void setSortDirection(final SortDirection sortDirection) {

		this.sortDirection = sortDirection;
	}

	public Boolean getAktiviert() {

		return aktiviert;
	}

	public void setAktiviert(final Boolean aktiviert) {

		this.aktiviert = aktiviert;
	}

}
