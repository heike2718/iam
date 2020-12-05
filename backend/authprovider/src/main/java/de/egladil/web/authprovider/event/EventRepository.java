// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.event;

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
