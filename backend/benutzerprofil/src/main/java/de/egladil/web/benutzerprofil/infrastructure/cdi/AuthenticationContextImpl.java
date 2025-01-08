// =====================================================
// Project: benutzerprofil
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.benutzerprofil.infrastructure.cdi;

import de.egladil.web.benutzerprofil.domain.auth.session.AuthenticatedUser;
import de.egladil.web.benutzerprofil.domain.auth.session.SessionUtils;
import jakarta.enterprise.context.RequestScoped;

/**
 * AuthenticationContextImpl
 */
@RequestScoped
public class AuthenticationContextImpl implements AuthenticationContext {

	private AuthenticatedUser user;

	@Override
	public AuthenticatedUser getUser() {

		return this.user;
	}

	public void setUser(final AuthenticatedUser user) {

		this.user = user;
	}

	@Override
	public boolean isUserInRole(final String role) {

		return !SessionUtils.ANONYME_UUID.equals(user.getName());
	}

}
