// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.event.impl;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import de.egladil.web.authprovider.event.EventRepository;
import de.egladil.web.authprovider.event.StoredEvent;

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
