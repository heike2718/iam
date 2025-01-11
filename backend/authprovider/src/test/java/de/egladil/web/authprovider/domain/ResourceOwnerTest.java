// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.web.authprovider.entities.LoginSecrets;
import de.egladil.web.authprovider.entities.ResourceOwner;
import io.quarkus.test.junit.QuarkusTest;

/**
 * ResourceOwnerTest
 */
@QuarkusTest
public class ResourceOwnerTest {

	@Test
	void deserialize() throws IOException {

		try (InputStream in = getClass().getResourceAsStream("/domain/data.json")) {

			ResourceOwner resourceOwner = new ObjectMapper().readValue(in, ResourceOwner.class);

			assertEquals("4992cb3a-361d-4d83-86c3-d448ffe0a10e", resourceOwner.getUuid());
			LoginSecrets loginSecrets = resourceOwner.getLoginSecrets();
			assertNotNull(loginSecrets);
			assertNotNull(loginSecrets.getSalt());

		}

	}

}
