// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.payload.profile;

import de.egladil.web.auth_validations.annotations.UuidString;
import de.egladil.web.auth_validations.dto.OAuthClientCredentials;
import jakarta.validation.constraints.NotNull;

/**
 * ChangeProfilePasswordPayload
 */
public class ChangeProfilePasswordPayload {

	@NotNull
	private OAuthClientCredentials clientCredentials;

	@NotNull
	private ProfilePasswordPayload passwordPayload;

	@NotNull
	@UuidString
	private String uuid;

	public OAuthClientCredentials getClientCredentials() {

		return clientCredentials;
	}

	public ProfilePasswordPayload getPasswordPayload() {

		return passwordPayload;
	}

	public String getUuid() {

		return uuid;
	}

	public void clean() {

		if (clientCredentials != null) {

			clientCredentials.clean();
		}

		if (passwordPayload != null) {

			passwordPayload.clean();
		}
	}

}
