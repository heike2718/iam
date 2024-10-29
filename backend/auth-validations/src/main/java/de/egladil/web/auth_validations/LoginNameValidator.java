// =====================================================
// Project: profil-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_validations;

import de.egladil.web.auth_validations.annotations.LoginName;

/**
 * LoginNameValidator
 */
public class LoginNameValidator extends AbstractWhitelistValidator<LoginName, String> {

	private static final String REGEXP = "[\\w\\.\\-@ äöüÄÖÜß_!;,]*";

	@Override
	protected String getWhitelist() {

		return REGEXP;
	}
}
