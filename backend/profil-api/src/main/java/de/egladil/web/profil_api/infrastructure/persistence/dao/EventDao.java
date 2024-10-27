// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_api.infrastructure.persistence.dao;

import de.egladil.web.profil_api.infrastructure.persistence.entities.StoredEvent;
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
	public void insertEvent(final StoredEvent event) {

		this.entityManager.persist(event);
	}
}
