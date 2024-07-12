// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.mailversand;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.web.auth_admin_api.domain.Jobstatus;

/**
 * MailversandauftragOverview
 */
public class MailversandauftragOverview {

	@JsonProperty
	@Schema(description = "die technische ID", example = "78573dc4-06d7-43f1-9b85-ae79f36c92b7")
	private String uuid;

	@JsonProperty
	@Schema(description = "Mailbetreff", example = "Aktivierung Ihres Benutzerkontos erforderlich")
	private String betreff;

	@JsonProperty
	@Schema(description = "Status des Auftrags", example = "COMPLETED")
	private Jobstatus status;

	@JsonProperty
	@Schema(
		description = "wie üblich formatiertes Datum mit Uhrzeit, wann der Versand begonnen hat", example = "09.07.2024 19:01:43")
	private String versandBegonnenAm;

	@JsonProperty
	@Schema(
		description = "wie üblich formatiertes Datum mit Uhrzeit, wann der Versand beendet wurde", example = "09.07.2024 23:56:01")
	private String versandBeendetAm;

	@JsonProperty
	@Schema(description = "Anzahl aller Empfänger")
	private int anzahlEmpfaenger;

	@JsonProperty
	@Schema(description = "Anzahl der Gruppen, also der Sammelmails")
	private int anzahlGruppen;

	@JsonProperty
	@Schema(description = "Anzahl der Benutzer, an die die Mail versendet wurde")
	private int anzahlVersendet;

}
