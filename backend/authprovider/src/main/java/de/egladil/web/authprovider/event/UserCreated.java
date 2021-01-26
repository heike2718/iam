// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.event;

/**
 * UserCreated
 */
public class UserCreated extends AbstractAuthproviderEvent {

	UserCreated() {

		super();
	}

	public UserCreated(final ResourceOwnerEventPayload eventPayload) {

		this.setResourceOwner(eventPayload);

	}

	@Override
	public boolean propagateToListeners() {

		return true;
	}

	@Override
	public boolean writeToEventStore() {

		return true;
	}

	@Override
	public AuthproviderEventType eventType() {

		return AuthproviderEventType.USER_CREATED;
	}
}
