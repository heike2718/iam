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

import de.egladil.web.authprovider.domain.TempPassword;
import de.egladil.web.authprovider.error.AuthRuntimeException;
import de.egladil.web.authprovider.utils.AuthTimeUtils;

/**
 * TempPasswordMailStrategy
 */
public class TempPasswordCreatedMailStrategy implements CreateDefaultMailDatenStrategy {

	private final String email;

	private final TempPassword tempPassword;

	private final String url;

	/**
	 * @param email
	 * @param tempPassword
	 * @param uriInfo
	 */
	public TempPasswordCreatedMailStrategy(final String email, final TempPassword tempPassword, final String url) {

		super();
		this.email = email;
		this.tempPassword = tempPassword;
		this.url = url;
	}

	@Override
	public DefaultEmailDaten createEmailDaten(final String messageId) {

		DefaultEmailDaten result = new DefaultEmailDaten();
		result.setBetreff("Minikänguru: Einmalpasswort");
		result.setEmpfaenger(email);
		result.setText(getText());
		result.setMessageId(messageId);
		return result;
	}

	private String getText() {

		try (InputStream in = getClass().getResourceAsStream("/mailtemplates/temppwdCreated.txt");
			StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, "utf-8");
			String text = sw.toString();

			LocalDateTime ldt = AuthTimeUtils.transformFromDate(tempPassword.getExpiresAt());
			String expiresAt = DateTimeFormatter.ofPattern("dd.MM.yyyy kk:mm:ss").format(ldt);

			String link = url + tempPassword.getTokenId();

			text = StringUtils.replace(text, "#0#", expiresAt);
			text = StringUtils.replace(text, "#1#", link);
			text = StringUtils.replace(text, "#2#", tempPassword.getPassword());
			return text;
		} catch (IOException e) {

			throw new AuthRuntimeException("Fehler beim Erzeugen des Mailtexts: " + e.getMessage(), e);
		}
	}
}
