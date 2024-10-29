// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.service.profile;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.crypto.AuthCryptoService;
import de.egladil.web.authprovider.dao.ResourceOwnerDao;
import de.egladil.web.authprovider.domain.LoginSecrets;
import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.authprovider.error.AuthException;
import de.egladil.web.authprovider.error.AuthPersistenceException;
import de.egladil.web.authprovider.error.ConcurrentUpdateException;
import de.egladil.web.authprovider.payload.MessagePayload;
import de.egladil.web.authprovider.payload.ResponsePayload;
import de.egladil.web.authprovider.payload.profile.ProfilePasswordPayload;
import de.egladil.web.authprovider.service.AuthMailService;
import de.egladil.web.authprovider.service.ChangeLoginSecretsDelegate;
import de.egladil.web.authprovider.service.mail.CreateDefaultMailDatenStrategy;
import de.egladil.web.authprovider.service.mail.PasswordChangedMailStrategy;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

/**
 * ChangePasswordService
 */
@RequestScoped
public class ChangePasswordService {

	private static final Logger LOG = LoggerFactory.getLogger(ChangePasswordService.class);

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	@Inject
	ResourceOwnerDao resourceOwnerDao;

	@Inject
	AuthCryptoService authCryptoService;

	@Inject
	ChangeLoginSecretsDelegate changeLoginSecretsDelegate;

	@Inject
	AuthMailService mailService;

	/**
	 * Das alte Passwort wird durch ein neues Passwort ersetzt.
	 *
	 * @param  userUUID
	 *                  String - die UUID des Benutzers.
	 * @param  payload
	 *                  ProfilePasswordPayload
	 * @return
	 */
	public ResponsePayload changePassword(final String userUUID, final ProfilePasswordPayload payload) {

		try {

			Optional<ResourceOwner> optResourceOwner = resourceOwnerDao.findByUUID(userUUID);

			if (!optResourceOwner.isPresent()) {

				LOG.warn(
					"An dieser Stelle müsste der ResourceOwner mit UUID {} vorhanden sein, weil das schon im AuthorizationFilter geprüft worden sein müsste",
					userUUID);
				throw new NotFoundException();
			}

			ResourceOwner resourceOwner = optResourceOwner.get();
			LoginSecrets loginSecrets = resourceOwner.getLoginSecrets();

			authCryptoService.verifyPassword(payload.getPasswort().toCharArray(), resourceOwner);

			changeLoginSecretsDelegate.updateLoginSecrets(loginSecrets,
				payload.getZweiPassworte().getPasswort().toCharArray());

			sendMail(resourceOwner.getEmail());

			return ResponsePayload.messageOnly(MessagePayload.info(applicationMessages.getString("Password.changed.success")));

		} catch (ConcurrentUpdateException e) {

			LOG.warn("ConcurrentUpdate beim Ändern des Passworts durch {}", userUUID);

			return ResponsePayload
				.messageOnly(MessagePayload.error(applicationMessages.getString("Password.changed.error")));
		} catch (AuthPersistenceException e) {

			return ResponsePayload
				.messageOnly(MessagePayload.error(applicationMessages.getString("Password.changed.error")));
		} catch (AuthException e) {

			LOG.warn("Passwort ändern: altes Passwort stimmt nicht: {}", userUUID);
			return ResponsePayload
				.messageOnly(MessagePayload.error(applicationMessages.getString("Password.changed.authFailure")));
		} finally {

			payload.getZweiPassworte().clean();
		}

	}

	private void sendMail(final String email) {

		CreateDefaultMailDatenStrategy strategy = new PasswordChangedMailStrategy(email);

		SendMailProfilChangedTask task = new SendMailProfilChangedTask(mailService, strategy);

		try {

			Boolean outcome = task.call();

			if (outcome) {

				LOG.debug("Mail ProfileChanged versendet");
			} else {

				LOG.warn("Beim Versenden der Mail ist ein Fehler aufgetreten");
			}
		} catch (Exception e) {

			LOG.error("Senden der Mail konnte nicht beendet werden: " + e.getMessage(), e);
		}
	}
}
