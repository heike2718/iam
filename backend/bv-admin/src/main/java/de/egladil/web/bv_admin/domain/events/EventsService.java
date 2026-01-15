// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.events;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.bv_admin.infrastructure.persistence.dao.EventDao;
import de.egladil.web.bv_admin.infrastructure.persistence.entities.PersistentesEreignis;

/**
 * EventsService
 */
@ApplicationScoped
public class EventsService {

    private static final Logger LOG = LoggerFactory.getLogger(EventsService.class);

    @Inject
    EventDao eventDao;

    /**
     * Erzeugt einen neuen Eintrag in EVENTS
     *
     * @param event AuthAdminEvent
     */
    public void handleEvent(final AuthAdminEvent event) {

        if (event.writeToEventStore()) {

            String body = event.serializePayload();

            LOG.debug("Event body = " + body);

            PersistentesEreignis storedEvent = PersistentesEreignis
                    .createEvent(event.occuredOn(), event.eventType().getLabel(), body);

            this.eventDao.insertEvent(storedEvent);
        }
    }

}
