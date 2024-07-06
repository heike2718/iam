// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.benutzer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.auth_admin_api.domain.auth.dto.MessagePayload;
import de.egladil.web.auth_admin_api.domain.events.AuthAdminEventPayload;
import de.egladil.web.auth_admin_api.domain.events.EventsService;
import de.egladil.web.auth_admin_api.domain.events.PropagateEventService;
import de.egladil.web.auth_admin_api.domain.events.UserDeletedEvent;
import de.egladil.web.auth_admin_api.domain.exceptions.AuthAdminAPIRuntimeException;
import de.egladil.web.auth_admin_api.domain.exceptions.CommandPropagationFailedException;
import de.egladil.web.auth_admin_api.infrastructure.cdi.AuthenticationContext;
import de.egladil.web.auth_admin_api.infrastructure.persistence.dao.BenutzerDao;
import de.egladil.web.auth_admin_api.infrastructure.persistence.dao.SaltDao;
import de.egladil.web.auth_admin_api.infrastructure.persistence.entities.PersistenterUserReadOnly;
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
	 * @param  userSearchDto
	 * @return
	 */
	public BenutzerSearchResult findUsers(final BenutzerSuchparameter userSearchDto) {

		int anzahl = benutzerDao.countTreffer(userSearchDto);
		List<PersistenterUserReadOnly> trefferliste = benutzerDao.findUsers(userSearchDto);
		List<BenutzerTrefferlisteItem> items = trefferliste.stream().map(this::mapFromDB).toList();

		BenutzerSearchResult result = new BenutzerSearchResult();
		result.setAnzahlGesamt(anzahl);
		result.setItems(items);
		return result;
	}

	BenutzerTrefferlisteItem mapFromDB(final PersistenterUserReadOnly fromDB) {

		BenutzerTrefferlisteItem result = new BenutzerTrefferlisteItem();
		result.setAktiviert(fromDB.aktiviert);
		result.setDateModified(fromDB.datumGeaendert);
		result.setEmail(fromDB.email);
		result.setNachname(fromDB.nachname);
		result.setRollen(fromDB.rollen);
		result.setUuid(fromDB.uuid);
		result.setVorname(fromDB.vorname);
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

			propagateEventService.propagateDeleteUserToMkGateway(user.uuid);
			saltDao.deleteSaltAndCascade(user.saltId);

			AuthAdminEventPayload eventPayload = new AuthAdminEventPayload()
				.withAkteur(authCtx.getUser().getUuid()).withTarget(user.uuid);

			eventsService.handleEvent(new UserDeletedEvent(eventPayload));

		} catch (CommandPropagationFailedException e) {

			LOGGER.error("CommandPropagationFailed: Löschen des Benutzerkontos {} wird abgebrochen: {}", user.uuid, e.getMessage(),
				e);
			throw new AuthAdminAPIRuntimeException(e.getMessage(), e);

		}
	}
}
