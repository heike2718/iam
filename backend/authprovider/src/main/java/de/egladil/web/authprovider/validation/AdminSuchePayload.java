// =====================================================
// Projekt: de.egladil.mkv.persistence
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.validation;

import java.io.Serializable;

import javax.validation.constraints.Size;

import de.egladil.web.commons_validation.annotations.LoginName;

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
