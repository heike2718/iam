// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_validations.dto;

/**
 * ExchangeTokenResponse
 */
public class ExchangeTokenResponse {

	private String nonce;

	private String jwt;

	public static ExchangeTokenResponse create(final String jwt, final String nonce) {

		ExchangeTokenResponse result = new ExchangeTokenResponse();
		result.jwt = jwt;
		result.nonce = nonce;
		return result;
	}

	public String getNonce() {

		return nonce;
	}

	public void setNonce(final String nonce) {

		this.nonce = nonce;
	}

	public String getJwt() {

		return jwt;
	}

	public void setJwt(final String jwt) {

		this.jwt = jwt;
	}

}
