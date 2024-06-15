// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.users;

import java.util.Arrays;
import java.util.Optional;

/**
 * UserSortColumn
 */
public enum UserSortColumn {

	EMAIL("email"),
	DATE_MODIFIED_STRING("datumGeaendert"),
	NACHNAME("nachname"),
	VORNAME("vorname");

	private final String label;

	private UserSortColumn(final String label) {

		this.label = label;
	}

	public static UserSortColumn valueOfLabel(final String label) {

		Optional<UserSortColumn> opt = Arrays.stream(values()).filter(v -> v.label.equalsIgnoreCase(label)).findFirst();

		if (opt.isPresent()) {

			return opt.get();
		}

		throw new IllegalStateException("unbekanntes Label für die Suche nach USER");
	}

}
