// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.crypto;

import org.apache.shiro.crypto.hash.Hash;

import de.egladil.web.authprovider.domain.Client;
import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.authprovider.error.ClientAuthException;

public interface AuthCryptoService {

	/**
	 * Das gegebene Passwort wird gesalzen und gehashed. Das Salz, der hash-Wert und alle Infos zum erneuten Prüfen mit
	 * checkPassword() stehen im zurückgegenen Hash und müssen persistiert werden. Den Hashwert holt man mit getBytes(),
	 * das Salz mit getSalt(). <br>
	 * <br>
	 * Das Salz und der Hash-Wert werden in der DB am besten mit der toBase64()-Methode von ByteSource gespeichert.
	 * Rückkonvertierung dann mit java.util.Base64.
	 *
	 * @param  password
	 *                  char[] der Aufrufer ist verantwortlich für das Löschen des char[]
	 * @return          Hash
	 */
	Hash hashPassword(char[] password);

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
