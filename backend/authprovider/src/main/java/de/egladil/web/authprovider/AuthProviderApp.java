// =====================================================
// Projekt: authenticationprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * AuthProviderApp
 */
@ApplicationPath("/authprovider")
public class AuthProviderApp extends Application {

	public static final String CLIENT_COOKIE_PREFIX = "AUTH";

	public static final String STAGE_DEV = "dev";

	public static final String STAGE_PROD = "prod";

}
