// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.mailversand;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.web.auth_admin_api.domain.Jobstatus;

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
	@Schema(description = "Jahr und Monat des Versands im Format YYYY-MM")
	private String versandJahrMonat;

	@JsonProperty
	@Schema(description = "Status des Auftrags", example = "IN_PROGRESS")
	private Jobstatus status;

	@JsonProperty
	@Schema(
		description = "wie üblich formatiertes Datum mit Uhrzeit, wann der Auftrag erfasst wurde", example = "09.07.2024 18:44:13")
	private String erfasstAm;

	@JsonProperty
	@Schema(
		description = "wie üblich formatiertes Datum mit Uhrzeit, wann der Versand begonnen hat", example = "09.07.2024 19:01:43")
	private String versandBegonnenAm;

	@JsonProperty
	@Schema(
		description = "wie üblich formatiertes Datum mit Uhrzeit, wann der Versand beendet wurde", example = "09.07.2024 23:56:01")
	private String versandBeendetAm;

	@JsonProperty
	@Schema(description = "Anzahl der Benutzer, an die die Infomail gesendet wir bzw. wurde", example = "4352")
	private int anzahlEmpfaenger;

	@JsonProperty
	@Schema(description = "Anzahl bereits versendeter Mails", example = "2524")
	private int anzahlVersendet;

	@JsonProperty
	@Schema(description = "true, wenn eine der Sammelmails nicht fehlerfrei versendet werden konnte, sonst false")
	private boolean versandMitFehlern;

	@JsonProperty
	@Schema(description = "Die Gruppen von Mailempfängern, an die die Mail jeweils als Sammelmail versendet wird / wurde")
	private List<Mailversandgruppe> mailversandgruppen;

}
