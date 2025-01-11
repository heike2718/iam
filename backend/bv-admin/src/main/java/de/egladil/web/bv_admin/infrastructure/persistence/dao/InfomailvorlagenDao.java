// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.infrastructure.persistence.dao;

import java.util.Date;
import java.util.List;

import de.egladil.web.bv_admin.infrastructure.persistence.entities.PersistenterInfomailText;
import de.egladil.web.bv_admin.infrastructure.persistence.entities.PersistenterInfomailTextReadOnly;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

/**
 * InfomailvorlagenDao
 */
@RequestScoped
public class InfomailvorlagenDao {

	@Inject
	EntityManager entityManager;

	/**
	 * Läd alle InfomailsTexte
	 *
	 * @return
	 */
	public List<PersistenterInfomailTextReadOnly> loadInfomailTexte() {

		return entityManager.createNamedQuery(PersistenterInfomailTextReadOnly.LOAD_ALL, PersistenterInfomailTextReadOnly.class)
			.getResultList();
	}

	/**
	 * Gibt den PersistenterInfomailTextReadOnly zur gegebenen uuid zurück oder null.
	 *
	 * @param uuid String
	 * @return PersistenterInfomailTextReadOnly oder null
	 */
	public PersistenterInfomailTextReadOnly findInfomailtextReadOnlyByID(final String uuid) {

		return entityManager.find(PersistenterInfomailTextReadOnly.class, uuid);
	}

	/**
	 * Gibt den PersistenterInfomailText zur gegebenen uuid zurück oder null.
	 *
	 * @param uuid String
	 * @return PersistenterInfomailText oder null
	 */
	public PersistenterInfomailText findInfomailtextByID(final String uuid) {

		return entityManager.find(PersistenterInfomailText.class, uuid);
	}

	/**
	 * Speichert den gegebenen Infomailtext
	 *
	 * @param persistenterInfomailText
	 * @return String die uuid
	 */
	public String saveInfomailText(final PersistenterInfomailText persistenterInfomailText) {

		persistenterInfomailText.setGeaendertAm(new Date());

		if (persistenterInfomailText.getUuid() != null) {

			entityManager.merge(persistenterInfomailText);

		} else {

			entityManager.persist(persistenterInfomailText);
		}

		return persistenterInfomailText.getUuid();
	}

}
