// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.mailversand.api;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.web.bv_admin.domain.Jobstatus;
import de.egladil.web.bv_admin.domain.validation.StringLatinConstants;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * MailversandauftragDetails
 */
@Schema(description = "Auftrag für einen Sammelmailversand zur Anzeige in der GUI")
public class MailversandauftragDetails {

	@JsonProperty
	@Schema(description = "die technische ID", example = "78573dc4-06d7-43f1-9b85-ae79f36c92b7")
	private String uuid;

	@JsonProperty
	@Schema(description = "ID des gespeicherten Mailtexts und Betreffs", example = "663d1c4e-46b7-4b41-a3cc-c753b8f7148c")
	private String idInfomailtext;

	@JsonProperty
	@Schema(description = "Betreff der Mail, die versendet weren wird")
	@Pattern(regexp = StringLatinConstants.WHITELIST_REGEXP)
	@Size(max = 100)
	private String betreff;

	@JsonProperty
	@Schema(description = "Text der Mail, die versendet werden wird")
	@Pattern(regexp = StringLatinConstants.WHITELIST_REGEXP)
	@Size(max = 65535)
	private String mailtext;

	@JsonProperty
	@Schema(description = "Jahr und Monat des Versands im Format YYYY-MM")
	private String versandJahrMonat;

	@JsonProperty
	@Schema(description = "Status des Auftrags", example = "IN_PROGRESS")
	private Jobstatus status;

	@JsonProperty
	@Schema(description = "wie üblich formatiertes Datum mit Uhrzeit, wann der Auftrag erfasst wurde", example = "09.07.2024 18:44:13")
	private String erfasstAm;

	@JsonProperty
	@Schema(description = "wie üblich formatiertes Datum mit Uhrzeit, wann der Versand begonnen hat", example = "09.07.2024 19:01:43")
	private String versandBegonnenAm;

	@JsonProperty
	@Schema(description = "wie üblich formatiertes Datum mit Uhrzeit, wann der Versand beendet wurde", example = "09.07.2024 23:56:01")
	private String versandBeendetAm;

	@JsonProperty
	@Schema(description = "Anzahl der Benutzer, an die die Infomail gesendet wir bzw. wurde", example = "4352")
	private long anzahlEmpfaenger;

	@JsonProperty
	@Schema(description = "Anzahl bereits versendeter Mails", example = "2524")
	private long anzahlVersendet;

	@JsonProperty
	@Schema(description = "true, wenn eine der Sammelmails nicht fehlerfrei versendet werden konnte, sonst false")
	private boolean versandMitFehlern;

	@JsonProperty
	@Schema(description = "Die Gruppen von Mailempfängern, an die die Mail jeweils als Sammelmail versendet wird / wurde")
	private List<Mailversandgruppe> mailversandgruppen;

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

	public String getVersandJahrMonat() {

		return versandJahrMonat;
	}

	public void setVersandJahrMonat(final String versandJahrMonat) {

		this.versandJahrMonat = versandJahrMonat;
	}

	public Jobstatus getStatus() {

		return status;
	}

	public void setStatus(final Jobstatus status) {

		this.status = status;
	}

	public String getErfasstAm() {

		return erfasstAm;
	}

	public void setErfasstAm(final String erfasstAm) {

		this.erfasstAm = erfasstAm;
	}

	public String getVersandBegonnenAm() {

		return versandBegonnenAm;
	}

	public void setVersandBegonnenAm(final String versandBegonnenAm) {

		this.versandBegonnenAm = versandBegonnenAm;
	}

	public String getVersandBeendetAm() {

		return versandBeendetAm;
	}

	public void setVersandBeendetAm(final String versandBeendetAm) {

		this.versandBeendetAm = versandBeendetAm;
	}

	public long getAnzahlEmpfaenger() {

		return anzahlEmpfaenger;
	}

	public void setAnzahlEmpfaenger(final long anzahlEmpfaenger) {

		this.anzahlEmpfaenger = anzahlEmpfaenger;
	}

	public long getAnzahlVersendet() {

		return anzahlVersendet;
	}

	public void setAnzahlVersendet(final long anzahlVersendet) {

		this.anzahlVersendet = anzahlVersendet;
	}

	public boolean isVersandMitFehlern() {

		return versandMitFehlern;
	}

	public void setVersandMitFehlern(final boolean versandMitFehlern) {

		this.versandMitFehlern = versandMitFehlern;
	}

	public List<Mailversandgruppe> getMailversandgruppen() {

		return mailversandgruppen;
	}

	public void setMailversandgruppen(final List<Mailversandgruppe> mailversandgruppen) {

		this.mailversandgruppen = mailversandgruppen;
	}

	public String getBetreff() {

		return betreff;
	}

	public void setBetreff(final String betreff) {

		this.betreff = betreff;
	}

	public String getMailtext() {

		return mailtext;
	}

	public void setMailtext(final String mailtext) {

		this.mailtext = mailtext;
	}

}
