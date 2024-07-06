// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.events;

/**
 * UserDeletedEvent
 */
public class UserDeletedEvent extends AbstractAuthAdminEvent {

	public UserDeletedEvent(final AuthAdminEventPayload eventPayload) {

		super(eventPayload);

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
	public EventType eventType() {

		return EventType.USER_DELETED;
	}

}
