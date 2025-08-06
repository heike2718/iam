// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.benutzerprofil.domain.auth.session;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Session
 */
public class Session {

	@JsonIgnore
	private String sessionId;

	@JsonProperty
	private long expiresAt;

	@JsonProperty
	private boolean sessionActive;

	@JsonIgnore
	private String csrfTokenHmac;

	@JsonIgnore
	private AuthenticatedUser user;

	public static Session createAnonymous(final String sessionId) {

		Session session = new Session();
		session.sessionId = sessionId;
		session.sessionActive = false;
		return session;

	}

	@Override
	public String toString() {

		return "Session [sessionId=" + sessionId + ", expiresAt=" + expiresAt + ", admin=" + user + "]";
	}

	@JsonIgnore
	public boolean isAnonym() {

		return user == null;
	}

	/**
	 * @return the expiresAt
	 */
	public long getExpiresAt() {

		return expiresAt;
	}

	/**
	 * @param expiresAt the expiresAt to set
	 */
	public void setExpiresAt(final long expiresAt) {

		this.expiresAt = expiresAt;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((sessionId == null) ? 0 : sessionId.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {

			return true;
		}

		if (!(obj instanceof Session)) {

			return false;
		}
		Session other = (Session) obj;

		if (sessionId == null) {

			if (other.sessionId != null) {

				return false;
			}
		} else if (!sessionId.equals(other.sessionId)) {

			return false;
		}
		return true;
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {

		return sessionId;
	}

	/**
	 * @return the admin
	 */
	public AuthenticatedUser getUser() {

		return user;
	}

	public Session withUser(final AuthenticatedUser user) {

		if (user == null) {

			throw new IllegalArgumentException("admin null");
		}

		this.user = user;
		return this;
	}

	public boolean isSessionActive() {
		return sessionActive;
	}

	public void setSessionActive(boolean sessionActive) {
		this.sessionActive = sessionActive;
	}

	public String getCsrfTokenHmac() {
		return csrfTokenHmac;
	}

	public void setCsrfTokenHmac(String csrfTokenHmac) {
		this.csrfTokenHmac = csrfTokenHmac;
	}
}
