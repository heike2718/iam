// =====================================================
// Project: benutzerprofil
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.benutzerprofil.domain.auth.session;

import org.apache.commons.lang3.StringUtils;

import io.quarkus.security.runtime.QuarkusPrincipal;

/**
 * AuthenticatedUser
 */
public class AuthenticatedUser extends QuarkusPrincipal {

	private String idReference;

	private String fullName;

	private String uuid;

	public static AuthenticatedUser createAnonymousUser() {

		AuthenticatedUser result = new AuthenticatedUser(SessionUtils.ANONYME_UUID);
		result.fullName = "Gast";
		return result;
	}

	/**
	 * @param uuid String die UUID des Benutzerkontos.
	 */
	public AuthenticatedUser(final String uuid) {

		super(uuid);

	}

	@Override
	public String toString() {

		return "AuthenticatedUser [uuid=" + StringUtils.abbreviate(getName(), 11) + "]";
	}

	/**
	 * @return the idReference
	 */
	public String getIdReference() {

		return idReference;
	}

	public AuthenticatedUser withIdReference(final String idReference) {

		this.idReference = idReference;
		return this;
	}

	public String getFullName() {

		return fullName;
	}

	public AuthenticatedUser withFullName(final String fullName) {

		this.fullName = fullName;
		return this;
	}

	public String getUuid() {

		return uuid;
	}

	public AuthenticatedUser withUuid(final String uuid) {

		this.uuid = uuid;
		return this;
	}
}
