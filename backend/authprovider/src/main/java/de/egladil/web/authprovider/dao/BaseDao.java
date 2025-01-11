// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.dao;

import de.egladil.web.authprovider.domain.AuthProviderEntity;
import de.egladil.web.authprovider.error.AuthPersistenceException;
import de.egladil.web.authprovider.error.ConcurrentUpdateException;
import de.egladil.web.authprovider.error.DuplicateEntityException;

/**
 * BaseDao
 */
public interface BaseDao {

	/**
	 * Tja, was wohl.
	 *
	 * @param entity T @return AuthProviderEntity
	 * @throws DuplicateEntityException Die Message ist bei den bekannten UK-Constraints bereits menschenlesbar. Sonst
	 * generisch mit dem constraintName.
	 * @throws ConcurrentUpdateException Die Message wird von der OptimisticLockException übernommen, muss also im
	 * Context noch sinnvoll übersetzt werden.
	 * @throws AuthPersistenceException in diesem Fall wurde die Exception bereits geloggt.
	 */
	<T extends AuthProviderEntity> T save(final T entity)
		throws DuplicateEntityException, ConcurrentUpdateException, AuthPersistenceException;

	/**
	 * Tja, was wohl.
	 *
	 * @param id
	 * @return
	 */
	<T extends AuthProviderEntity> T findById(Class<T> clazz, Long id);

	/**
	 * select count(*)
	 *
	 * @param clazz
	 * @return
	 */
	<T extends AuthProviderEntity> int count(Class<T> clazz);

	/**
	 * Löscht die gegebene Entity.<br>
	 * <br>
	 * <strong>Achtung: </strong> Der Aufrufer muss die Transaction markieren!!!! Es wird nichts geloggt. Das sollte die
	 * aufrufende Klasse tun.
	 *
	 * @param <T>
	 * @param entity
	 * @return boolean true, wenn gelöscht, sonst Exception zwecks Mockito
	 */
	<T extends AuthProviderEntity> boolean delete(final T entity);

}
