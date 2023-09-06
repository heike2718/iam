// =====================================================
// Projekt: checklistenserver
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.dao.impl;

import java.math.BigInteger;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

import de.egladil.web.authprovider.dao.BaseDao;
import de.egladil.web.authprovider.domain.AuthProviderEntity;

/**
 * BaseDaoImpl
 */
public abstract class BaseDaoImpl implements BaseDao {

	@Inject
	EntityManager em;

	/**
	 * Erzeugt eine Instanz von BaseDaoImpl
	 */
	public BaseDaoImpl() {

	}

	/**
	 * Erzeugt eine Instanz von BaseDaoImpl
	 */
	public BaseDaoImpl(final EntityManager em) {

		super();
		this.em = em;
	}

	@Override
	public <T extends AuthProviderEntity> T findById(final Class<T> clazz, final Long id) {

		return em.find(clazz, id);
	}

	@Override
	public <T extends AuthProviderEntity> int count(final Class<T> clazz) {

		String stmt = "SELECT COUNT(*) from " + clazz.getSimpleName();

		Query query = em.createQuery(stmt);
		BigInteger obj = (BigInteger) query.getSingleResult();

		return obj.intValue();
	}

	@Override
	@Transactional(value = TxType.REQUIRED)
	public <T extends AuthProviderEntity> T save(final T entity) {

		T persisted;

		if (entity.getId() == null) {

			em.persist(entity);
			persisted = entity;
		} else {

			persisted = em.merge(entity);
		}
		return persisted;
	}

	protected EntityManager getEm() {

		return em;
	}

	@Override
	// @Transactional(value = TxType.REQUIRED)
	public <T extends AuthProviderEntity> boolean delete(final T entity) {

		if (entity == null) {

			throw new IllegalArgumentException("entity null");
		}

		em.remove(em.contains(entity) ? entity : em.merge(entity));
		return true;
	}

}
