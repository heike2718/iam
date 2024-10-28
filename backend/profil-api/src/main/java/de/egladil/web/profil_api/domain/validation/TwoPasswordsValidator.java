// =====================================================
// Projekt: commons-validation
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.profil_api.domain.validation;

import de.egladil.web.profil_api.domain.passwort.TwoPasswords;
import de.egladil.web.profil_api.domain.validation.annotations.ValidPasswords;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * RegistrationCredentialsValidator
 */
public class TwoPasswordsValidator implements ConstraintValidator<ValidPasswords, TwoPasswords> {

	@Override
	public boolean isValid(final TwoPasswords value, final ConstraintValidatorContext context) {

		if (value == null) {

			return true;
		}

		if (value.getPasswort() != null && !value.getPasswort().equals(value.getPasswortWdh())) {

			context.buildConstraintViolationWithTemplate("Die Passwörter stimmen nicht überein").addBeanNode()
				.addConstraintViolation();
			return false;

		}
		return true;
	}
}
