// =====================================================
// Project: profil-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_api.domain.validation;

import de.egladil.web.profil_api.domain.validation.annotations.LoginName;

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
