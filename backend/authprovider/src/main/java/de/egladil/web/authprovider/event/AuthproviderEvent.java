// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.event;

import java.time.LocalDateTime;

/**
 * AuthproviderEvent
 */
public interface AuthproviderEvent {

	boolean writeToEventStore();

	boolean propagateToListeners();

	AuthproviderEventType eventType();

	Object payload();

	String serializePayload();

	LocalDateTime occuredOn();
}
