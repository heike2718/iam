// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.payload.profile;

import javax.validation.constraints.NotNull;

import de.egladil.web.commons_validation.annotations.UuidString;
import de.egladil.web.commons_validation.payload.OAuthClientCredentials;

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
