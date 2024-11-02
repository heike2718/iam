// =====================================================
// Projekt: profil-api
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.auth_validations;

import de.egladil.web.auth_validations.annotations.PasswortLogin;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Pattern für das Login-Passwort. Das muss keinen Regeln genügen, darf nur keine ungültigen Zeichen enthalten. Aus Gründen der
 * Abwärtskompatibilität müssen die früher erlaubten Sonderzeichen lt, gt, double quote und back slash leider übernommen werden,
 * weil sich sonst vielleicht Menschen nicht mehr einloggen können. Bei neuen Passwörtern wird es nicht mehr möglich sein, diese
 * einzugeben.
 */
public class PasswortLoginValidator extends AbstractWhitelistValidator<PasswortLogin, String> {

	/**
	 * ^[\d a-zA-ZäÄöÖüÜß!\"#$%&)(*+,-./:;<=>?@][^_'`'{|}~]*$
	 */
	private static final String REGEXP = "^[\\d a-zA-ZäÄöÖüÜß!#$%&()*+,\\-./:;=?@\\[\\]^_`'{|}~]*$";

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
