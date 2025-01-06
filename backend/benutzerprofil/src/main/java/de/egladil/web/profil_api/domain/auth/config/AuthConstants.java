// =====================================================
// Project: profil-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_api.domain.auth.config;

/**
 * AuthConstants
 */

public interface AuthConstants {

	final String CSRF_TOKEN_COOKIE_NAME = "XSRF-TOKEN";

	// Wichtig: der Name des SessionCookies muss mit JSESSIONID beginnen!!!
	// Außerdem müssen die Requests vom frontend das Attribut withCredentials: true haben (Angular - Intercepror!)
	final String SESSION_COOKIE_NAME = "JSESSIONID_PROFIL_API";

}
