// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.crypto.JWTService;
import de.egladil.web.authprovider.domain.Client;
import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.authprovider.error.ClientAccessTokenNotFoundException;
import de.egladil.web.authprovider.payload.ClientCredentials;
import de.egladil.web.authprovider.payload.SignUpLogInResponseData;
import io.quarkus.test.junit.QuarkusTest;

/**
 * AuthorizationServiceTest
 */
@QuarkusTest
public class AuthorizationServiceTest {

	private static final Logger LOG = LoggerFactory.getLogger(AuthorizationServiceTest.class);

	private static final String ACCESS_TOKEN = "ashdhqoHIDHIP";

	private String privateKeyLocation = "/home/heike/.keystore/private/authprov_private_key.pem";

	private String publicKeyLocation = "/home/heike/.keystore/public/authprov_public_key.pem";

	private ClientCredentials clientCredentials;

	private ClientService clientService;

	private AuthorizationService service;

	@BeforeEach
	void setUp() throws ClientAccessTokenNotFoundException {

		Security.addProvider(new BouncyCastleProvider());

		clientCredentials = ClientCredentials.createWithState(ACCESS_TOKEN, "localhost:4200", "login");

		clientService = Mockito.mock(ClientService.class);

		service = new AuthorizationService(clientService, new JWTService(privateKeyLocation, publicKeyLocation));
	}

	@Test
	void testLoginWithoutName() throws ClientAccessTokenNotFoundException {

		// Arrange
		ResourceOwner resourceOwner = new ResourceOwner();
		resourceOwner.setUuid("4d8ed03a-575a-442e-89f4-0e54e51dd0d8");
		Client client = new Client();

		client.setJwtExpirationMinutes(876000);
		// 100 Jahre
		// client.setJwtExpirationMinutes(10512000);

		Mockito.when(clientService.findAndCheckClient(clientCredentials)).thenReturn(client);

		// Act
		SignUpLogInResponseData responseData = service.createAuthorization(resourceOwner, clientCredentials, "");

		// Assert
		String jwt = responseData.getIdToken();
		LOG.info("JWT=\n" + jwt);

		assertEquals("login", responseData.getState());
	}

	@Test
	void testLoginWithName() throws ClientAccessTokenNotFoundException {

		// Arrange
		ResourceOwner resourceOwner = new ResourceOwner();
		resourceOwner.setUuid("4d8ed03a-575a-442e-89f4-0e54e51dd0d8");
		resourceOwner.setVorname("Max");
		resourceOwner.setNachname("Mustermann");

		Client client = new Client();
		client.setJwtExpirationMinutes(1);
		client.setVornameNachnameRequired(true);

		Mockito.when(clientService.findAndCheckClient(clientCredentials)).thenReturn(client);

		// Act
		SignUpLogInResponseData responseData = service.createAuthorization(resourceOwner, clientCredentials, "");

		// Assert
		String jwt = responseData.getIdToken();

		LOG.info("JWT=\n" + jwt);

		assertEquals("login", responseData.getState());
	}

	@Test
	void testLoginNameRequiredAberKeinNameVorhanden() throws ClientAccessTokenNotFoundException {

		// Arrange
		ResourceOwner resourceOwner = new ResourceOwner();
		resourceOwner.setUuid("4d8ed03a-575a-442e-89f4-0e54e51dd0d8");

		Client client = new Client();
		client.setJwtExpirationMinutes(5);
		client.setVornameNachnameRequired(true);

		Mockito.when(clientService.findAndCheckClient(clientCredentials)).thenReturn(client);

		// Act
		SignUpLogInResponseData responseData = service.createAuthorization(resourceOwner, clientCredentials, "");

		// Assert
		String jwt = responseData.getIdToken();

		LOG.info("JWT=\n" + jwt);

		assertEquals("login", responseData.getState());
	}

}
