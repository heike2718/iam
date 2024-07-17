// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.mailversand.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.auth_admin_api.domain.exceptions.MailversandException;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * MailService
 */
@ApplicationScoped
public class MailService {

	private static final String DEFAULT_ENCODING = "UTF-8";

	private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

	@ConfigProperty(name = "emails.standardempfaenger")
	private String standardempfaenger;

	/**
	 * Versendet eine Mail.
	 *
	 * @param  betreff
	 * @param  body
	 * @param  empfaenger
	 * @throws MailversandException
	 *                              - alle Exceptions die hier gefangen werden, werden in eine MailversandException gewrapped.
	 */
	public void sendeMail(final AuthAdminMailDto mailDto) throws MailversandException {

		String body = mailDto.getBody();

		if (mailDto.isAttachSpammailhinweis()) {

			body += getMailbodySpammailhinweis();
		}

		String betreff = mailDto.getBetreff();

		LOGGER.info("Sende Mails an TO {}", standardempfaenger);
		LOGGER.info("Sende Mails BCC an {}", StringUtils.join(mailDto.getBccEmpfaenger()));
		LOGGER.info("Betreff: {}", betreff);
		LOGGER.info("Body: {}", body);

	}

	private String getMailbodySpammailhinweis() {

		try (InputStream in = getClass().getResourceAsStream("/mails/mailsuffix.txt"); StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, Charset.forName(DEFAULT_ENCODING));

			return "\n\n" + sw.toString();

		} catch (IOException e) {

			LOGGER.warn("Standardmailende konnte nicht geladen werden: " + e.getMessage(), e);
			return "\n\nMit freundlichen Grüßen,\nHeike Winkelvoß";
		}
	}

}
