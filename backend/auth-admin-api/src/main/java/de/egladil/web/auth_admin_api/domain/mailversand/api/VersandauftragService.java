// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.mailversand.api;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.auth_admin_api.domain.Jobstatus;
import de.egladil.web.auth_admin_api.domain.auth.dto.MessagePayload;
import de.egladil.web.auth_admin_api.domain.exceptions.AuthAdminAPIRuntimeException;
import de.egladil.web.auth_admin_api.domain.exceptions.AuthAdminSQLExceptionHelper;
import de.egladil.web.auth_admin_api.domain.exceptions.ConflictException;
import de.egladil.web.auth_admin_api.domain.utils.AuthAdminCollectionUtils;
import de.egladil.web.auth_admin_api.infrastructure.persistence.dao.MailversandDao;
import de.egladil.web.auth_admin_api.infrastructure.persistence.entities.PersistenteMailversandgruppe;
import de.egladil.web.auth_admin_api.infrastructure.persistence.entities.PersistenterInfomailTextReadOnly;
import de.egladil.web.auth_admin_api.infrastructure.persistence.entities.PersistenterMailversandauftrag;
import de.egladil.web.auth_admin_api.infrastructure.persistence.entities.PersistenterMailversandauftragReadOnly;
import de.egladil.web.auth_admin_api.infrastructure.persistence.entities.PersistenterUserReadOnly;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

/**
 * VersandauftragService
 */
@ApplicationScoped
public class VersandauftragService {

	private static final Logger LOGGER = LoggerFactory.getLogger(VersandauftragService.class);

	private static final DateTimeFormatter DATE_TIME_FORMATTER_JAHR_MONAT = DateTimeFormatter.ofPattern("yyyy-MM");

	private static final DateTimeFormatter DATE_TIME_FORMATTER_DEFAULT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

	private static final int MAX_DB_IN_BUNCH_SIZE = 1000;

	@ConfigProperty(name = "emails.groupsize", defaultValue = "50")
	int emailsGroupSize;

	@Inject
	MailversandDao dao;

	public List<MailversandauftragOverview> versandauftraegeLaden() {

		List<PersistenterMailversandauftragReadOnly> ausDB = dao.loadAllMailversandauftraege();

		return ausDB.stream().map(this::mapFromDBToOverview).toList();
	}

	/**
	 * Läd die Details eines Mailversandauftrags.
	 *
	 * @param  uuid
	 * @return      MailversandauftragDetailsResponseDto
	 */
	public MailversandauftragDetailsResponseDto detailsLaden(final String uuid) {

		PersistenterMailversandauftrag fromDB = dao.findMailversandauftragByUUID(uuid);
		MailversandauftragDetailsResponseDto result = new MailversandauftragDetailsResponseDto();
		result.setUuid(uuid);

		if (fromDB == null) {

			LOGGER.warn("Mailversandauftrag mit UUID={} existiert nicht oder nicht mehr", StringUtils.abbreviate(uuid, 11));
			return result;

		}

		List<PersistenteMailversandgruppe> versandgruppen = dao.findAllMailversandgruppenWithVersandauftragUUID(uuid);

		MailversandauftragDetails versandauftrag = mapFromDBToDetails(fromDB);
		List<Mailversandgruppe> gruppen = versandgruppen.stream().map(this::mapFromDB).toList();
		versandauftrag.setMailversandgruppen(gruppen);
		result.setVersandauftrag(versandauftrag);
		return result;
	}

