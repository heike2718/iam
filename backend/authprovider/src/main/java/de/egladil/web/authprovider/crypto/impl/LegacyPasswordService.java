// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.crypto.impl;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.crypto.hash.SimpleHashRequest;
import org.apache.shiro.lang.util.ByteSource;
import org.apache.shiro.lang.util.SimpleByteSource;

import de.egladil.web.authprovider.config.PasswordConfig;

/**
 * LegacyPasswordService
 */
public class LegacyPasswordService {

	private final PasswordConfig passwordConfig;

	public LegacyPasswordService(final PasswordConfig passwordConfig) {

		super();
		this.passwordConfig = passwordConfig;
	}

	public boolean verifyPassword(final char[] password, final String persistentHashValue, final String persistentSalt) {

		ByteSource bsPwd = ByteSource.Util.bytes(new String(password));
		ByteSource bsSalt = ByteSource.Util.bytes(Base64.getDecoder().decode(persistentSalt));

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("SimpleHash.iterations", Integer.valueOf(passwordConfig.getIterations()));

		HashRequest hashRequest = new SimpleHashRequest(passwordConfig.getCryptoAlgorithm(), bsPwd, bsSalt, parameters);
		Hash expectedHash = this.computeHash(hashRequest);

		final String expectedHashValue = new SimpleByteSource(expectedHash.getBytes()).toBase64();

		if (MessageDigest.isEqual(expectedHashValue.getBytes(), persistentHashValue.getBytes())) {

			return true;
		}
		return false;
	}

	private Hash computeHash(final HashRequest request) {

		if (request == null || request.getSource() == null || request.getSource().isEmpty()) {

			return null;
		}

		ByteSource source = request.getSource();

		ByteSource publicSalt = request.getSalt().get();
		ByteSource privateSalt = new SimpleByteSource(passwordConfig.getPepper());
		ByteSource combinedPepperAndSalt = combinePepperAndSalt(privateSalt, publicSalt);

		Hash computed = new SimpleHash(passwordConfig.getCryptoAlgorithm(), source, combinedPepperAndSalt,
			passwordConfig.getIterations());

		SimpleHash result = new SimpleHash(passwordConfig.getCryptoAlgorithm());
		result.setBytes(computed.getBytes());
		result.setIterations(passwordConfig.getIterations());
		// Only expose the public salt - not the real/combined salt that might have been used:
		result.setSalt(publicSalt);

		return result;
	}

	private ByteSource combinePepperAndSalt(final ByteSource pepper, final ByteSource publicSalt) {

		byte[] privateSaltBytes = pepper != null ? pepper.getBytes() : null;
		int privateSaltLength = privateSaltBytes != null ? privateSaltBytes.length : 0;

		byte[] publicSaltBytes = publicSalt != null ? publicSalt.getBytes() : null;
		int extraBytesLength = publicSaltBytes != null ? publicSaltBytes.length : 0;

		int length = privateSaltLength + extraBytesLength;

		if (length <= 0) {

			return null;
		}

		byte[] combined = new byte[length];

		int i = 0;

		for (int j = 0; j < privateSaltLength; j++) {

			assert privateSaltBytes != null;
			combined[i++] = privateSaltBytes[j];
		}

		for (int j = 0; j < extraBytesLength; j++) {

			assert publicSaltBytes != null;
			combined[i++] = publicSaltBytes[j];
		}

		return ByteSource.Util.bytes(combined);
	}

}
