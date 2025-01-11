// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.auth_code_store;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * OneTimeTokenJwtData enthält ein einmalig gültiges Token, die ClientId und das JWT für den Server-Server-Tausch gegen
 * das JWT.
 */
public class OneTimeTokenJwtData {

	@JsonIgnore
	private String oneTimeToken;

	@JsonIgnore
	private String clientId;

	@JsonIgnore
	private String jwt;

	public OneTimeTokenJwtData(final String oneTimeToken, final String clientId, final String jwt) {

		if (StringUtils.isBlank(oneTimeToken)) {

			throw new IllegalArgumentException("oneTimeToken darf nicht blank sein.");
		}

		if (StringUtils.isBlank(clientId)) {

			throw new IllegalArgumentException("clientId darf nicht blank sein.");
		}

		if (StringUtils.isBlank(jwt)) {

			throw new IllegalArgumentException("jwt darf nicht blank sein.");
		}

		this.oneTimeToken = oneTimeToken;
		this.clientId = clientId;
		this.jwt = jwt;
	}

	@Override
	public int hashCode() {

		return Objects.hash(oneTimeToken);
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
		OneTimeTokenJwtData other = (OneTimeTokenJwtData) obj;
		return Objects.equals(oneTimeToken, other.oneTimeToken);
	}

	public String oneTimeToken() {

		return oneTimeToken;
	}

	public String clientId() {

		return clientId;
	}

	public String jwt() {

		return jwt;
	}

}
