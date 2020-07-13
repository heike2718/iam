// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.payload;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.web.commons_validation.payload.TwoPasswords;

/**
 * SignUpCredentialsTest
 */
public class SignUpCredentialsTest {

	@Test
	void serialize() throws JsonProcessingException {

		// Arrange
		SignUpCredentials cred = new SignUpCredentials();
		ClientCredentials clientCredentials = ClientCredentials.createWithState("ahsgagsgdga",
			"http://localhost:4200", "horst");
		cred.setAgbGelesen(true);
		cred.setClientCredentials(clientCredentials);
		cred.setEmail("zezeze@egladil.de");
		cred.setLoginName("zezeze");
		cred.setVorname("Jane");
		cred.setNachname("Doe");
		cred.setNonce("GT543ERG");

		cred.setTwoPasswords(new TwoPasswords("start123", "start123"));

		// Act + Assert
		System.out.println(new ObjectMapper().writeValueAsString(cred));
	}

	@Test
	void deserialize() throws JsonParseException, JsonMappingException, IOException {

		// Arrange
		String json = "{\"email\":\"zezeze@egladil.de\",\"loginName\":\"zezeze\",\"vorname\":\"Jane\",\"nachname\":\"Doe\",\"twoPasswords\":{\"passwort\":\"start123\",\"passwortWdh\":\"start123\"},\"agbGelesen\":true,\"clientCredentials\":{\"accessToken\":\"ahsgagsgdga\",\"redirectUrl\":\"http://localhost:4200\",\"state\":\"horst\"},\"kleber\":null}";

		// Act
		SignUpCredentials cred = new ObjectMapper().readValue(json, SignUpCredentials.class);

		// Assert
		final ClientCredentials clientCredentials = cred.getClientCredentials();
		assertNotNull(clientCredentials);
		assertEquals("ahsgagsgdga", clientCredentials.getAccessToken());
		assertEquals("http://localhost:4200", clientCredentials.getRedirectUrl());
		assertEquals("horst", clientCredentials.getState());

		TwoPasswords twoPasswords = cred.getTwoPasswords();
		assertNotNull(twoPasswords);
		assertEquals("start123", twoPasswords.getPasswort());
		assertEquals("start123", twoPasswords.getPasswortWdh());
	}

}
