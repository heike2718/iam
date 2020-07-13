// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.egladil.web.authprovider.payload.ClientCredentials;
import de.egladil.web.authprovider.payload.SignUpCredentials;
import de.egladil.web.commons_validation.InvalidProperty;
import de.egladil.web.commons_validation.ValidationDelegate;
import de.egladil.web.commons_validation.exception.InvalidInputException;
import de.egladil.web.commons_validation.payload.ResponsePayload;
import de.egladil.web.commons_validation.payload.TwoPasswords;

/**
 * RegistrationCredentialsValidationTest
 */
public class RegistrationCredentialsValidationTest {

	private ValidationDelegate validationDelegate;

	@BeforeEach
	void setUp() {

		validationDelegate = new ValidationDelegate();
	}

	@Test
	void validCredentialsIsValid() {

		validationDelegate.check(getValidCredentials(), SignUpCredentials.class);
	}

	@Test
	void agbErforderlich() {

		// Arrange
		final SignUpCredentials credentials = getValidCredentials();
		credentials.setAgbGelesen(false);

		// Act + Assert
		final Throwable ex = assertThrows(InvalidInputException.class, () -> {

			validationDelegate.check(credentials, SignUpCredentials.class);
		});
		InvalidInputException e = (InvalidInputException) ex;
		ResponsePayload responsePayload = e.getResponsePayload();
		assertNotNull(responsePayload);
		assertEquals("ERROR", responsePayload.getMessage().getLevel());
		assertEquals("Die Eingaben sind nicht korrekt.", responsePayload.getMessage().getMessage());
		@SuppressWarnings("unchecked")
		Collection<InvalidProperty> invalidProperties = (Collection<InvalidProperty>) responsePayload.getData();
		assertEquals(1, invalidProperties.size());
		InvalidProperty prop = invalidProperties.iterator().next();
		assertEquals("agbGelesen", prop.getName());
		assertEquals("Bitte stimmen Sie den Datenschutzhinweisen zu.", prop.getMessage());
	}

	@Test
	void passwordsNotEqual() {

		// Arrange
		final SignUpCredentials credentials = getValidCredentials();
		credentials.getTwoPasswords().setPasswortWdh("123start");

		// Act + Assert
		final Throwable ex = assertThrows(InvalidInputException.class, () -> {

			validationDelegate.check(credentials, SignUpCredentials.class);
		});
		InvalidInputException e = (InvalidInputException) ex;
		ResponsePayload responsePayload = e.getResponsePayload();
		assertNotNull(responsePayload);
		assertEquals("ERROR", responsePayload.getMessage().getLevel());
		assertEquals("Die Eingaben sind nicht korrekt.", responsePayload.getMessage().getMessage());
		@SuppressWarnings("unchecked")
		Collection<InvalidProperty> invalidProperties = (Collection<InvalidProperty>) responsePayload.getData();
		assertEquals(1, invalidProperties.size());
		InvalidProperty prop = invalidProperties.iterator().next();
		assertEquals("twoPasswords", prop.getName());
		System.out.println("passwordsNotEqual: message='" + prop.getMessage() + "'");
		assertTrue(prop.getMessage().contains("Die Passwörter stimmen nicht überein"));
	}

	@Test
	void multipleViolations() {

		// Arrange
		final SignUpCredentials credentials = getValidCredentials();
		credentials.getTwoPasswords().setPasswortWdh("123start");
		credentials.setKleber("hui");

		// Act + Assert
		final Throwable ex = assertThrows(InvalidInputException.class, () -> {

			validationDelegate.check(credentials, SignUpCredentials.class);
		});
		InvalidInputException e = (InvalidInputException) ex;
		ResponsePayload responsePayload = e.getResponsePayload();
		assertNotNull(responsePayload);
		assertEquals("ERROR", responsePayload.getMessage().getLevel());
		assertEquals("Die Eingaben sind nicht korrekt.", responsePayload.getMessage().getMessage());
		@SuppressWarnings("unchecked")
		List<InvalidProperty> invalidProperties = (List<InvalidProperty>) responsePayload.getData();
		assertEquals(2, invalidProperties.size());

		{

			InvalidProperty prop = invalidProperties.get(0);
			assertEquals("twoPasswords", prop.getName());
			System.out.println("passwordsNotEqual: message='" + prop.getMessage() + "'");
			assertTrue(prop.getMessage().contains("Die Passwörter stimmen nicht überein"));
		}

		{

			InvalidProperty prop = invalidProperties.get(1);
			assertEquals("kleber", prop.getName());
			assertEquals("", prop.getMessage());
		}
	}

	@Test
	void clientCredentialsErforderlich() {

		// Arrange
		final SignUpCredentials credentials = getValidCredentials();
		credentials.setClientCredentials(null);

		// Act + Assert
		final Throwable ex = assertThrows(InvalidInputException.class, () -> {

			validationDelegate.check(credentials, SignUpCredentials.class);
		});
		InvalidInputException e = (InvalidInputException) ex;
		ResponsePayload responsePayload = e.getResponsePayload();
		assertNotNull(responsePayload);
		assertEquals("ERROR", responsePayload.getMessage().getLevel());
		assertEquals("Die Eingaben sind nicht korrekt.", responsePayload.getMessage().getMessage());
		@SuppressWarnings("unchecked")
		Collection<InvalidProperty> invalidProperties = (Collection<InvalidProperty>) responsePayload.getData();
		assertEquals(1, invalidProperties.size());
		InvalidProperty prop = invalidProperties.iterator().next();
		assertEquals("clientCredentials", prop.getName());
		assertEquals("darf nicht null sein", prop.getMessage());
	}

	private SignUpCredentials getValidCredentials() {

		SignUpCredentials result = new SignUpCredentials();
		result.setAgbGelesen(true);
		result.setEmail("bla@eladil.de");
		result.setLoginName("Herbert");
		result.setTwoPasswords(new TwoPasswords("start123", "start123"));

		ClientCredentials clientCredentials = ClientCredentials.createWithState("asghkggd", "localhost:4200", "horst");
		result.setClientCredentials(clientCredentials);

		return result;
	}

}
