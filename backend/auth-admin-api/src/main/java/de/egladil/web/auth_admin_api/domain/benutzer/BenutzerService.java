// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.benutzer;

import java.util.List;

import de.egladil.web.auth_admin_api.infrastructure.persistence.dao.BenutzerDao;
import de.egladil.web.auth_admin_api.infrastructure.persistence.entities.PersistenterUserReadOnly;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * BenutzerService
 */
@ApplicationScoped
public class BenutzerService {

	@Inject
	BenutzerDao benutzerDao;

	/**
	 * Sucht die users.
	 *
	 * @param  userSearchDto
	 * @return
	 */
	public BenutzerSearchResult findUsers(final BenutzerSuchparameter userSearchDto) {

		int anzahl = benutzerDao.countTreffer(userSearchDto);
		List<PersistenterUserReadOnly> trefferliste = benutzerDao.findUsers(userSearchDto);
		List<BenutzerTrefferlisteItem> items = trefferliste.stream().map(this::mapFromDB).toList();

		BenutzerSearchResult result = new BenutzerSearchResult();
		result.setAnzahlGesamt(anzahl);
		result.setItems(items);
		return result;
	}

	BenutzerTrefferlisteItem mapFromDB(final PersistenterUserReadOnly fromDB) {

		BenutzerTrefferlisteItem result = new BenutzerTrefferlisteItem();
		result.setAktiviert(fromDB.aktiviert);
		result.setDateModified(fromDB.datumGeaendert);
		result.setEmail(fromDB.email);
		result.setNachname(fromDB.nachname);
		result.setRollen(fromDB.rollen);
		result.setUuid(fromDB.uuid);
		result.setVorname(fromDB.vorname);
		return result;
	}
}
