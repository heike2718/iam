// =====================================================
// Project: auth-validations
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_validations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.egladil.web.auth_validations.dto.ZweiPassworte;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * ZweiPassworteValidationTest
 */
public class ZweiPassworteValidationTest {

	private Validator validator;

	@BeforeEach
	void setUp() {

		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	@Test
	void should_pass_whenValid() {

		// Arrange
		ZweiPassworte bean = createValidPayload();

		// Act
		Set<ConstraintViolation<ZweiPassworte>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(0, constraintViolations.size());

	}

	@Test
	void shouldFail_when_patternDoesNotMatch() {

		// Arrange
		String missmatching = "Qwe<rtz>!2";
		ZweiPassworte bean = new ZweiPassworte();
		bean.setPasswort(missmatching);
		bean.setPasswortWdh(missmatching);

		List<String> expectedMessages = Arrays
			.asList(new String[] { "Das (neue) Passwort ist nicht regelkonform. ",
				"Das wiederholte Passwort ist nicht regelkonform. " });

		// Act
		Set<ConstraintViolation<ZweiPassworte>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(2, constraintViolations.size());

		List<String> messages = constraintViolations.stream().map(cv -> cv.getMessage()).toList();
		List<String> notContained = new ArrayList<>();

		for (String message : expectedMessages) {

			if (!messages.contains(message)) {

				notContained.add(message);
			}
		}

		if (notContained.size() > 0) {

			System.out.println("nicht enthaltene messages: " + notContained);
			fail("nicht alle erwarteten Messages sind enthalten");
		}
	}

	@Test
	void shouldFail_when_leadingSpaces() {

		// Arrange
		String missmatching = "  Qwertz!2";
		ZweiPassworte bean = new ZweiPassworte();
		bean.setPasswort(missmatching);
		bean.setPasswortWdh(missmatching);

		List<String> expectedMessages = Arrays
			.asList(new String[] { "Das (neue) Passwort ist nicht regelkonform. ",
				"Das wiederholte Passwort ist nicht regelkonform. " });

		// Act
		Set<ConstraintViolation<ZweiPassworte>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(2, constraintViolations.size());

		List<String> messages = constraintViolations.stream().map(cv -> cv.getMessage()).toList();
		List<String> notContained = new ArrayList<>();

		for (String message : expectedMessages) {

			if (!messages.contains(message)) {

				notContained.add(message);
			}
		}

		if (notContained.size() > 0) {

			System.out.println("nicht enthaltene messages: " + notContained);
			fail("nicht alle erwarteten Messages sind enthalten");
		}
	}

	@Test
	void shouldFail_when_tailingSpaces() {

		// Arrange
		String missmatching = "Qwertz!2    ";
		ZweiPassworte bean = new ZweiPassworte();
		bean.setPasswort(missmatching);
		bean.setPasswortWdh(missmatching);

		List<String> expectedMessages = Arrays
			.asList(new String[] { "Das (neue) Passwort ist nicht regelkonform. ",
				"Das wiederholte Passwort ist nicht regelkonform. " });

		// Act
		Set<ConstraintViolation<ZweiPassworte>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(2, constraintViolations.size());

		List<String> messages = constraintViolations.stream().map(cv -> cv.getMessage()).toList();
		List<String> notContained = new ArrayList<>();

		for (String message : expectedMessages) {

			if (!messages.contains(message)) {

				notContained.add(message);
			}
		}

		if (notContained.size() > 0) {

			System.out.println("nicht enthaltene messages: " + notContained);
			fail("nicht alle erwarteten Messages sind enthalten");
		}
	}

	@Test
	void shouldFail_when_differentPasswords() {

		// Arrange
		ZweiPassworte bean = createValidPayload();
		bean.setPasswortWdh("Qwertz!3");

		List<String> expectedMessages = Arrays
			.asList(new String[] { "Die Passwörter stimmen nicht überein" });

		// Act
		Set<ConstraintViolation<ZweiPassworte>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(2, constraintViolations.size());

		List<String> messages = new ArrayList<>(
			constraintViolations.stream().map(cv -> cv.getMessage()).collect(Collectors.toSet()));

		System.out.println(messages);

		List<String> notContained = new ArrayList<>();

		for (String message : expectedMessages) {

			if (!messages.contains(message)) {

				notContained.add(message);
			}
		}

		if (notContained.size() > 0) {

			System.out.println("nicht enthaltene messages: " + notContained);
			fail("nicht alle erwarteten Messages sind enthalten");
		}
	}

	@Test
	void shouldFail_when_passwortIsNull() {

		// Arrange
		ZweiPassworte bean = createValidPayload();
		bean.setPasswort(null);

		List<String> expectedMessages = Arrays
			.asList(new String[] { "Das (neue) Passwort ist erforderlich. " });

		// Act
		Set<ConstraintViolation<ZweiPassworte>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(1, constraintViolations.size());

		List<String> messages = new ArrayList<>(
			constraintViolations.stream().map(cv -> cv.getMessage()).collect(Collectors.toSet()));

		System.out.println(messages);

		List<String> notContained = new ArrayList<>();

		for (String message : expectedMessages) {

			if (!messages.contains(message)) {

				notContained.add(message);
			}
		}

		if (notContained.size() > 0) {

			System.out.println("fehlende Messages: " + notContained);
			fail("nicht alle erwarteten Messages sind enthalten");
		}
	}

	@Test
	void shouldFail_when_passwortWdhIsNull() {

		// Arrange
		ZweiPassworte bean = createValidPayload();
		bean.setPasswortWdh(null);

		List<String> expectedMessages = Arrays
			.asList(
				new String[] { "Das wiederholte Passwort ist erforderlich. ", "Die (neuen) Passwörter stimmen nicht überein. " });

		// Act
		Set<ConstraintViolation<ZweiPassworte>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(3, constraintViolations.size());

		List<String> messages = new ArrayList<>(
			constraintViolations.stream().map(cv -> cv.getMessage()).collect(Collectors.toSet()));

		System.out.println(messages);

		List<String> notContained = new ArrayList<>();

		for (String message : expectedMessages) {

			if (!messages.contains(message)) {

				notContained.add(message);
			}
		}

		if (notContained.size() > 0) {

			System.out.println("nicht enthaltene messages: " + notContained);
			fail("nicht alle erwarteten Messages sind enthalten");
		}
	}

	private ZweiPassworte createValidPayload() {

		ZweiPassworte result = new ZweiPassworte();
		result.setPasswort("Qwertz!2");
		result.setPasswortWdh(result.getPasswort());
		return result;
	}

}
