//=====================================================
// Projekt: authenticationprovider
// (c) Heike Winkelvoß
//=====================================================

package de.egladil.web.authprovider;

/**
 * CommonTestUtils
 */
public final class AuthTestUtils {

	/**
	 * Erzeugt eine Instanz von CommonTestUtils
	 */
	private AuthTestUtils() {
	}

	/**
	 * Gibt dem Pfad zum Dev-Config-Verzeichnis zurück.<br>
	 * <br>
	 * <b>Achtung: </b>Nur in Tests verwenden!!!!
	 *
	 * @return
	 */
	public static String getDevConfigRoot() {
		final String osName = System.getProperty("os.name");

		if (osName.contains("Windows")) {
			return "C:/Users/Winkelv/.esapi/mkvapi/config";
		}
		return "/home/heike/git/konfigurationen/authprovider";
	}
}
