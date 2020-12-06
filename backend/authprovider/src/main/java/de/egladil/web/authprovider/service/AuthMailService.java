// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.egladil.web.authprovider.AuthProviderApp;
import de.egladil.web.commons_mailer.CommonEmailService;
import de.egladil.web.commons_mailer.DefaultEmailDaten;
import de.egladil.web.commons_mailer.EmailServiceCredentials;
import de.egladil.web.commons_mailer.exception.EmailException;
import de.egladil.web.commons_mailer.exception.InvalidMailAddressException;

/**
 * AuthMailService
 */
@ApplicationScoped
public class AuthMailService {

	@ConfigProperty(name = "stage")
	String stage;

	@Inject
	CommonEmailService commonMailService;

	@Inject
	EmailServiceCredentials emailServiceCredentials;

	public static AuthMailService createForTest(final CommonEmailService commonMailService, final EmailServiceCredentials emailServiceCredentials) {

		AuthMailService result = new AuthMailService();
		result.stage = AuthProviderApp.STAGE_DEV;
		result.emailServiceCredentials = emailServiceCredentials;
		result.commonMailService = commonMailService;

		return result;
	}

	/**
	 * Erzeugt eine Instanz von AuthMailService
	 */
	public AuthMailService() {

	}

	/**
	 * Versendet die gegebenen Maildaten.
	 *
	 * @param  emailDaten
	 * @throws EmailException
	 * @throws InvalidMailAddressException
	 */
	public void sendMail(final DefaultEmailDaten emailDaten) throws EmailException, InvalidMailAddressException {

		if (AuthProviderApp.STAGE_DEV.equals(stage)) {

			String str = emailServiceCredentials.toString();

			System.out
				.println("=== (kommt nur auf DEV auf die Konsole): AuthMailService: " + str + ", passwd="
					+ new String(emailServiceCredentials.getPassword()) + " ===");
		}

		this.commonMailService.sendMail(emailDaten, emailServiceCredentials);
	}
}
