// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.dao;

import java.util.List;
import java.util.Optional;

import de.egladil.web.authprovider.entities.ResourceOwner;

/**
 * ResourceOwnerDao
 */
public interface ResourceOwnerDao extends BaseDao {

	/**
	 * Suche nach resourceOwner.email.
	 *
	 * @param email String
	 * @return Optional
	 */
	Optional<ResourceOwner> findByEmail(String email);

	/**
	 * Sucht alle USERS mit dem gleichen loginNamen, die eine andere UUID haben.
	 *
	 * @param loginName
	 * @param uuidOwner
	 * @return
	 */
	List<ResourceOwner> findOtherUsersWithSameLoginName(String loginName, String uuidOwner);

	/**
	 * Sucht alle USERS mit der gleichen email, die eine andere UUID haben.
	 *
	 * @param email
	 * @param uuidOwner
	 * @return
	 */
	List<ResourceOwner> findOtherUsersWithSameEmail(String email, String uuidOwner);

	/**
	 * Suche nach resourceOwner.loginName.
	 *
	 * @param email String
	 * @return Optional
	 */
	Optional<ResourceOwner> findByLoginName(String loginName);

	/**
	 * Suche nach resourceOwner.uuid.
	 *
	 * @param email String
	 * @return Optional
	 */
	Optional<ResourceOwner> findByUUID(String uuid);

	/**
	 * @param emailfragment
	 * @return List
	 */
	List<ResourceOwner> findByEmailLike(String emailfragment);

	/**
	 * @param nameFragment
	 * @return List
	 */
	List<ResourceOwner> findByNamenLike(String nameFragment);

	/**
	 * @param uuidFragment
	 * @return List
	 */
	List<ResourceOwner> findByUuidLike(String uuidFragment);

	/**
	 * @param loginnameFragment
	 * @return List
	 */
	List<ResourceOwner> findByLoginnameLike(String loginnameFragment);

	/**
	 * Sucht alle RessourceOwner, bei denen das aktiviert-Flad und das bannedForMails-Flag die angegebenen Werte hat.
	 * @param activated boolean
	 * @param bannedForMails boolean
	 * @return List
	 */
	List<ResourceOwner> findUsersWithActivationAndBannedState(boolean activated, boolean bannedForMails);
}
