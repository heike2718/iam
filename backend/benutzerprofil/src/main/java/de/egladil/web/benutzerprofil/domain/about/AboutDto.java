// =====================================================
// Project: benutzerprofil
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.benutzerprofil.domain.about;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * AboutDto
 */
public class AboutDto {

	@JsonProperty
	private final String name = "benutzerprofil";

	@JsonProperty
	private String version;

	public AboutDto withVersion(final String version) {

		this.version = version;
		return this;
	}

}
