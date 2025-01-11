// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.service.mail;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URISyntaxException;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

import de.egladil.web.authprovider.domain.ActivationCode;
import de.egladil.web.authprovider.utils.AuthTimeUtils;
import io.quarkus.test.junit.QuarkusTest;

/**
 * RegistrationEmailStrategyTest
 */
@QuarkusTest
public class RegistrationEmailStrategyTest {

	@Test
	void mailtextNotEmpty() throws URISyntaxException {

		// Arrange
		String accountActivationUrl = "http://localhost:9000/authprovider/api/registration/confirmation?code=";

		ActivationCode activationCode = new ActivationCode();
		activationCode.setConfirmationCode("skahxa-ashqhoh");
		activationCode.setExpirationTime(AuthTimeUtils.transformFromLocalDateTime(AuthTimeUtils.now().plus(2, ChronoUnit.HOURS)));

		// Act
		DefaultEmailDaten emailDaten = new RegistrationMailStrategy("heike@egladil.de", "günni", activationCode,
			accountActivationUrl).createEmailDaten("RegistrationService");
		String mailtext = emailDaten.getText();

		System.out.println(mailtext);

		// Assert
		assertTrue(mailtext.startsWith("Guten Tag,"));
		assertTrue(mailtext.contains(accountActivationUrl));
		assertEquals("heike@egladil.de", emailDaten.getEmpfaenger());
		assertEquals("Mathe für jung und alt: Aktivierung Benutzerkonto", emailDaten.getBetreff());
	}
}
