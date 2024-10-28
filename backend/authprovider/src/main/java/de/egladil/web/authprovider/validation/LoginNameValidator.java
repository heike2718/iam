// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.validation;

import de.egladil.web.authprovider.validation.annotations.LoginName;

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
