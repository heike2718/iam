// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.dao.PacemakerDao;
import de.egladil.web.authprovider.entities.Pacemaker;
import de.egladil.web.authprovider.payload.MessagePayload;
import de.egladil.web.authprovider.payload.ResponsePayload;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

/**
 * HeartbeatService
 */
@RequestScoped
public class HeartbeatService {

	private static final String MONITOR_ID = "authprovider-database";

	private static final Logger LOG = LoggerFactory.getLogger(HeartbeatService.class);

	@Inject
	PacemakerDao pacemakerDao;

	/**
	 * Erzeugt eine Instanz von HeartbeatService
	 */
	public HeartbeatService() {

	}

	/**
	 * Erzeugt eine Instanz von HeartbeatService zum Testen ohne DB.
	 */
	public HeartbeatService(final PacemakerDao pacemakerDao) {

		super();
		this.pacemakerDao = pacemakerDao;
	}

	public ResponsePayload update() {

		try {

			Pacemaker pacemaker = pacemakerDao.findByMonitorId(HeartbeatService.MONITOR_ID);
			pacemaker.setWert("wert-" + System.currentTimeMillis());
			Pacemaker result = pacemakerDao.save(pacemaker);
			LOG.debug("pacemaker aktualisiert: {}", result);
			return ResponsePayload.messageOnly(MessagePayload.info(HeartbeatService.MONITOR_ID + " lebt"));
		} catch (Exception e) {

			String msg = "Fehler beim Speichern des pacemakers " + HeartbeatService.MONITOR_ID + ": " + e.getMessage();
			LOG.error("Fehler beim updaten des pacemakers: " + e.getMessage(), e);
			return ResponsePayload.messageOnly(MessagePayload.error(msg));
		}
	}
}
