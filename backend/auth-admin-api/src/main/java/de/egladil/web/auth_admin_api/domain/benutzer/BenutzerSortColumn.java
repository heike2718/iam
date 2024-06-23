// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.benutzer;

import java.util.Arrays;
import java.util.Optional;

/**
 * BenutzerSortColumn
 */
public enum BenutzerSortColumn {

	EMAIL("email"),
	DATE_MODIFIED_STRING("dateModified"),
	NACHNAME("nachname"),
	VORNAME("vorname");

	private final String label;

	private BenutzerSortColumn(final String label) {

		this.label = label;
	}

	public static BenutzerSortColumn valueOfLabel(final String label) {

		Optional<BenutzerSortColumn> opt = Arrays.stream(values()).filter(v -> v.label.equalsIgnoreCase(label)).findFirst();

		if (opt.isPresent()) {

			return opt.get();
		}

		throw new IllegalStateException("unbekanntes Label für die Suche nach USER");
	}

}
