// =====================================================
// Projekt: commons-validation
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.validation;

import de.egladil.web.authprovider.validation.annotations.GeneratedPasswort;
import de.egladil.web.commons_validation.AbstractWhitelistValidator;

/**
 * Validator für generiertes Passwort.
 */
public class GeneratedPasswortValidator extends AbstractWhitelistValidator<GeneratedPasswort, String> {

	private static final String REGEXP = "^[a-zA-Z0-9]{12}$";

	/**
	 * @see de.egladil.common.validation.validators.AbstractWhitelistValidator#getWhitelist()
	 */
	@Override
	protected String getWhitelist() {

		return REGEXP;
	}
}
