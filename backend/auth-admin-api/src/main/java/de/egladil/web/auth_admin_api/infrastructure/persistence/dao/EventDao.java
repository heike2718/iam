// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.infrastructure.persistence.dao;

import de.egladil.web.auth_admin_api.infrastructure.persistence.entities.PersistentesEreignis;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

/**
 * EventDao
 */
@RequestScoped
public class EventDao {

	@Inject
	EntityManager entityManager;

	@Transactional
	public void insertEvent(final PersistentesEreignis event) {

		this.entityManager.persist(event);
	}

}
