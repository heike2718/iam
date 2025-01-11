// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.auth_code_store;

/**
 * OAuthFlowType<br>
 * <ul>
 * <li>IMPLICITE_FLOW heißt, das idToken im SignUpLoginResponse ist das JWT</li>
 * <li>AUTHORIZATION_TOKEN_GRANT heißt, das idToken im SignUpLoginResponse ist ein one time token, das im AuthCodeStore
 * für den Server-Server-Austausch gegen das JWT gehalten wird, so dass der Browser das JWT niemals sieht.</li>
 * </ul>
 */
public enum OAuthFlowType {

	IMPLICITE_FLOW,
	AUTHORIZATION_TOKEN_GRANT;

}
