// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.validation;

import java.util.regex.Matcher;

import de.egladil.web.authprovider.validation.annotations.InputSecured;
import jakarta.validation.ConstraintValidatorContext;

/**
 * InputSecuredValidator.
 */
public class InputSecuredValidator extends AbstractWhitelistValidator<InputSecured, String> {

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {

		if (value == null) {

			return true;
		}

		if (!(value instanceof String)) {

			return false;
		}
		String strValue = (String) value;

		if (strValue.isEmpty()) {

			return true;
		}

		Matcher matcher = getPattern().matcher(strValue);
		boolean matches = matcher.matches();
		return matches;
	}

	@Override
	protected String getWhitelist() {

		return InputSecuredConstants.INPUT_SECURED_WHITELIST;
	}

}
