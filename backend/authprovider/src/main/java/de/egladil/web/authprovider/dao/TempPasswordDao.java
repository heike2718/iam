//=====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
//=====================================================

package de.egladil.web.authprovider.dao;

import java.util.Optional;

import de.egladil.web.authprovider.entities.TempPassword;

/**
 * TempPasswordDao
 */
public interface TempPasswordDao extends BaseDao {

	/**
	 * Sucht den Eintrag mit dem (eindeutiger) tokenID.
	 *
	 * @param tokenId String
	 * @return Optional
	 */
	Optional<TempPassword> findByTokenId(String tokenId);

}
