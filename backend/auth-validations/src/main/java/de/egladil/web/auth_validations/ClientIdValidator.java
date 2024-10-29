// =====================================================
// Projekt: commons-validation
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.auth_validations;

import de.egladil.web.auth_validations.annotations.ClientId;

/**
 * KuerzelValidator
 */
public class ClientIdValidator extends AbstractWhitelistValidator<ClientId, String> {

	private static final String REGEXP = "[a-zA-Z0-9+=]*";

	@Override
	protected String getWhitelist() {

		return REGEXP;
	}
}
