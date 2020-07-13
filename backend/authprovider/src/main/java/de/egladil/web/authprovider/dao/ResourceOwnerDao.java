// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.dao;

import java.util.List;
import java.util.Optional;

import de.egladil.web.authprovider.domain.ResourceOwner;

/**
 * ResourceOwnerDao
 */
public interface ResourceOwnerDao extends BaseDao {

	/**
	 * Suche nach resourceOwner.email.
	 *
	 * @param  email
	 *               String
	 * @return       Optional
	 */
	Optional<ResourceOwner> findByEmail(String email);

	/**
	 * Suche nach resourceOwner.loginName.
	 *
	 * @param  email
	 *               String
	 * @return       Optional
	 */
	Optional<ResourceOwner> findByLoginName(String loginName);

	/**
	 * Suche nach resourceOwner.uuid.
	 *
	 * @param  email
	 *               String
	 * @return       Optional
	 */
	Optional<ResourceOwner> findByUUID(String uuid);

	/**
	 * @param  emailfragment
	 * @return               List
	 */
	List<ResourceOwner> findByEmailLike(String emailfragment);

	/**
	 * @param  nameFragment
	 * @return              List
	 */
	List<ResourceOwner> findByNamenLike(String nameFragment);

	/**
	 * @param  uuidFragment
	 * @return              List
	 */
	List<ResourceOwner> findByUuidLike(String uuidFragment);

	/**
	 * @param  loginnameFragment
	 * @return                   List
	 */
	List<ResourceOwner> findByLoginnameLike(String loginnameFragment);
}
