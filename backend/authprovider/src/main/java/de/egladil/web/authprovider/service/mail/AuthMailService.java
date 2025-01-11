// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.service.mail;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.error.MailversandException;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * AuthMailService
 */
@ApplicationScoped
public class AuthMailService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthMailService.class);

	@ConfigProperty(name = "stage")
	String stage;

	@Inject
	Mailer mailer;

	/**
	 * Versendet die gegebenen Maildaten.
	 *
	 * @param emailDaten
	 * @throws EmailException
	 * @throws InvalidMailAddressException
	 */
	public boolean sendMail(final DefaultEmailDaten mailDto) throws MailversandException {

		String body = mailDto.getText();
		String betreff = mailDto.getBetreff();

		try {

			Mail mail = Mail.withText(mailDto.getEmpfaenger(), betreff, body)
				.addBcc(mailDto.getHiddenEmpfaenger().toArray(new String[] {}));
			mailer.send(new Mail[] { mail });

			return true;
		} catch (Exception e) {

			LOGGER.error("Exception beim Versand von mails an Gruppe: {}", e.getMessage(), e);
			throw new MailversandException("Das Senden der Mail hat nicht geklappt", e);
		}
	}
}
