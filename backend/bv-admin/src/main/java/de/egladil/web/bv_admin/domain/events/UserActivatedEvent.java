// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.events;

/**
 * UserActivatedEvent
 */
public class UserActivatedEvent extends AbstractAuthAdminEvent {

	public UserActivatedEvent(final AuthAdminEventPayload eventPayload) {

		super(eventPayload);

	}

	@Override
	public boolean writeToEventStore() {

		return true;
	}

	@Override
	public boolean propagateToListeners() {

		return false;
	}

	@Override
	public EventType eventType() {

		return EventType.USER_ACTIVATED;
	}

}
