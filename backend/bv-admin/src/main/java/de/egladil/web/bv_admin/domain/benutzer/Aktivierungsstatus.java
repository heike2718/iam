// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.benutzer;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Aktivierungsstatus
 */
@Schema(description = "DTO für den neuen Benutzerstatus")
@Deprecated(forRemoval = true)
public class Aktivierungsstatus {

	private boolean aktiviert;

	public boolean isAktiviert() {

		return aktiviert;
	}

	public void setAktiviert(final boolean aktiviert) {

		this.aktiviert = aktiviert;
	}

}
