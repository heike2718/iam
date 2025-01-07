// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.service.mail;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.authprovider.error.AuthRuntimeException;
import de.egladil.web.authprovider.utils.AuthTimeUtils;

/**
 * BenutzerkontoGeloeschtMailStrategie
 */
public class BenutzerkontoGeloeschtMailStrategie implements CreateDefaultMailDatenStrategy {

	private final String subject;

	private final String mailTo;

	private final ResourceOwner resourceOwner;

	private final LocalDateTime timestamp;

	/**
	 * @param subject
	 * @param mailTo
	 * @param resourceOwner
	 */
	public BenutzerkontoGeloeschtMailStrategie(final String subject, final String mailTo, final ResourceOwner resourceOwner) {

		this.subject = subject;
		this.mailTo = mailTo;
		this.resourceOwner = resourceOwner;
		this.timestamp = LocalDateTime.now();
	}

	@Override
	public DefaultEmailDaten createEmailDaten(final String messageId) {

		DefaultEmailDaten maildaten = new DefaultEmailDaten();
		maildaten.setEmpfaenger(mailTo);
		maildaten.setBetreff(subject);
		maildaten.setText(getText());
		maildaten.setMessageId(messageId);
		return maildaten;
	}

	private String getText() {

		try (InputStream in = getClass().getResourceAsStream("/mailtemplates/kontoGeloescht.txt");
			StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, "utf-8");
			String text = sw.toString();

			String wann = DateTimeFormatter.ofPattern(AuthTimeUtils.DEFAULT_DATE_TIME_FORMAT).format(timestamp);

			text = StringUtils.replace(text, "#1#", wann);
			text = StringUtils.replace(text, "#2#", resourceOwner.toLogString());

			return text;

		} catch (IOException e) {

			throw new AuthRuntimeException("Fehler beim Erzeugen des Mailtexts: " + e.getMessage(), e);
		}
	}

}
