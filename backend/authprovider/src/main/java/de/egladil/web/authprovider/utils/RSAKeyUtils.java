// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.utils;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Security;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import de.egladil.web.commons_net.file.CommonFileUtils;

/**
 * RSAKeyUtils
 */
public final class RSAKeyUtils {

	/**
	 * Erzeugt eine Instanz von RSAKeyUtils
	 */
	private RSAKeyUtils() {

		Security.addProvider(new BouncyCastleProvider());
	}

	/**
	 * Liest den PublicKey aus einem File.
	 *
	 * @param  pathToKeyFile
	 *                                  String
	 * @return                          PublicKey
	 * @throws IOException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 * @throws Exception
	 */
	public static PublicKey getPublicKeyRSA(final String pathToKeyFile) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

		byte[] keyBytes = CommonFileUtils.readBytes(new File(pathToKeyFile));
		return getPublicKeyRSA(keyBytes);
	}

	/**
	 * Wandelt die bytes in den PublicKey um.
	 *
	 * @param  keyBytes
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws IOException
	 */
	public static PublicKey getPublicKeyRSA(final byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {

		try (StringReader sr = new StringReader(new String(keyBytes)); PemReader pemReader = new PemReader(sr)) {

			PemObject pem = pemReader.readPemObject();
			byte[] pubKeyBytes = pem.getContent();
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(pubKeyBytes);
			RSAPublicKey pubKey = (RSAPublicKey) keyFactory.generatePublic(pubSpec);
			return pubKey;
		}
	}

}
