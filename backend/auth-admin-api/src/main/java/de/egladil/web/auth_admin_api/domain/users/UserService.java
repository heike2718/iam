// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.users;

import java.time.format.DateTimeFormatter;
import java.util.List;

import de.egladil.web.auth_admin_api.infrastructure.persistence.dao.UserDao;
import de.egladil.web.auth_admin_api.infrastructure.persistence.entities.PersistenterUserReadOnly;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * UserService
 */
@ApplicationScoped
public class UserService {

	private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Inject
	UserDao userDao;

	/**
	 * Sucht die users.
	 *
	 * @param  userSearchDto
	 * @return
	 */
	public UserSearchResult findUsers(final UserSearchDto userSearchDto) {

		int anzahl = userDao.countTreffer(userSearchDto);
		List<PersistenterUserReadOnly> trefferliste = userDao.findUsers(userSearchDto);
		List<UserTrefferlisteItem> items = trefferliste.stream().map(this::mapFromDB).toList();

		UserSearchResult result = new UserSearchResult();
		result.setAnzahlGesamt(anzahl);
		result.setItems(items);
		return result;
	}

	UserTrefferlisteItem mapFromDB(final PersistenterUserReadOnly fromDB) {

		UserTrefferlisteItem result = new UserTrefferlisteItem();
		result.setAktiviert(fromDB.aktiviert);
		result.setDateModified(fromDB.datumGeaendert);
		// result.setDateModified(
		// dateTimeFormatter.format(LocalDateTime.ofInstant(fromDB.getDatumGeaendert().toInstant(), ZoneId.systemDefault())));
		result.setEmail(fromDB.email);
		result.setNachname(fromDB.nachname);
		result.setRollen(fromDB.rollen);
		result.setUuid(fromDB.uuid);
		result.setVorname(fromDB.vorname);
		return result;
	}
}
