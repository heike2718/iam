// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.dao;

import java.util.Optional;

import de.egladil.web.authprovider.entities.ActivationCode;

/**
 * ActivationCodeDaoImpl
 */
public interface ActivationCodeDao extends BaseDao {

    /**
     * Sucht den Eintrag mit dem (eindeutigen) confirmationCode.
     *
     * @param confirmationCode
     * @return
     */
    Optional<ActivationCode> findByConfirmationCode(String confirmationCode);

}
