// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.payload;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * AuthenticationInfo enthält die AuthenticationInfo des authproviders. Neben Verwaltungsinfos enthält sie das JWT als
 * idToken.
 */
public class AuthenticationInfo {

	@JsonbProperty
	private String accessToken;

	@JsonbProperty
	private int expiresInSeconds;

	@JsonbProperty
	private String tokenType;

	@JsonbProperty
	private String idToken;

	public String getAccessToken() {

		return accessToken;
	}

	public void setAccessToken(final String accessToken) {

		this.accessToken = accessToken;
	}

	public int getExpiresInSeconds() {

		return expiresInSeconds;
	}

	public void setExpiresInSeconds(final int expiresInSeconds) {

		this.expiresInSeconds = expiresInSeconds;
	}

	public String getTokenType() {

		return tokenType;
	}

	public void setTokenType(final String tokenType) {

		this.tokenType = tokenType;
	}

	public String getIdToken() {

		return idToken;
	}

	public void setIdToken(final String idToken) {

		this.idToken = idToken;
	}

}
