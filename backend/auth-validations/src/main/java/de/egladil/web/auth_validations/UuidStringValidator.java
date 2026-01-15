// =====================================================
// Projekt: auth-validations
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.auth_validations;

import de.egladil.web.auth_validations.annotations.UuidString;

/**
 * KuerzelValidator
 */
public class UuidStringValidator extends AbstractWhitelistValidator<UuidString, String> {

    private static final String REGEXP = "^[a-zA-Z0-9\\-]*$";

    @Override
    protected String getWhitelist() {

        return REGEXP;
    }
}
