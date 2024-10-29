// =====================================================
// Projekt: auth-validations
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.auth_validations.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.egladil.web.auth_validations.TwoPasswordsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Zwei passwörter sind valid, wenn sie gleich sind.
 */
@Target({ ElementType.TYPE, ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TwoPasswordsValidator.class)
@Documented
public @interface ValidPasswords {

	String message() default "";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
