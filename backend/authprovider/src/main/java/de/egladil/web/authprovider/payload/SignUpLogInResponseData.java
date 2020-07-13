// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.web.authprovider.auth_code_store.OAuthFlowType;

/**
 * SignUpLogInResponseData enthalten die Daten, die nach einem SignUp oder einem LogIn zurückgegebenn werden.
 * <ul>
 * <li><strong>state: </strong>Kontext zur Aktion, wird durch den Client erzeugt und unverändert wieder
 * zurückgegeben.</li>
 * <li><strong>nonce: </strong> Kontext, der vom Client gesetzt wird</li>
 * <li><strong>idToken: </strong> das JWT</li>
 * </ul>
 */
public class SignUpLogInResponseData {

	@JsonProperty
	private String state;

	@JsonProperty
	private String idToken;

	@JsonProperty
	private String nonce;

	@JsonProperty
	private OAuthFlowType oauthFlowType = OAuthFlowType.IMPLICITE_FLOW;

	/**
	 * Erzeugt eine Instanz von SignUpLogInResponseData
	 */
	SignUpLogInResponseData() {

	}

	public String getIdToken() {

		return idToken;
	}

	void setIdToken(final String idToken) {

		this.idToken = idToken;
	}

	public String getState() {

		return state;
	}

	public void setState(final String state) {

		this.state = state;
	}

	public String getNonce() {

		return nonce;
	}

	void setNonce(final String nonce) {

		this.nonce = nonce;
	}

	void setTokenExchangeType(final OAuthFlowType oauthFlowType) {

		this.oauthFlowType = oauthFlowType;
	}

	public OAuthFlowType getOauthFlowType() {

		return oauthFlowType;
	}
}
