// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.service.profile;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.dao.ResourceOwnerDao;
import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.authprovider.error.AuthRuntimeException;
import de.egladil.web.authprovider.error.DuplicateEntityException;
import de.egladil.web.authprovider.payload.User;
import de.egladil.web.authprovider.payload.profile.ProfileDataPayload;
import de.egladil.web.authprovider.service.ResourceOwnerService;

/**
 * ChangeDataService
 */
@RequestScoped
public class ChangeDataService {

	private static final Logger LOG = LoggerFactory.getLogger(ChangeDataService.class);

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	@Inject
	ResourceOwnerDao resourceOwnerDao;

	@Inject
	ResourceOwnerService resourceOwnerService;

	/**
	 * Ändert die Daten zu gegebenem ResourceOwner, falls es dadurch nicht zu Eindeutigkeitskonflikten kommt.
	 *
	 * @param  uuid
	 *                 String die uuid des ResourceOwners
	 * @param  payload
	 *                 ChangeProfileDataPayload
	 * @return         User Daten des geänderten ResourceOwner
	 */
	public User changeData(final String uuid, final ProfileDataPayload payload) {

		String checkOutcome = resourceOwnerService.changeLoginNameAndEmailAllowed(payload.getLoginName(), payload.getEmail(),
			uuid);

		if (checkOutcome != null) {

			throw new DuplicateEntityException(applicationMessages.getString(checkOutcome));
		}

		Optional<ResourceOwner> optRO = resourceOwnerDao.findByUUID(uuid);

		if (!optRO.isPresent()) {

			throw new AuthRuntimeException(
				"Fehler im Ablauf: der ResourceOwner muss an dieser Stelle auf jeden Fall vorhanden sein");
		}

		ResourceOwner resourceOwner = optRO.get();
		resourceOwner.setEmail(payload.getEmail());
		resourceOwner.setLoginName(payload.getLoginName());
		resourceOwner.setNachname(payload.getNachname() != null ? payload.getNachname().trim() : null);
		resourceOwner.setVorname(payload.getVorname() != null ? payload.getVorname().trim() : null);

		try {

			ResourceOwner persisted = resourceOwnerDao.save(resourceOwner);

			LOG.info("{}: Daten geändert", resourceOwner);

			return User.fromResourceOwner(persisted);

		} catch (PersistenceException e) {

			LOG.error("Fehler beim Ändern der Benutzerdaten zu UUID={}, {}: {}", uuid, payload, e.getMessage(), e);
			throw new AuthRuntimeException("Benutzerdaten konnten nicht geändert werden: " + e.getMessage(), e);

		}
	}
}
