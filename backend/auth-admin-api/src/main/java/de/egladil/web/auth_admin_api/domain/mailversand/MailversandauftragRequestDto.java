// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.mailversand;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * MailversandauftragRequestDto
 */
@Schema(description = "Auftrag für einen Sammelmailversand zum Anlegen / Ändern")
public class MailversandauftragRequestDto {

	@Schema(description = "ID des gespeicherten Mailtexts und Betreffs", example = "663d1c4e-46b7-4b41-a3cc-c753b8f7148c")
	private String idInfomailtext;

	@Schema(description = "die technischen IDs der ausgewählten Benutzer, an die die Mails versendet werden sollen")
	private List<Long> benutzerIds;

	public String getIdInfomailtext() {

		return idInfomailtext;
	}

	public void setIdInfomailtext(final String idInfomailtext) {

		this.idInfomailtext = idInfomailtext;
	}

	public List<Long> getBenutzerIds() {

		return benutzerIds;
	}

	public void setBenutzerIds(final List<Long> benutzerIds) {

		this.benutzerIds = benutzerIds;
	}

}
