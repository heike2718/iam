// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.benutzer;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Aktivierungsstatus
 */
@Schema(description = "DTO für den neuen Benutzerstatus")
public class Aktivierungsstatus {

	private boolean aktiviert;

	public boolean isAktiviert() {

		return aktiviert;
	}

	public void setAktiviert(final boolean aktiviert) {

		this.aktiviert = aktiviert;
	}

}
