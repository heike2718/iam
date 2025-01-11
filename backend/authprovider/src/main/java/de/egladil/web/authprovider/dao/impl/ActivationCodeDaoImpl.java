// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.dao.impl;

import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import de.egladil.web.authprovider.dao.ActivationCodeDao;
import de.egladil.web.authprovider.entities.ActivationCode;
import de.egladil.web.authprovider.error.AuthRuntimeException;

/**
 * ActivationCodeDaoImpl
 */
@RequestScoped
public class ActivationCodeDaoImpl extends BaseDaoImpl implements ActivationCodeDao {

	/**
	 * Erzeugt eine Instanz von ActivationCodeDaoImpl
	 */
	public ActivationCodeDaoImpl() {

	}

	/**
	 * Erzeugt eine Instanz von ActivationCodeDaoImpl
	 */
	public ActivationCodeDaoImpl(final EntityManager em) {

		super(em);
	}

	@Override
	public Optional<ActivationCode> findByConfirmationCode(final String confirmationCode) {

		TypedQuery<ActivationCode> query = getEm().createNamedQuery("findActivationCodeByConfirmationCode", ActivationCode.class);
		query.setParameter("confirmationCode", confirmationCode);

		List<ActivationCode> trefferliste = query.getResultList();

		if (trefferliste.size() > 1) {

			throw new AuthRuntimeException(
				"mehr als ein Eintrag mit CONFIRM_CODE='" + confirmationCode + "' in Tabelle activationcodes");
		}

		return trefferliste.isEmpty() ? Optional.empty() : Optional.of(trefferliste.get(0));
	}

}
