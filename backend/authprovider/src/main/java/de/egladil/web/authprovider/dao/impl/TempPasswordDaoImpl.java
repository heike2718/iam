//=====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
//=====================================================

package de.egladil.web.authprovider.dao.impl;

import java.util.Optional;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import de.egladil.web.authprovider.dao.TempPasswordDao;
import de.egladil.web.authprovider.entities.TempPassword;

/**
 * TempPasswordDaoImpl
 */
@RequestScoped
public class TempPasswordDaoImpl extends BaseDaoImpl implements TempPasswordDao {

	/**
	 * Erzeugt eine Instanz von TempPasswordDaoImpl
	 */
	public TempPasswordDaoImpl() {
	}

	/**
	 * Erzeugt eine Instanz von TempPasswordDaoImpl
	 */
	public TempPasswordDaoImpl(final EntityManager em) {
		super(em);
	}

	@Override
	public Optional<TempPassword> findByTokenId(final String tokenId) {

		try {
			TempPassword result = getEm().createNamedQuery(TempPassword.FIND_BY_TOKEN_ID, TempPassword.class)
				.setParameter("tokenId", tokenId).getSingleResult();

			return Optional.of(result);

		} catch (NoResultException e) {
			return Optional.empty();
		}
	}

}
