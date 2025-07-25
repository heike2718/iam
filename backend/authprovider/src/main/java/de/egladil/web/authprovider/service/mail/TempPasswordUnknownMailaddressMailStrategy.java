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

/**
 * TempPasswordUnknownMailaddressMailStrategy
 */
public class TempPasswordUnknownMailaddressMailStrategy implements CreateDefaultMailDatenStrategy {

	private final String email;

	/**
	 * @param email
	 */
	public TempPasswordUnknownMailaddressMailStrategy(final String email) {

		super();
		this.email = email;
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

		try (InputStream in = getClass().getResourceAsStream("/mailtemplates/temppwdUnknownAccount.txt");
			StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, "utf-8");
			String text = sw.toString();
			return text;
		} catch (IOException e) {

			throw new AuthRuntimeException("Fehler beim Erzeugen des Mailtexts: " + e.getMessage(), e);
		}
	}
}
