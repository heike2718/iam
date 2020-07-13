// =====================================================
// Project: commons
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.payload;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

import de.egladil.web.commons_validation.annotations.UuidList;
import de.egladil.web.commons_validation.annotations.UuidString;

/**
 * RefreshAccessTokenPayload
 */
public class RefreshAccessTokenPayload {

	@NotNull
	@UuidList
	private List<String> clientAccessToken;

	@NotNull
	@UuidString
	private String userRefreshToken;

	private boolean force;

	public String getUserRefreshToken() {

		return userRefreshToken;
	}

	public void setUserRefreshToken(final String userRefreshToken) {

		this.userRefreshToken = userRefreshToken;
	}

	@Override
	public String toString() {

		final StringBuffer sb = new StringBuffer();
		clientAccessToken.forEach(at -> sb.append(StringUtils.abbreviate(at, 11) + " "));

		return "RefreshAccessTokenPayload [clientAccessToken=" + sb.toString() + ", userRefreshToken="
			+ StringUtils.abbreviate(userRefreshToken, 11)
			+ ", force=" + force + "]";
	}

	public List<String> getClientAccessToken() {

		return clientAccessToken;
	}

	public void setClientAccessToken(final List<String> clientAccessToken) {

		this.clientAccessToken = clientAccessToken;
	}

	public boolean isForce() {

		return force;
	}

	public void setForce(final boolean force) {

		this.force = force;
	}

}
