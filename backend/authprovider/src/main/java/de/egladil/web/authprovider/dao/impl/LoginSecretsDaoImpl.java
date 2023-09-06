//=====================================================
// Project: authprovider
// (c) Heike Winkelvoß
//=====================================================
package de.egladil.web.authprovider.dao.impl;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;

import de.egladil.web.authprovider.dao.LoginSecretsDao;

/**
 * LoginSecretsDaoImpl
 */
@RequestScoped
public class LoginSecretsDaoImpl extends BaseDaoImpl implements LoginSecretsDao {

	/**
	 *
	 */
	public LoginSecretsDaoImpl() {
	}

	/**
	 * @param em
	 */
	public LoginSecretsDaoImpl(final EntityManager em) {
		super(em);
	}

}
