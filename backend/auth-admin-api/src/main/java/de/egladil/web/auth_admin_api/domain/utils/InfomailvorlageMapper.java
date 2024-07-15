// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.utils;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import de.egladil.web.auth_admin_api.domain.infomails.InfomailResponseDto;
import de.egladil.web.auth_admin_api.infrastructure.persistence.entities.PersistenterInfomailTextReadOnly;

/**
 * InfomailvorlageMapper
 */
public class InfomailvorlageMapper {

	/**
	 * Mapped PersistenterInfomailTextReadOnly zu einem Dto.
	 *
	 * @param  fromDB
	 * @return
	 */
	public static InfomailResponseDto mapFromDB(final PersistenterInfomailTextReadOnly fromDB) {

		InfomailResponseDto result = new InfomailResponseDto();
		result.setUuid(fromDB.uuid);
		result.setBetreff(fromDB.betreff);
		result.setMailtext(fromDB.mailtext);

		if (fromDB.uuidsMailversandauftraege != null) {

			result.setUuidsMailversandauftraege(Arrays.asList(StringUtils.split(fromDB.uuidsMailversandauftraege, ",")));
		}

		return result;

	}

}
