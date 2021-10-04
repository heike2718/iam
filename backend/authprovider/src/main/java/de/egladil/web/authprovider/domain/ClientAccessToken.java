// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.domain;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

/**
 * ClientAccessToken
 */
public class ClientAccessToken implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	private String accessToken;

	@NotNull
	private Date accessTokenExpiresAt;

	@NotNull
	private String clientId;

	public String getAccessToken() {

		return accessToken;
	}

	public void setAccessToken(final String accessToken) {

		this.accessToken = accessToken;
	}

	public Date getAccessTokenExpiresAt() {

		return accessTokenExpiresAt;
	}

	public void setAccessTokenExpiresAt(final Date accessTokenExpiresAt) {

		this.accessTokenExpiresAt = accessTokenExpiresAt;
	}

	@Override
	public String toString() {

		return "ClientAccessToken [accessToken=" + StringUtils.abbreviate(accessToken, 11) + ", clientId="
			+ StringUtils.abbreviate(clientId, 11) + ", expiresAt=" + accessTokenExpiresAt.getTime() + "]";
	}

	public String getClientId() {

		return clientId;
	}

	public void setClientId(final String clientId) {

		this.clientId = clientId;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessToken == null) ? 0 : accessToken.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {

			return true;
		}

		if (obj == null) {

			return false;
		}

		if (getClass() != obj.getClass()) {

			return false;
		}
		ClientAccessToken other = (ClientAccessToken) obj;

		if (accessToken == null) {

			if (other.accessToken != null) {

				return false;
			}
		} else if (!accessToken.equals(other.accessToken)) {

			return false;
		}
		return true;
	}

}
