// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.payload.profile;

import de.egladil.web.auth_validations.annotations.UuidString;
import de.egladil.web.auth_validations.dto.OAuthClientCredentials;
import jakarta.validation.constraints.NotNull;

/**
 * SelectProfilePayload
 */
public class SelectProfilePayload {

	@NotNull
	private OAuthClientCredentials clientCredentials;

	@NotNull
	@UuidString
	private String uuid;

	public OAuthClientCredentials getClientCredentials() {

		return clientCredentials;
	}

	public String getUuid() {

		return uuid;
	}

	public void clean() {

		if (clientCredentials != null) {

			clientCredentials.clean();
		}
	}

}
