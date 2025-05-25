// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.benutzer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.bv_admin.domain.auth.dto.MessagePayload;
import de.egladil.web.bv_admin.domain.events.AuthAdminEventPayload;
import de.egladil.web.bv_admin.domain.events.EventsService;
import de.egladil.web.bv_admin.domain.events.PropagateEventService;
import de.egladil.web.bv_admin.domain.events.UserActivatedEvent;
import de.egladil.web.bv_admin.domain.events.UserDeactivatedEvent;
import de.egladil.web.bv_admin.domain.events.UserDeletedEvent;
import de.egladil.web.bv_admin.domain.exceptions.AuthAdminAPIRuntimeException;
import de.egladil.web.bv_admin.domain.exceptions.CommandPropagationFailedException;
import de.egladil.web.bv_admin.infrastructure.cdi.AuthenticationContext;
import de.egladil.web.bv_admin.infrastructure.persistence.dao.BenutzerDao;
import de.egladil.web.bv_admin.infrastructure.persistence.dao.SaltDao;
import de.egladil.web.bv_admin.infrastructure.persistence.entities.PersistenterUser;
import de.egladil.web.bv_admin.infrastructure.persistence.entities.PersistenterUserReadOnly;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * BenutzerService
 */
@ApplicationScoped
public class BenutzerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BenutzerService.class);

	@Context
	AuthenticationContext authCtx;

	@Inject
	PropagateEventService propagateEventService;

	@Inject
	EventsService eventsService;

	@Inject
	BenutzerDao benutzerDao;

	@Inject
	SaltDao saltDao;

	/**
	 * Sucht die users.
	 *
	 * @param userSearchDto
	 * @return
	 */
	public BenutzerSearchResult findUsers(final BenutzerSuchparameter userSearchDto) {

		int anzahl = benutzerDao.countTreffer(userSearchDto);
		List<PersistenterUserReadOnly> trefferliste = benutzerDao.findUsers(userSearchDto);
		List<BenutzerTrefferlisteItem> items = trefferliste.stream().map(this::mapFromDB).toList();

		// Damit ich nicht versehentlich an meinem eigenen Benutzerkonto oder dem der anderen BV-Admins herumfingere,
		// werden die mit
		// der Rolle AUTH_ADMIN aus der Trefferliste entfernt.

		BenutzerSearchResult result = new BenutzerSearchResult();
		result.setAnzahlGesamt(anzahl);
		result.setItems(items);
		return result;
	}

	public List<BenutzerTrefferlisteItem> findBenutzersByUUIDs(final List<String> uuids) {

		List<PersistenterUserReadOnly> persistenteUsers = benutzerDao.findUsersByUUIDList(uuids);
		return persistenteUsers.stream().map(this::mapFromDB).toList();

	}

	/**
	 * Setzt das Attribut aktiviert.
	 *
	 * @param uuid String die UUID des zu ändernden Benutzers.
	 * @param aktivierungsstatus
	 * @return UpdateBenutzerResponseDto
	 */
	public UpdateBenutzerResponseDto updateAktivierungsstatus(final String uuid, final Aktivierungsstatus aktivierungsstatus) {

		PersistenterUser user = benutzerDao.findUserByUUID(uuid);

		if (user == null) {

			LOGGER.warn("USER {} existiert nicht oder nicht mehr");
			UpdateBenutzerResponseDto responseDto = new UpdateBenutzerResponseDto();
			responseDto.setUuid(uuid);
			return responseDto;
		}

		this.doUpdate(user, aktivierungsstatus.isAktiviert());

		PersistenterUserReadOnly result = benutzerDao.findUserReadonlyByUUID(uuid);

		if (result == null) {

			LOGGER.warn("echt jetzt? Genau in dieser Nanosekunde wurde USER {} von anderswoher geloescht?");
			UpdateBenutzerResponseDto responseDto = new UpdateBenutzerResponseDto();
			responseDto.setUuid(uuid);
			return responseDto;
		}

		UpdateBenutzerResponseDto responseDto = new UpdateBenutzerResponseDto();
		responseDto.setUuid(uuid);
		responseDto.setBenuzer(this.mapFromDB(result));
		return responseDto;
	}

	@Transactional
	void doUpdate(final PersistenterUser user, final boolean aktiviert) {

		user.setAktiviert(aktiviert);
		benutzerDao.updateUser(user);

		AuthAdminEventPayload eventPayload = new AuthAdminEventPayload().withAkteur(authCtx.getUser().getUuid())
			.withTarget(user.getUuid());

		if (aktiviert) {

			eventsService.handleEvent(new UserActivatedEvent(eventPayload));
		} else {

			eventsService.handleEvent(new UserDeactivatedEvent(eventPayload));
		}

	}

	BenutzerTrefferlisteItem mapFromDB(final PersistenterUserReadOnly fromDB) {

		BenutzerTrefferlisteItem result = new BenutzerTrefferlisteItem();
		result.setAktiviert(fromDB.isAktiviert());
		result.setAenderungsdatum(fromDB.getAenderungsdatum());
		result.setEmail(fromDB.getEmail());
		result.setNachname(fromDB.getNachname());
		result.setRollen(fromDB.getRollen());
		result.setUuid(fromDB.getUuid());
		result.setVorname(fromDB.getVorname());
		result.setCryptoAlgorithm(fromDB.getCryptoAlgorithm());
		return result;
	}

	/**
	 * Löscht einen gegebenen User.
	 *
	 * @param uuid
	 */
	public DeleteBenutzerResponseDto deleteUser(final String uuid) {

		PersistenterUserReadOnly user = benutzerDao.findUserReadonlyByUUID(uuid);

		if (user == null) {

			Response response = Response.status(Status.NOT_FOUND)
				.entity(MessagePayload.warn("Benutzer existiert nicht oder nicht mehr")).build();

			throw new WebApplicationException(response);
		}

		doDelete(user);
		return new DeleteBenutzerResponseDto(uuid);

	}

	@Transactional
	void doDelete(final PersistenterUserReadOnly user) throws AuthAdminAPIRuntimeException {

		try {

			propagateEventService.propagateDeleteUserToMkGateway(user.getUuid());

			LOGGER.info("delete {} synchronized with mk-gateway", user.getUuid());

			if (user.getSaltId() != null) {
				saltDao.deleteSaltAndCascade(user.getSaltId());
			}

			AuthAdminEventPayload eventPayload = new AuthAdminEventPayload().withAkteur(authCtx.getUser().getUuid())
				.withTarget(user.getUuid());

			eventsService.handleEvent(new UserDeletedEvent(eventPayload));

		} catch (CommandPropagationFailedException e) {

			LOGGER.error("CommandPropagationFailed: Löschen des Benutzerkontos {} wird abgebrochen: {}", user.getUuid(), e.getMessage(),
				e);
			throw new AuthAdminAPIRuntimeException(e.getMessage(), e);

		}
	}
}
