// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.auth_validations.utils.SecUtils;
import de.egladil.web.authprovider.crypto.AuthCryptoService;
import de.egladil.web.authprovider.dao.LoginSecretsDao;
import de.egladil.web.authprovider.domain.CryptoAlgorithm;
import de.egladil.web.authprovider.entities.LoginSecrets;
import de.egladil.web.authprovider.error.AuthPersistenceException;
import de.egladil.web.authprovider.error.AuthRuntimeException;
import de.egladil.web.authprovider.error.ConcurrentUpdateException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

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
	 * @param loginSecrets
	 * @param password
	 * @return LoginSecrets
	 */
	public LoginSecrets updateLoginSecrets(final LoginSecrets loginSecrets, final char[] password) throws AuthRuntimeException {

		try {

			String passwordHash = authCryptoService.hashPassword(password);
			loginSecrets.setPasswordhash(passwordHash);
			loginSecrets.setSalt(null);
			loginSecrets.setCryptoAlgorithm(CryptoAlgorithm.ARGON2);

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
