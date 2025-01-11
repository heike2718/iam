// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.dao.impl;

import java.util.List;
import java.util.Optional;

import de.egladil.web.authprovider.dao.ResourceOwnerDao;
import de.egladil.web.authprovider.entities.ResourceOwner;
import de.egladil.web.authprovider.error.AuthRuntimeException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

/**
 * ResourceOwnerDaoImpl
 */
@RequestScoped
public class ResourceOwnerDaoImpl extends BaseDaoImpl implements ResourceOwnerDao {

	/**
	 * Erzeugt eine Instanz von ResourceOwnerDaoImpl
	 */
	public ResourceOwnerDaoImpl() {

	}

	/**
	 * Erzeugt eine Instanz von ResourceOwnerDaoImpl
	 */
	public ResourceOwnerDaoImpl(final EntityManager em) {

		super(em);
	}

	@Override
	public Optional<ResourceOwner> findByLoginName(final String loginName) {

		if (loginName == null) {

			throw new IllegalArgumentException("loginName null");
		}

		List<ResourceOwner> trefferliste = em.createNamedQuery(ResourceOwner.FIND_BY_LOGINNAME, ResourceOwner.class)
			.setParameter("loginName", loginName).getResultList();

		if (trefferliste.isEmpty()) {

			return Optional.empty();
		}

		if (trefferliste.size() > 1) {

			throw new AuthRuntimeException("Mehr als ein Eintrag mit LOGIN_NAME='" + loginName + "' in tabelle users");
		}
		return Optional.of(trefferliste.get(0));
	}

	@Override
	public List<ResourceOwner> findByLoginnameLike(final String loginnameFragment) {

		if (loginnameFragment == null) {

			throw new IllegalArgumentException("loginnameFragment null");
		}

		return em.createNamedQuery(ResourceOwner.FIND_BY_LOGINNAME_LIKE, ResourceOwner.class)
			.setParameter("email", "%" + loginnameFragment.trim().toLowerCase() + "%").getResultList();
	}

	@Override
	public Optional<ResourceOwner> findByEmail(final String email) {

		if (email == null) {

			throw new IllegalArgumentException("email null");
		}

		List<ResourceOwner> trefferliste = em.createNamedQuery(ResourceOwner.FIND_BY_EMAIL, ResourceOwner.class)
			.setParameter("email", email.toLowerCase()).getResultList();

		if (trefferliste.isEmpty()) {

			return Optional.empty();
		}

		if (trefferliste.size() > 1) {

			throw new AuthRuntimeException("Mehr als ein Eintrag mit EMAIL='" + email + "' in tabelle users");
		}
		return Optional.of(trefferliste.get(0));
	}

	@Override
	public Optional<ResourceOwner> findByUUID(final String uuid) {

		if (uuid == null) {

			throw new IllegalArgumentException("uuid null");
		}

		List<ResourceOwner> trefferliste = em.createNamedQuery(ResourceOwner.FIND_BY_UUID, ResourceOwner.class)
			.setParameter("uuid", uuid).getResultList();

		if (trefferliste.isEmpty()) {

			return Optional.empty();
		}

		if (trefferliste.size() > 1) {

			throw new AuthRuntimeException("Mehr als ein Eintrag mit UUID='" + uuid + "' in tabelle users");
		}
		return Optional.of(trefferliste.get(0));
	}

	@Override
	public List<ResourceOwner> findByEmailLike(final String emailfragment) {

		if (emailfragment == null) {

			throw new IllegalArgumentException("emailfragment null");
		}

		return em.createNamedQuery(ResourceOwner.FIND_BY_EMAIL_LIKE, ResourceOwner.class)
			.setParameter("email", "%" + emailfragment.trim().toLowerCase() + "%").getResultList();
	}

	@Override
	public List<ResourceOwner> findByNamenLike(final String nameFragment) {

		if (nameFragment == null) {

			throw new IllegalArgumentException("nameFragment null");
		}

		String stmt = "select * from USERS u where u.VORNAME IS NOT NULL AND lower(u.VORNAME) LIKE :vorname OR u.NACHNAME IS NOT NULL AND lower(u.NACHNAME) LIKE :nachname";

		Query query = em.createNativeQuery(stmt, ResourceOwner.class);

		query.setParameter("vorname", "%" + nameFragment.trim().toLowerCase() + "%");
		query.setParameter("nachname", "%" + nameFragment.trim().toLowerCase() + "%");

		@SuppressWarnings("unchecked")
		List<ResourceOwner> trefferliste = query.getResultList();
		return trefferliste;
	}

	@Override
	public List<ResourceOwner> findByUuidLike(final String uuidFragment) {

		if (uuidFragment == null) {

			throw new IllegalArgumentException("uuidFragment null");
		}

		return em.createNamedQuery(ResourceOwner.FIND_BY_UUID_LIKE, ResourceOwner.class)
			.setParameter("email", "%" + uuidFragment.trim().toLowerCase() + "%").getResultList();
	}

	@Override
	public List<ResourceOwner> findOtherUsersWithSameLoginName(final String loginName, final String uuidOwner) {

		List<ResourceOwner> resultList = em.createNamedQuery(ResourceOwner.FIND_OTHER_BY_LOGINNAME, ResourceOwner.class)
			.setParameter("uuid", uuidOwner).setParameter("loginName", loginName.toLowerCase()).getResultList();

		return resultList;
	}

	@Override
	public List<ResourceOwner> findOtherUsersWithSameEmail(final String email, final String uuidOwner) {

		List<ResourceOwner> resultList = em.createNamedQuery(ResourceOwner.FIND_OTHER_BY_EMAIL, ResourceOwner.class)
			.setParameter("uuid", uuidOwner).setParameter("email", email.toLowerCase()).getResultList();

		return resultList;
	}
}
