// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.egladil.web.auth_validations.dto.ZweiPassworte;
import de.egladil.web.authprovider.payload.ClientCredentials;
import de.egladil.web.authprovider.payload.SignUpCredentials;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * RegistrationCredentialsValidationTest
 */
@QuarkusTest
public class RegistrationCredentialsValidationTest {

	private Validator validator;

	@BeforeEach
	void setUp() {

		final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	@Test
	void validCredentialsIsValid() {

		Set<ConstraintViolation<SignUpCredentials>> constraintViolations = validator.validate(getValidCredentials());

		assertTrue(constraintViolations.isEmpty());
	}

	@Test
	void agbErforderlich() {

		// Arrange
		final SignUpCredentials credentials = getValidCredentials();
		credentials.setAgbGelesen(false);

		// Act
		Set<ConstraintViolation<SignUpCredentials>> constraintViolations = validator.validate(credentials);

		// Assert
		assertEquals(1, constraintViolations.size());

		ConstraintViolation<SignUpCredentials> cv = constraintViolations.iterator().next();
		assertEquals("Bitte stimmen Sie den Datenschutzhinweisen zu.", cv.getMessage());
	}

	@Test
	void passwordsNotEqual() {

		// Arrange
		final SignUpCredentials credentials = getValidCredentials();
		credentials.getTwoPasswords().setPasswortWdh("123start$trats321");

		// Act
		Set<ConstraintViolation<SignUpCredentials>> constraintViolations = validator.validate(credentials);

		// Assert
		assertEquals(2, constraintViolations.size());

		List<String> messages = constraintViolations.stream().map(cv -> cv.getMessage()).toList();

		assertTrue(messages.contains("Die Passwörter stimmen nicht überein"));
		assertTrue(messages.contains("twoPasswords ist nicht valid"));
	}

	@Test
	void multipleViolations() {

		// Arrange
		final SignUpCredentials credentials = getValidCredentials();
		credentials.getTwoPasswords().setPasswortWdh("123starttrats1321!");
		credentials.setKleber("hui");

		// Act
		Set<ConstraintViolation<SignUpCredentials>> constraintViolations = validator.validate(credentials);

		// Assert
		assertEquals(3, constraintViolations.size());

		List<String> messages = constraintViolations.stream().map(cv -> cv.getMessage()).toList();

		assertTrue(messages.contains("Die Passwörter stimmen nicht überein"));
		assertTrue(messages.contains("twoPasswords ist nicht valid"));
		assertTrue(messages.contains(""));
	}

	@Test
	void clientCredentialsErforderlich() {

		// Arrange
		final SignUpCredentials credentials = getValidCredentials();
		credentials.setClientCredentials(null);

		// Act
		Set<ConstraintViolation<SignUpCredentials>> constraintViolations = validator.validate(credentials);

		// Assert
		assertEquals(1, constraintViolations.size());

		ConstraintViolation<SignUpCredentials> cv = constraintViolations.iterator().next();
		assertEquals("clientCredentials ist erforderlich.", cv.getMessage());
	}

	private SignUpCredentials getValidCredentials() {

		SignUpCredentials result = new SignUpCredentials();
		result.setAgbGelesen(true);
		result.setEmail("bla@eladil.de");
		result.setLoginName("Herbert");
		result.setTwoPasswords(new ZweiPassworte("start123!321trats", "start123!321trats"));

		ClientCredentials clientCredentials = ClientCredentials.createWithState("asghkggd", "localhost:4200", "horst");
		result.setClientCredentials(clientCredentials);

		return result;
	}

}
