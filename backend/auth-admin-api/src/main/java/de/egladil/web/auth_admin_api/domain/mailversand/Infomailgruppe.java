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
 * Infomailgruppe
 */
@Schema(description = "Gruppe, an die eine Infosammelmail gesendet wurde")
public class Infomailgruppe {

	@JsonProperty
	@Schema(description = "die technische ID", example = "78573dc4-06d7-43f1-9b85-ae79f36c92b7")
	private String uuid;

	@JsonProperty
	@Schema(description = "der Status dieser Mail", example = "WAITING")
	private Jobstatus status;

	@JsonProperty
	@Schema(description = "fortlaufende Nummer zur Sortierung")
	private int sortnr;

	@JsonProperty
	@Schema(description = "die Empfängermailadressen")
	private List<String> empfaengerEmails;

}
