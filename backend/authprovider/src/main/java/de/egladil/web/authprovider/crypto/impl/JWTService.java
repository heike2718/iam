// =====================================================
// Project: commons-crypto
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.crypto.impl;

import java.io.IOException;
import java.io.StringReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import de.egladil.web.authprovider.error.CommonCryptoException;
import jakarta.enterprise.context.RequestScoped;

/**
 * JWTService
 */
@RequestScoped
public class JWTService {

	public DecodedJWT verify(final String token, final byte[] publicKeyData) throws JWTVerificationException, CommonCryptoException {

		try {

			PublicKey publicKey = getPublicKeyRSA(publicKeyData);

			Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) publicKey, null);

			JWTVerifier verifier = JWT.require(algorithm).acceptLeeway(1).acceptExpiresAt(5).withIssuer("heike2718/authprovider")
				.build();

			DecodedJWT result = verifier.verify(token);

			return result;

		} catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {

			throw new CommonCryptoException("Fehler beim Verifizieren eines JWT: " + e.getMessage(), e);

		}
	}

	private PublicKey getPublicKeyRSA(final byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {

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
