// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.event;

/**
 * UserChanged
 */
public class UserChanged extends AbstractAuthproviderEvent {

	UserChanged() {

		super();

	}

	public UserChanged(final ResourceOwnerEventPayload eventPayload) {

		this.setResourceOwner(eventPayload);

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

		return AuthproviderEventType.USER_CHANGED;
	}

}
