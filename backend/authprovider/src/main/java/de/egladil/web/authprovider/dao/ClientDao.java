//=====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
//=====================================================

package de.egladil.web.authprovider.dao;

import de.egladil.web.authprovider.entities.Client;

/**
 * ClientDao
 */
public interface ClientDao extends BaseDao {

	/**
	 *
	 * @param clientId String
	 * @return Client oder null
	 */
	Client findByClientId(String clientId);

}
