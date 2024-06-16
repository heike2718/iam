// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.auth.session;

import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.security.runtime.QuarkusPrincipal;

/**
 * AuthenticatedUser
 */
public class AuthenticatedUser extends QuarkusPrincipal {

	@JsonIgnore
	private static final String ALLOWED_ROLE = "AUTH_ADMIN";

	@JsonProperty
	private String idReference;

	@JsonProperty
	private String[] roles = new String[0];

	@JsonProperty
	private String fullName;

	public static AuthenticatedUser createAnonymousUser() {

		AuthenticatedUser result = new AuthenticatedUser("Anonym");
		result.roles = new String[0];
		result.fullName = "Gast";
		return result;
	}

	/**
	 * @param uuid
	 *             String die UUID des Benutzerkontos.
	 */
	public AuthenticatedUser(final String uuid) {

		super(uuid);

	}

	@Override
	public String toString() {

		return "AuthenticatedUser [uuid=" + StringUtils.abbreviate(getName(), 11) + ", roles=" + Arrays.toString(roles) + "]";
	}

	/**
	 * @return the idReference
	 */
	public String getIdReference() {

		return idReference;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {

		return fullName;
	}

	/**
	 * @return the uuid
	 */
	public String getUuid() {

		return getName();
	}

	/**
	 * @return the roles
	 */
	public String[] getRoles() {

		return roles;
	}

	public AuthenticatedUser withIdReference(final String idReference) {

		this.idReference = idReference;
		return this;
	}

	public AuthenticatedUser withRoles(final String[] roles) {

		this.roles = roles;
		return this;
	}

	public AuthenticatedUser withFullName(final String fullName) {

		this.fullName = fullName;
		return this;
	}

	/**
	 * Nur user mit der Rolle AUTH_ADMIN sind autorisiert.
	 *
	 * @return
	 */
	public boolean isAuthorized() {

		Optional<String> optAdminRole = Arrays.stream(roles).filter(r -> ALLOWED_ROLE.equals(r)).findFirst();

		return optAdminRole.isPresent();

	}
}
