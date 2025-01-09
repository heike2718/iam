// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.mailversand.api;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.web.bv_admin.domain.Jobstatus;

/**
 * Mailversandgruppe
 */
@Schema(description = "Gruppe, an die eine Sammelmail gesendet wurde")
public class Mailversandgruppe {

	@JsonProperty
	@Schema(description = "die technische ID", example = "78573dc4-06d7-43f1-9b85-ae79f36c92b7")
	private String uuid;

	@JsonProperty
	@Schema(description = "ID des Meilversandauftrags", example = "663d1c4e-46b7-4b41-a3cc-c753b8f7148c")
	private String idMailversandauftrag;

	@JsonProperty
	@Schema(description = "fortlaufende Nummer zur Sortierung")
	private int sortnr;

	@JsonProperty
	@Schema(description = "der Status des Versands dieser Mail", example = "WAITING")
	private Jobstatus status;

	@JsonProperty
	@Schema(description = "die UUIDs der Benutzer")
	private List<String> empfaengerUUIDs = new ArrayList<>();

	@JsonProperty
	@Schema(description = "Datum der letzten Änderung")
	private String aenderungsdatum;

	@JsonIgnore // wollen keine emails im Frontend haben, die es eventuell gar nicht mehr gibt.
	private List<String> empfaengerEmails = new ArrayList<>();

	public String getUuid() {

		return uuid;
	}

	public void setUuid(final String uuid) {

		this.uuid = uuid;
	}

	public Jobstatus getStatus() {

		return status;
	}

	public void setStatus(final Jobstatus status) {

		this.status = status;
	}

	public int getSortnr() {

		return sortnr;
	}

	public void setSortnr(final int sortnr) {

		this.sortnr = sortnr;
	}

	public List<String> getEmpfaengerUUIDs() {

		return empfaengerUUIDs;
	}

	public void setEmpfaengerUUIDs(final List<String> empfaengerUUIDs) {

		this.empfaengerUUIDs = empfaengerUUIDs;
	}

	public List<String> getEmpfaengerEmails() {

		return empfaengerEmails;
	}

	public void setEmpfaengerEmails(final List<String> empfaengerEmails) {

		this.empfaengerEmails = empfaengerEmails;
	}

	public String getIdMailversandauftrag() {

		return idMailversandauftrag;
	}

	public void setIdMailversandauftrag(final String idMailversandauftrag) {

		this.idMailversandauftrag = idMailversandauftrag;
	}

	public String getAenderungsdatum() {

		return aenderungsdatum;
	}

	public void setAenderungsdatum(final String aenderungsdatum) {

		this.aenderungsdatum = aenderungsdatum;
	}

}
