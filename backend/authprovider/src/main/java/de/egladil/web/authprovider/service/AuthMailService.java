// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

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

	private static final String UNKNOWN_MAILHOST = "will-be-replaced";

	@ConfigProperty(name = "stage")
	String stage;

	@Inject
	CommonEmailService commonMailService;

	@Inject
	EmailServiceCredentials emailServiceCredentials;

	@ConfigProperty(name = "email.host")
	String emailHost;

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
	public boolean sendMail(final DefaultEmailDaten emailDaten) throws EmailException, InvalidMailAddressException {

		if (AuthProviderApp.STAGE_DEV.equals(stage)) {

			String str = emailServiceCredentials.toString();

			System.out
				.println("=== (kommt nur auf DEV auf die Konsole): AuthMailService: " + str + ", passwd="
					+ new String(emailServiceCredentials.getPassword()) + " ===");
		}

		if (UNKNOWN_MAILHOST.equals(emailHost)) {

			System.out.println(emailDaten.getBetreff());
			System.out.println(emailDaten.getText());
		} else {

			this.commonMailService.sendMail(emailDaten, emailServiceCredentials);
		}

		return true;
	}
}
