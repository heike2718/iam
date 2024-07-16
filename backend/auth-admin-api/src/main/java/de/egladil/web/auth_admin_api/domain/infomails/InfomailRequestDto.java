// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.infomails;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.web.auth_admin_api.domain.validation.StringLatinConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * InfomailRequestDto
 */
@Schema(description = "Betreff und Text für eine Infomail")
public class InfomailRequestDto {

	@JsonProperty
	@Pattern(regexp = StringLatinConstants.WHITELIST_REGEXP)
	@Size(max = 100)
	@NotBlank
	private String betreff;

	@JsonProperty
	@Pattern(regexp = StringLatinConstants.WHITELIST_REGEXP_PLUS_WHITESPACE)
	@Size(max = 65535)
	@NotBlank
	private String mailtext;

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
