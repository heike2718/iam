// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.service.mail;

import java.text.MessageFormat;
import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;

import de.egladil.web.authprovider.event.ResourceOwnerEventPayload;
import de.egladil.web.commons_mailer.DefaultEmailDaten;
import de.egladil.web.commons_net.time.CommonTimeUtils;

/**
 * MinikaengurukontenInfoStrategie
 */
public class MinikaengurukontenInfoStrategie implements CreateDefaultMailDatenStrategy {

	private final ResourceOwnerEventPayload resourceOwner;

	private final MinikaengurukontenMailKontext kontext;

	private final String stage;

	/**
	 * @param resourceOwner
	 */
	public MinikaengurukontenInfoStrategie(final ResourceOwnerEventPayload resourceOwner, final MinikaengurukontenMailKontext kontext, final String stage) {

		this.resourceOwner = resourceOwner;
		this.kontext = kontext;
		this.stage = stage;
	}

	@Override
	public DefaultEmailDaten createEmailDaten(final String messageId) {

		String text = getText();

		if (text.isBlank()) {

			return null;
		}

		String betreff = stage + " - Infomail vom Authprovider";

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

		case SYNC_FAILED:
			betreff += ": Fehler beim Synchronisieren von Benutzerkonten";
			break;

		default:
			break;
		}

		DefaultEmailDaten maildaten = new DefaultEmailDaten();
		maildaten.setEmpfaenger("info@egladil.de");
		maildaten.setBetreff(betreff);
		maildaten.setText(getText());
		maildaten.setMessageId(messageId);
		return maildaten;
	}

	/**
	 * @return
	 */
	private String getText() {

		String textTemplate = "";
		String zeitpunkt = CommonTimeUtils.format(LocalDateTime.now());
		String resourceOwnerDetails = StringUtils.abbreviate(resourceOwner.getUuid(), 11) + " - " + resourceOwner.getVorname() + " "
			+ resourceOwner.getNachname();

		switch (kontext) {

		case LOGIN_INAKTIV:
			textTemplate = "Inaktiver User {0} hat am {1} versucht, sich einzuloggen.";
			break;

		case USER_CREATED:
			textTemplate = "Neues Benutzerkonto {0} am {1} erstellt";
			break;

		case CONFIRMATION_EXPIRED:
			textTemplate = "Benutzerkonto {0} am {1} gelöscht, weil Aktivierungscode angelaufen war.";
			break;

		case SYNC_FAILED:
			textTemplate = "Anlegen oder Löschen des Benutzerkontos {0} konnte am {1} nicht an Client-Application propagiert werden. VServer1 nicht erreichbar?";
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
		USER_CREATED,
		SYNC_FAILED
	};

}
