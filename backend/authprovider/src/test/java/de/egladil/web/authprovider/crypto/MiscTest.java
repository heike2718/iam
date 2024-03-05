// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.crypto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import io.quarkus.test.junit.QuarkusTest;

/**
 * MiscTest
 */
@QuarkusTest
public class MiscTest {

	private static final Logger LOG = LoggerFactory.getLogger(MiscTest.class);

	private static final String EXTREMELY_LONG_LIVED_JWT = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiI0ZDhlZDAzYS01NzVhLTQ0MmUtODlmNC0wZTU0ZTUxZGQwZDgiLCJmdWxsX25hbWUiOiJNYXggTXVzdGVybWFubiIsImlzcyI6ImhlaWtlMjcxOC9hdXRocHJvdmlkZXIiLCJleHAiOjMxNzAyOTIyMDIsImlhdCI6MTU5MzQ5MjIwMn0.AxqHzdqf_4FKs-1ek3WLdhwuhk2LyzGOh6qPjYObg-1M5U-9zZWPqsscuOBafGgIsOU9lWZ61gmb73CLmCJ0GXkimOibBBSsUniWSXV95LIN_KZ7v6IrmW_-qoENYiegRQDGYh2YR9KdzKhSdGyg8t-SAGKYDYrZYoJZP3NOOU5y6b6lg0FcW4_ePPnGv0yP8jisbzSPsT3DHa41SrUcmZ65YOum4_xoKJgdZipKgZF31lptR5s7Zk9OaaeppxePcBlfn4ahp1bQIfyxpbmBX9TfKMsQyAs46XIP878HqAC_yO1_qR1U8minwZc5D1JMfhzrAiILyQjDIfScdIE-_w";

	private static final String EXPIRED_JWT = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiI0ZDhlZDAzYS01NzVhLTQ0MmUtODlmNC0wZTU0ZTUxZGQwZDgiLCJmdWxsX25hbWUiOiJNYXggTXVzdGVybWFubiIsImlzcyI6ImhlaWtlMjcxOC9hdXRocHJvdmlkZXIiLCJleHAiOjE1OTM0OTI0MTcsImlhdCI6MTU5MzQ5MjM1N30.GbuPm5HffNmLGFw4BxX2a1dBVLTDspODFOG7IX7lvBlxVNPsDvYqZhP1KvcIPKr44J-JAqaCYchY27Wr30DJni_EXej54BWwVgjnxywmQUTeH6LxEz2sZdF7Vnw45XeNqXqBziNEVQKhXsVunndN2aNWGyV3QpC5rdjZCNpASVpqGs0tB18pnZ3_xruVgnOwx_uSUmWPMUciBexlkijptsuXT33LBDLK54f7PjPzJ-ruSN_f-wz4ijEGo9Cp_P9VtgmVIxvpBI296oEimEg0jybmeULDBsWZB-wOqzSiOOvqFa4GlmFI0KO56IqXf2nmZvdNa-Rpv8RkdYHyaf2odw";

	@BeforeEach
	void setUp() {

		Security.addProvider(new BouncyCastleProvider());
	}

	@Test
	void readPemFilePrivate() throws Exception {

		// Arrange
		String path = "/home/heike/.keystore/private/authprov_private_key.pem";

		// Act
		PemFile pemFile = new PemFile(path);

		// Assert
		assertNotNull(pemFile.getPemObject());

		KeyFactory factory = KeyFactory.getInstance("RSA", "BC");

		byte[] content = pemFile.getPemObject().getContent();
		PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(content);

		final PrivateKey privateKey = factory.generatePrivate(privKeySpec);

		assertTrue(privateKey instanceof RSAPrivateKey);

		LOG.info(String.format("Instantiated private key: %s", privateKey));

	}

	@Test
	void readPemFilePublic() throws Exception {

		// Act + Assert
		final PublicKey publicKey = this.getPublicKey();

		LOG.info(String.format("Instantiated p key: %s", publicKey));
	}

	private RSAPublicKey getPublicKey() throws Exception {

		String path = "/home/heike/.keystore/public/authprov_public_key.pem";

		// Act
		PemFile pemFile = new PemFile(path);

		// Assert
		assertNotNull(pemFile.getPemObject());

		KeyFactory factory = KeyFactory.getInstance("RSA", "BC");

		byte[] content = pemFile.getPemObject().getContent();
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(content);

		return (RSAPublicKey) factory.generatePublic(publicKeySpec);
	}

	@Test
	void verifyJwtSuccess() throws Exception {

		// Arrange
		RSAPublicKey publicKey = getPublicKey();
		RSAPrivateKey privateKey = null;
		Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
		JWTVerifier verifier = JWT.require(algorithm).withIssuer("heike2718/authprovider").build();
		DecodedJWT jwt = verifier.verify(EXTREMELY_LONG_LIVED_JWT);
		assertEquals("heike2718/authprovider", jwt.getIssuer());
	}

	@Test
	void getNameFromJwt() throws Exception {

		// Arrange
		RSAPublicKey publicKey = getPublicKey();
		RSAPrivateKey privateKey = null;
		Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
		JWTVerifier verifier = JWT.require(algorithm).withIssuer("heike2718/authprovider").build();
		DecodedJWT jwt = verifier.verify(EXTREMELY_LONG_LIVED_JWT);
		assertEquals("heike2718/authprovider", jwt.getIssuer());
		assertEquals("Max Mustermann", jwt.getClaim("full_name").asString());
	}

	@Test
	void verifyJwtInvalid() throws Exception {

		// Arrange
		String token = "eyJ0eXAiOilKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJoZWlrZTI3MTgvYXV0aHByb3ZpZGVyIn0.SIlD4UO9qOfV3RgbiJ1Hoah4ny8bmVFCfXb_PvA8_2B2PO-gA6BL8t4Y_O5Z5TuQWOEPq4SczzN9KO4rbaATH_w66mZxmg6Wu3Ec_LBEg9NAyAMz0JueYQ_ApQ_uFeMN8BzTnrGf9AxrQxXamGVcQnAT4yyBntrsHZeSPm7TK9ECc949VHp0ucrKF_r2BXSXUqmb1oHLckUB1kw5bnrxU_5eKfxk_PCcMgVOK-DdlDo9ivt5rtvidjFGrCi6oW1ocjHOP_On4dfT7LsoYbKDW-jnhzkoRl4rGkUQn3GIg_6geGj0aEUNhL93j8pcIaCAx46w4e06F89e9srrXRielg";
		RSAPublicKey publicKey = getPublicKey();
		RSAPrivateKey privateKey = null;

		try {

			Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
			JWTVerifier verifier = JWT.require(algorithm).withIssuer("heike2718/authprovider").build();
			verifier.verify(token);
			fail("keine JWTVerificationException");
		} catch (JWTVerificationException e) {

			assertEquals("The string '{\"typ\":)JWT\",\"alg\":\"RS256\"}' doesn't have a valid JSON format.", e.getMessage());
		}
	}

	@Test
	void verifyJwtExpired() throws Exception {

		// Arrange
		RSAPublicKey publicKey = getPublicKey();
		RSAPrivateKey privateKey = null;

		try {

			Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
			JWTVerifier verifier = JWT.require(algorithm).withIssuer("heike2718/authprovider").build();
			verifier.verify(EXPIRED_JWT);
			fail("keine TokenExpiredException");
		} catch (TokenExpiredException e) {

			assertEquals("The Token has expired on 2020-06-30T04:46:57Z.", e.getMessage());
		}
	}

}
