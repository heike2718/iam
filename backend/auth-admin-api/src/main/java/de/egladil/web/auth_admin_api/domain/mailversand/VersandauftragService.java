// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.mailversand;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.auth_admin_api.domain.Jobstatus;
import de.egladil.web.auth_admin_api.domain.auth.dto.MessagePayload;
import de.egladil.web.auth_admin_api.domain.exceptions.AuthAdminAPIRuntimeException;
import de.egladil.web.auth_admin_api.infrastructure.persistence.dao.MailsUndVersandDao;
import de.egladil.web.auth_admin_api.infrastructure.persistence.entities.PersistenteMailversandgruppe;
import de.egladil.web.auth_admin_api.infrastructure.persistence.entities.PersistenterInfomailTextReadOnly;
import de.egladil.web.auth_admin_api.infrastructure.persistence.entities.PersistenterMailversandauftrag;
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

	@ConfigProperty(name = "emails.groupsize", defaultValue = "50")
	int emailsGroupSize;

	@Inject
	MailsUndVersandDao dao;

	/**
	 * Legt einen neuen Mailversandauftrag mit den zugehörigen Gruppen an.
	 *
	 * @param  requestDto
	 * @return            MailversandauftragOverview
	 */
	public MailversandauftragOverview versandauftragAnlegen(final MailversandauftragRequestDto requestDto) {

		if (requestDto.getBenutzerIds().isEmpty()) {

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

		String checksum = calculateChecksum(requestDto.getBenutzerIds());
		List<List<Long>> idGroups = groupTheIds(requestDto.getBenutzerIds());
		List<List<String>> emailAddressGroups = loadConfirmedEmails(idGroups);

		return createNewVersandauftrag(infomailtext, idGroups, emailAddressGroups, checksum);
	}

	@Transactional
	MailversandauftragOverview createNewVersandauftrag(final PersistenterInfomailTextReadOnly infomailtext, final List<List<Long>> idGroups, final List<List<String>> emailAddressGroups, final String checksum) {

		String empfaengerIds = getEmpfaengerIds(idGroups);
		long anzahlEmpfaenger = countElements(emailAddressGroups);
		Date geaendertAm = new Date();
		LocalDateTime now = LocalDateTime.now();

		PersistenterMailversandauftrag persistenterVersandauftrag = new PersistenterMailversandauftrag();
		persistenterVersandauftrag.setAnzahlEmpfaenger(anzahlEmpfaenger);
		persistenterVersandauftrag.setChecksumEmpfaengerIDs(checksum);
		persistenterVersandauftrag.setEmpgaengerIDs(empfaengerIds);
		persistenterVersandauftrag.setErfasstAm(now);
		persistenterVersandauftrag.setGeaendertAm(geaendertAm);
		persistenterVersandauftrag.setVersandJahrMonat(this.getVersamdJahrMonat(now));
		persistenterVersandauftrag.setIdInfomailtext(infomailtext.uuid);
		persistenterVersandauftrag.setStatus(Jobstatus.NEW);

		String versandauftragUuid = dao.insertMailversandauftrag(persistenterVersandauftrag);

		int sortnr = 0;

		for (List<String> emailGruppe : emailAddressGroups) {

			PersistenteMailversandgruppe gruppe = new PersistenteMailversandgruppe();
			gruppe.setEmpfaengerEmails(StringUtils.join(emailGruppe, ","));
			gruppe.setGeaendertAm(geaendertAm);
			gruppe.setIdVersandauftrag(versandauftragUuid);
			gruppe.setSortnr(++sortnr);
			gruppe.setStatus(Jobstatus.NEW);

			dao.insertMailversandgruppe(gruppe);

		}

		MailversandauftragOverview result = new MailversandauftragOverview();
		result.setAnzahlEmpfaenger(anzahlEmpfaenger);
		result.setAnzahlGruppen(emailAddressGroups.size());
		result.setBetreff(infomailtext.betreff);
		result.setStatus(persistenterVersandauftrag.getStatus());
		result.setUuid(versandauftragUuid);
		result.setIdInfomailtext(infomailtext.uuid);

		return result;

	}

	String getEmpfaengerIds(final List<List<Long>> idGroups) {

		return idGroups.stream()
			.flatMap(List::stream)
			.map(String::valueOf)
			.collect(Collectors.joining(","));
	}

	long countElements(final List<List<String>> groupedLists) {

		return groupedLists.stream()
			.flatMap(List::stream) // Flatten the list of lists into a single stream of String
			.count(); // Count the elements in the stream
	}

	List<List<Long>> groupTheIds(final List<Long> benutzerIDs) {

		List<List<Long>> groupedLists = new ArrayList<>();

		for (int i = 0; i < benutzerIDs.size(); i += emailsGroupSize) {

			groupedLists.add(new ArrayList<>(benutzerIDs.subList(i, Math.min(i + emailsGroupSize, benutzerIDs.size()))));
		}
		return groupedLists;

	}

	/**
	 * @param  idGroups
	 * @return
	 */
	List<List<String>> loadConfirmedEmails(final List<List<Long>> idGroups) {

		List<List<String>> emailAddressGroups = new ArrayList<>();

		for (List<Long> idGroup : idGroups) {

			List<String> allEmails = loadAllEmails(idGroup);

			if (!allEmails.isEmpty()) {

				emailAddressGroups.add(allEmails);
			}
		}
		return emailAddressGroups;
	}

	List<String> loadAllEmails(final List<Long> benutzerIDs) {

		List<PersistenterUserReadOnly> users = dao.findUsersByIds(benutzerIDs);
		return users.stream().map(u -> u.email).toList();
	}

	String calculateChecksum(final List<Long> benutzerIds) {

		Collections.sort(benutzerIds);

		String idsString = StringUtils.join(benutzerIds, ",");

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

			LOGGER.error("Exception beim Berechnen der Checksumme der BenutzerIDs: {}", e.getMessage(), e);
			throw new AuthAdminAPIRuntimeException("Exception beim Berechnen der Checksumme der BenutzerIDs");
		}

	}

	MailversandauftragOverview mapFromDBToOverview(final PersistenterMailversandauftrag fromDB) {

		MailversandauftragOverview result = new MailversandauftragOverview();

		return result;

	}

	String getVersamdJahrMonat(final LocalDateTime ldt) {

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM");
		return dtf.format(ldt);
	}
}
