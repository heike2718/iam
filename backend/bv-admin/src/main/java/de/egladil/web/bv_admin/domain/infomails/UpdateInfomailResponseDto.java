// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.infomails;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UpdateInfomailResponseDto
 */
@Schema(description = "Ergebnis nach dem Ändern des InfomailTexts. Attribut infomail wird null sein, wenn statusCode != 200")
public class UpdateInfomailResponseDto {

	@JsonProperty
	private String uuid;

	@JsonProperty
	InfomailResponseDto infomail;

	public String getUuid() {

		return uuid;
	}

	public UpdateInfomailResponseDto withUuid(final String uuid) {

		this.uuid = uuid;
		return this;
	}

	public InfomailResponseDto getInfomail() {

		return infomail;
	}

	public UpdateInfomailResponseDto withInfomail(final InfomailResponseDto infomail) {

		this.infomail = infomail;
		return this;
	}

}
