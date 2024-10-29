// =====================================================
// Projekt: de.egladil.mkv.persistence
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.validation;

import java.io.Serializable;

import de.egladil.web.auth_validations.annotations.LoginName;
import jakarta.validation.constraints.Size;

/**
 * AdminSuchePayload kapselt die Whitelist für Suchen in MKV-Admin.
 */
public class AdminSuchePayload implements Serializable {

	/* serialVersionUID */
	private static final long serialVersionUID = 1L;

	@LoginName
	@Size(max = 100)
	private final String code;

	/**
	 * Erzeugt eine Instanz von AdminSuchePayload
	 */
	public AdminSuchePayload(final String code) {

		this.code = code;
	}
}
