// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.infomails;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.bv_admin.domain.exceptions.AuthAdminAPIRuntimeException;
import de.egladil.web.bv_admin.domain.utils.InfomailvorlageMapper;
import de.egladil.web.bv_admin.infrastructure.persistence.dao.InfomailvorlagenDao;
import de.egladil.web.bv_admin.infrastructure.persistence.entities.PersistenterInfomailText;
import de.egladil.web.bv_admin.infrastructure.persistence.entities.PersistenterInfomailTextReadOnly;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * InfomailvorlagenService kümmert sich um die Vorlagen für Infomails.
 */
@ApplicationScoped
public class InfomailvorlagenService {

	private static final Logger LOGGER = LoggerFactory.getLogger(InfomailvorlagenService.class);

	@Inject
	InfomailvorlagenDao dao;

	public List<InfomailResponseDto> loadInfomailTexte() {

		List<PersistenterInfomailTextReadOnly> trefferliste = dao.loadInfomailTexte();
		return trefferliste.stream().map(InfomailvorlageMapper::mapFromDB).toList();
	}

	/**
	 * Legt einen neuen InfomailText an.
	 *
	 * @param requestPayload
	 * @return InfomailResponseDto
	 */
	public InfomailResponseDto infomailAnlegen(final InfomailRequestDto requestPayload) {

		PersistenterInfomailText persistenterInfomailText = new PersistenterInfomailText();
		persistenterInfomailText.setBetreff(requestPayload.getBetreff());
		persistenterInfomailText.setMailtext(requestPayload.getMailtext());

		String uuid = doSave(persistenterInfomailText);

		PersistenterInfomailTextReadOnly result = dao.findInfomailtextReadOnlyByID(uuid);

		if (result == null) {

			LOGGER.error("extrem unwahrscheinlich: gerade erst angelegt und schon nicht mehr gefunden: uuid={}", uuid);
			throw new AuthAdminAPIRuntimeException("extrem unwahrscheinlich: gerade erst angelegt und schon nicht mehr gefunden");
		}

		return InfomailvorlageMapper.mapFromDB(result);
	}

	/**
	 * Ändert einen vorhandenen Infomailtext.
	 *
	 * @param uuid
	 * @param requestPayload
	 * @return UpdateInfomailResponseDto
	 */
	public UpdateInfomailResponseDto infomailAendern(final String uuid, final InfomailRequestDto requestPayload) {

		PersistenterInfomailText ausDB = dao.findInfomailtextByID(uuid);

		if (ausDB == null) {

			LOGGER.warn("kein INFOMAIL_TEXT mit uuid={} vorhanden", uuid);
			return new UpdateInfomailResponseDto().withUuid(uuid);
		}

		ausDB.setBetreff(requestPayload.getBetreff());
		ausDB.setMailtext(requestPayload.getMailtext());

		doSave(ausDB);

		PersistenterInfomailTextReadOnly result = dao.findInfomailtextReadOnlyByID(uuid);

		if (result == null) {

			LOGGER.error("extrem unwahrscheinlich: gerade erst geaendert und schon nicht mehr gefunden: uuid={}", uuid);
			throw new AuthAdminAPIRuntimeException("extrem unwahrscheinlich: gerade erst geaendert und schon nicht mehr gefunden");
		}

		return new UpdateInfomailResponseDto().withUuid(uuid).withInfomail(InfomailvorlageMapper.mapFromDB(result));

	}

	@Transactional
	String doSave(final PersistenterInfomailText persistenterInfomailText) {

		return dao.saveInfomailText(persistenterInfomailText);
	}

}
