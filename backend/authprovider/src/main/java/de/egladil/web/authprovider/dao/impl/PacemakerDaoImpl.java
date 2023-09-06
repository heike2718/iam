// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.dao.impl;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.dao.PacemakerDao;
import de.egladil.web.authprovider.domain.Pacemaker;

/**
 * PacemakerDaoImpl
 */
@RequestScoped
public class PacemakerDaoImpl extends BaseDaoImpl implements PacemakerDao {

	private static final Logger LOG = LoggerFactory.getLogger(PacemakerDaoImpl.class);

	/**
	 * Erzeugt eine Instanz von PacemakerDaoImpl
	 */
	public PacemakerDaoImpl() {

	}

	/**
	 * Erzeugt eine Instanz von PacemakerDaoImpl
	 */
	public PacemakerDaoImpl(final EntityManager em) {

		super(em);
	}

	@Override
	public Pacemaker findByMonitorId(final String monitorId) {

		LOG.debug("monitorId='{}'", monitorId);

		String stmt = "select p from Pacemaker p where monitorId = :monitorId";
		TypedQuery<Pacemaker> query = getEm().createQuery(stmt, Pacemaker.class);
		query.setParameter("monitorId", monitorId);

		return query.getSingleResult();
	}

}
