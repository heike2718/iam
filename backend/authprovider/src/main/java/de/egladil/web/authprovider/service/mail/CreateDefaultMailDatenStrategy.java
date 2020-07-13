// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.service.mail;

import de.egladil.web.commons_mailer.DefaultEmailDaten;

/**
 * CreateDefaultMailDatenStrategy
 */
public interface CreateDefaultMailDatenStrategy {

	DefaultEmailDaten createEmailDaten();
}
