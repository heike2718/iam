// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.service;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.egladil.web.authprovider.error.InvalidRedirectUrl;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

/**
 * ClientServiceTest
 */
@QuarkusTest
public class ClientServiceTest {

	@Inject
	ClientService clientService;

	@Test
	void checkRedirectUrlsSuccessMitProtokollHttp() {

		// Arrange
		String allowedRedirectUrls = "localhost:4200/listen,localhost:4200";
		String redirectUrl = "http://localhost:4200";

		// Act
		clientService.checkRedirectUrl(allowedRedirectUrls, redirectUrl);
	}

	@Test
	void checkRedirectUrlsSuccessMitProtokollHttps() {

		// Arrange
		String allowedRedirectUrls = "localhost:4200/listen,localhost:4200";
		String redirectUrl = "https://localhost:4200";

		// Act
		clientService.checkRedirectUrl(allowedRedirectUrls, redirectUrl);
	}

	@Test
	void checkRedirectUrlsSuccessOhneProtokoll() {

		// Arrange
		String allowedRedirectUrls = "localhost:4200/listen,localhost:4200";
		String redirectUrl = "localhost:4200";

		// Act
		clientService.checkRedirectUrl(allowedRedirectUrls, redirectUrl);
	}

	@Test
	void checkRedirectUrlsSuccessOhneProtokollWithTailingSlash() {

		// Arrange
		String allowedRedirectUrls = "localhost:4200/listen,localhost:4200";
		String redirectUrl = "localhost:4200/";

		// Act
		clientService.checkRedirectUrl(allowedRedirectUrls, redirectUrl);
	}

	@Test
	void checkRedirectUrlsFailure() {

		// Arrange
		String allowedRedirectUrls = "localhost:4200/listen,localhost:4200";
		String redirectUrl = "http://localhost:4200/guenther";

		// Act + Assert
		try {

			clientService.checkRedirectUrl(allowedRedirectUrls, redirectUrl);
			fail("keine InvalidRedirectUrl");
		} catch (InvalidRedirectUrl e) {

			assertNull(e.getMessage());
		}
	}

	@Test
	void testUpdateAllClientSecrets() {

		List<String> clientIds = Arrays
			.asList(new String[] { "4O2UEGhpdGvhJt0Fk3aHkxS078jBsLlBf6XZ6BgR31cj", "k7AxUVYzr1FBAvD8e99orRqKqx4jBwcr7Dmgn5jdBf8J",
				"N7SsGenun4znNUdQzyLD0wzOfRHOmc9XN35TOGfbBcvA", "NBptB82KjFkelkF55Aq4SmQSL3DXZHHurbe7l5W9LT7U",
				"Pou3AonyzNXEA3vGCf1OpPWGLKXbHt4Rdgu7dsi6IiHB", "T73pF0WTZLuFTv0nbXgqIiXAxyl935c4WCBNwq32uvfQ",
				"um6uonyq9u6wfz2xbmsr681qetn8sk7me2kcr6vknhqx", "WLJLH4vsldWapZrMZi2U5HKRBVpgyUiRTWwX7aiJd8nX" });

		String clientSecret = "start123";

		for (String clientId : clientIds) {
			clientService.resetClientSecret(clientId, clientSecret);
			System.out.println("clientId " + clientId + " fertig");
		}
	}

}
