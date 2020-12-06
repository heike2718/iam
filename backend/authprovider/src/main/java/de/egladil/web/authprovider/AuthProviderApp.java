// =====================================================
// Projekt: authenticationprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * AuthProviderApp
 */
@ApplicationPath("/authprovider")
public class AuthProviderApp extends Application {

	public static final String CLIENT_COOKIE_PREFIX = "AUTH";

	public static final List<String> DEV_STAGES = Arrays.asList(new String[] { "dev", "a300" });
}
