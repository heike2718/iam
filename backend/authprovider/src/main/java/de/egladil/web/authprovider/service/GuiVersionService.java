// =====================================================
// Project: mk-gateway
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.service;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * GuiVersionService
 */
@ApplicationScoped
public class GuiVersionService {

	@ConfigProperty(name = "quarkus.application.version", defaultValue = "")
	String version;

	/**
	 * Gibt die GUI-Version zurück. Das sind immer die 3 ersten Versionszahlen Major.Middle.Minor.
	 *
	 * @return String
	 */
	public String getExcpectedGuiVersion() {

		String[] tokens = version.split("\\.");
		return tokens[0] + "." + tokens[1] + "." + tokens[2];
	}
}
