// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.about;

import jakarta.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

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
