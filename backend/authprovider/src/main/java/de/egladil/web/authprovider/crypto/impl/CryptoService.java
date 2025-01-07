// =====================================================
// Projekt: commons-crypto
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.crypto.impl;

import java.security.SecureRandom;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import de.egladil.web.authprovider.error.CommonCryptoException;
import jakarta.enterprise.context.RequestScoped;

/**
 * CryptoService Wrapper für Apache Shiro
 */
@RequestScoped
public class CryptoService {

	/**
	 * Erzeugt eine Instanz von CryptoService
	 */
	public CryptoService() {

		Security.addProvider(new BouncyCastleProvider());
	}

	public String generateRandomString(final String algorithm, final int length, final char[] charPool) {

		try {

			SecureRandom secureRandom = SecureRandom.getInstance(algorithm);
			// nach ESAPI
			StringBuilder sb = new StringBuilder();

			for (int loop = 0; loop < length; loop++) {

				int index = secureRandom.nextInt(charPool.length);
				sb.append(charPool[index]);
			}
			return sb.toString();
		} catch (final Exception e) {

			throw new CommonCryptoException("Fehler beim generieren eines Zufallsstrings: " + e.getMessage(), e);
		}
	}
}
