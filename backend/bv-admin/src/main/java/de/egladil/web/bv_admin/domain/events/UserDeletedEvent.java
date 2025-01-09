// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.events;

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
