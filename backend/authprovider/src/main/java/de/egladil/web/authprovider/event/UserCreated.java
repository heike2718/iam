// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.event;

import de.egladil.web.authprovider.domain.ResourceOwner;

/**
 * UserCreated
 */
public class UserCreated extends AbstractAuthproviderEvent {

	UserCreated() {

		super();
	}

	public UserCreated(final ResourceOwner resourceOwner) {

		super(resourceOwner);
	}

	@Override
	public boolean propagateToListeners() {

		return true;
	}

	@Override
	public boolean writeToEventStore() {

		return false;
	}

	@Override
	public AuthproviderEventType eventType() {

		return AuthproviderEventType.USER_CREATED;
	}
}
