// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.service.mail;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.egladil.web.authprovider.domain.ActivationCode;
import de.egladil.web.commons_mailer.DefaultEmailDaten;
import de.egladil.web.commons_net.time.CommonTimeUtils;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.UriInfo;

/**
 * RegistrationEmailStrategyTest
 */
@QuarkusTest
public class RegistrationEmailStrategyTest {

	@Test
	void mailtextNotEmpty() throws URISyntaxException {

		// Arrange
		URI value = new URI("http://localhost:9000/authprovider");
		UriInfo uriInfo = Mockito.mock(UriInfo.class);
		Mockito.when(uriInfo.getBaseUri()).thenReturn(value);

		ActivationCode activationCode = new ActivationCode();
		activationCode.setConfirmationCode("skahxa-ashqhoh");
		activationCode
			.setExpirationTime(CommonTimeUtils.transformFromLocalDateTime(CommonTimeUtils.now().plus(2, ChronoUnit.HOURS)));

		// Act
		DefaultEmailDaten emailDaten = new RegistrationMailStrategy("heike@egladil.de", "günni", activationCode, uriInfo)
			.createEmailDaten("RegistrationService");
		String mailtext = emailDaten.getText();

		// Assert
		assertTrue(mailtext.startsWith("Guten Tag,"));
		assertTrue(mailtext.contains("http://localhost:9000/authprovider/registration/confirmation?code=skahxa-ashqhoh"));
		assertEquals("heike@egladil.de", emailDaten.getEmpfaenger());
		assertEquals("Mathe für jung und alt: Aktivierung Benutzerkonto", emailDaten.getBetreff());
	}
}
