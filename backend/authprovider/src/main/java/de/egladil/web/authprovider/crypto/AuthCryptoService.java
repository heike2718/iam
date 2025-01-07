// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.crypto;

import de.egladil.web.authprovider.domain.Client;
import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.authprovider.error.ClientAuthException;

public interface AuthCryptoService {

	/**
	 * @param  password
	 *                  char[] der Aufrufer ist verantwortlich für das Löschen des char[]
	 * @return          String
	 */
	String hashPassword(char[] password);

	/**
	 * Prüft, ob das gegebene Passwort zum gegebenen ResourceOwner gehört.
	 *
	 * @param  password
	 * @param  resourceOwner
	 * @return               boolean, damit sich die Methode mocken lässt.
	 */
	boolean verifyPassword(char[] password, ResourceOwner resourceOwner);

	/**
	 * Verifiziert das clientSecret.
	 *
	 * @param  password
	 * @param  client
	 * @return                     boolean
	 * @throws ClientAuthException
	 */
	boolean verifyClientSecret(final char[] password, final Client client) throws ClientAuthException;

	/**
	 * Verwendet die pw- Einstellungen aus der crypto-Konfiguration, um einen Zufallsstring zu generieren. Basiert auf
	 * SecureRandom.
	 *
	 * @return String
	 */
	String generateTemporaryPasswort();

	/**
	 * Generiert eine ClientId.
	 *
	 * @return String
	 */
	String generateClientID();

}
