// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.crypto.impl;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.auth_validations.utils.SecUtils;
import de.egladil.web.authprovider.config.PasswordConfig;
import de.egladil.web.authprovider.crypto.AuthCryptoService;
import de.egladil.web.authprovider.dao.LoginSecretsDao;
import de.egladil.web.authprovider.domain.CryptoAlgorithm;
import de.egladil.web.authprovider.entities.Client;
import de.egladil.web.authprovider.entities.LoginSecrets;
import de.egladil.web.authprovider.entities.ResourceOwner;
import de.egladil.web.authprovider.error.AuthException;
import de.egladil.web.authprovider.error.LogmessagePrefixes;
import de.egladil.web.authprovider.utils.AuthUtils;
import io.vertx.core.http.HttpServerRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * AuthCryptoServiceImpl wrapper für CryptoService
 *
 * @author heike
 */
@ApplicationScoped
public class AuthCryptoServiceImpl implements AuthCryptoService {

	/**
	 * übernomemen aus org.apache.shiro.crypto.support.hashes.argon2.Argon2Hash. Muss bei updates von shiro immer mit
	 * getestet werden!!!
	 */
	private static final String DEFAULT_ALGORITHM = "argon2id";

	private static final String MESSAGE_FORMAT_FAILED_LOGIN = "ipAddress={0}, userAgent={1}, ressourceOwner={2}";

	private static final Logger LOG = LoggerFactory.getLogger(AuthCryptoServiceImpl.class);

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	@Inject
	LoginSecretsDao loginSecretsDao;

	@Inject
	HttpServerRequest request;

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

	@Override
	public String hashPassword(final char[] password) {

		try {

			DefaultPasswordService passwordService = createArgon2PasswordService();
			String pepperedPassword = getPepperedPassword(password);
			return passwordService.encryptPassword(pepperedPassword);
		} finally {

			SecUtils.wipe(password);
		}
	}

	@Override
	public boolean verifyPassword(final char[] password, final ResourceOwner resourceOwner) {

		LoginSecrets loginSecrets = resourceOwner.getLoginSecrets();

		boolean matches = this.verifyLoginSecrets(loginSecrets, password);

		if (!matches) {

			LOG.warn("ResourceOwner {} stimmt, passwort nicht", getFailedLoginDetails(resourceOwner));
			throw new AuthException(applicationMessages.getString("Authentication.incorrectCredentials"));
		}

		return matches;
	}

	@Transactional
	void rehashAndStorePassword(final LoginSecrets loginSecrets, final char[] password) {

		String rehashedPassword = this.hashPassword(password);
		loginSecrets.setCryptoAlgorithm(CryptoAlgorithm.ARGON2);
		loginSecrets.setPasswordhash(rehashedPassword);
		loginSecrets.setSalt(null);

		loginSecretsDao.save(loginSecrets);
	}

	private String getFailedLoginDetails(final ResourceOwner resourceOwner) {

		String ipAddress = AuthUtils.getIPAddress(request);
		String userAgent = AuthUtils.getUserAgent(request);
		return MessageFormat.format(MESSAGE_FORMAT_FAILED_LOGIN,
			new Object[] { ipAddress, userAgent, resourceOwner.toLogString() });
	}

	@Override
	public boolean verifyClientSecret(final char[] password, final Client client) {

		if (checklistenappClientId.equals(client.getClientId())) {

			LOG.info("checklistenapp in PROD");

			return checklistenappClientSecret.equals(new String(password));
		}

		LoginSecrets loginSecrets = client.getLoginSecrets();

		boolean matches = this.verifyLoginSecrets(loginSecrets, password);

		if (!matches) {

			LOG.warn(LogmessagePrefixes.BOT + "Client {} stimmt, passwort nicht", client);
			throw new AuthException(applicationMessages.getString("Authentication.incorrectCredentials"));
		}

		return matches;
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

	void setStageForTest(final String stage) {

		this.stage = stage;
	}

	void setChecklistenappClientIdForTest(String clientId) {
		this.checklistenappClientId = clientId;
	}

	/**
	 * Verifiziert die LoginSecrets. Falls sie noch mit SHA-256 berechnet sind, werden sie umgehashed.
	 *
	 * @param loginSecrets
	 * @param password
	 * @return
	 */
	boolean verifyLoginSecrets(final LoginSecrets loginSecrets, final char[] password) {

		boolean matches = false;

		try {

			if (CryptoAlgorithm.SHA_256 == loginSecrets.getCryptoAlgorithm()) {

				matches = new LegacyPasswordService(passwordConfig).verifyPassword(password, loginSecrets.getPasswordhash(),
					loginSecrets.getSalt().getWert());

				// Umwandeln, damit die alten verschwinden.
				if (matches) {

					this.rehashAndStorePassword(loginSecrets, password);
				}

			} else {

				DefaultPasswordService passwordService = createArgon2PasswordService();
				String pepperedPassword = getPepperedPassword(password);
				matches = passwordService.passwordsMatch(pepperedPassword, loginSecrets.getPasswordhash());
			}

			return matches;
		} finally {

			SecUtils.wipe(password);
		}
	}

	DefaultPasswordService createArgon2PasswordService() {

		DefaultHashService hashService = new DefaultHashService();
		hashService.setDefaultAlgorithmName(DEFAULT_ALGORITHM);

		DefaultPasswordService passwordService = new DefaultPasswordService();
		passwordService.setHashService(hashService);

		return passwordService;
	}

	/**
	 * @param password
	 * @return
	 */
	private String getPepperedPassword(final char[] password) {

		return passwordConfig.getPepper() + new String(password);
	}

	void setCryptoService(final CryptoService cryptoService) {

		this.cryptoService = cryptoService;
	}

	void setPasswordConfig(final PasswordConfig passwordConfig) {

		this.passwordConfig = passwordConfig;
	}

}
