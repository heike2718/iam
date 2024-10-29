// =====================================================
// Project: auth-validations
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_validations;

import java.lang.annotation.Annotation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * AbstractWhitelistValidator validiert Strings gegen eine Whitelist. Ist der Wert null oder leer, wird er als valid
 * angesehen. Bei Pflichtattributen müssen also zusätzliche Annotationen angebracht werden.
 */
public abstract class AbstractWhitelistValidator<A extends Annotation, T> implements ConstraintValidator<A, T> {

	/**
	 * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
	 */
	@Override
	public void initialize(final A constraintAnnotation) {

		// nix
	}

	@Override
	public boolean isValid(final T value, final ConstraintValidatorContext context) {

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

	public Pattern getPattern() {

		Pattern pattern = Pattern.compile(getWhitelist());
		return pattern;
	}

	/**
	 * Gibt die Whitelist als regular expression zurück.
	 *
	 * @return
	 */
	protected abstract String getWhitelist();
}
