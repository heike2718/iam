// =====================================================
// Project: benutzerprofil
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.benutzerprofil.domain.benutzer;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ChangeBenutzerdatenResponseDto
 */
public class ChangeBenutzerdatenResponseDto {

	@JsonProperty
	private BenutzerDto benutzer;

	@JsonProperty
	private boolean securityEvent;

	public BenutzerDto getBenutzer() {

		return benutzer;
	}

	public ChangeBenutzerdatenResponseDto withBenutzer(final BenutzerDto benutzer) {

		this.benutzer = benutzer;
		return this;
	}

	public boolean isSecurityEvent() {

		return securityEvent;
	}

	public ChangeBenutzerdatenResponseDto withSecurityEvent(final boolean securityEvent) {

		this.securityEvent = securityEvent;
		return this;
	}

}
