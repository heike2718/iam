// =====================================================
// Projekt: authenticationprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.crypto.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.egladil.web.authprovider.config.PasswordConfig;
import de.egladil.web.authprovider.domain.CryptoAlgorithm;
import de.egladil.web.authprovider.entities.Client;
import de.egladil.web.authprovider.entities.LoginSecrets;
import de.egladil.web.authprovider.entities.ResourceOwner;
import de.egladil.web.authprovider.error.AuthException;

/**
 * AuthCryptoServiceImplTest
 */
public class AuthCryptoServiceImplTest {

	private AuthCryptoServiceImpl authCryptoService;

	private CryptoService cryptoService;

	private PasswordConfig passwordConfig;

	@BeforeEach
	void setUp() {

		this.cryptoService = new CryptoService();

		passwordConfig = new PasswordConfig();
		passwordConfig.setCryptoAlgorithm("SHA-256");
		passwordConfig.setIterations(4098);
		passwordConfig.setPepper("z0eiPZVJxq/xhYD1RkXACJMKqtmzMQQ9blaR+ozXMk8=");
		passwordConfig.setRandomAlgorithm("SHA1PRNG");
		passwordConfig.setTempPwdCharPool("abcdefghijklmnopqrstuvwxyz0123456789");
		passwordConfig.setTempPwdLength(8);

		authCryptoService = new AuthCryptoServiceImpl();
		authCryptoService.setCryptoService(cryptoService);
		authCryptoService.setPasswordConfig(passwordConfig);
		authCryptoService.setStageForTest("qs");
	}

	@Test
	void generateClientID() {

		// Act
		String clientID = authCryptoService.generateClientID();

		// Assert
		assertEquals(44, clientID.length());

		System.out.println(clientID);
	}

	@Test
	void hashAndVerifyClientSecret() {

		System.out.println(passwordConfig.toString());

		String str = "start123";
		final char[] pwd = str.toCharArray();

		final String computedHash = authCryptoService.hashPassword(pwd);

		for (char c : pwd) {

			assertEquals(0x00, c);
		}

		System.out.println("str = " + str);
		System.out.println("persistablePwd = " + computedHash);

		LoginSecrets loginSecrets = new LoginSecrets();
		loginSecrets.setPasswordhash(computedHash);
		loginSecrets.setCryptoAlgorithm(CryptoAlgorithm.ARGON2);

		Client client = new Client();
		client.setClientId("GerMkzlT2moZq762D5zKAorpg8aUjumXzNQz2yOUd9zQ");
		client.setLoginSecrets(loginSecrets);

		// Act + Assert (wenn korrekt, keine Exception)

		char[] pwd2 = "start123".toCharArray();
		authCryptoService.verifyClientSecret(pwd2, client);

		for (char c : pwd) {

			assertEquals(0x00, c);
		}
	}

	@Test
	void hashAndVerifyPassword() {

		final char[] pwd = "Gehe1m".toCharArray();

		final String computedHash = authCryptoService.hashPassword(pwd);

		for (char c : pwd) {

			assertEquals(0x00, c);
		}

		System.out.println("computedHash = " + computedHash);

		LoginSecrets loginSecrets = new LoginSecrets();
		loginSecrets.setPasswordhash(computedHash);
		loginSecrets.setCryptoAlgorithm(CryptoAlgorithm.ARGON2);

		ResourceOwner resourceOwner = new ResourceOwner();
		resourceOwner.setAktiviert(true);
		resourceOwner.setLoginSecrets(loginSecrets);
		resourceOwner.setLoginName("klaus-dieter");
		resourceOwner.setEmail("kd@web.de");

		// Act + Assert (wenn korrrekt, keine Exception

		char[] pwd2 = "Gehe1m".toCharArray();
		authCryptoService.verifyPassword(pwd2, resourceOwner);

		for (char c : pwd2) {

			assertEquals(0x00, c);
		}
	}

	@Test
	void test_authException() {

		final char[] pwd = "Gehe1m".toCharArray();

		final String computedHash = authCryptoService.hashPassword(pwd);

		LoginSecrets loginSecrets = new LoginSecrets();
		loginSecrets.setPasswordhash(computedHash);
		loginSecrets.setCryptoAlgorithm(CryptoAlgorithm.ARGON2);

		ResourceOwner resourceOwner = new ResourceOwner();
		resourceOwner.setAktiviert(true);
		resourceOwner.setLoginSecrets(loginSecrets);
		resourceOwner.setLoginName("klaus-dieter");
		resourceOwner.setEmail("kd@web.de");

		try {

			authCryptoService.verifyPassword("gehe1m".toCharArray(), resourceOwner);
			fail("keine AuthException");
		} catch (AuthException e) {

			assertEquals(
				"Das hat leider nicht geklappt: falsche Loginname/Email - Passwort - Kombination oder noch nicht aktiviertes Benutzerkonto. Bestehen die Probleme weiterhin, senden Sie bitte eine Mail.",
				e.getMessage());
		}
	}

	@Override
	public String toString() {

		return "AuthCryptoServiceImplTest [authCryptoService=" + authCryptoService + ", cryptoService=" + cryptoService
			+ ", passwordConfig=" + passwordConfig + "]";
	}
}
