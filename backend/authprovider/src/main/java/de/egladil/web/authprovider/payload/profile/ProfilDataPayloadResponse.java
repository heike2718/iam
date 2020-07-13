// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.payload.profile;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ProfilDataPayloadResponse
 */
public class ProfilDataPayloadResponse {

	@JsonProperty
	private final String nonce;

	@JsonProperty
	private final ProfileDataPayload profilData;

	/**
	 * @param nonce
	 * @param profilData
	 */
	public ProfilDataPayloadResponse(final String nonce, final ProfileDataPayload profilData) {

		this.nonce = nonce;
		this.profilData = profilData;
	}

	public String getNonce() {

		return nonce;
	}

	public ProfileDataPayload getProfilData() {

		return profilData;
	}

}
