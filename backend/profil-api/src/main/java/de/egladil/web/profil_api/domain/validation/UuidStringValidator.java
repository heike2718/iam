// =====================================================
// Projekt: commons-validation
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.profil_api.domain.validation;

import de.egladil.web.profil_api.domain.validation.annotations.UuidString;

/**
 * KuerzelValidator
 */
public class UuidStringValidator extends AbstractWhitelistValidator<UuidString, String> {

	private static final String REGEXP = "[a-fA-F0-9\\-]*";

	@Override
	protected String getWhitelist() {

		return REGEXP;
	}
}
