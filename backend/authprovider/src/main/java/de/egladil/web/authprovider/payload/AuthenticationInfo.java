// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * AuthenticationInfo enthält die AuthenticationInfo des authproviders. Neben Verwaltungsinfos enthält sie das JWT als
 * idToken.
 */
public class AuthenticationInfo {

	@JsonProperty
	private String accessToken;

	@JsonProperty
	private int expiresInSeconds;

	@JsonProperty
	private String tokenType;

	@JsonProperty
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
