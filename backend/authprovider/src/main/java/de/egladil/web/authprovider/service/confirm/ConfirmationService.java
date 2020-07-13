//=====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
//=====================================================

package de.egladil.web.authprovider.service.confirm;

/**
 * ConfirmationService
 */
public interface ConfirmationService {

	/**
	 * Prüft den confirmationCode und aktiviert ggf. das Konto.
	 *
	 * @param confirmationCode String
	 * @return ConfirmationStatus
	 */
	ConfirmationStatus confirmCode(String confirmationCode);

}
