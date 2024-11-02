// =====================================================
// Projekt: profil-api
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.auth_validations;

import de.egladil.web.auth_validations.annotations.PasswortNeu;
import jakarta.validation.ConstraintValidatorContext;

/**
 * PasswortNeuValidator.
 */
public class PasswortNeuValidator extends AbstractWhitelistValidator<PasswortNeu, String> {

	// ^(?!\s)(?=.*\d)(?=.*[a-zA-ZäÄöÖüÜß])(?=.*[!#$%&()*+,\-./:;=?@\[\]^_`'{|}~])[a-zA-ZäÄöÖüÜß\d!#$%&()*+,\-./:;=?@\[\]^_`'{|}~
	// ]{8,100}(?<!\s)$
	// ^(?!\s)(?=.*\d)(?=.*[a-zA-ZäÄöÖüÜß])[a-zA-ZäÄöÖüÜß \d!#$%&()*+,\-./:;=?@\[\]^_`'{|}~]{8,100}(?<!\s)
	private static final String REGEXP = "^(?!\\s)(?=.*\\d)(?=.*[a-zA-ZäÄöÖüÜß])[a-zA-ZäÄöÖüÜß \\d!#$%&()*+,\\-./:;=?@\\[\\]^_`'{|}~]{6,100}(?<!\\s)";

	@Override
	protected String getWhitelist() {

		return REGEXP;
	}

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {

		if (value == null) {

			return true;
		}

		if (!super.isValid(value, context)) {

			return false;
		}
		final String trimmed = value.trim();

		if (trimmed.length() < value.length()) {

			return false;
		}
		return true;
	}
}
