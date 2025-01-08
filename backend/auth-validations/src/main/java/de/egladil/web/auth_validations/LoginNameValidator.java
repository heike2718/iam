// =====================================================
// Project: benutzerprofil
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_validations;

import de.egladil.web.auth_validations.annotations.LoginName;

/**
 * LoginNameValidator
 */
public class LoginNameValidator extends AbstractWhitelistValidator<LoginName, String> {

	private static final String REGEXP = "^(?!.*\\.\\.)[a-zA-Z0-9.!#$%&'*+/=?\\^_`{|}~ @-]*$";

	@Override
	protected String getWhitelist() {

		return REGEXP;
	}
}
