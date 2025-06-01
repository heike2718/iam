// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.events;

/**
 * MailaddressUnbannedEvent
 */
public class MailaddressUnbannedEvent extends AbstractAuthAdminEvent {

	public MailaddressUnbannedEvent(final AuthAdminEventPayload eventPayload) {

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

		return EventType.MAILADRESS_UNBANNED;
	}

}
