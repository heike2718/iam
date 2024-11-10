// =====================================================
// Project: auth-validations
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_validations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.egladil.web.auth_validations.annotations.InputSecured;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * InputSecuredValidationTest
 */
public class InputSecuredValidationTest {

	private static final String EXPECTED_MESSAGE = "Eingabe ungültig. Erlaubt sind Buchstaben basierend auf dem lateinischen Alphabet einschließlich Zeichen mit Akzenten, Ziffern, Leezeichen und die Sonderzeichen ! # $ % & ( ) * + , - . / : ; = ? @ ^ _ ` ` { | } [ ] ~ °.";

	private Validator validator;

	class InputSecuredBean {

		@InputSecured
		private final String value;

		public InputSecuredBean(final String value) {

			super();
			this.value = value;
		}
	}

	@BeforeEach
	void setUp() {

		Locale.setDefault(Locale.GERMAN);
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	@Test
	void shouldPass_when_all_letters() {

		// Arrange
		String value = InputSecuredConstants.DIACRITICS;
		InputSecuredBean bean = new InputSecuredBean(value);

		// Act
		Set<ConstraintViolation<InputSecuredBean>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(0, constraintViolations.size());

	}

	@Test
	void shouldPass_when_all_digits() {

		// Arrange
		String value = InputSecuredConstants.DIGITS;
		InputSecuredBean bean = new InputSecuredBean(value);

		// Act
		Set<ConstraintViolation<InputSecuredBean>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(0, constraintViolations.size());

	}

	@Test
	void shouldPass_when_all_specialChars() {

		// Arrange
		String value = InputSecuredConstants.SPECIAL_CHARS_FOR_MESSAGE;
		InputSecuredBean bean = new InputSecuredBean(value);

		// Act
		Set<ConstraintViolation<InputSecuredBean>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(0, constraintViolations.size());

	}

	@Test
	void shouldPass_when_space() {

		// Arrange
		String value = " ";
		InputSecuredBean bean = new InputSecuredBean(value);

		// Act
		Set<ConstraintViolation<InputSecuredBean>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(0, constraintViolations.size());

	}

	@Test
	void shouldNotPass_when_vulnerableSpecialChars() {

		// Arrange
		String value = "<\" \\ \">";
		InputSecuredBean bean = new InputSecuredBean(value);

		// Act
		Set<ConstraintViolation<InputSecuredBean>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(1, constraintViolations.size());

		ConstraintViolation<InputSecuredBean> cv = constraintViolations.iterator().next();

		assertEquals(EXPECTED_MESSAGE, cv.getMessage());

	}

	@Test
	void shouldNotPass_when_otherAlphabets() {

		// Arrange
		String value = "السنجاب Σκίουρος リス 松鼠 Белка";
		InputSecuredBean bean = new InputSecuredBean(value);

		// Act
		Set<ConstraintViolation<InputSecuredBean>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(1, constraintViolations.size());

		ConstraintViolation<InputSecuredBean> cv = constraintViolations.iterator().next();

		assertEquals(EXPECTED_MESSAGE, cv.getMessage());

	}

	@Test
	void shouldNotPass_when_linebreaks() {

		// Arrange
		String value = "\n \t \r\t";
		InputSecuredBean bean = new InputSecuredBean(value);

		// Act
		Set<ConstraintViolation<InputSecuredBean>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(1, constraintViolations.size());

		ConstraintViolation<InputSecuredBean> cv = constraintViolations.iterator().next();

		assertEquals(EXPECTED_MESSAGE, cv.getMessage());

	}

	@Test
	void shouldNotPass_when_startsWithPointPoint() {

		// Arrange
		String value = ".. Hallo";
		InputSecuredBean bean = new InputSecuredBean(value);

		// Act
		Set<ConstraintViolation<InputSecuredBean>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(1, constraintViolations.size());

		ConstraintViolation<InputSecuredBean> cv = constraintViolations.iterator().next();

		assertEquals(EXPECTED_MESSAGE, cv.getMessage());

	}

	@Test
	void shouldNotPass_when_containsWithPointPointInTheMiddle() {

		// Arrange
		String value = "Guten ../ Tag";
		InputSecuredBean bean = new InputSecuredBean(value);

		// Act
		Set<ConstraintViolation<InputSecuredBean>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(1, constraintViolations.size());

		ConstraintViolation<InputSecuredBean> cv = constraintViolations.iterator().next();

		assertEquals(EXPECTED_MESSAGE, cv.getMessage());

	}

}
