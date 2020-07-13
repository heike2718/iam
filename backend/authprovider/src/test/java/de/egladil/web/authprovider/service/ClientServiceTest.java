//=====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
//=====================================================

package de.egladil.web.authprovider.service;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

import de.egladil.web.authprovider.error.InvalidRedirectUrl;

/**
* ClientServiceTest
*/
public class ClientServiceTest {

	@Test
	void checkRedirectUrlsSuccessMitProtokollHttp() {
		// Arrange
		String allowedRedirectUrls = "localhost:4200/listen,localhost:4200";
		String redirectUrl = "http://localhost:4200";
		ClientService clientService = new ClientService();

		// Act
		clientService.checkRedirectUrl(allowedRedirectUrls, redirectUrl);
	}

	@Test
	void checkRedirectUrlsSuccessMitProtokollHttps() {
		// Arrange
		String allowedRedirectUrls = "localhost:4200/listen,localhost:4200";
		String redirectUrl = "https://localhost:4200";
		ClientService clientService = new ClientService();

		// Act
		clientService.checkRedirectUrl(allowedRedirectUrls, redirectUrl);
	}

	@Test
	void checkRedirectUrlsSuccessOhneProtokoll() {
		// Arrange
		String allowedRedirectUrls = "localhost:4200/listen,localhost:4200";
		String redirectUrl = "localhost:4200";
		ClientService clientService = new ClientService();

		// Act
		clientService.checkRedirectUrl(allowedRedirectUrls, redirectUrl);
	}

	@Test
	void checkRedirectUrlsSuccessOhneProtokollWithTailingSlash() {
		// Arrange
		String allowedRedirectUrls = "localhost:4200/listen,localhost:4200";
		String redirectUrl = "localhost:4200/";
		ClientService clientService = new ClientService();

		// Act
		clientService.checkRedirectUrl(allowedRedirectUrls, redirectUrl);
	}

	@Test
	void checkRedirectUrlsFailure() {
		// Arrange
		String allowedRedirectUrls = "localhost:4200/listen,localhost:4200";
		String redirectUrl = "http://localhost:4200/guenther";
		ClientService clientService = new ClientService();

		// Act + Assert
		try {
			clientService.checkRedirectUrl(allowedRedirectUrls, redirectUrl);
			fail("keine InvalidRedirectUrl");
		} catch (InvalidRedirectUrl e) {
			assertNull(e.getMessage());
		}
	}

}
