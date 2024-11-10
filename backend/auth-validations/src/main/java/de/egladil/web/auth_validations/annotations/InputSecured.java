// =====================================================
// Project: profil-api
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

import de.egladil.web.auth_validations.IValidationMessages;
import de.egladil.web.auth_validations.InputSecuredValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * StringLatin und einige Sonderzeichen, die in Namen oder Bezeichnungen akzeptabel sind. Null und blank sind erlaubt.
 * Es gibt keine Längenbegrenzung.
 */
@Documented
@Retention(RUNTIME)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Constraint(validatedBy = { InputSecuredValidator.class })
public @interface InputSecured {

	String message() default "Eingabe ungültig. " + IValidationMessages.INPUT_SECURED_ERLAUBTE_ZEICHEN;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
