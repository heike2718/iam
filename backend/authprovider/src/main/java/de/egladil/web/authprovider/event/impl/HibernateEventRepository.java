// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.event.impl;

import de.egladil.web.authprovider.event.EventRepository;
import de.egladil.web.authprovider.event.StoredEvent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

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
