// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.infrastructure.persistence.dao;

import java.util.List;

import de.egladil.web.auth_admin_api.domain.Jobstatus;
import de.egladil.web.auth_admin_api.infrastructure.persistence.entities.PersistenteMailversandgruppe;
import de.egladil.web.auth_admin_api.infrastructure.persistence.entities.PersistenterInfomailTextReadOnly;
import de.egladil.web.auth_admin_api.infrastructure.persistence.entities.PersistenterMailversandauftrag;
import de.egladil.web.auth_admin_api.infrastructure.persistence.entities.PersistenterUserReadOnly;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

/**
 * MailversandDao
 */
@RequestScoped
public class MailversandDao {

	@Inject
	EntityManager entityManager;

	/**
	 * Gibt den PersistenterInfomailTextReadOnly zur gegebenen uuid zurück oder null.
	 *
	 * @param  uuid
	 *              String
	 * @return      PersistenterInfomailTextReadOnly oder null
	 */
	public PersistenterInfomailTextReadOnly findInfomailtextReadOnlyByID(final String uuid) {

		return entityManager.find(PersistenterInfomailTextReadOnly.class, uuid);
	}

	/**
	 * Läd alle aktivierten PersistenterUserReadOnly mit den gegebenen UUIDs.
	 *
	 * @param  uuids
	 *               List von String
	 * @return       List
	 */
	public List<PersistenterUserReadOnly> findAktivierteUsersByUUIDs(final List<String> uuids) {

		return entityManager.createNamedQuery(PersistenterUserReadOnly.FIND_BY_A_UUID_LIST,
			PersistenterUserReadOnly.class).setParameter("aktiviert", true).setParameter("uuids", uuids).getResultList();
	}

	/**
	 * Tut das, was der Name sagt.
	 *
	 * @param  entity
	 *                PersistenterMailversandauftrag
	 * @return        String
	 */
	public String insertMailversandauftrag(final PersistenterMailversandauftrag entity) {

		entityManager.persist(entity);

		return entity.getUuid();

	}

	/**
	 * Tut das, was der Name sagt.
	 *
	 * @param  entity
	 *                PersistenteMailversandgruppe
	 * @return        String
	 */
	public String insertMailversandgruppe(final PersistenteMailversandgruppe entity) {

		entityManager.persist(entity);

		return entity.getUuid();
	}

	/**
	 * Gibt von allen noch nicht beendeten Mailversandaufträgen den mit dem frühesten Einstelldatum zurück.
	 *
	 * @return PersistenterMailversandauftrag oder null;
	 */
	public PersistenterMailversandauftrag findOldestNotCompletedMailversandauftrag() {

		List<PersistenterMailversandauftrag> trefferliste = entityManager
			.createNamedQuery(PersistenterMailversandauftrag.FIND_NOT_COMPLETED, PersistenterMailversandauftrag.class)
			.setParameter("statusWaiting", Jobstatus.WAITING)
			.setParameter("statusInProgress", Jobstatus.IN_PROGRESS)
			.getResultList();

		return trefferliste.isEmpty() ? null : trefferliste.get(0);
	}

	/**
	 * Mit UUID finden.
	 *
	 * @param  uuid
	 * @return      PersistenterMailversandauftrag oder null
	 */
	public PersistenterMailversandauftrag findMailversandauftragByUUID(final String uuid) {

		return entityManager.find(PersistenterMailversandauftrag.class, uuid);
	}

	/**
	 * Mit UUID finden.
	 *
	 * @param  uuid
	 * @return      PersistenteMailversandgruppe oder null
	 */
	public PersistenteMailversandgruppe findMailversandgruppeByUUID(final String uuid) {

		return entityManager.find(PersistenteMailversandgruppe.class, uuid);
	}

	@Transactional
	public PersistenterMailversandauftrag updateMailversandauftrag(final PersistenterMailversandauftrag entity) {

		return entityManager.merge(entity);

	}

	@Transactional
	public PersistenteMailversandgruppe updateMailversandgruppe(final PersistenteMailversandgruppe entity) {

		return entityManager.merge(entity);

	}

	public List<PersistenteMailversandgruppe> findAllMailversandgruppenWithVersandauftragUUID(final String idVersandauftrag) {

		return entityManager
			.createNamedQuery(PersistenteMailversandgruppe.FIND_BY_VERSANDAUFTAG, PersistenteMailversandgruppe.class)
			.setParameter("idVersandauftrag", idVersandauftrag).getResultList();
	}

	/**
	 * @param persistenterMailversandAuftrag
	 */
	@Transactional
	public void removeMailversandauftrag(final PersistenterMailversandauftrag persistenterMailversandAuftrag) {

		entityManager.remove(persistenterMailversandAuftrag);
	}
}
