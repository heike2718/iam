// =====================================================
// Project: commons
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * OAuthAccessTokenPayload
 * <ul>
 * <li><strong>accessToken: </strong> autorisiert den Client gegenüber dem AuthProvider für eine begrenzte Zeit</li>
 * <li><strong>expiresAt: </strong> Ablauf der Gültigkeit des accessTokens. <strong>in Millisekunden seit
 * 1.1.1970</strong></li>
 * </ul>
 */
public class OAuthAccessTokenPayload {

	@JsonProperty
	private String accessToken;

	@JsonProperty
	private long expiresAt;

	@JsonProperty
	private String nonce;

	public String getAccessToken() {

		return accessToken;
	}

	public long getExpiresAt() {

		return expiresAt;
	}

	public void setAccessToken(final String accessToken) {

		this.accessToken = accessToken;
	}

	public void setExpiresAt(final long expiresAt) {

		this.expiresAt = expiresAt;
	}

	public String getNonce() {

		return nonce;
	}

	public void setNonce(final String nonce) {

		this.nonce = nonce;
	}
}
