// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.service.mail;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URISyntaxException;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

import de.egladil.web.authprovider.domain.Client;
import de.egladil.web.authprovider.domain.TempPassword;
import de.egladil.web.authprovider.utils.AuthTimeUtils;
import io.quarkus.test.junit.QuarkusTest;

/**
 * TempPasswordEmailStrategyTest
 */
@QuarkusTest
public class TempPasswordEmailStrategyTest {

	@Test
	void ohneRedirectUrl() throws URISyntaxException {

		// Arrange
		TempPassword tempPassword = new TempPassword();
		tempPassword.setTokenId("skahxa-ashqhoh");
		tempPassword.setPassword("totalgeh31m");
		tempPassword.setExpiresAt(AuthTimeUtils.transformFromLocalDateTime(AuthTimeUtils.now().plus(1, ChronoUnit.HOURS)));

		// Act
		DefaultEmailDaten emailDaten = new TempPasswordCreatedMailStrategy("heike@egladil.de", tempPassword,
			"http://localhost:4300/password/temp/change?tokenId=").createEmailDaten("TempPassword");
		String mailtext = emailDaten.getText();

		System.out.println(mailtext);

		// Assert
		assertTrue(mailtext.startsWith("Guten Tag,"));
		assertTrue(mailtext.contains("http://localhost:4300/password/temp/change?tokenId=skahxa-ashqhoh"));
		assertTrue(mailtext.contains("totalgeh31m"));
		assertEquals("heike@egladil.de", emailDaten.getEmpfaenger());
		assertEquals("Minikänguru: Einmalpasswort", emailDaten.getBetreff());
	}

	@Test
	void mitRedirectUrl() throws URISyntaxException {

		// Arrange
		TempPassword tempPassword = new TempPassword();
		tempPassword.setTokenId("skahxa-ashqhoh");
		tempPassword.setPassword("totalgeh31m");
		tempPassword.setExpiresAt(AuthTimeUtils.transformFromLocalDateTime(AuthTimeUtils.now().plus(1, ChronoUnit.HOURS)));

		Client client = new Client();
		client.setId(29l);
		client.setClientId("yxvhkcshl");
		client.setBaseUrl("localhost:4200");
		tempPassword.setClient(client);

		// Act
		DefaultEmailDaten emailDaten = new TempPasswordCreatedMailStrategy("heike@egladil.de", tempPassword,
			"http://localhost:4300/password/temp/change?tokenId=").createEmailDaten("TempPassword");
		String mailtext = emailDaten.getText();

		System.out.println(mailtext);

		// Assert
		assertTrue(mailtext.startsWith("Guten Tag,"));
		assertTrue(mailtext.contains("http://localhost:4300/password/temp/change?tokenId=skahxa-ashqhoh"));
		assertTrue(mailtext.contains("totalgeh31m"));
		assertEquals("heike@egladil.de", emailDaten.getEmpfaenger());
		assertEquals("Minikänguru: Einmalpasswort", emailDaten.getBetreff());
	}

}
