// =====================================================
// Projekt: profil-api
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.auth_validations;

import de.egladil.web.auth_validations.annotations.ValidPasswords;
import de.egladil.web.auth_validations.dto.ZweiPassworte;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * RegistrationCredentialsValidator
 */
public class TwoPasswordsValidator implements ConstraintValidator<ValidPasswords, ZweiPassworte> {

	@Override
	public boolean isValid(final ZweiPassworte value, final ConstraintValidatorContext context) {

		if (value == null) {

			return true;
		}

		if (value.getPasswort() != null && !value.getPasswort().equals(value.getPasswortWdh())) {

			context.buildConstraintViolationWithTemplate("Die (neuen) Passwörter stimmen nicht überein.").addBeanNode()
				.addConstraintViolation();
			return false;

		}
		return true;
	}
}
