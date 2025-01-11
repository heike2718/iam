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

import de.egladil.web.authprovider.entities.ActivationCode;
import de.egladil.web.authprovider.error.AuthRuntimeException;
import de.egladil.web.authprovider.utils.AuthTimeUtils;

/**
 * RegistrationMailStrategy
 */
public class RegistrationMailStrategy implements CreateDefaultMailDatenStrategy {

	// private static final String LINK_PREFIX = "authprovider/registration/confirmation?code=";

	private final String emailTo;

	private final String loginName;

	private final ActivationCode activationCode;

	// private final UriInfo uriInfo;

	private final String accountConfirmationUrl;

	/**
	 * @param emailTo
	 * @param loginName
	 * @param activationCode
	 * @param uriInfo
	 */
	public RegistrationMailStrategy(final String emailTo, final String loginName, final ActivationCode activationCode,
		final String accountConfirmationUrl) {

		this.emailTo = emailTo;
		this.loginName = loginName;
		this.activationCode = activationCode;
		this.accountConfirmationUrl = accountConfirmationUrl;
	}

	@Override
	public DefaultEmailDaten createEmailDaten(final String messageId) {

		DefaultEmailDaten maildaten = new DefaultEmailDaten();
		maildaten.setEmpfaenger(emailTo);
		maildaten.setBetreff("Mathe für jung und alt: Aktivierung Benutzerkonto");
		maildaten.setText(getText());
		maildaten.setMessageId(messageId);
		return maildaten;
	}

	private String getText() {

		try (InputStream in = getClass().getResourceAsStream("/mailtemplates/registrierung.txt");
			StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, "utf-8");
			String text = sw.toString();

			LocalDateTime ldt = AuthTimeUtils.transformFromDate(activationCode.getExpirationTime());
			String expiresAt = DateTimeFormatter.ofPattern(AuthTimeUtils.DEFAULT_DATE_TIME_FORMAT).format(ldt);

			text = StringUtils.replace(text, "#0#", expiresAt);
			text = StringUtils.replace(text, "#1#", getLinkConfirmUrl());

			if (StringUtils.isNotBlank(loginName) && !emailTo.equals(loginName)) {

				text = StringUtils.replace(text, "#2#", "Ihr Loginname ist " + loginName + ".");
			} else {

				text = StringUtils.replace(text, "#2#", "");
			}

			return text;

		} catch (IOException e) {

			throw new AuthRuntimeException("Fehler beim Erzeugen des Mailtexts: " + e.getMessage(), e);
		}
	}

	private String getLinkConfirmUrl() {

		// String baseUrl = uriInfo.getBaseUri().toString();
		//
		// if (accountConfirmationUrl.endsWith("/")) {
		//
		// return accountConfirmationUrl.substr + activationCode.getConfirmationCode();
		// }
		// return baseUrl + "/" + LINK_PREFIX + activationCode.getConfirmationCode();

		return accountConfirmationUrl + activationCode.getConfirmationCode();

	}

	@Override
	public String toString() {

		return "RegistrationMailStrategy [emailTo=" + emailTo + ", loginName=" + loginName + ", activationCode="
			+ StringUtils.abbreviate(activationCode.getConfirmationCode(), 11) + ", accountConfirmationUrl="
			+ accountConfirmationUrl + "]";
	}

}
