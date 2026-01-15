// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.infrastructure.persistence.dao;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import de.egladil.web.bv_admin.infrastructure.persistence.entities.PersistentesEreignis;

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
