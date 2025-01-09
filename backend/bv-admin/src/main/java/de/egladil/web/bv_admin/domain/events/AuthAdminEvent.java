// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.events;

import java.time.LocalDateTime;

/**
 * AuthAdminEvent
 */
public interface AuthAdminEvent {

	boolean writeToEventStore();

	boolean propagateToListeners();

	EventType eventType();

	Object payload();

	String serializePayload();

	LocalDateTime occuredOn();

	void writeToConsoleQuietly();
}
