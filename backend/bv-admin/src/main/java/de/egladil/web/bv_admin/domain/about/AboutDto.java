// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.about;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * AboutDto
 */
public class AboutDto {

	@JsonProperty
	private final String name = "bv-admin";

	@JsonProperty
	private String env;

	@JsonProperty
	private String stage;

	@JsonProperty
	private String version;

	public AboutDto withEnv(final String env) {

		this.env = env;
		return this;
	}

	public AboutDto withVersion(final String version) {

		this.version = version;
		return this;
	}

	public AboutDto withStage(final String stage) {

		this.stage = stage;
		return this;
	}

}
