// =====================================================
// Projekt: commons-validation
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.auth_validations;

import de.egladil.web.auth_validations.annotations.Honeypot;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * HoneypotValidator
 */
public class HoneypotValidator implements ConstraintValidator<Honeypot, String> {

	@Override
	public void initialize(final Honeypot constraintAnnotation) {

		// nix zu tun
	}

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {

		if (value == null) {

			return true;
		}
		return value.isEmpty();
	}
}
