// =====================================================
// Projekt: authenticationprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.crypto.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.util.SimpleByteSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.egladil.web.authprovider.config.PasswordConfig;
import de.egladil.web.authprovider.domain.Client;
import de.egladil.web.authprovider.domain.LoginSecrets;
import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.authprovider.domain.Salt;
import de.egladil.web.authprovider.error.AuthException;
import de.egladil.web.commons_crypto.CryptoService;
import de.egladil.web.commons_crypto.impl.CryptoServiceImpl;

/**
 * AuthCryptoServiceImplTest
 */
public class AuthCryptoServiceImplTest {

	private AuthCryptoServiceImpl authCryptoService;

	private CryptoService cryptoService;

	private PasswordConfig passwordConfig;

	@BeforeEach
	void setUp() {

		this.cryptoService = new CryptoServiceImpl();

		passwordConfig = new PasswordConfig();
		passwordConfig.setCryptoAlgorithm("SHA-256");
		passwordConfig.setIterations(4098);
		passwordConfig.setPepper("z0eiPZVJxq/xhYD1RkXACJMKqtmzMQQ9blaR+ozXMk8=");
		passwordConfig.setRandomAlgorithm("SHA1PRNG");
		passwordConfig.setTempPwdCharPool("abcdefghijklmnopqrstuvwxyz0123456789");
		passwordConfig.setTempPwdLength(8);

		authCryptoService = new AuthCryptoServiceImpl(passwordConfig, cryptoService);
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
	@DisplayName("should hash print password and salt")
	void hashAndVerifyClientSecret() {

		System.out.println(passwordConfig.toString());

		String str = "start123";
		final char[] pwd = str.toCharArray();

		final Hash computedHash = authCryptoService.hashPassword(pwd);

		for (char c : pwd) {

			assertEquals(0x00, c);
		}

		final String persistableSalt = computedHash.getSalt().toBase64();
		final String persistablePwd = new SimpleByteSource(computedHash.getBytes()).toBase64();

		System.out.println("str = " + str);
		System.out.println("persistablePwd = " + persistablePwd);
		System.out.println("persistableSalt = " + persistableSalt);

		Salt salt = new Salt();
		salt.setIterations(passwordConfig.getIterations());
		salt.setAlgorithmName(passwordConfig.getCryptoAlgorithm());
		salt.setWert(persistableSalt);

		LoginSecrets loginSecrets = new LoginSecrets();
		loginSecrets.setPasswordhash(persistablePwd);
		loginSecrets.setSalt(salt);

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
	@DisplayName("should hash and verify password")
	void hashAndVerifyPassword() {

		final char[] pwd = "Gehe1m".toCharArray();

		final Hash computedHash = authCryptoService.hashPassword(pwd);

		for (char c : pwd) {

			assertEquals(0x00, c);
		}

		final String persistableSalt = computedHash.getSalt().toBase64();
		final String persistablePwd = new SimpleByteSource(computedHash.getBytes()).toBase64();

		System.out.println("persistablePwd = " + persistablePwd);
		System.out.println("persistableSalt = " + persistableSalt);

		Salt salt = new Salt();
		salt.setIterations(passwordConfig.getIterations());
		salt.setAlgorithmName(passwordConfig.getCryptoAlgorithm());
		salt.setWert(persistableSalt);

		LoginSecrets loginSecrets = new LoginSecrets();
		loginSecrets.setPasswordhash(persistablePwd);
		loginSecrets.setSalt(salt);

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
	@DisplayName("should hash and verify password")
	void atuthFailure() {

		final char[] pwd = "Gehe1m".toCharArray();

		final Hash computedHash = authCryptoService.hashPassword(pwd);

		final String persistableSalt = computedHash.getSalt().toBase64();
		final String persistablePwd = new SimpleByteSource(computedHash.getBytes()).toBase64();

		System.out.println("persistablePwd = " + persistablePwd);
		System.out.println("persistableSalt = " + persistableSalt);

		Salt salt = new Salt();
		salt.setIterations(passwordConfig.getIterations());
		salt.setAlgorithmName(passwordConfig.getCryptoAlgorithm());
		salt.setWert(persistableSalt);

		LoginSecrets loginSecrets = new LoginSecrets();
		loginSecrets.setPasswordhash(persistablePwd);
		loginSecrets.setSalt(salt);

		ResourceOwner resourceOwner = new ResourceOwner();
		resourceOwner.setAktiviert(true);
		resourceOwner.setLoginSecrets(loginSecrets);
		resourceOwner.setLoginName("klaus-dieter");
		resourceOwner.setEmail("kd@web.de");

		try {

			authCryptoService.verifyPassword("gehe1m".toCharArray(), resourceOwner);
			fail("keine AuthException");
		} catch (AuthException e) {

			assertEquals("Das hat leider nicht geklappt: falsche Loginname/Email - Passwort - Kombination", e.getMessage());
		}
	}

	@Override
	public String toString() {

		return "AuthCryptoServiceImplTest [authCryptoService=" + authCryptoService + ", cryptoService=" + cryptoService
			+ ", passwordConfig=" + passwordConfig + "]";
	}
}
