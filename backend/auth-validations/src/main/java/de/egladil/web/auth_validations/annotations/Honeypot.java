// =====================================================
// Projekt: commons-validation
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.auth_validations.annotations;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import de.egladil.web.auth_validations.HoneypotValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Wert muss entweder null sein oder (ungetrimmt) die Länge 0 haben.
 */
@Documented
@Retention(RUNTIME)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Constraint(validatedBy = { HoneypotValidator.class })
public @interface Honeypot {

	String message() default "{de.egladil.constraints.honeypot}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
