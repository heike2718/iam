// =====================================================
// Project: benutzerprofil
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.benutzerprofil.domain.passwort;

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
import de.egladil.web.benutzerprofil.domain.passwort.PasswortPayload;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * PasswortPayloadValidationTest
 */
public class PasswortPayloadValidationTest {

	private Validator validator;

	@BeforeEach
	void setUp() {

		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	@Test
	void should_pass_when_valid() {

		// Arrange
		PasswortPayload bean = createValidPayload();

		// Act
		Set<ConstraintViolation<PasswortPayload>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(0, constraintViolations.size());
	}

	@Test
	void should_notPass_when_zweiPassworteInvalid() {

		// Arrange
		PasswortPayload bean = createValidPayload();

		ZweiPassworte invalidZweiPassworte = new ZweiPassworte();
		invalidZweiPassworte.setPasswort("Qw<ert>z!");

		bean.setZweiPassworte(invalidZweiPassworte);

		List<String> expectedMessages = Arrays.asList(new String[] { "Das (neue) Passwort ist nicht regelkonform.",
			"Das wiederholte Passwort ist erforderlich.", "Die (neuen) Passwörter stimmen nicht überein." });

		// Act
		Set<ConstraintViolation<PasswortPayload>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(4, constraintViolations.size());

		List<String> messages = new ArrayList<>(
			constraintViolations.stream().map(cv -> cv.getMessage()).collect(Collectors.toSet()));

		// System.out.println(messages);

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
	void should_notPass_when_passwortInvalid() {

		// Arrange
		PasswortPayload bean = createValidPayload();
		bean.setPasswort("Qw<ert>z!");

		List<String> expectedMessages = Arrays.asList(new String[] { "Das aktuelle Passwort enthält ungültige Zeichen." });

		// Act
		Set<ConstraintViolation<PasswortPayload>> constraintViolations = validator.validate(bean);

		// Assert
		assertEquals(1, constraintViolations.size());

		List<String> messages = new ArrayList<>(
			constraintViolations.stream().map(cv -> cv.getMessage()).collect(Collectors.toSet()));

		// System.out.println(messages);

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

	private PasswortPayload createValidPayload() {

		PasswortPayload result = new PasswortPayload();
		result.setPasswort("start123");
		result.setZweiPassworte(createValidZweiPassworte());

		return result;
	}

	private ZweiPassworte createValidZweiPassworte() {

		ZweiPassworte result = new ZweiPassworte();
		result.setPasswort("Qwertz!2");
		result.setPasswortWdh(result.getPasswort());
		return result;
	}
}
