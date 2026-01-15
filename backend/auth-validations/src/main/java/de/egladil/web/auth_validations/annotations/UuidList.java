// =====================================================
// Projekt: commons-validation
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.auth_validations.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import de.egladil.web.auth_validations.UuidListValidator;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *
 */
@Documented
@Retention(RUNTIME)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Constraint(validatedBy = { UuidListValidator.class })
public @interface UuidList {

    String message() default "{de.egladil.constraints.invalidChars}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
