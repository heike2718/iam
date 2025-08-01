// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.payload;

import jakarta.validation.constraints.NotNull;

/**
 * LoginCredentials sind die Daten für die Authentisierung eines ResourceOwners erforderlich sind. Das sind die Daten
 * des ResourceOwners:
 * <ul>
 * <li>Loginname/Email (der Kontext wird über den LoginCredentialsType mitgeteilt)</li>
 * <li>Passwort</li>
 * </ul>
 * sowie des Clients, von dem aus zur LoginResource redirectet wurde:
 * <ul>
 * <li>clientId</li>
 * <li>redirectUrl (Rücksprung)</li>
 * </ul>
 */
public class LoginCredentials {

	@NotNull
	private AuthorizationCredentials authorizationCredentials;

	// https://javaee.github.io/jsonb-spec/users-guide.html Beim login sind die clientCredentials-Attribute null :/
	@NotNull
	private ClientCredentials clientCredentials;

	public AuthorizationCredentials getAuthorizationCredentials() {

		return authorizationCredentials;
	}

	public void setAuthorizationCredentials(final AuthorizationCredentials authorizationCredentials) {

		this.authorizationCredentials = authorizationCredentials;
	}

	public ClientCredentials getClientCredentials() {

		return clientCredentials;
	}

	public void setClientCredentials(final ClientCredentials clientCredentials) {

		this.clientCredentials = clientCredentials;
	}
}
