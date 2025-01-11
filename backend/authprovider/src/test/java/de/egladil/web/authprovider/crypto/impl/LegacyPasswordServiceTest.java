// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.crypto.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import de.egladil.web.authprovider.config.PasswordConfig;

/**
 * LegacyPasswordServiceTest
 */
public class LegacyPasswordServiceTest {

	@Test
	void testClientSecretBenutzerprofil() {

		// Arrange
		String persistedSaltValue = "eFMwUWp5cTdad2t1NnpzZGtYQjIvUT09";
		String persistedPasswordHash = "PuDq/o0PkklF+yhrZqr2R1CrILBjh21NlirmmV1t7oA=";

		char[] password = "start123".toCharArray();

		PasswordConfig passwordConfig = new PasswordConfig();
		passwordConfig.setCryptoAlgorithm("SHA-256");
		passwordConfig.setIterations(4098);
		passwordConfig.setPepper("z0eiPZVJxq/xhYD1RkXACJMKqtmzMQQ9blaR+ozXMk8=");
		passwordConfig.setRandomAlgorithm("SHA1PRNG");
		passwordConfig.setTempPwdCharPool("abcdefghijklmnopqrstuvwxyz0123456789");
		passwordConfig.setTempPwdLength(8);

		LegacyPasswordService legacyPasswordService = new LegacyPasswordService(passwordConfig);

		// act
		boolean matches = legacyPasswordService.verifyPassword(password, persistedPasswordHash, persistedSaltValue);

		assertTrue(matches);

	}

}
