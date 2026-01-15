// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.service.mail;

/**
 * CreateDefaultMailDatenStrategy
 */
public interface CreateDefaultMailDatenStrategy {

    /**
     * @param betreff
     * @return
     */
    DefaultEmailDaten createEmailDaten(String betreff);
}
