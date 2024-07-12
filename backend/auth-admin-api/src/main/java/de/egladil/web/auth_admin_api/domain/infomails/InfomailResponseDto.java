// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.infomails;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.web.auth_admin_api.domain.validation.StringLatinConstants;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * InfomailResponseDto
 */
@Schema(description = "eine Infomail")
public class InfomailResponseDto {

	@JsonProperty
	@Schema(description = "UUID des Infomailtextes", example = "5eaaf0ed-4949-4ccd-a9c3-2db9af3559c2")
	@Pattern(regexp = "^[abcdef\\d\\-]*$")
	@Size(max = 36)
	private String uuid;

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
	@Schema(description = "UUIDs der Versandaufträge mit diesem oder einem angepassten Mailtext")
	private List<String> uuidsMailversandauftraege = new ArrayList<>();

	public String getUuid() {

		return uuid;
	}

	public void setUuid(final String uuid) {

		this.uuid = uuid;
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

	public List<String> getUuidsMailversandauftraege() {

		return uuidsMailversandauftraege;
	}

	public void setUuidsMailversandauftraege(final List<String> uuidsMailversandauftraege) {

		this.uuidsMailversandauftraege = uuidsMailversandauftraege;
	}
}
