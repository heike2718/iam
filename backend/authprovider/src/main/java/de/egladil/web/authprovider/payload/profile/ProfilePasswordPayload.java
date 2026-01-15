// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.payload.profile;

import jakarta.validation.constraints.NotNull;

import de.egladil.web.auth_validations.annotations.PasswortLogin;
import de.egladil.web.auth_validations.annotations.ValidPasswords;
import de.egladil.web.auth_validations.dto.ZweiPassworte;
import de.egladil.web.auth_validations.utils.SecUtils;

/**
 * ProfilePasswordPayload
 */
public class ProfilePasswordPayload {

    @NotNull
    @PasswortLogin
    private String passwort;

    @NotNull
    @ValidPasswords
    private ZweiPassworte zweiPassworte;

    /**
     * Entfernt alle sensiblen Infos: also password und passwordWdh.
     */
    public void clean() {

        if (zweiPassworte != null) {

            zweiPassworte.clean();
            zweiPassworte = null;
        }

        if (passwort != null) {

            passwort = SecUtils.wipe(passwort);
        }
    }

    public ZweiPassworte getZweiPassworte() {

        return zweiPassworte;
    }

    public void setZweiPassworte(final ZweiPassworte zweiPassworte) {

        this.zweiPassworte = zweiPassworte;
    }

    public String getPasswort() {

        return passwort;
    }

    public void setPasswort(final String passwort) {

        this.passwort = passwort;
    }

}
