// =====================================================
// Projekt: profil-api
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.auth_validations;

import de.egladil.web.auth_validations.annotations.PasswortNeu;
import jakarta.validation.ConstraintValidatorContext;

/**
 * PasswortNeuValidator
 * (?=[^A-ZÄÖÜa-zäöüß]*[A-ZÄÖÜa-zäöüß])(?=[^\d]*[\d])[A-Za-z0-9ÄÖÜäöüß !"#\$%&'\(\)\*\+,\-\.\/:<=>\?@\[\]\^\\_ `'{|}~
 * ]{8,100}$
 */
public class PasswortNeuValidator extends AbstractWhitelistValidator<PasswortNeu, String> {

	/**
	 * ^(?=.*\d)(?=.*[a-zA-ZäÄöÖüÜß])(?=.*[!#$%&()*+,\-./:;=?@\[\]^_`'{|}~])[\da-zA-ZäÄöÖüÜß!#$%&()*+,\-./:;=?@\[\]^_`'{|}~]{12,100}$
	 */
	private static final String REGEXP = "^(?=.*\\d)(?=.*[a-zA-ZäÄöÖüÜß])(?=.*[!#$%&()*+,\\-./:;=?@\\[\\]^_`'{|}~])[\\da-zA-ZäÄöÖüÜß!#$%&()*+,\\-./:;=?@\\[\\]^_`'{|}~]{12,100}$";

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
