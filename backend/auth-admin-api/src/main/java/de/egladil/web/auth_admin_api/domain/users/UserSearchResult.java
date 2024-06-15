// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.users;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;

/**
 * UserSearchResult
 */
public class UserSearchResult {

	@JsonProperty
	private int anzahlGesamt;

	@JsonProperty
	@Valid
	private List<UserTrefferlisteItem> items;

	public int getAnzahlGesamt() {

		return anzahlGesamt;
	}

	public void setAnzahlGesamt(final int anzahlGsamt) {

		this.anzahlGesamt = anzahlGsamt;
	}

	public List<UserTrefferlisteItem> getItems() {

		return items;
	}

	public void setItems(final List<UserTrefferlisteItem> items) {

		this.items = items;
	}

}
