// =====================================================
// Projekt: commons-validation
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.profil_api.domain.validation;

import de.egladil.web.profil_api.domain.validation.annotations.Passwort;
import jakarta.validation.ConstraintValidatorContext;

/**
 * PasswortValidator
 * (?=[^A-ZÄÖÜa-zäöüß]*[A-ZÄÖÜa-zäöüß])(?=[^\d]*[\d])[A-Za-z0-9ÄÖÜäöüß !"#\$%&'\(\)\*\+,\-\.\/:<=>\?@\[\]\^\\_ `'{|}~
 * ]{8,100}$
 */
public class PasswortValidator extends AbstractWhitelistValidator<Passwort, String> {

	// private static final String REGEXP =
	// "(?=[^A-ZÄÖÜ]*[A-ZÄÖÜ])(?=[^a-zäöüß]*[a-zäöüß])(?=[^\\d]*[\\d])[A-Za-z0-9ÄÖÜäöüß
	// !\"#\\$%&'\\(\\)\\*\\+,\\-\\.\\/:;<=>\\?@\\[\\]\\^\\\\_`'{|}~]{8,20}$";

	/**
	 * (?=[^A-ZÄÖÜa-zäöüß]*[A-ZÄÖÜa-zäöüß])(?=[^\d]*[\d])[A-Za-z0-9ÄÖÜäöüß!"#\$%&'\(\)\*\+,\-\.\/:<=>\?@\[\]\^\\_ `'{|}~
	 * ]{8,100}$
	 */
	private static final String REGEXP = "(?=[^A-ZÄÖÜa-zäöüß]*[A-ZÄÖÜa-zäöüß])(?=[^\\d]*[\\d])[A-Za-z0-9ÄÖÜäöüß!\"#\\$%&'\\(\\)\\*\\+,\\-\\.\\/:;<=>\\?@\\[\\]\\^\\\\_`'{|}~ ]{8,100}$";

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
