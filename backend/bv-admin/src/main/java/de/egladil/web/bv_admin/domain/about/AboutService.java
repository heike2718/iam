// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.about;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * AboutService
 */
@ApplicationScoped
public class AboutService {

	@ConfigProperty(name = "env")
	String env;

	@ConfigProperty(name = "stage")
	String stage;

	@ConfigProperty(name = "quarkus.application.version")
	String version;

	public AboutDto getAboutDto() {

		return new AboutDto().withEnv(env).withStage(stage).withVersion(version);
	}

}
