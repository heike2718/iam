//=====================================================
// Project: commons
// (c) Heike Winkelvoß
//=====================================================
package de.egladil.web.authprovider.payload;

/**
 * JWTPayload enthält ein JWT.
 */
public class JWTPayload {

	private String jwt;

	private long expiresAtSeconds;

	/**
	 *
	 */
	public JWTPayload() {
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(final String jwt) {
		this.jwt = jwt;
	}

	public long getExpiresAtSeconds() {
		return expiresAtSeconds;
	}

	public void setExpiresAtSeconds(final long expiresAtSeconds) {
		this.expiresAtSeconds = expiresAtSeconds;
	}

	@Override
	public String toString() {
		return "JWTPayload [jwt=" + jwt.substring(0, 20) + "..., expiresAtSeconds=" + expiresAtSeconds + "]";
	}
}
