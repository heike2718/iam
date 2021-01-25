// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.event;

import de.egladil.web.authprovider.domain.ResourceOwner;

/**
 * LoginversuchInaktiverUser
 */
public class LoginversuchInaktiverUser extends AbstractAuthproviderEvent {

	LoginversuchInaktiverUser() {

		super();
	}

	public LoginversuchInaktiverUser(final ResourceOwner resourceOwner) {

		super(resourceOwner);
	}

	@Override
	public boolean writeToEventStore() {

		return true;
	}

	@Override
	public boolean propagateToListeners() {

		return true;
	}

	@Override
	public AuthproviderEventType eventType() {

		return AuthproviderEventType.LOGINVERSUCH_INAKTIVER_USER;
	}
}
