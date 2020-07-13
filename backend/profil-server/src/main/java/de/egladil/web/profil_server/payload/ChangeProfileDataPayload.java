// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_server.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.web.commons_validation.payload.OAuthClientCredentials;

/**
 * ChangeProfileDataPayload
 */
public class ChangeProfileDataPayload {

	@JsonProperty
	private OAuthClientCredentials clientCredentials;

	@JsonProperty
	private ProfileDataPayload profileData;

	@JsonProperty
	private String uuid;

	public static ChangeProfileDataPayload create(final OAuthClientCredentials clientCredentials, final ProfileDataPayload profileData, final String uuid) {

		ChangeProfileDataPayload result = new ChangeProfileDataPayload();
		result.clientCredentials = clientCredentials;
		result.profileData = profileData;
		result.uuid = uuid;
		return result;
	}
}