	/**
	 * Legt einen neuen Mailversandauftrag mit den zugehörigen Gruppen an.
	 *
	 * @param  requestDto
	 * @return            MailversandauftragOverview
	 */
	public MailversandauftragOverview versandauftragAnlegen(final MailversandauftragRequestDto requestDto) {

		if (requestDto.getBenutzerUUIDs().isEmpty()) {

			String message = "benutzerIds waren leer. Mindestens 1 Empfänger wird benötigt.";
			LOGGER.error(message, requestDto.getIdInfomailtext());

			try (Response response = Response.status(412)
				.entity(MessagePayload.error(message)).build()) {

				throw new WebApplicationException(response);

			}
		}

		PersistenterInfomailTextReadOnly infomailtext = dao.findInfomailtextReadOnlyByID(requestDto.getIdInfomailtext());

		if (infomailtext == null) {

			LOGGER.error("Infomailtext mit UUID={} existiert nicht oder nicht mehr", requestDto.getIdInfomailtext());

			try (Response response = Response.status(412)
				.entity(MessagePayload.error("kein Infomailtext mit der gegebenen UUID vorhanden")).build()) {

				throw new WebApplicationException(response);

			}
		}

		String checksum = calculateChecksum(requestDto.getBenutzerUUIDs());
		List<List<String>> confirmedUUIDGroups = getAllConfirmedUUIDsInGroups(requestDto);

		try {

			return createNewVersandauftrag(infomailtext, confirmedUUIDGroups, checksum);
		} catch (Exception e) {

			ConstraintViolationException cve = AuthAdminSQLExceptionHelper.unwrappConstraintViolationException(e,
				"Beim Anlegen eines Mailversandauftrags ist etwas schiefgegaben");

			LOGGER.error("infomailtext={}, checksum={}: {}", infomailtext.uuid, checksum, cve.getMessage());

			throw new ConflictException("An diesen Benutzerkreis wurde in diesem Monat bereits eine Mail versendet.");
		}
	}

	private List<List<String>> getAllConfirmedUUIDsInGroups(final MailversandauftragRequestDto requestDto) {

		List<List<String>> uuidGroups = AuthAdminCollectionUtils.groupTheStrings(requestDto.getBenutzerUUIDs(),
			MAX_DB_IN_BUNCH_SIZE);
		List<List<String>> confirmedUUIDGroups = extractConfirmedUUIDs(uuidGroups);
		List<String> confirmedUUIDs = AuthAdminCollectionUtils.joinTheGroups(confirmedUUIDGroups);
		return AuthAdminCollectionUtils.groupTheStrings(confirmedUUIDs, emailsGroupSize);
	}

	@Transactional
	MailversandauftragOverview createNewVersandauftrag(final PersistenterInfomailTextReadOnly infomailtext, final List<List<String>> confirmedUUIDGroups, final String checksum) {

		long anzahlEmpfaenger = AuthAdminCollectionUtils.countElements(confirmedUUIDGroups);
		Date geaendertAm = new Date();
		LocalDateTime now = LocalDateTime.now();

		PersistenterMailversandauftrag persistenterVersandauftrag = new PersistenterMailversandauftrag();
		persistenterVersandauftrag.setAnzahlEmpfaenger(anzahlEmpfaenger);
		persistenterVersandauftrag.setChecksumEmpfaengerIDs(checksum);
		persistenterVersandauftrag.setErfasstAm(now);
		persistenterVersandauftrag.setGeaendertAm(geaendertAm);
		persistenterVersandauftrag.setVersandJahrMonat(DATE_TIME_FORMATTER_JAHR_MONAT.format(now));
		persistenterVersandauftrag.setIdInfomailtext(infomailtext.uuid);
		persistenterVersandauftrag.setStatus(Jobstatus.WAITING);

		String versandauftragUuid = dao.insertMailversandauftrag(persistenterVersandauftrag);

		int sortnr = 0;

		for (List<String> uuidGroup : confirmedUUIDGroups) {

			PersistenteMailversandgruppe gruppe = new PersistenteMailversandgruppe();
			gruppe.setEmpfaengerUUIDs(StringUtils.join(uuidGroup, ","));
			gruppe.setGeaendertAm(geaendertAm);
			gruppe.setIdVersandauftrag(versandauftragUuid);
			gruppe.setSortnr(++sortnr);
			gruppe.setStatus(Jobstatus.WAITING);

			dao.insertMailversandgruppe(gruppe);

		}

		MailversandauftragOverview result = new MailversandauftragOverview();
		result.setAnzahlEmpfaenger(anzahlEmpfaenger);
		result.setAnzahlGruppen(confirmedUUIDGroups.size());
		result.setBetreff(infomailtext.betreff);
		result.setStatus(persistenterVersandauftrag.getStatus());
		result.setUuid(versandauftragUuid);
		result.setIdInfomailtext(infomailtext.uuid);

		return result;

	}

