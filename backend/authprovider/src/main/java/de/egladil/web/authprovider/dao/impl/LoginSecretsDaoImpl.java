//=====================================================
// Project: authprovider
// (c) Heike Winkelvoß
//=====================================================
package de.egladil.web.authprovider.dao.impl;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;

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
