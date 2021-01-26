// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.event;

import javax.enterprise.event.Event;

/**
 * LoggableEventDelegate
 */
public class LoggableEventDelegate {

	/**
	 * Erzeugt ein AuthproviderEvent-Objekt und feuert, falls der EventBus zur Verfügung steht.
	 *
	 * @param eventPayload
	 *                     AuthproviderEvent
	 * @param event
	 *                     CDI-Event
	 */
	public void fireAuthProviderEvent(final AuthproviderEvent eventPayload, final Event<AuthproviderEvent> event) {

		if (event != null) {

			event.fire(eventPayload);
		} else {

			eventPayload.writeToConsoleQuietly();
		}
	}
}