	/**
	 * @param  idGroups
	 * @return
	 */
	List<List<String>> extractConfirmedUUIDs(final List<List<String>> idGroups) {

		List<List<String>> emailAddressGroups = new ArrayList<>();

		for (List<String> idGroup : idGroups) {

			List<String> theUUIDs = loadAllUsersAktiviert(idGroup);

			if (!theUUIDs.isEmpty()) {

				emailAddressGroups.add(theUUIDs);
			}
		}
		return emailAddressGroups;
	}

	private List<String> loadAllUsersAktiviert(final List<String> benutzerIDs) {

		List<PersistenterUserReadOnly> users = dao.findAktivierteUsersByUUIDs(benutzerIDs);
		return users.stream().map(u -> u.uuid).toList();
	}

	String calculateChecksum(final List<String> uuids) {

		Collections.sort(uuids);

		String idsString = StringUtils.join(uuids, ",");

		MessageDigest md;

		try {

			md = MessageDigest.getInstance("SHA-256");
			byte[] hashBytes = md.digest(idsString.getBytes());
			StringBuilder sb = new StringBuilder();

			for (byte b : hashBytes) {

				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {

			LOGGER.error("Exception beim Berechnen der Checksumme der Benutzer-UUIDs: {}", e.getMessage(), e);
			throw new AuthAdminAPIRuntimeException("Exception beim Berechnen der Checksumme der Benutzer-UUIDs");
		}

	}

	MailversandauftragOverview mapFromDBToOverview(final PersistenterMailversandauftragReadOnly fromDB) {

		MailversandauftragOverview result = new MailversandauftragOverview();
		result.setAnzahlEmpfaenger(fromDB.anzahlEmpfaenger);
		result.setAnzahlGruppen(fromDB.anzahlGruppen);
		result.setBetreff(fromDB.betreff);
		result.setIdInfomailtext(fromDB.idInfomailtext);
		result.setStatus(fromDB.status);
		result.setUuid(fromDB.uuid);
		result.setErfasstAm(DATE_TIME_FORMATTER_DEFAULT.format(fromDB.erfasstAm));

		return result;

	}

	MailversandauftragDetails mapFromDBToDetails(final PersistenterMailversandauftrag fromDB) {

		MailversandauftragDetails versandauftrag = new MailversandauftragDetails();
		versandauftrag.setAnzahlEmpfaenger(fromDB.getAnzahlEmpfaenger());
		versandauftrag.setAnzahlVersendet(fromDB.getAnzahlVersendet());
		versandauftrag.setErfasstAm(DATE_TIME_FORMATTER_DEFAULT.format(fromDB.getErfasstAm()));
		versandauftrag.setIdInfomailtext(fromDB.getIdInfomailtext());
		versandauftrag.setMailversandgruppen(new ArrayList<>());
		versandauftrag.setStatus(fromDB.getStatus());
		versandauftrag.setUuid(fromDB.getUuid());

		if (fromDB.getVersandBeendetAm() != null) {

			versandauftrag.setVersandBeendetAm(DATE_TIME_FORMATTER_DEFAULT.format(fromDB.getVersandBeendetAm()));
		}

		if (fromDB.getVersandBegonnenAm() != null) {

			versandauftrag.setVersandBegonnenAm(DATE_TIME_FORMATTER_DEFAULT.format(fromDB.getVersandBegonnenAm()));
		}

		versandauftrag.setVersandJahrMonat(fromDB.getVersandJahrMonat());
		versandauftrag.setVersandMitFehlern(fromDB.isVersandMitFehlern());

		return versandauftrag;
	}

	Mailversandgruppe mapFromDB(final PersistenteMailversandgruppe fromDB) {

		Mailversandgruppe result = new Mailversandgruppe();

		if (StringUtils.isNotBlank(fromDB.getEmpfaengerUUIDs())) {

			result.setEmpfaengerUUIDs(Arrays.asList(StringUtils.split(fromDB.getEmpfaengerUUIDs(), ",")));
		}

		result.setIdMailversandauftrag(fromDB.getIdVersandauftrag());
		result.setSortnr(fromDB.getSortnr());
		result.setStatus(fromDB.getStatus());
		result.setUuid(fromDB.getUuid());
		return result;

	}
}
