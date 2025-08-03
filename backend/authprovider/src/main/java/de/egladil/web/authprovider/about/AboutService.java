// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.about;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * AboutService
 */
@ApplicationScoped
public class AboutService {

	@ConfigProperty(name = "quarkus.application.version")
	String version;

	public AboutDto getAboutDto() {

		return new AboutDto().withVersion(version);
	}

}
