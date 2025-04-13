// =====================================================
// Project: benutzerprofil
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.benutzerprofil.domain.auth.config;

/**
 * AuthConstants
 */

public interface AuthConstants {

	final String CSRF_TOKEN_COOKIE_NAME = "XSRF-TOKEN";

	final String CSRF_TOKEN_HEADER_NAME = "X-XSRF-TOKEN";

	// Wichtig: der Name des SessionCookies muss mit JSESSIONID beginnen!!!
	// Außerdem müssen die Requests vom frontend das Attribut withCredentials: true haben (Angular - Intercepror!)
	final String SESSION_COOKIE_NAME = "JSESSIONID";

	/**
	 * Dient zur Unterscheidung im Browser: Cookie wird nur an diese Sub-URL gesendet.
	 */
	final String COOKIE_PATH = "/benutzerprofil";

}
