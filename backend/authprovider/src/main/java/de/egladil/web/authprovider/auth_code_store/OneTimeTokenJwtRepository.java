// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.auth_code_store;

import java.util.Optional;

/**
 * OneTimeTokenJwtRepository
 */
public interface OneTimeTokenJwtRepository {

	/**
	 * Fügt einen Eintrag hinzu.
	 *
	 * @param data
	 */
	void addToken(OneTimeTokenJwtData data);

	/**
	 * Holt das Token ab und entfernt es aus dem Store.
	 *
	 * @param oneTimeToken
	 * @return Optional
	 */
	Optional<OneTimeTokenJwtData> getAndRemoveWithOneTimeToken(String oneTimeToken);

}
