// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.dao.impl;

import java.util.List;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.dao.ClientDao;
import de.egladil.web.authprovider.entities.Client;
import de.egladil.web.authprovider.error.AuthRuntimeException;

/**
 * ClientDaoImpl
 */
@RequestScoped
public class ClientDaoImpl extends BaseDaoImpl implements ClientDao {

	private static final Logger LOG = LoggerFactory.getLogger(ClientDaoImpl.class);

	/**
	 * Erzeugt eine Instanz von ClientDaoImpl
	 */
	public ClientDaoImpl() {

	}

	/**
	 * Erzeugt eine Instanz von ClientDaoImpl
	 */
	public ClientDaoImpl(final EntityManager em) {

		super(em);
	}

	@Override
	public Client findByClientId(final String clientId) {

		String stmt = "select c from Client c where clientId = :clientId";
		TypedQuery<Client> query = getEm().createQuery(stmt, Client.class);
		query.setParameter("clientId", clientId);

		List<Client> trefferliste = query.getResultList();

		if (trefferliste.size() == 1) {

			LOG.debug("Client mit clientId={} gefunden", StringUtils.abbreviate(clientId, 15));
			return trefferliste.get(0);
		}

		throw new AuthRuntimeException(
			"keinen oder mehr als ein Client mit clientId=" + StringUtils.abbreviate(clientId, 15) + " gefunden");

	}
}
