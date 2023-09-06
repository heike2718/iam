// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.crypto.impl;

import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.config.PasswordConfig;
import de.egladil.web.authprovider.crypto.AuthCryptoService;
import de.egladil.web.authprovider.domain.Client;
import de.egladil.web.authprovider.domain.LoginSecrets;
import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.authprovider.domain.Salt;
import de.egladil.web.authprovider.error.AuthException;
import de.egladil.web.authprovider.error.ClientAuthException;
import de.egladil.web.authprovider.error.LogmessagePrefixes;
import de.egladil.web.commons_crypto.CryptoService;
import de.egladil.web.commons_crypto.PasswordAlgorithm;
import de.egladil.web.commons_crypto.PasswordAlgorithmBuilder;
import de.egladil.web.commons_validation.SecUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * AuthCryptoServiceImpl wrapper für CryptoService
 *
 * @author heike
 */
@ApplicationScoped
public class AuthCryptoServiceImpl implements AuthCryptoService {

	private static final Logger LOG = LoggerFactory.getLogger(AuthCryptoServiceImpl.class);

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	@ConfigProperty(name = "stage")
	String stage;

	@ConfigProperty(name = "checklistenapp.client-id")
	String checklistenappClientId;

	@ConfigProperty(name = "checklistenapp.client-secret")
	String checklistenappClientSecret;

	@Inject
	PasswordConfig passwordConfig;

	@Inject
	CryptoService cryptoService;

	/**
	 * Erzeugt eine Instanz von AuthCryptoServiceImpl
	 */
	public AuthCryptoServiceImpl() {

	}

	/**
	 * Zu Testzwecken ohne CDI
	 */
	public AuthCryptoServiceImpl(final PasswordConfig passwordConfig, final CryptoService cryptoService) {

		this.passwordConfig = passwordConfig;
		this.cryptoService = cryptoService;
	}

	@Override
	public Hash hashPassword(final char[] password) {

		try {

		// @formatter:off
		PasswordAlgorithm passwordAlgorithm = PasswordAlgorithmBuilder.instance()
			.withAlgorithmName(passwordConfig.getCryptoAlgorithm())
			.withNumberIterations(passwordConfig.getIterations())
			.withPepper(passwordConfig.getPepper())
			.build();
		// @formatter:on

			final ByteSource salt = new SimpleByteSource(cryptoService.generateSalt(128));
			final Hash hash = passwordAlgorithm.hashPassword(password, salt);
			return hash;
		} finally {

			SecUtils.wipe(password);
		}
	}

	@Override
	public boolean verifyPassword(final char[] password, final ResourceOwner resourceOwner) {

		try {

			LoginSecrets loginSecrets = resourceOwner.getLoginSecrets();
			final Salt salt = loginSecrets.getSalt();

		// @formatter:off
		PasswordAlgorithm passwordAlgorithm =
			PasswordAlgorithmBuilder.instance()
			.withAlgorithmName(salt.getAlgorithmName())
			.withNumberIterations(salt.getIterations())
			.withPepper(passwordConfig.getPepper())
			.build();
		// @formatter:on

			final boolean korrekt = passwordAlgorithm.verifyPassword(password, loginSecrets.getPasswordhash(), salt.getWert());

			if (!korrekt) {

				LOG.warn("ResourceOwner {} stimmt, passwort nicht", resourceOwner);
				throw new AuthException(applicationMessages.getString("Authentication.incorrectCredentials"));
			}
			return true;
		} finally {

			SecUtils.wipe(password);
		}
	}

	@Override
	public boolean verifyClientSecret(final char[] password, final Client client) {

		if ("prod".equalsIgnoreCase(stage) && checklistenappClientId.equals(client.getClientId())) {

			LOG.info("checklistenapp in PROD");

			return checklistenappClientSecret.equals(new String(password));
		}

		try {

			LoginSecrets loginSecrets = client.getLoginSecrets();

			final Salt salt = loginSecrets.getSalt();

		// @formatter:off
		String pepper = passwordConfig.getPepper();
		PasswordAlgorithm passwordAlgorithm =
			PasswordAlgorithmBuilder.instance()
			.withAlgorithmName(salt.getAlgorithmName())
			.withNumberIterations(salt.getIterations())
			.withPepper(pepper)
			.build();
		// @formatter:on

			final boolean korrekt = passwordAlgorithm.verifyPassword(password, loginSecrets.getPasswordhash(), salt.getWert());

			if (!korrekt) {

				LOG.warn(LogmessagePrefixes.BOT + "Client {} stimmt, passwort nicht", client);
				throw new ClientAuthException("Ungültige ClientCredentials");
			}
			return true;
		} finally {

			SecUtils.wipe(password);
		}

	}

	@Override
	public String generateTemporaryPasswort() {

		return cryptoService.generateRandomString(passwordConfig.getRandomAlgorithm(), passwordConfig.getTempPwdLength(),
			passwordConfig.getTempPwdCharPool().toCharArray());
	}

	@Override
	public String generateClientID() {

		String charpool = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		int length = 44;

		return cryptoService.generateRandomString(passwordConfig.getRandomAlgorithm(), length, charpool.toCharArray());
	}
}
