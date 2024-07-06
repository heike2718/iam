// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.benutzer;

import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import de.egladil.web.auth_admin_api.domain.auth.dto.MessagePayload;
import de.egladil.web.auth_admin_api.infrastructure.persistence.dao.BenutzerDao;
import de.egladil.web.auth_admin_api.infrastructure.persistence.dao.SaltDao;
import de.egladil.web.auth_admin_api.infrastructure.persistence.entities.PersistenterUserReadOnly;
import de.egladil.web.auth_admin_api.infrastructure.persistence.entities.Salt;
import de.egladil.web.auth_admin_api.infrastructure.restclient.MkGatewayRestClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * BenutzerService
 */
@ApplicationScoped
public class BenutzerService {

	@Inject
	BenutzerDao benutzerDao;

	@Inject
	SaltDao saltDao;

	@Inject
	@RestClient
	MkGatewayRestClient mkGatewayRestClient;

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
	public void deleteUser(final String uuid) {

		PersistenterUserReadOnly user = benutzerDao.findUserReadonlyByUUID(uuid);

		if (user == null) {

			Response response = Response.status(Status.NOT_FOUND)
				.entity(MessagePayload.warn("Benutzer existiert nicht oder nicht mehr")).build();

			throw new WebApplicationException(response);
		}

		Salt salt = saltDao.findSaltByID(user.saltId);

		if (salt == null) {

			Response response = Response.status(Status.NOT_FOUND)
				.entity(MessagePayload.warn("Benutzer wurde anscheinend zwischendurch gelöscht")).build();

			throw new WebApplicationException(response);
		}

	}

	@Transactional
	void doDelete(final PersistenterUserReadOnly user, final Salt salt) {

	}

	void propagateDeleteToMkGateway(final String uuid) {

		Response response = null;

		try {

		} catch (WebApplicationException e) {

		} catch (ProcessingException e) {

		} finally {

			if (response != null) {

				response.close();
			}
		}
	}
}
