// =====================================================
// Projekt: profil-api
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.profil_api.domain.validation;

import de.egladil.web.profil_api.domain.validation.annotations.PasswortLogin;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Pattern für das Login-Passwort. Das muss keinen Regeln genügen, darf nur keine ungültigen Zeichen enthalten.
 * (?=[^A-ZÄÖÜa-zäöüß]*[A-ZÄÖÜa-zäöüß])(?=[^\d]*[\d])[A-Za-z0-9ÄÖÜäöüß !"#\$%&'\(\)\*\+,\-\.\/:<=>\?@\[\]\^\\_ `'{|}~
 * ]{8,100}$
 */
public class PasswortLoginValidator extends AbstractWhitelistValidator<PasswortLogin, String> {

	/**
	 * ^[\da-zA-ZäÄöÖüÜß!#$%&()*+,\-./:;=?@\[\]^_`'{|}~]*$
	 */
	private static final String REGEXP = "^[\\da-zA-ZäÄöÖüÜß!#$%&()*+,\\-./:;=?@\\[\\]^_`'{|}~]*$";

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
