// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.service;

import java.util.Base64;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.apache.shiro.crypto.hash.Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.crypto.AuthCryptoService;
import de.egladil.web.authprovider.dao.LoginSecretsDao;
import de.egladil.web.authprovider.domain.LoginSecrets;
import de.egladil.web.authprovider.domain.Salt;
import de.egladil.web.authprovider.error.AuthPersistenceException;
import de.egladil.web.authprovider.error.AuthRuntimeException;
import de.egladil.web.authprovider.error.ConcurrentUpdateException;
import de.egladil.web.commons_validation.SecUtils;

/**
 * ChangeLoginSecretsService
 */
@RequestScoped
public class ChangeLoginSecretsDelegate {

	private static final Logger LOG = LoggerFactory.getLogger(ChangeLoginSecretsDelegate.class);

	@Inject
	LoginSecretsDao loginSecretsDao;

	@Inject
	AuthCryptoService authCryptoService;

	/**
	 * Hashed und salzt das neue Passwort und speichert die Änderungen in der DB.
	 *
	 * @param  loginSecrets
	 * @param  password
	 * @return              LoginSecrets
	 */
	public LoginSecrets updateLoginSecrets(final LoginSecrets loginSecrets, final char[] password) throws AuthRuntimeException {

		try {

			Hash hash = authCryptoService.hashPassword(password);
			loginSecrets.setPasswordhash(Base64.getEncoder().encodeToString(hash.getBytes()));

			Salt salt = loginSecrets.getSalt();
			salt.setAlgorithmName(hash.getAlgorithmName());
			salt.setIterations(hash.getIterations());
			salt.setWert(hash.getSalt().toBase64());

			LoginSecrets persisted = this.loginSecretsDao.save(loginSecrets);

			LOG.debug("Passwort mit ID {} geändert", loginSecrets.getId());

			return persisted;
		} catch (ConcurrentUpdateException e) {

			LOG.warn("ConcurrentUpdateException beim Speichern des neuen Passworts: {}", e.getMessage());

			throw new AuthPersistenceException("Beim Speichern des neuen Passworts ist ein Fehler aufgetreten.");

		} finally {

			SecUtils.wipe(password);
		}
	}
}
