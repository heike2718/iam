// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.infrastructure.persistence.dao;

import de.egladil.web.auth_admin_api.infrastructure.persistence.entities.Salt;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

/**
 * SaltDao
 */
@RequestScoped
public class SaltDao {

	@Inject
	EntityManager entityManager;

	/**
	 * Holt das Salt mit der gegebenen ID.
	 *
	 * @param  id
	 * @return    Salt oder null
	 */
	public Salt findSaltByID(final Long id) {

		return entityManager.find(Salt.class, id);

	}

	/**
	 * Löscht den Eintrag mit der gegebenen ID.<br>
	 * <br>
	 * Wegen der FK-Konstruktion wird die bis zum USER kaskadiert.
	 *
	 * @param id
	 */
	public void deleteSaltAndCascade(final Long id) {

		Salt salt = findSaltByID(id);

		if (salt != null) {

			entityManager.remove(salt);
		}

	}

}
