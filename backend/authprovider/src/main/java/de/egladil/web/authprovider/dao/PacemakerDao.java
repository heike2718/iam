//=====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
//=====================================================

package de.egladil.web.authprovider.dao;

import de.egladil.web.authprovider.domain.Pacemaker;

/**
 * PacemakerDao
 */
public interface PacemakerDao extends BaseDao {

	/**
	 * Sucht den Pacemaker mit dem gegebenen fachlichen Schlüssel
	 *
	 * @param monitorId String
	 * @return Pacemaker oder exception
	 */
	Pacemaker findByMonitorId(String monitorId);

}
