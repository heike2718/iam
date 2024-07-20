// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.mailversand.api;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * MailversandgruppeDetailsResponseDto
 */
public class MailversandgruppeDetailsResponseDto {

	@JsonProperty
	@Schema(description = "die technische ID", example = "78573dc4-06d7-43f1-9b85-ae79f36c92b7")
	private String uuid;

	@JsonProperty
	@Schema(description = "falls noch vorhanden, die Details der Mailversandgruppe, sonst null")
	private MailversandgruppeDetails mailversandgruppe;

	public String getUuid() {

		return uuid;
	}

	public void setUuid(final String uuid) {

		this.uuid = uuid;
	}

	public MailversandgruppeDetails getMailversandgruppe() {

		return mailversandgruppe;
	}

	public void setMailversandgruppe(final MailversandgruppeDetails mailversndgruppe) {

		this.mailversandgruppe = mailversndgruppe;
	}

}
