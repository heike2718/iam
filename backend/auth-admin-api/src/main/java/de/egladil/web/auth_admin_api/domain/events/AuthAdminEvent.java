// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.events;

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
