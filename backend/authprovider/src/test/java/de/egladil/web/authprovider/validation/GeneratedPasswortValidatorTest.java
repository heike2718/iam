// =====================================================
// Projekt: de.egladil.persistence.tools
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.validation.annotations.GeneratedPasswort;

/**
 * GeneratedPasswortValidatorTest
 */
public class GeneratedPasswortValidatorTest {

	private static final Logger LOG = LoggerFactory.getLogger(GeneratedPasswortValidatorTest.class);

	private static final String INVALID_CHARS = "!\"#$%&()*+/:;<=>?@[\\]^{|}~@- _.,'`'äöüßÄÖÜ";

	// Leerzeichen, Minus, Unterstrich, Punkt, Komma, Apostrophe
	private static final String VALID_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";

	private class TestObject {

		@GeneratedPasswort
		private final String value;

		/**
		 * Erzeugt eine Instanz von TestObject
		 */
		public TestObject(final String value) {

			super();
			this.value = value;
		}
	}

	@Test
	@DisplayName("passes when value null")
	public void validate1() {

		// Arrange
		final TestObject testObject = new TestObject(null);

		final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		final Validator validator = validatorFactory.getValidator();

		// Act
		final Set<ConstraintViolation<TestObject>> errors = validator.validate(testObject);

		// Assert
		assertTrue(errors.isEmpty());
	}

	@Test
	@DisplayName("fails when value too short")
	public void validate2() {

		// Arrange
		final TestObject testObject = new TestObject("AAAAAAABBDS");

		final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		final Validator validator = validatorFactory.getValidator();

		// Act
		final Set<ConstraintViolation<TestObject>> errors = validator.validate(testObject);

		// Assert
		assertFalse(errors.isEmpty());
		assertEquals(1, errors.size());

		final ConstraintViolation<TestObject> cv = errors.iterator().next();
		LOG.debug(cv.getMessage());
		assertEquals("value", cv.getPropertyPath().toString());
	}

	@Test
	@DisplayName("fails when value too long")
	public void validate3() {

		// Arrange
		final TestObject testObject = new TestObject("HTRBAAAAAAA89");

		final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		final Validator validator = validatorFactory.getValidator();

		// Act
		final Set<ConstraintViolation<TestObject>> errors = validator.validate(testObject);

		// Assert
		assertFalse(errors.isEmpty());
		assertEquals(1, errors.size());

		final ConstraintViolation<TestObject> cv = errors.iterator().next();
		LOG.debug(cv.getMessage());
		assertEquals("value", cv.getPropertyPath().toString());
	}

	@Test
	@DisplayName("passes when value valid")
	public void validate4() {

		// Arrange
		final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		final Validator validator = validatorFactory.getValidator();

		for (final char c : VALID_CHARS.toCharArray()) {

			final TestObject testObject = new TestObject("AAAAAAAHTRF" + c);

			// Act
			final Set<ConstraintViolation<TestObject>> errors = validator.validate(testObject);

			// Assert
			assertTrue(errors.isEmpty(), "Fehler bei [" + c + "]");
		}

	}

	@Test
	@DisplayName("fails when value invalid")
	public void validate5() {

		// Arrange
		final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		final Validator validator = validatorFactory.getValidator();

		for (final char c : INVALID_CHARS.toCharArray()) {

			final TestObject testObject = new TestObject("AAAAAAAHTRF" + c);

			// Act
			final Set<ConstraintViolation<TestObject>> errors = validator.validate(testObject);

			// Assert
			assertFalse(errors.isEmpty(), "Fehler bei [" + c + "]");
			assertEquals(1, errors.size());

			final ConstraintViolation<TestObject> cv = errors.iterator().next();
			LOG.debug(cv.getMessage());
			assertEquals("value", cv.getPropertyPath().toString());
		}

	}

}
