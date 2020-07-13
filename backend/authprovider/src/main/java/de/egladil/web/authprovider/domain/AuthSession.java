// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.domain;

import java.io.Serializable;
import java.security.Principal;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * AuthSession
 */
public class AuthSession implements Principal, Serializable {

	private static final long serialVersionUID = 1L;

	private String sessionId;

	@JsonIgnore
	private String uuid;

	private String csrfToken;

	private long expiresAt;

	public static AuthSession create(final String sessionId, final String uuid) {

		AuthSession result = new AuthSession();

		result.sessionId = sessionId;
		result.uuid = uuid;

		return result;
	}

	@Override
	public String getName() {

		return this.uuid;
	}

	public void clearSessionId() {

		this.sessionId = null;
	}

	public String getCsrfToken() {

		return csrfToken;
	}

	public void setCsrfToken(final String csrfToken) {

		this.csrfToken = csrfToken;
	}

	public long getExpiresAt() {

		return expiresAt;
	}

	public void setExpiresAt(final long expiresAt) {

		this.expiresAt = expiresAt;
	}

	public String getSessionId() {

		return sessionId;
	}

	public String getUuid() {

		return uuid;
	}

}
