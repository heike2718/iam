// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.service.profile;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.error.MailversandException;
import de.egladil.web.authprovider.service.mail.AuthMailService;
import de.egladil.web.authprovider.service.mail.CreateDefaultMailDatenStrategy;

/**
 * SendMailProfilChangedTask sendet eine Mail darüber, dass sich am Profil (Loginname, Vorname, Nachname, Mailadresse,
 * Passwort) ertwas geändert hat.
 */
public class SendMailProfilChangedTask implements Callable<Boolean> {

	private static final Logger LOG = LoggerFactory.getLogger(SendMailProfilChangedTask.class);

	private final AuthMailService mailService;

	private final CreateDefaultMailDatenStrategy mailStrategy;

	/**
	 * @param emailTo
	 * @param mailService
	 * @param mailStrategy
	 */
	public SendMailProfilChangedTask(final AuthMailService mailService, final CreateDefaultMailDatenStrategy mailStrategy) {

		this.mailService = mailService;
		this.mailStrategy = mailStrategy;
	}

	@Override
	public Boolean call() throws Exception {

		try {

			mailService.sendMail(mailStrategy.createEmailDaten(""));

			return Boolean.TRUE;
		} catch (MailversandException e) {

			LOG.error(e.getMessage(), e);
			return Boolean.FALSE;
		}
	}
}
