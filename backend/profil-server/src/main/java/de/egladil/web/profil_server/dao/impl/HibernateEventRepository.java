// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_server.dao.impl;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import de.egladil.web.profil_server.domain.StoredEvent;
import de.egladil.web.profil_server.event.EventRepository;

/**
 * HibernateEventRepository
 */
@RequestScoped
public class HibernateEventRepository implements EventRepository {

	@Inject
	EntityManager em;

	@Transactional
	@Override
	public void appendEvent(final StoredEvent event) {

		this.em.persist(event);
	}

}
