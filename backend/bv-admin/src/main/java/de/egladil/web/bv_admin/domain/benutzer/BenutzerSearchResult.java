// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.benutzer;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;

/**
 * BenutzerSearchResult
 */
@Schema(description = "Ergebnis der Suche")
public class BenutzerSearchResult {

	@JsonProperty
	private int anzahlGesamt;

	@JsonProperty
	@Valid
	private List<BenutzerTrefferlisteItem> items;

	public int getAnzahlGesamt() {

		return anzahlGesamt;
	}

	public void setAnzahlGesamt(final int anzahlGsamt) {

		this.anzahlGesamt = anzahlGsamt;
	}

	public List<BenutzerTrefferlisteItem> getItems() {

		return items;
	}

	public void setItems(final List<BenutzerTrefferlisteItem> items) {

		this.items = items;
	}

}
