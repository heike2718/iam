// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.benutzerprofil.domain.passwort;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.web.auth_validations.dto.OAuthClientCredentials;

/**
 * ChangeProfilePasswordPayload
 */
public class ChangeProfilePasswordPayload {

	@JsonProperty
	private OAuthClientCredentials clientCredentials;

	@JsonProperty
	private PasswortPayload passwordPayload;

	@JsonProperty
	private String uuid;

	public static ChangeProfilePasswordPayload create(final OAuthClientCredentials clientCredentials,
		final PasswortPayload passwordPayload, final String uuid) {

		ChangeProfilePasswordPayload result = new ChangeProfilePasswordPayload();
		result.clientCredentials = clientCredentials;
		result.passwordPayload = passwordPayload;
		result.uuid = uuid;
		return result;
	}
}
