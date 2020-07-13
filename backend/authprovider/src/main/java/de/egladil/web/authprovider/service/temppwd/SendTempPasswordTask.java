// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.service.temppwd;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.domain.TempPassword;
import de.egladil.web.authprovider.service.AuthMailService;
import de.egladil.web.authprovider.service.mail.CreateDefaultMailDatenStrategy;
import de.egladil.web.authprovider.service.mail.TempPasswordCreatedMailStrategy;
import de.egladil.web.commons_mailer.exception.EmailException;
import de.egladil.web.commons_mailer.exception.InvalidMailAddressException;

/**
 * SendTempPasswordTask
 */
public class SendTempPasswordTask implements Callable<Boolean> {

	private static final Logger LOG = LoggerFactory.getLogger(SendTempPasswordTask.class);

	private final String email;

	private final TempPassword tempPassword;

	private final AuthMailService mailService;

	private final String urlPrefix;

	/**
	 * @param email
	 * @param tempPassword
	 * @param mailService
	 * @param uriInfo
	 */
	public SendTempPasswordTask(final String email, final TempPassword tempPassword, final AuthMailService mailService, final String urlPrefix) {

		super();
		this.email = email;
		this.tempPassword = tempPassword;
		this.mailService = mailService;
		this.urlPrefix = urlPrefix;
	}

	@Override
	public Boolean call() throws Exception {

		try {

			CreateDefaultMailDatenStrategy mailStrategy = new TempPasswordCreatedMailStrategy(email, tempPassword,
				urlPrefix);

			mailService.sendMail(mailStrategy.createEmailDaten());

			return Boolean.TRUE;
		} catch (InvalidMailAddressException e) {

			LOG.warn(e.getMessage());
			return Boolean.FALSE;
		} catch (EmailException e) {

			LOG.error(e.getMessage(), e);
			return Boolean.FALSE;
		}
	}

}
