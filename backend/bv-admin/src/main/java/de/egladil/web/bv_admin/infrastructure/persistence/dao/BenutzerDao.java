// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.infrastructure.persistence.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.bv_admin.domain.benutzer.BenutzerSuchparameter;
import de.egladil.web.bv_admin.domain.benutzer.UsersSortColumn;
import de.egladil.web.bv_admin.infrastructure.persistence.entities.PersistenterUser;
import de.egladil.web.bv_admin.infrastructure.persistence.entities.PersistenterUserReadOnly;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

/**
 * BenutzerDao
 */
@RequestScoped
public class BenutzerDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(BenutzerDao.class);

	@Inject
	EntityManager entityManager;

	/**
	 * Zählt alle Treffer.
	 *
	 * @param userSerachDto
	 * @return
	 */
	public int countTreffer(final BenutzerSuchparameter userSerachDto) {

		String stmt = "SELECT count(*) from VW_USERS_SUCHE u ";

		Query query = createQueryAndReplaceSuchparameter(stmt, userSerachDto, Long.class, true);

		@SuppressWarnings("unchecked")
		List<Long> trefferliste = query.getResultList();

		return trefferliste.get(0).intValue();
	}

	/**
	 * Gibt den Teil der Treffer zurück, der mittels pagination-Parameter abgefragt wurde. Dabei werden alle user mit
	 * einer ADMIN-Rolle ausgeschlossen. Wenn diese mal tatsächlich deaktiviert werden müssen, dann eben auf dem Server
	 * in der DB.
	 *
	 * @param benutzerSuchparameter
	 * @return List von PersistenterUserReadOnly
	 */
	@SuppressWarnings("unchecked")
	public List<PersistenterUserReadOnly> findUsers(final BenutzerSuchparameter benutzerSuchparameter) {

		String stmt = "SELECT u.ID, u.UUID, u.VORNAME, u.NACHNAME, u.EMAIL, u.AKTIVIERT, u.ROLLEN, u.DATE_MODIFIED_STRING, u.SLZ_ID from VW_USERS_SUCHE u ";

		int offset = benutzerSuchparameter.getPageIndex() * benutzerSuchparameter.getPageSize();

		Query query = createQueryAndReplaceSuchparameter(stmt, benutzerSuchparameter, PersistenterUserReadOnly.class, false)
			.setFirstResult(offset).setMaxResults(benutzerSuchparameter.getPageSize());

		List<PersistenterUserReadOnly> resultList = query.getResultList();
		return resultList;
	}

	/**
	 * @param uuid String der unique key (fachlicher Schlüssel)
	 * @return PersistenterUserReadOnly oder null
	 */
	public PersistenterUserReadOnly findUserReadonlyByUUID(final String uuid) {

		List<PersistenterUserReadOnly> trefferliste = entityManager
			.createNamedQuery(PersistenterUserReadOnly.FIND_BY_UUID, PersistenterUserReadOnly.class).setParameter("uuid", uuid)
			.getResultList();

		return trefferliste.isEmpty() ? null : trefferliste.get(0);

	}

	/**
	 * @param uuid String der unique key (fachlicher Schlüssel)
	 * @return PersistenterUser oder null
	 */
	public PersistenterUser findUserByUUID(final String uuid) {

		List<PersistenterUser> trefferliste = entityManager.createNamedQuery(PersistenterUser.FIND_BY_UUID, PersistenterUser.class)
			.setParameter("uuid", uuid).getResultList();

		return trefferliste.isEmpty() ? null : trefferliste.get(0);

	}

	/**
	 * @param uuid String der unique key (fachlicher Schlüssel)
	 * @return PersistenterUser oder null
	 */
	public List<PersistenterUserReadOnly> findUsersByUUIDList(final List<String> uuids) {

		List<PersistenterUserReadOnly> trefferliste = entityManager
			.createNamedQuery(PersistenterUserReadOnly.FIND_BY_A_UUID_LIST, PersistenterUserReadOnly.class)
			.setParameter("uuids", uuids).setParameter("aktiviert", true).getResultList();

		return trefferliste;

	}

	/**
	 * Speichert einen vorhandenen user.
	 *
	 * @param user PersistenterUser
	 */
	public void updateUser(final PersistenterUser user) {

		this.entityManager.merge(user);
	}

	@SuppressWarnings("rawtypes")
	Query createQueryAndReplaceSuchparameter(final String stmt, final BenutzerSuchparameter userSearchDto, final Class clazz,
		final boolean forCount) {

		List<String> conditions = new ArrayList<>();

		if (StringUtils.isNotBlank(userSearchDto.getUuid())) {

			conditions.add("u.UUID like :uuid");
		}

		if (StringUtils.isNotBlank(userSearchDto.getEmail())) {

			conditions.add("u.EMAIL like :email");
		}

		if (StringUtils.isNotBlank(userSearchDto.getVorname())) {

			conditions.add("u.VORNAME like :vorname");
		}

		if (StringUtils.isNotBlank(userSearchDto.getNachname())) {

			conditions.add("u.NACHNAME like :nachname");
		}

		if (StringUtils.isNotBlank(userSearchDto.getRolle())) {

			conditions.add("u.ROLLEN like :rollen");
		}

		if (StringUtils.isNotBlank(userSearchDto.getAenderungsdatum())) {

			conditions.add("u.DATE_MODIFIED_STRING like :aenderungsdatum");
		}

		conditions.add("u.ROLLEN not like :excludedString");

		if (conditions.isEmpty()) {

			return entityManager.createNativeQuery(stmt, clazz);
		}

		String joined = stmt + " where " + StringUtils.join(conditions, " and ");

		if (!forCount) {

			if (StringUtils.isNotBlank(userSearchDto.getSortByLabelname())) {

				UsersSortColumn userSortColumn = UsersSortColumn.valueOfLabel(userSearchDto.getSortByLabelname());
				String dbFieldName = userSortColumn.toString();

				String sortDirection = userSearchDto.getSortDirection() == null ? "asc"
					: userSearchDto.getSortDirection().toString();

				joined += " ORDER BY u." + dbFieldName + " " + sortDirection + ", u.ID";
			} else {

				joined += " ORDER BY u.ID";
			}

		}

		Query query = entityManager.createNativeQuery(joined, clazz);

		LOGGER.debug(joined);

		if (StringUtils.isNotBlank(userSearchDto.getUuid())) {

			query.setParameter("uuid", "%" + userSearchDto.getUuid() + "%");
		}

		if (StringUtils.isNotBlank(userSearchDto.getEmail())) {

			query.setParameter("email", "%" + userSearchDto.getEmail() + "%");
		}

		if (StringUtils.isNotBlank(userSearchDto.getVorname())) {

			query.setParameter("vorname", "%" + userSearchDto.getVorname() + "%");
		}

		if (StringUtils.isNotBlank(userSearchDto.getNachname())) {

			query.setParameter("nachname", "%" + userSearchDto.getNachname() + "%");
		}

		if (StringUtils.isNotBlank(userSearchDto.getRolle())) {

			query.setParameter("rollen", "%" + userSearchDto.getRolle() + "%");
		}

		if (StringUtils.isNotBlank(userSearchDto.getAenderungsdatum())) {

			query.setParameter("aenderungsdatum", "%" + userSearchDto.getAenderungsdatum() + "%");
		}

		query.setParameter("excludedString", "%ADMIN%");

		return query;

	}

}
