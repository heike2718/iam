// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.service.mail;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;

import de.egladil.web.authprovider.error.AuthRuntimeException;
import de.egladil.web.commons_mailer.DefaultEmailDaten;

/**
 * PasswordChangedMailStrategy
 */
public class PasswordChangedMailStrategy implements CreateDefaultMailDatenStrategy {

	private final String email;

	/**
	 * @param email
	 */
	public PasswordChangedMailStrategy(final String email) {

		this.email = email;
	}

	@Override
	public DefaultEmailDaten createEmailDaten() {

		DefaultEmailDaten result = new DefaultEmailDaten();
		result.setBetreff("Minikänguru: Passwort geändert");
		result.setEmpfaenger(email);
		result.setText(getText());

		return result;

	}

	private String getText() {

		try (InputStream in = getClass().getResourceAsStream("/mailtemplates/passwordChanged.txt");
			StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, "utf-8");
			String text = sw.toString();
			return text;
		} catch (IOException e) {

			throw new AuthRuntimeException("Fehler beim Erzeugen des Mailtexts: " + e.getMessage(), e);
		}
	}

}
