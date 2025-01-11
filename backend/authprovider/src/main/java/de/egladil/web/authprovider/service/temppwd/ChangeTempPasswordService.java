// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.service.temppwd;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.api.ClientInformation;
import de.egladil.web.authprovider.dao.TempPasswordDao;
import de.egladil.web.authprovider.entities.Client;
import de.egladil.web.authprovider.entities.LoginSecrets;
import de.egladil.web.authprovider.entities.ResourceOwner;
import de.egladil.web.authprovider.entities.TempPassword;
import de.egladil.web.authprovider.error.LogmessagePrefixes;
import de.egladil.web.authprovider.payload.ChangeTempPasswordPayload;
import de.egladil.web.authprovider.payload.MessagePayload;
import de.egladil.web.authprovider.payload.ResponsePayload;
import de.egladil.web.authprovider.service.ChangeLoginSecretsDelegate;
import de.egladil.web.authprovider.service.mail.AuthMailService;
import de.egladil.web.authprovider.service.mail.CreateDefaultMailDatenStrategy;
import de.egladil.web.authprovider.service.mail.TempPasswordChangedMailStrategy;
import de.egladil.web.authprovider.service.profile.SendMailProfilChangedTask;
import de.egladil.web.authprovider.utils.AuthTimeUtils;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

/**
 * ChangeTempPasswordService
 */
@RequestScoped
public class ChangeTempPasswordService {

	private static final Logger LOG = LoggerFactory.getLogger(ChangeTempPasswordService.class);

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	@Inject
	TempPasswordDao tempPasswordDao;

	@Inject
	AuthMailService mailService;

	@Inject
	ChangeLoginSecretsDelegate changeLoginSecretsDelegate;

	/**
	 *
	 */
	public ChangeTempPasswordService() {

		super();
	}

	/**
	 * @param tempPasswordDao
	 * @param mailService
	 * @param changeLoginSecretsDelegate
	 */
	ChangeTempPasswordService(final TempPasswordDao tempPasswordDao, final AuthMailService mailService,
		final ChangeLoginSecretsDelegate changeLoginSecretsDelegate) {

		super();
		this.tempPasswordDao = tempPasswordDao;
		this.mailService = mailService;
		this.changeLoginSecretsDelegate = changeLoginSecretsDelegate;
	}

	/**
	 * Wenn alles gut geht, wird das Passwort des Benutzerkontos geändert und das TempPasswort gelöscht.
	 *
	 * @param payload ChangeTempPasswordPayload
	 * @return ResponsePayload
	 */
	@Transactional(value = TxType.REQUIRED)
	public ResponsePayload changeTempPassword(final ChangeTempPasswordPayload payload) {

		try {

			Optional<TempPassword> optTempPassword = tempPasswordDao.findByTokenId(payload.getTokenId());

			if (!optTempPassword.isPresent()) {

				LOG.warn("Unbekannte tokenId: {}", payload);

				return ResponsePayload.messageOnly(MessagePayload.error(applicationMessages.getString("TempPassword.notFound")));
			}

			TempPassword tempPassword = optTempPassword.get();

			LocalDateTime expiresAt = AuthTimeUtils.transformFromDate(tempPassword.getExpiresAt());

			if (AuthTimeUtils.now().isAfter(expiresAt)) {

				this.deleteTempPasswordQuietly(tempPassword);

				return ResponsePayload.messageOnly(MessagePayload.error(applicationMessages.getString("TempPassword.notFound")));
			}

			if (!tempPassword.getPassword().equals(payload.getTempPassword())) {

				LOG.warn("Falsches Einmalpasswort '{}': ", payload.getTempPassword(), payload);

				return ResponsePayload
					.messageOnly(MessagePayload.error(applicationMessages.getString("TempPassword.incorrectCredentials")));
			}

			ResourceOwner resourceOwner = tempPassword.getResourceOwner();

			if (!resourceOwner.getEmail().equalsIgnoreCase(payload.getEmail())) {

				LOG.warn("Unbekannte Email: {}, ", payload, resourceOwner);

				return ResponsePayload
					.messageOnly(MessagePayload.error(applicationMessages.getString("TempPassword.incorrectCredentials")));
			}

			if (!resourceOwner.isAktiviert()) {

				LOG.warn("Konto {} noch nicht aktiviert", resourceOwner);

				return ResponsePayload
					.messageOnly(MessagePayload.error(applicationMessages.getString("TempPassword.incorrectCredentials")));
			}

			Client client = tempPassword.getClient();

			LoginSecrets loginSecrets = resourceOwner.getLoginSecrets();

			changeLoginSecretsDelegate.updateLoginSecrets(loginSecrets, payload.getZweiPassworte().getPasswort().toCharArray());

			sendMail(payload.getEmail());

			this.deleteTempPasswordQuietly(tempPassword);

			LOG.info("Einmalpasswort erfolgreich geändert: {}", resourceOwner);

			if (client != null) {

				ClientInformation data = ClientInformation.fromClient(client);

				return new ResponsePayload(MessagePayload.info(applicationMessages.getString("TempPassword.changed.success")),
					data);

			}

			return ResponsePayload.messageOnly(MessagePayload.info(applicationMessages.getString("TempPassword.changed.success")));
		} finally {

			payload.clean();
		}
	}

	private void deleteTempPasswordQuietly(final TempPassword tempPassword) {

		try {

			tempPasswordDao.delete(tempPassword);
			LOG.debug("{} gelöscht", tempPassword);
		} catch (Exception e) {

			LOG.warn(LogmessagePrefixes.DATENMUELL + "TempPassword '{}' ist abgelaufen, konnte aber nicht gelöscht werden.",
				tempPassword);
			LOG.error("unerwartete Exception beim Löschen eines TempPasswords: {}", e.getMessage(), e);
		}
	}

	private void sendMail(final String email) {

		CreateDefaultMailDatenStrategy strategy = new TempPasswordChangedMailStrategy(email);

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
