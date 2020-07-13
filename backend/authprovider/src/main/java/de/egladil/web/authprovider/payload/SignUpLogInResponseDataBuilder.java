// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.payload;

import de.egladil.web.authprovider.auth_code_store.OAuthFlowType;

/**
 * SignUpLogInResponseDataBuilder
 */
public class SignUpLogInResponseDataBuilder {

	private SignUpLogInResponseData result = new SignUpLogInResponseData();

	/**
	 * Erzeugt eine Instanz von SignUpLogInResponseDataBuilder
	 */
	private SignUpLogInResponseDataBuilder() {

	}

	public static SignUpLogInResponseDataBuilder instance() {

		return new SignUpLogInResponseDataBuilder();
	}

	public SignUpLogInResponseDataBuilder withNonce(final String nonce) {

		this.result.setNonce(nonce);
		return this;
	}

	public SignUpLogInResponseDataBuilder withState(final String state) {

		this.result.setState(state);
		return this;
	}

	public SignUpLogInResponseDataBuilder withIdToken(final String idToken) {

		this.result.setIdToken(idToken);
		return this;
	}

	public SignUpLogInResponseDataBuilder withOauthFlowType(final OAuthFlowType oauthFlowType) {

		this.result.setTokenExchangeType(oauthFlowType);
		return this;
	}

	public SignUpLogInResponseData build() {

		return result;
	}

}
