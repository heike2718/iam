// =====================================================
// Projekt: commons-mailer
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.service.mail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

/**
 * DefaultEmailDaten ist eine Default-Implementierung, die man für das Interface verwenden kann.
 */
public class DefaultEmailDaten implements EmailDaten {

	private String messageId;

	private String empfaenger;

	private String betreff;

	private String text;

	private List<String> hiddenEmpfaenger = new ArrayList<>();

	@Override
	public int hashCode() {

		return Objects.hash(messageId);
	}

	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {

			return true;
		}

		if (obj == null) {

			return false;
		}

		if (getClass() != obj.getClass()) {

			return false;
		}
		DefaultEmailDaten other = (DefaultEmailDaten) obj;
		return Objects.equals(messageId, other.messageId);
	}

	public void addHiddenEmpfaenger(final String empfaenger) {

		if (empfaenger != null) {

			hiddenEmpfaenger.add(empfaenger);
		}
	}

	public void addHiddenEmpfaenger(final Collection<String> empfaenger) {

		if (empfaenger != null) {

			hiddenEmpfaenger.addAll(empfaenger);
		}
	}

	@Override
	public String getEmpfaenger() {

		return this.empfaenger;
	}

	@Override
	public String getBetreff() {

		return this.betreff;
	}

	@Override
	public String getText() {

		return this.text;
	}

	@Override
	public Collection<String> getHiddenEmpfaenger() {

		return this.hiddenEmpfaenger;
	}

	@Override
	public List<String> alleEmpfaengerFuersLog() {

		List<String> result = this.hiddenEmpfaenger.stream().filter(e -> StringUtils.isNotBlank(e)).collect(Collectors.toList());
		result.add(this.empfaenger);
		return result;
	}

	public void setEmpfaenger(final String empfaenger) {

		this.empfaenger = empfaenger;
	}

	public void setBetreff(final String betreff) {

		this.betreff = betreff;
	}

	public void setText(final String text) {

		this.text = text;
	}

	public String getMessageId() {

		return messageId;
	}

	public void setMessageId(final String messageId) {

		this.messageId = messageId;
	}

}
