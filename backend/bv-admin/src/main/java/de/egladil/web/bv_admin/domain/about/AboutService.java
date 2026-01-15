// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.about;

import jakarta.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * AboutService
 */
@ApplicationScoped
public class AboutService {

    @ConfigProperty(name = "stage")
    String stage;

    @ConfigProperty(name = "quarkus.application.version")
    String version;

    public AboutDto getAboutDto() {

        return new AboutDto().withStage(stage).withVersion(version);
    }

}
