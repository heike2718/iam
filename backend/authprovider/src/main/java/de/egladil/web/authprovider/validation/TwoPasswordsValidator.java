// =====================================================
// Projekt: commons-validation
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.validation;

import de.egladil.web.authprovider.payload.ZweiPassworte;
import de.egladil.web.authprovider.validation.annotations.ValidPasswords;
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

			context.buildConstraintViolationWithTemplate("Die Passwörter stimmen nicht überein").addBeanNode()
				.addConstraintViolation();
			return false;

		}
		return true;
	}
}
