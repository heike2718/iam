// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.benutzer;

import java.util.Arrays;
import java.util.Optional;

/**
 * UsersSortColumn mapped Frontend-Labelnamen auf column-names in der View VW_USERS_SUCHE.
 */
public enum UsersSortColumn {

	EMAIL("email"),
	DATE_MODIFIED_STRING("aenderungsdatum"),
	NACHNAME("nachname"),
	VORNAME("vorname");

	private final String label;

	private UsersSortColumn(final String label) {

		this.label = label;
	}

	public static UsersSortColumn valueOfLabel(final String label) {

		Optional<UsersSortColumn> opt = Arrays.stream(values()).filter(v -> v.label.equalsIgnoreCase(label)).findFirst();

		if (opt.isPresent()) {

			return opt.get();
		}

		throw new IllegalStateException("unbekanntes Label für die Suche nach USER");
	}

	public String getLabel() {

		return label;
	}
}
