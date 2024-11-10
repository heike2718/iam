// =====================================================
// Project: auth-validations
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_validations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.egladil.web.auth_validations.annotations.LoginName;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * LoginNameValidationTest
 */
public class LoginNameValidationTest {

	private static final String EXPECTED_MESSAGE = "Der Login-Name enthält ungültige Zeichen. Erlaubt sind die Buchstaben A-Z und a-z, die Ziffern 0-9, Leerzeichen und die Sonderzeichen @ ! # $ % & ' * + - / = ? ^ _ . ` { | } ~ .";

	private Validator validator;

	class LoginNameBean {

		@LoginName
		private final String value;

		public LoginNameBean(final String value) {

			super();
			this.value = value;
		}
	}

	@BeforeEach
	void setUp() {

		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	@Test
	void shouldPass_when_allowed_letters() {

		// Arrange
		String value = InputSecuredConstants.LATIN;
		LoginNameBean bean = new LoginNameBean(value);

		// Act
		Set<ConstraintViolation<LoginNameBean>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(0, constraintViolations.size());

	}

	@Test
	void shouldPass_when_digits() {

		// Arrange
		String value = InputSecuredConstants.DIGITS;
		LoginNameBean bean = new LoginNameBean(value);

		// Act
		Set<ConstraintViolation<LoginNameBean>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(0, constraintViolations.size());

	}

	@Test
	void shouldPass_when_allowedSpecialChars() {

		//
		String value = "! # $ % & ' * + - / = ? ^ _ . ` { | } ~ @";
		LoginNameBean bean = new LoginNameBean(value);

		// Act
		Set<ConstraintViolation<LoginNameBean>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(0, constraintViolations.size());
	}

	@Test
	void shouldNotPass_when_vulnerableSpecialChars() {

		// Arrange
		String value = "<\" \\ \">";
		LoginNameBean bean = new LoginNameBean(value);

		// Act
		Set<ConstraintViolation<LoginNameBean>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(1, constraintViolations.size());

		ConstraintViolation<LoginNameBean> cv = constraintViolations.iterator().next();

		assertEquals(EXPECTED_MESSAGE, cv.getMessage());

	}

	@Test
	void shouldNotPass_when_doublePointsInTheMiddle() {

		// Arrange
		String value = "hallo..@";
		LoginNameBean bean = new LoginNameBean(value);

		// Act
		Set<ConstraintViolation<LoginNameBean>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(1, constraintViolations.size());

		ConstraintViolation<LoginNameBean> cv = constraintViolations.iterator().next();

		assertEquals(EXPECTED_MESSAGE, cv.getMessage());

	}

	@Test
	void shouldNotPass_when_startsWithPointPoint() {

		// Arrange
		String value = "..hallo";
		LoginNameBean bean = new LoginNameBean(value);

		// Act
		Set<ConstraintViolation<LoginNameBean>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(1, constraintViolations.size());

		ConstraintViolation<LoginNameBean> cv = constraintViolations.iterator().next();

		assertEquals(EXPECTED_MESSAGE, cv.getMessage());

	}

}
