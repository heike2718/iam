// =====================================================
// Project: auth-validations
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_validations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.egladil.web.auth_validations.annotations.PasswortNeu;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * PasswortNeuValidationTest
 */
public class PasswortNeuValidationTest {

	private static final String INVALID_VALUE_MESSAGE = "Das neue Passwort ist zu kurz (min. 8), zu lang (max 100), enthält ungültige Zeichen, Leerzeichen am Anfang oder am Ende oder nicht mindestens einen Buchstaben und eine Ziffer.";

	private static final String KLEINBUCHSTABEN = "abcdefghijklmnopqrstuvwxyzäöüß";

	private static final String GROSSBUCHSTABEN = "ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜ";

	private static final String BUCHSTABEN = KLEINBUCHSTABEN + GROSSBUCHSTABEN;

	private static final String ZIFFERN = "0123456789";

	private static final String SONDERZEICHEN = "!#$%&()*+,-./:;=?@[]^_`'{|}~";

	private Validator validator;

	class PasswortNeuBean {

		@PasswortNeu
		private final String passwort;

		public PasswortNeuBean(final String passwort) {

			super();
			this.passwort = passwort;
		}

	}

	@BeforeEach
	void setUp() {

		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	@Test
	void shouldPass_when_minimalValidNurKleinbuchstaben() {

		// Arrange
		String passwort = generateRandomString(Arrays.asList(new String[] { KLEINBUCHSTABEN, ZIFFERN }), 8);
		PasswortNeuBean bean = new PasswortNeuBean(passwort);

		System.out.println(passwort);

		// Act
		Set<ConstraintViolation<PasswortNeuBean>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(0, constraintViolations.size());

	}

	@Test
	void shouldPass_when_minimalValidNurGrossbuchstaben() {

		// Arrange
		String passwort = generateRandomString(Arrays.asList(new String[] { GROSSBUCHSTABEN, ZIFFERN }), 8);
		PasswortNeuBean bean = new PasswortNeuBean(passwort);

		System.out.println(passwort);

		// Act
		Set<ConstraintViolation<PasswortNeuBean>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(0, constraintViolations.size());

	}

	@Test
	void shouldPass_when_minimalValidKleinUndGrossbuchstaben() {

		// Arrange
		String passwort = generateRandomString(Arrays.asList(new String[] { BUCHSTABEN, ZIFFERN }), 8);
		PasswortNeuBean bean = new PasswortNeuBean(passwort);

		System.out.println(passwort);

		// Act
		Set<ConstraintViolation<PasswortNeuBean>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(0, constraintViolations.size());

	}

	@Test
	void shouldPass_when_allCharClassesAndMaximalLenth() {

		// Arrange
		String passwort = generateRandomString(
			Arrays.asList(new String[] { KLEINBUCHSTABEN, GROSSBUCHSTABEN, ZIFFERN, SONDERZEICHEN }), 100);
		PasswortNeuBean bean = new PasswortNeuBean(passwort);

		System.out.println("length=" + passwort.length() + ", passwort=" + passwort);

		// Act
		Set<ConstraintViolation<PasswortNeuBean>> constraintViolations = validator.validate(bean);

		// Assert

		if (constraintViolations.size() > 0) {

			ConstraintViolation<PasswortNeuBean> cv = constraintViolations.iterator().next();

			System.out.println(cv.getMessage());
		}

		assertEquals(0, constraintViolations.size());

	}

	@Test
	void shouldPass_when_allCharClassesAndOneSpaceInTheMiddle() {

		// Arrange
		String passwort1 = generateRandomString(
			Arrays.asList(new String[] { KLEINBUCHSTABEN, GROSSBUCHSTABEN, ZIFFERN, SONDERZEICHEN }), 40);

		String passwort2 = generateRandomString(
			Arrays.asList(new String[] { KLEINBUCHSTABEN, GROSSBUCHSTABEN, ZIFFERN, SONDERZEICHEN }), 40);

		String passwort = passwort1 + " " + passwort2;
		PasswortNeuBean bean = new PasswortNeuBean(passwort);

		System.out.println("length=" + passwort.length() + ", passwort=" + passwort);

		// Act
		Set<ConstraintViolation<PasswortNeuBean>> constraintViolations = validator.validate(bean);

		// Assert

		if (constraintViolations.size() > 0) {

			ConstraintViolation<PasswortNeuBean> cv = constraintViolations.iterator().next();

			System.out.println(cv.getMessage());
		}

		assertEquals(0, constraintViolations.size());

	}

	@Test
	void shouldNotPass_when_allCharClassesAndTooShort() {

		// Arrange
		String passwort = generateRandomString(
			Arrays.asList(new String[] { KLEINBUCHSTABEN, GROSSBUCHSTABEN, ZIFFERN, SONDERZEICHEN }), 5);
		PasswortNeuBean bean = new PasswortNeuBean(passwort);

		System.out.println("length=" + passwort.length() + ", passwort=" + passwort);

		// Act
		Set<ConstraintViolation<PasswortNeuBean>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(1, constraintViolations.size());
		ConstraintViolation<PasswortNeuBean> cv = constraintViolations.iterator().next();
		assertEquals(INVALID_VALUE_MESSAGE, cv.getMessage());

	}

	@Test
	void shouldNotPass_when_allCharClassesAndTooLong() {

		// Arrange
		String passwort = generateRandomString(
			Arrays.asList(new String[] { KLEINBUCHSTABEN, GROSSBUCHSTABEN, ZIFFERN, SONDERZEICHEN }), 101);
		PasswortNeuBean bean = new PasswortNeuBean(passwort);

		System.out.println("length=" + passwort.length() + ", passwort=" + passwort);

		// Act
		Set<ConstraintViolation<PasswortNeuBean>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(1, constraintViolations.size());
		ConstraintViolation<PasswortNeuBean> cv = constraintViolations.iterator().next();
		assertEquals(INVALID_VALUE_MESSAGE, cv.getMessage());

	}

	@Test
	void shouldNotPass_when_allCharClassesAndLeadingSpace() {

		// Arrange
		String passwort = " "
			+ generateRandomString(Arrays.asList(new String[] { KLEINBUCHSTABEN, GROSSBUCHSTABEN, ZIFFERN, SONDERZEICHEN }), 8);
		PasswortNeuBean bean = new PasswortNeuBean(passwort);

		System.out.println("length=" + passwort.length() + ", passwort=" + passwort);

		// Act
		Set<ConstraintViolation<PasswortNeuBean>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(1, constraintViolations.size());
		ConstraintViolation<PasswortNeuBean> cv = constraintViolations.iterator().next();
		assertEquals(INVALID_VALUE_MESSAGE, cv.getMessage());

	}

	@Test
	void shouldNotPass_when_allCharClassesAndTrailingSpace() {

		// Arrange
		String passwort = generateRandomString(
			Arrays.asList(new String[] { KLEINBUCHSTABEN, GROSSBUCHSTABEN, ZIFFERN, SONDERZEICHEN }), 8) + " ";
		PasswortNeuBean bean = new PasswortNeuBean(passwort);

		System.out.println("length=" + passwort.length() + ", passwort=" + passwort);

		// Act
		Set<ConstraintViolation<PasswortNeuBean>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(1, constraintViolations.size());
		ConstraintViolation<PasswortNeuBean> cv = constraintViolations.iterator().next();
		assertEquals(INVALID_VALUE_MESSAGE, cv.getMessage());

	}

	@Test
	void shouldNotPass_when_allCharClassesAndLowerThan() {

		// Arrange
		String passwort = generateRandomString(
			Arrays.asList(new String[] { KLEINBUCHSTABEN, GROSSBUCHSTABEN, ZIFFERN, SONDERZEICHEN }), 8) + "<";
		PasswortNeuBean bean = new PasswortNeuBean(passwort);

		System.out.println("length=" + passwort.length() + ", passwort=" + passwort);

		// Act
		Set<ConstraintViolation<PasswortNeuBean>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(1, constraintViolations.size());
		ConstraintViolation<PasswortNeuBean> cv = constraintViolations.iterator().next();
		assertEquals(INVALID_VALUE_MESSAGE, cv.getMessage());

	}

	@Test
	void shouldNotPass_when_allCharClassesAndGreaterThan() {

		// Arrange
		String passwort = generateRandomString(
			Arrays.asList(new String[] { KLEINBUCHSTABEN, GROSSBUCHSTABEN, ZIFFERN, SONDERZEICHEN }), 8) + ">";
		PasswortNeuBean bean = new PasswortNeuBean(passwort);

		System.out.println("length=" + passwort.length() + ", passwort=" + passwort);

		// Act
		Set<ConstraintViolation<PasswortNeuBean>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(1, constraintViolations.size());
		ConstraintViolation<PasswortNeuBean> cv = constraintViolations.iterator().next();
		assertEquals(INVALID_VALUE_MESSAGE, cv.getMessage());

	}

	@Test
	void shouldNotPass_when_allCharClassesAndDoubleQuotes() {

		// Arrange
		String passwort = generateRandomString(
			Arrays.asList(new String[] { KLEINBUCHSTABEN, GROSSBUCHSTABEN, ZIFFERN, SONDERZEICHEN }), 8) + "\"";
		PasswortNeuBean bean = new PasswortNeuBean(passwort);

		System.out.println("length=" + passwort.length() + ", passwort=" + passwort);

		// Act
		Set<ConstraintViolation<PasswortNeuBean>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(1, constraintViolations.size());
		ConstraintViolation<PasswortNeuBean> cv = constraintViolations.iterator().next();
		assertEquals(INVALID_VALUE_MESSAGE, cv.getMessage());

	}

	@Test
	void shouldNotPass_when_allCharClassesAndBackslash() {

		// Arrange
		String passwort = generateRandomString(
			Arrays.asList(new String[] { KLEINBUCHSTABEN, GROSSBUCHSTABEN, ZIFFERN, SONDERZEICHEN }), 8) + "\\";
		PasswortNeuBean bean = new PasswortNeuBean(passwort);

		System.out.println("length=" + passwort.length() + ", passwort=" + passwort);

		// Act
		Set<ConstraintViolation<PasswortNeuBean>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(1, constraintViolations.size());
		ConstraintViolation<PasswortNeuBean> cv = constraintViolations.iterator().next();
		assertEquals(INVALID_VALUE_MESSAGE, cv.getMessage());

	}

	@Test
	void shouldNotPass_when_allCharClassesAndInvalidLetter() {

		// Arrange
		String passwort = generateRandomString(
			Arrays.asList(new String[] { KLEINBUCHSTABEN, GROSSBUCHSTABEN, ZIFFERN, SONDERZEICHEN }), 8) + "ê";
		PasswortNeuBean bean = new PasswortNeuBean(passwort);

		System.out.println("length=" + passwort.length() + ", passwort=" + passwort);

		// Act
		Set<ConstraintViolation<PasswortNeuBean>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(1, constraintViolations.size());
		ConstraintViolation<PasswortNeuBean> cv = constraintViolations.iterator().next();
		assertEquals(INVALID_VALUE_MESSAGE, cv.getMessage());

	}

	@Test
	void testASpecialRandomlyGeneratedString() {

		// Arrange
		String passwort = KLEINBUCHSTABEN + GROSSBUCHSTABEN + ZIFFERN + SONDERZEICHEN;
		// String passwort =
		// "ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜabcdefghijklmnopqrstuvwxyzäöüß0123456789!#$%&()*+,-./:;=?@[]^_`'{|}~";
		// String passwort =
		// "ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜabcdefghijklmnopqrstuvwxyzäöüß0123456789!#$%&()*+,-./:;=?@[]^_`'{|}~";

		PasswortNeuBean bean = new PasswortNeuBean(passwort);

		System.out.println("length=" + passwort.length() + ", passwort=" + passwort);

		// Act
		Set<ConstraintViolation<PasswortNeuBean>> constraintViolations = validator.validate(bean);

		if (constraintViolations.size() > 0) {

			ConstraintViolation<PasswortNeuBean> cv = constraintViolations.iterator().next();

			System.out.println(cv.getMessage());
		}

	}

	private String generateRandomString(final List<String> characterClasses, final int length) {

		if (characterClasses.size() > length) {

			throw new IllegalArgumentException("Length must be at least as large as the number of character classes.");
		}

		Random random = new Random();
		StringBuffer allCharacters = new StringBuffer();
		List<Character> result = new ArrayList<>();

		// Step 1: Ensure at least one character from each class is present
		for (String charClass : characterClasses) {

			char selectedChar = charClass.charAt(random.nextInt(charClass.length()));
			result.add(selectedChar);
			allCharacters.append(charClass);
		}

		// Step 2: Fill the rest of the string with characters from the combined pool
		for (int i = characterClasses.size(); i < length; i++) {

			char selectedChar = allCharacters.charAt(random.nextInt(allCharacters.length()));
			result.add(selectedChar);
		}

		// Step 3: Shuffle the result to randomize the order
		Collections.shuffle(result);

		// Convert the result list to a string
		StringBuilder randomString = new StringBuilder();

		for (char c : result) {

			randomString.append(c);
		}

		return randomString.toString();

	}

}
