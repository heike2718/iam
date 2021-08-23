// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.temporal.ChronoUnit;

import javax.enterprise.context.RequestScoped;

import org.apache.commons.io.IOUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;

import de.egladil.web.authprovider.domain.Client;
import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.authprovider.error.AuthConfigurationException;
import de.egladil.web.authprovider.error.AuthCryptoException;
import de.egladil.web.authprovider.payload.JWTPayload;
import de.egladil.web.commons_net.time.CommonTimeUtils;
import de.egladil.web.commons_net.time.TimeInterval;

/**
 * JWTService
 */
@RequestScoped
public class JWTService {

	private static final Logger LOGGER = LoggerFactory.getLogger(JWTService.class);

	private static final String ISSUER = "heike2718/authprovider";

	@ConfigProperty(name = "private-key-location")
	String privateKeyLocation;

	@ConfigProperty(name = "public-key-location")
	String publicKeyLocation;

	/**
	 * Erzeugt eine Instanz von JWTService
	 */
	public JWTService() {

	}

	/**
	 * @param publicKeyLocation
	 */
	public JWTService(final String privateKeyLocation, final String publicKeyLocation) {

		this.privateKeyLocation = privateKeyLocation;
		this.publicKeyLocation = publicKeyLocation;
	}

	/**
	 * Erzeugt für den User ein neues JWT und refreshed das ClintAccessToken.
	 *
	 * @param  resourceOwner
	 * @param  client
	 * @param  clientAccessToken
	 * @return                   JWTPayload
	 */
	public JWTPayload createJWT(final ResourceOwner resourceOwner, final Client client) {

		PrivateKey privateKey = getPrivateKey();

		TimeInterval jwtInterval = CommonTimeUtils.getInterval(CommonTimeUtils.now(), client.getJwtExpirationMinutes(),
			ChronoUnit.MINUTES);

		Algorithm algorithm = Algorithm.RSA256(null, (RSAPrivateKey) privateKey);

		String jwt = null;

		Builder builder = createMinimal(resourceOwner, jwtInterval);

		if (resourceOwner.hasName()) {

			builder = builder.withClaim(Claims.full_name.name(), resourceOwner.getFullName());
		}

		if (resourceOwner.getRoles() != null) {

			String[] roles = resourceOwner.getRoles().split(",");
			builder = builder.withArrayClaim(Claims.groups.name(), roles);
		}

		jwt = builder.sign(algorithm);

		JWTPayload result = new JWTPayload();
		result.setJwt(jwt);
		long expiresAtSeconds = jwtInterval.getEndTime().getTime() / 1000;
		result.setExpiresAtSeconds(expiresAtSeconds);

		return result;
	}

	/**
	 * Erzeugt für den User ein neues JWT und refreshed das ClintAccessToken.
	 *
	 * @param  resourceOwner
	 * @param  client
	 * @param  clientAccessToken
	 * @return                   JWTPayload
	 */
	public JWTPayload createJWTWithEmail(final ResourceOwner resourceOwner, final Client client) {

		PrivateKey privateKey = getPrivateKey();

		TimeInterval jwtInterval = CommonTimeUtils.getInterval(CommonTimeUtils.now(), client.getJwtExpirationMinutes(),
			ChronoUnit.MINUTES);

		Algorithm algorithm = Algorithm.RSA256(null, (RSAPrivateKey) privateKey);

		String jwt = null;

		Builder builder = createMinimal(resourceOwner, jwtInterval).withClaim(Claims.email.name(), resourceOwner.getEmail());

		if (resourceOwner.hasName()) {

			builder = builder.withClaim(Claims.full_name.name(), resourceOwner.getFullName());
		}

		if (resourceOwner.getRoles() != null) {

			String[] roles = resourceOwner.getRoles().split(",");
			builder = builder.withArrayClaim(Claims.groups.name(), roles);
		}

		jwt = builder.sign(algorithm);

		JWTPayload result = new JWTPayload();
		result.setJwt(jwt);
		long expiresAtSeconds = jwtInterval.getEndTime().getTime() / 1000;
		result.setExpiresAtSeconds(expiresAtSeconds);

		return result;
	}

	/**
	 * @return String
	 */
	public String getPublicKey() {

		try (InputStream in = new FileInputStream(new File(publicKeyLocation));
			StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, "UTF-8");

			String result = sw.toString();
			LOGGER.info("public key = {}", result.substring(0, 30));

			return result;
		} catch (IOException e) {

			throw new AuthConfigurationException("Lesen des public keys: " + e.getMessage(), e);
		}
	}

	private Builder createMinimal(final ResourceOwner resourceOwner, final TimeInterval jwtInterval) {

		return JWT.create().withIssuer(ISSUER)
			.withIssuedAt(jwtInterval.getStartTime()).withExpiresAt(jwtInterval.getEndTime())
			.withSubject(resourceOwner.getUuid());
	}

	private PrivateKey getPrivateKey() {

		try {

			PemFile pemFile = new PemFile(privateKeyLocation);
			KeyFactory factory = KeyFactory.getInstance("RSA", "BC");

			byte[] content = pemFile.getPemObject().getContent();
			PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(content);

			final PrivateKey privateKey = factory.generatePrivate(privKeySpec);

			LOGGER.info("private key gelesen");

			return privateKey;
		} catch (IOException e) {

			throw new AuthConfigurationException("Lesen des private keys: " + e.getMessage(), e);
		} catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException e) {

			throw new AuthCryptoException("Lesen des private keys" + e.getMessage(), e);
		}
	}
}
