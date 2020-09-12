// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_server.event;

import java.time.LocalDateTime;

/**
 * ProfilEvent
 */
public interface ProfilEvent {

	String TYPE_USER_LOGGED_IN = "UserLoggedIn";

	String TYPE_USER_LOGGED_OUT = "UserLoggedOut";

	String TYPE_USER_CHANGED = "VeranstalterChanged";

	String TYPE_USER_DELETED = "UserDeleted";

	/**
	 * @return LocalDateTime
	 */
	LocalDateTime occuredOn();

	/**
	 * @return String
	 */
	String typeName();

	/**
	 * @return boolean
	 */
	boolean propagateToListeners();

}
