// =====================================================
// Projekt: auth-validations
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

import de.egladil.web.auth_validations.PasswortLoginValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * mindestens 8 Zeichen, höchstens 100 Zeichen, mindestens ein Buchstabe, mindestens eine Ziffer, keine Leerzeichen<br>
 * <br>
 * Erlaubte Sonderzeichen: !\"#$%&)(*+,-./:;<=>?@][^_'`'{|}~ Wenn der Wert null ist, wird er als gültig angesehen. Es
 * muss also zusätzlich eine NotNull-Anntotation angebracht werden.
 */
@Documented
@Retention(RUNTIME)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Constraint(validatedBy = { PasswortLoginValidator.class })
public @interface PasswortLogin {

	String message() default "Das Passwort enthält ungültige Zeichen.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
