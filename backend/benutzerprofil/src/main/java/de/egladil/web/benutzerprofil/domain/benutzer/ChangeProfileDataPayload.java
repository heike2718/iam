// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.benutzerprofil.domain.benutzer;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.web.auth_validations.dto.OAuthClientCredentials;

/**
 * ChangeProfileDataPayload
 */
public class ChangeProfileDataPayload {

	@JsonProperty
	private OAuthClientCredentials clientCredentials;

	/**
	 * Wiird auf das JSON gemapped, das authprovider erwartet
	 */
	@JsonProperty(value = "profileData")
	private BenutzerDto benutzerDto;

	@JsonProperty
	private String uuid;

	public static ChangeProfileDataPayload create(final OAuthClientCredentials clientCredentials, final BenutzerDto benutzerDto, final String uuid) {

		ChangeProfileDataPayload result = new ChangeProfileDataPayload();
		result.clientCredentials = clientCredentials;
		result.benutzerDto = benutzerDto;
		result.uuid = uuid;
		return result;
	}
}
