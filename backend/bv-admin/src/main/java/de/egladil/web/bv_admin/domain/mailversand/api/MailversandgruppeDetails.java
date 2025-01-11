// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.mailversand.api;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.web.bv_admin.domain.Jobstatus;
import de.egladil.web.bv_admin.domain.benutzer.BenutzerTrefferlisteItem;

/**
 * MailversandgruppeDetails
 */
@Schema(description = "Deteils einer Mailversandgruppe")
public class MailversandgruppeDetails {

	@JsonProperty
	@Schema(description = "die technische ID", example = "78573dc4-06d7-43f1-9b85-ae79f36c92b7")
	private String uuid;

	@JsonProperty
	@Schema(description = "ID des gespeicherten Mailtexts und Betreffs", example = "663d1c4e-46b7-4b41-a3cc-c753b8f7148c")
	private String idInfomailtext;

	@JsonProperty
	@Schema(description = "fortlaufende Nummer zur Sortierung")
	private int sortnr;

	@JsonProperty
	@Schema(description = "der Status des Versands dieser Mail", example = "WAITING")
	private Jobstatus status;

	@JsonProperty
	@Schema(description = "Datum der letzten Änderung")
	private String aenderungsdatum;

	@JsonProperty
	@Schema(description = "Benutzer, an die die Mail versendet wurde. Wenn sie inzwischen gelöscht sind, wird ein Platzhalter zurückgegeben")
	private List<BenutzerTrefferlisteItem> benutzer;

	public String getUuid() {

		return uuid;
	}

	public void setUuid(final String uuid) {

		this.uuid = uuid;
	}

	public String getIdInfomailtext() {

		return idInfomailtext;
	}

	public void setIdInfomailtext(final String idInfomailtext) {

		this.idInfomailtext = idInfomailtext;
	}

	public int getSortnr() {

		return sortnr;
	}

	public void setSortnr(final int sortnr) {

		this.sortnr = sortnr;
	}

	public Jobstatus getStatus() {

		return status;
	}

	public void setStatus(final Jobstatus status) {

		this.status = status;
	}

	public String getAenderungsdatum() {

		return aenderungsdatum;
	}

	public void setAenderungsdatum(final String aenderungsdatum) {

		this.aenderungsdatum = aenderungsdatum;
	}

	public List<BenutzerTrefferlisteItem> getBenutzer() {

		return benutzer;
	}

	public void setBenutzer(final List<BenutzerTrefferlisteItem> benutzer) {

		this.benutzer = benutzer;
	}

}
