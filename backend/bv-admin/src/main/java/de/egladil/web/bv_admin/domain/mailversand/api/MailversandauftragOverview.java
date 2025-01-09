// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.mailversand.api;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.web.bv_admin.domain.Jobstatus;

/**
 * MailversandauftragOverview
 */
public class MailversandauftragOverview {

	@JsonProperty
	@Schema(description = "die technische ID", example = "78573dc4-06d7-43f1-9b85-ae79f36c92b7")
	private String uuid;

	@JsonProperty
	@Schema(description = "ID des gespeicherten Mailtexts und Betreffs", example = "663d1c4e-46b7-4b41-a3cc-c753b8f7148c")
	private String idInfomailtext;

	@JsonProperty
	@Schema(description = "Mailbetreff", example = "Aktivierung Ihres Benutzerkontos erforderlich")
	private String betreff;

	@JsonProperty
	@Schema(description = "Erfasungsdatum", example = "16.07.2024 11:55:22")
	private String erfasstAm;

	@JsonProperty
	@Schema(description = "Status des Auftrags", example = "COMPLETED")
	private Jobstatus status;

	@JsonProperty
	@Schema(description = "Anzahl der Benutzer, an die die Infomail gesendet werden sollte", example = "4352")
	private long anzahlEmpfaenger;

	@JsonProperty
	@Schema(description = "Anzahl der Mailversandgruppen", example = "28")
	private long anzahlGruppen;

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

	public String getBetreff() {

		return betreff;
	}

	public void setBetreff(final String betreff) {

		this.betreff = betreff;
	}

	public Jobstatus getStatus() {

		return status;
	}

	public void setStatus(final Jobstatus status) {

		this.status = status;
	}

	public long getAnzahlGruppen() {

		return anzahlGruppen;
	}

	public void setAnzahlGruppen(final long anzahlGruppen) {

		this.anzahlGruppen = anzahlGruppen;
	}

	public long getAnzahlEmpfaenger() {

		return anzahlEmpfaenger;
	}

	public void setAnzahlEmpfaenger(final long anzahlEmpfaenger) {

		this.anzahlEmpfaenger = anzahlEmpfaenger;
	}

	public String getErfasstAm() {

		return erfasstAm;
	}

	public void setErfasstAm(final String erfasstAm) {

		this.erfasstAm = erfasstAm;
	}

}
