//=====================================================
// Project: authprovider
// (c) Heike Winkelvoß
//=====================================================
package de.egladil.web.authprovider.payload;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.web.auth_validations.annotations.UuidString;
import jakarta.validation.constraints.Size;

/**
 *
 */
@Schema(name = "BannedEmailsResponseDto", description = "Ergebnis der Abfrage von für Mailversand gebanneten users.")
public class BannedEmailsResponseDto {

	@JsonProperty
	private List<String> bannedEmails;

	@JsonProperty
	@UuidString
	@Size(max = 36)
	private String nonce;

	public List<String> getBannedUUIDs() {
		return bannedEmails;
	}

	public void setBannedUUIDs(List<String> bannedUUIDs) {
		this.bannedEmails = bannedUUIDs;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
}
