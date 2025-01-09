// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.mailversand.api;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * MailversandauftragDetailsResponseDto
 */
@Schema(description = "Kombiniert uuid mit dem MailversandauftragDetailsDto.")
public class MailversandauftragDetailsResponseDto {

	@JsonProperty
	private String uuid;

	@JsonProperty
	private MailversandauftragDetails versandauftrag;

	public String getUuid() {

		return uuid;
	}

	public void setUuid(final String uuid) {

		this.uuid = uuid;
	}

	public MailversandauftragDetails getVersandauftrag() {

		return versandauftrag;
	}

	public void setVersandauftrag(final MailversandauftragDetails versandauftrag) {

		this.versandauftrag = versandauftrag;
	}
}
