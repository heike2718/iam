// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_server.event;

import de.egladil.web.profil_server.domain.StoredEvent;

/**
 * EventRepository
 */
public interface EventRepository {

	/**
	 * Speichert das event in der Datenbank.
	 *
	 * @param event
	 */
	void appendEvent(StoredEvent event);

}
