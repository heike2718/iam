// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.service.mail;

import java.text.MessageFormat;
import java.time.LocalDateTime;

import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.commons_mailer.DefaultEmailDaten;
import de.egladil.web.commons_net.time.CommonTimeUtils;

/**
 * MinikaengurukontenInfoStrategie
 */
public class MinikaengurukontenInfoStrategie implements CreateDefaultMailDatenStrategy {

	private final ResourceOwner resourceOwner;

	private final MinikaengurukontenMailKontext kontext;

	/**
	 * @param resourceOwner
	 */
	public MinikaengurukontenInfoStrategie(final ResourceOwner resourceOwner, final MinikaengurukontenMailKontext kontext) {

		this.resourceOwner = resourceOwner;
		this.kontext = kontext;
	}

	@Override
	public DefaultEmailDaten createEmailDaten() {

		String text = getText();

		if (text.isBlank()) {

			return null;
		}

		String betreff = "Infomail vom Authprovider";

		switch (kontext) {

		case LOGIN_INAKTIV:
			betreff += ": nicht aktivierter User hat versucht, sich einzuloggen";
			break;

		case USER_CREATED:
			betreff += ": neuer User";
			break;

		case CONFIRMATION_EXPIRED:
			betreff += ": Aktivierungstoken abgelaufen";
			break;

		default:
			break;
		}

		DefaultEmailDaten maildaten = new DefaultEmailDaten();
		maildaten.setEmpfaenger("info@egladil.de");
		maildaten.setBetreff(betreff);
		maildaten.setText(getText());
		return maildaten;
	}

	/**
	 * @return
	 */
	private String getText() {

		String textTemplate = "";
		String zeitpunkt = CommonTimeUtils.format(LocalDateTime.now());
		String resourceOwnerDetails = resourceOwner.getUuid() + " - " + resourceOwner.getFullName() + " ("
			+ resourceOwner.getRoles() + ")";

		switch (kontext) {

		case LOGIN_INAKTIV:
			textTemplate = "Inaktiver User {0} hat am {1} versucht, sich einzuloggen.";
			break;

		case USER_CREATED:
			textTemplate = "Neuer Minikänguruveranstalter {0} am {1} erstellt";
			break;

		case CONFIRMATION_EXPIRED:
			textTemplate = "Benutzerkonto {0} am {1} gelöscht, weil Aktivierungscode angelaufen war.";
			break;

		default:
			break;
		}

		if (textTemplate.isEmpty()) {

			return textTemplate;
		}

		return MessageFormat.format(textTemplate, resourceOwnerDetails, zeitpunkt);
	}

	public enum MinikaengurukontenMailKontext {
		CONFIRMATION_EXPIRED,
		LOGIN_INAKTIV,
		USER_CREATED
	};

}
