// =====================================================
// Projekt: commons-mailer
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.service.mail;

import java.util.Collection;
import java.util.List;

/**
 * EmailDaten sind die Infos, die zum Erzeugen einer Mail benötigt werden.
 */
public interface EmailDaten {

	/**
	 * Mail an einen einzelnen Empfänger.
	 *
	 * @return String die Zielmailadresse
	 */
	String getEmpfaenger();

	/**
	 * @return String den Betreff
	 */
	String getBetreff();

	/**
	 * @return String den Text (Body)
	 */
	String getText();

	/**
	 * @return Collection von weiteren Empfängern, an die die Mail als BCC, also hidden, versendet wird.
	 */
	Collection<String> getHiddenEmpfaenger();

	/**
	 * @return List vollständige Empfängerliste.
	 */
	List<String> alleEmpfaengerFuersLog();
}
