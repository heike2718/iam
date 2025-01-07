// =====================================================
// Project: commons-mailer
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.service.mail;

import java.util.ArrayList;
import java.util.List;

/**
 * EmailDatenBuilder
 */
public class EmailDatenBuilder {

	private final String empfaenger;

	private final String betreff;

	private String text;

	private List<String> hiddenEmpfaenger = new ArrayList<>();

	/**
	 * @param empfaenger
	 * @param betreff
	 */
	public EmailDatenBuilder(final String empfaenger, final String betreff) {

		this.empfaenger = empfaenger;
		this.betreff = betreff;
	}

	public EmailDatenBuilder withText(final String text) {

		this.text = text;
		return this;
	}

	public EmailDatenBuilder addHidden(final String emailAddress) {

		hiddenEmpfaenger.add(emailAddress);
		return this;
	}

	public EmailDaten build() {

		DefaultEmailDaten result = new DefaultEmailDaten();
		result.setBetreff(betreff);
		result.setEmpfaenger(empfaenger);
		result.setText(text);

		hiddenEmpfaenger.stream().forEach(s -> result.addHiddenEmpfaenger(s));

		return result;

	}

}
