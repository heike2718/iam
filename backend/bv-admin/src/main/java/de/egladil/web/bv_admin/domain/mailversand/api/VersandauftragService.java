// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.mailversand.api;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.bv_admin.domain.Jobstatus;
import de.egladil.web.bv_admin.domain.auth.dto.MessagePayload;
import de.egladil.web.bv_admin.domain.benutzer.BenutzerService;
import de.egladil.web.bv_admin.domain.benutzer.BenutzerTrefferlisteItem;
import de.egladil.web.bv_admin.domain.exceptions.AuthAdminAPIRuntimeException;
import de.egladil.web.bv_admin.domain.exceptions.AuthAdminSQLExceptionHelper;
import de.egladil.web.bv_admin.domain.exceptions.ConflictException;
import de.egladil.web.bv_admin.domain.utils.AuthAdminCollectionUtils;
import de.egladil.web.bv_admin.infrastructure.persistence.dao.MailversandDao;
import de.egladil.web.bv_admin.infrastructure.persistence.entities.PersistenteMailversandgruppe;
import de.egladil.web.bv_admin.infrastructure.persistence.entities.PersistenterInfomailTextReadOnly;
import de.egladil.web.bv_admin.infrastructure.persistence.entities.PersistenterMailversandauftrag;
import de.egladil.web.bv_admin.infrastructure.persistence.entities.PersistenterMailversandauftragReadOnly;
import de.egladil.web.bv_admin.infrastructure.persistence.entities.PersistenterUserReadOnly;
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

	/**
	 *
	 */
	private static final String GELOESCHT = "geloescht";

	private static final Logger LOGGER = LoggerFactory.getLogger(VersandauftragService.class);

	private static final DateTimeFormatter DATE_TIME_FORMATTER_JAHR_MONAT = DateTimeFormatter.ofPattern("yyyy-MM");

	private static final DateTimeFormatter DATE_TIME_FORMATTER_DEFAULT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

	private static final int MAX_DB_IN_BUNCH_SIZE = 1000;

	@ConfigProperty(name = "emails.groupsize", defaultValue = "50")
	int emailsGroupSize;

	@Inject
	MailversandDao mailversandDao;

	@Inject
	BenutzerService benutzerService;

	public List<MailversandauftragOverview> versandauftraegeLaden() {

		List<PersistenterMailversandauftragReadOnly> ausDB = mailversandDao.loadAllMailversandauftraege();

		return ausDB.stream().map(this::mapFromDBToOverview).toList();
	}

	/**
	 * Läd die Details eines Mailversandauftrags.
	 *
	 * @param uuid
	 * @return MailversandauftragDetailsResponseDto
	 */
	public MailversandauftragDetailsResponseDto detailsMailversandauftragLaden(final String uuid) {

		PersistenterMailversandauftrag fromDB = mailversandDao.findMailversandauftragByUUID(uuid);
		MailversandauftragDetailsResponseDto result = new MailversandauftragDetailsResponseDto();
		result.setUuid(uuid);

		if (fromDB == null) {

			LOGGER.warn("Mailversandauftrag mit UUID={} existiert nicht oder nicht mehr", StringUtils.abbreviate(uuid, 11));
			return result;

		}

		List<PersistenteMailversandgruppe> versandgruppen = mailversandDao.findAllMailversandgruppenWithVersandauftragUUID(uuid);

		MailversandauftragDetails versandauftrag = mapFromDBToDetails(fromDB);
		List<Mailversandgruppe> gruppen = versandgruppen.stream().map(this::mapFromDB).toList();
		versandauftrag.setMailversandgruppen(gruppen);
		result.setVersandauftrag(versandauftrag);
		return result;
	}

	/**
	 * Läd die Deteils einer Mailversandgruppe und die Liste der Benutzer, an die die Mail versendet wurde.
	 *
	 * @param gruppeUuid
	 * @return MailversandgruppeDetailsResponseDto
	 */
	public MailversandgruppeDetailsResponseDto detailsMailversandgruppeLaden(final String gruppeUuid) {

		PersistenteMailversandgruppe gruppeDB = mailversandDao.findMailversandgruppeByUUID(gruppeUuid);
		MailversandgruppeDetailsResponseDto result = new MailversandgruppeDetailsResponseDto();
		result.setUuid(gruppeUuid);

		if (gruppeDB == null) {

			return result;
		}

		PersistenterMailversandauftrag auftragDB = mailversandDao.findMailversandauftragByUUID(gruppeDB.getIdVersandauftrag());

		MailversandgruppeDetails gruppeDetails = new MailversandgruppeDetails();
		gruppeDetails.setAenderungsdatum(DATE_TIME_FORMATTER_DEFAULT.format(gruppeDB.getGeaendertAm()));
		gruppeDetails.setSortnr(gruppeDB.getSortnr());
		gruppeDetails.setStatus(gruppeDB.getStatus());
		gruppeDetails.setUuid(gruppeUuid);
		gruppeDetails.setIdInfomailtext(auftragDB.getIdInfomailtext());

		List<String> benutzerUUIDs = Arrays.asList(StringUtils.split(gruppeDB.getEmpfaengerUUIDs(), ","));

		List<BenutzerTrefferlisteItem> benutzerTrefferlisteItems = benutzerService.findBenutzersByUUIDs(benutzerUUIDs);

		final List<BenutzerTrefferlisteItem> benutzers = new ArrayList<>();

		benutzerUUIDs.forEach(uuid -> {

			Optional<BenutzerTrefferlisteItem> optBenutzer = benutzerTrefferlisteItems.stream()
				.filter(i -> i.getUuid().equals(uuid)).findFirst();

			if (optBenutzer.isEmpty()) {

				benutzers.add(createMarkerGeloeschterBenutzer(uuid));
			} else {

				benutzers.add(optBenutzer.get());
			}

		});

		gruppeDetails.setBenutzer(benutzers);
		result.setMailversandgruppe(gruppeDetails);

		return result;
	}

	/**
	 * Legt einen neuen Mailversandauftrag mit den zugehörigen Gruppen an.
	 *
	 * @param requestDto
	 * @return MailversandauftragOverview
	 */
	public MailversandauftragOverview versandauftragAnlegen(final MailversandauftragRequestDto requestDto) {

		if (requestDto.getBenutzerUUIDs().isEmpty()) {

			String message = "benutzerIds waren leer. Mindestens 1 Empfänger wird benötigt.";
			LOGGER.error(message, requestDto.getIdInfomailtext());

			Response response = Response.status(412).entity(MessagePayload.error(message)).build();
			throw new WebApplicationException(response);
		}

		PersistenterInfomailTextReadOnly infomailtext = mailversandDao.findInfomailtextReadOnlyByID(requestDto.getIdInfomailtext());

		if (infomailtext == null) {

			LOGGER.error("Infomailtext mit UUID={} existiert nicht oder nicht mehr", requestDto.getIdInfomailtext());

			Response response = Response.status(412)
				.entity(MessagePayload.error("kein Infomailtext mit der gegebenen UUID vorhanden")).build();
			throw new WebApplicationException(response);
		}

		String checksum = calculateChecksum(requestDto.getBenutzerUUIDs());
		List<List<String>> confirmedUUIDGroups = getAllConfirmedUUIDsInGroups(requestDto);

		try {

			return createNewVersandauftrag(infomailtext, confirmedUUIDGroups, checksum);
		} catch (Exception e) {

			ConstraintViolationException cve = AuthAdminSQLExceptionHelper.unwrappConstraintViolationException(e,
				"Beim Anlegen eines Mailversandauftrags ist etwas schiefgegaben");

			LOGGER.error("infomailtext={}, checksum={}: {}", infomailtext.getUuid(), checksum, cve.getMessage());

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
	MailversandauftragOverview createNewVersandauftrag(final PersistenterInfomailTextReadOnly infomailtext,
		final List<List<String>> confirmedUUIDGroups, final String checksum) {

		long anzahlEmpfaenger = AuthAdminCollectionUtils.countElements(confirmedUUIDGroups);
		Date geaendertAm = new Date();
		LocalDateTime now = LocalDateTime.now();

		PersistenterMailversandauftrag persistenterVersandauftrag = new PersistenterMailversandauftrag();
		persistenterVersandauftrag.setAnzahlEmpfaenger(anzahlEmpfaenger);
		persistenterVersandauftrag.setChecksumEmpfaengerIDs(checksum);
		persistenterVersandauftrag.setErfasstAm(now);
		persistenterVersandauftrag.setGeaendertAm(geaendertAm);
		persistenterVersandauftrag.setVersandJahrMonat(DATE_TIME_FORMATTER_JAHR_MONAT.format(now));
		persistenterVersandauftrag.setIdInfomailtext(infomailtext.getUuid());
		persistenterVersandauftrag.setBetreff(infomailtext.getBetreff());
		persistenterVersandauftrag.setMailtext(infomailtext.getMailtext());
		persistenterVersandauftrag.setStatus(Jobstatus.WAITING);

		String versandauftragUuid = mailversandDao.insertMailversandauftrag(persistenterVersandauftrag);

		int sortnr = 0;

		for (List<String> uuidGroup : confirmedUUIDGroups) {

			PersistenteMailversandgruppe gruppe = new PersistenteMailversandgruppe();
			gruppe.setEmpfaengerUUIDs(StringUtils.join(uuidGroup, ","));
			gruppe.setGeaendertAm(now);
			gruppe.setIdVersandauftrag(versandauftragUuid);
			gruppe.setSortnr(++sortnr);
			gruppe.setStatus(Jobstatus.WAITING);

			mailversandDao.insertMailversandgruppe(gruppe);

		}

		MailversandauftragOverview result = new MailversandauftragOverview();
		result.setAnzahlEmpfaenger(anzahlEmpfaenger);
		result.setAnzahlGruppen(confirmedUUIDGroups.size());
		result.setBetreff(infomailtext.getBetreff());
		result.setStatus(persistenterVersandauftrag.getStatus());
		result.setUuid(versandauftragUuid);
		result.setIdInfomailtext(infomailtext.getUuid());

		return result;

	}

	/**
	 * @param idGroups
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

		List<PersistenterUserReadOnly> users = mailversandDao.findActivatedAndNotBannedUsersByUUIDs(benutzerIDs);
		return users.stream().map(u -> u.getUuid()).toList();
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
		result.setAnzahlEmpfaenger(fromDB.getAnzahlEmpfaenger());
		result.setAnzahlGruppen(fromDB.getAnzahlGruppen());
		result.setBetreff(fromDB.getBetreff());
		result.setIdInfomailtext(fromDB.getIdInfomailtext());
		result.setStatus(fromDB.getStatus());
		result.setUuid(fromDB.getUuid());
		result.setErfasstAm(DATE_TIME_FORMATTER_DEFAULT.format(fromDB.getErfasstAm()));

		return result;

	}

	MailversandauftragDetails mapFromDBToDetails(final PersistenterMailversandauftrag fromDB) {

		MailversandauftragDetails versandauftrag = new MailversandauftragDetails();
		versandauftrag.setAnzahlEmpfaenger(fromDB.getAnzahlEmpfaenger());
		versandauftrag.setAnzahlVersendet(fromDB.getAnzahlVersendet());
		versandauftrag.setErfasstAm(DATE_TIME_FORMATTER_DEFAULT.format(fromDB.getErfasstAm()));
		versandauftrag.setIdInfomailtext(fromDB.getIdInfomailtext());
		versandauftrag.setBetreff(fromDB.betreff);
		versandauftrag.setMailtext(fromDB.mailtext);
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
		result.setAenderungsdatum(DATE_TIME_FORMATTER_DEFAULT.format(fromDB.getGeaendertAm()));
		return result;

	}

	BenutzerTrefferlisteItem createMarkerGeloeschterBenutzer(final String uuid) {

		BenutzerTrefferlisteItem result = new BenutzerTrefferlisteItem();
		result.setUuid(uuid);
		result.setAenderungsdatum(GELOESCHT);
		result.setAktiviert(false);
		result.setEmail(GELOESCHT);
		result.setNachname(GELOESCHT);
		result.setRollen(GELOESCHT);
		result.setVorname(GELOESCHT);
		return result;
	}

	/**
	 * Löscht den gegebenen Mailversandauftrag, falls er existiert.
	 *
	 * @param uuid String
	 * @return SingleUuidDto
	 */
	public SingleUuidDto versandauftragLoeschen(final String uuid) {

		SingleUuidDto result = new SingleUuidDto(uuid);

		boolean removed = mailversandDao.removeMailversandauftrag(uuid);

		if (!removed) {

			LOGGER.warn("Es gibt keinen Versandauftrag mit der UUID={}. Ist also nix zu loeschen", uuid);
		}

		return result;
	}

	public SingleUuidDto mailversandAbbrechen(final String uuid) {

		PersistenterMailversandauftrag fromDB = mailversandDao.findMailversandauftragByUUID(uuid);

		if (fromDB == null) {

			LOGGER.warn("Es gibt keinen Versandauftrag mit der UUID={}. Ist also nix zu loeschen", uuid);
			throw new WebApplicationException(404);

		}

		List<PersistenteMailversandgruppe> versandgruppen = mailversandDao.findAllMailversandgruppenWithVersandauftragUUID(uuid);

		this.doCancel(fromDB, versandgruppen);

		return new SingleUuidDto(uuid);

	}

	@Transactional
	void doCancel(final PersistenterMailversandauftrag versandauftrag, final List<PersistenteMailversandgruppe> versandgruppen) {

		versandauftrag.setStatus(Jobstatus.CANCELLED);
		versandauftrag.setGeaendertAm(new Date());
		versandauftrag.setVersandBeendetAm(LocalDateTime.now());

		mailversandDao.updateMailversandauftrag(versandauftrag);

		for (PersistenteMailversandgruppe gruppe : versandgruppen) {

			if (gruppe.getStatus() == Jobstatus.WAITING) {

				gruppe.setStatus(Jobstatus.CANCELLED);
				gruppe.setGeaendertAm(LocalDateTime.now());

				mailversandDao.updateMailversandgruppe(gruppe);
			}
		}

	}

	/**
	 * Versetzt alle Mailversandgruppen mit Status CANCELLED zurück in WAITING
	 *
	 * @param uuid String die uuid des Versandauftrags
	 * @return MailversandauftragOverview
	 */
	public SingleUuidDto mailversandFortsetzen(final String uuid) {

		PersistenterMailversandauftrag fromDB = mailversandDao.findMailversandauftragByUUID(uuid);

		if (fromDB == null) {

			LOGGER.warn("Es gibt keinen Versandauftrag mit der UUID={}. Ist also nix zu loeschen", uuid);
			throw new WebApplicationException(404);

		}

		List<PersistenteMailversandgruppe> versandgruppen = mailversandDao.findAllMailversandgruppenWithVersandauftragUUID(uuid);

		doResetVersandauftrag(fromDB, versandgruppen);
		LOGGER.info("Versandauftrag {} wird fortgesetzt", fromDB.getUuid());
		return new SingleUuidDto(uuid);
	}

	@Transactional
	void doResetVersandauftrag(final PersistenterMailversandauftrag versandauftrag,
		final List<PersistenteMailversandgruppe> versandgruppen) {

		for (PersistenteMailversandgruppe gruppe : versandgruppen) {

			if (gruppe.getStatus() != Jobstatus.COMPLETED) {

				gruppe.setStatus(Jobstatus.WAITING);
				mailversandDao.updateMailversandgruppe(gruppe);
			}
		}

		versandauftrag.setStatus(Jobstatus.WAITING);
		versandauftrag.setVersandBegonnenAm(null);
		versandauftrag.setVersandBeendetAm(null);
		versandauftrag.setVersandMitFehlern(false);
		mailversandDao.updateMailversandauftrag(versandauftrag);
	}

	/**
	 * @param versandgruppe
	 * @return Mailversandgruppe
	 */
	public MailversandgruppeDetailsResponseDto mailversandgruppeAendern(final MailversandgruppeDetails versandgruppe) {

		PersistenteMailversandgruppe ausDB = mailversandDao.findMailversandgruppeByUUID(versandgruppe.getUuid());

		if (ausDB == null) {

			String message = "mailversandgruppe mit UUID [" + versandgruppe.getUuid() + "] existiert nicht.";
			LOGGER.error(message);

			Response response = Response.status(404).entity(MessagePayload.error(message)).build();
			throw new WebApplicationException(response);
		}

		List<String> empfaengerUUIds = versandgruppe.getBenutzer().stream().map(b -> b.getUuid()).toList();

		ausDB.setEmpfaengerUUIDs(StringUtils.join(empfaengerUUIds, ","));
		ausDB.setStatus(versandgruppe.getStatus());
		ausDB.setGeaendertAm(LocalDateTime.now());
		PersistenteMailversandgruppe gespeicherte = mailversandDao.updateMailversandgruppe(ausDB);

		return detailsMailversandgruppeLaden(gespeicherte.getUuid());
	}

}
