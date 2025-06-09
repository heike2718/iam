// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.service.profile;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.dao.ResourceOwnerDao;
import de.egladil.web.authprovider.entities.ResourceOwner;
import de.egladil.web.authprovider.error.AuthRuntimeException;
import de.egladil.web.authprovider.error.DuplicateEntityException;
import de.egladil.web.authprovider.event.AuthproviderEvent;
import de.egladil.web.authprovider.event.AuthproviderEventHandler;
import de.egladil.web.authprovider.event.LoggableEventDelegate;
import de.egladil.web.authprovider.event.ResourceOwnerEventPayload;
import de.egladil.web.authprovider.event.UserChanged;
import de.egladil.web.authprovider.payload.DuplicateAttributeType;
import de.egladil.web.authprovider.payload.User;
import de.egladil.web.authprovider.payload.profile.ProfileDataPayload;
import de.egladil.web.authprovider.service.ResourceOwnerService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;

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

	@Inject
	AuthproviderEventHandler eventHandler;

	@Inject
	Event<AuthproviderEvent> authproviderEvent;

	/**
	 * Ändert die Daten zu gegebenem ResourceOwner, falls es dadurch nicht zu Eindeutigkeitskonflikten kommt.
	 *
	 * @param uuid String die uuid des ResourceOwners
	 * @param payload ChangeProfileDataPayload
	 * @return User Daten des geänderten ResourceOwner
	 */
	public User changeData(final String uuid, final ProfileDataPayload payload) {

		DuplicateAttributeType checkOutcome = resourceOwnerService.changeLoginNameAndEmailAllowed(payload.getLoginName(),
			payload.getEmail(), uuid);

		if (checkOutcome != null) {

			LOG.debug(checkOutcome.toString());

			DuplicateEntityException duplicateEntityException = new DuplicateEntityException(
				applicationMessages.getString(checkOutcome.getApplicationMessagesKey()));
			duplicateEntityException.setDuplicateEntityType(checkOutcome);
			throw duplicateEntityException;
		}

		Optional<ResourceOwner> optRO = resourceOwnerDao.findByUUID(uuid);

		if (!optRO.isPresent()) {

			throw new AuthRuntimeException(
				"Fehler im Ablauf: der ResourceOwner muss an dieser Stelle auf jeden Fall vorhanden sein");
		}

		ResourceOwner resourceOwner = optRO.get();

		if (getResetBanFlag(resourceOwner, payload.getEmail())) {
			resourceOwner.setBannedForMails(false);
		}

		resourceOwner.setLoginName(payload.getLoginName());
		resourceOwner.setEmail(payload.getEmail());
		resourceOwner.setNachname(payload.getNachname() != null ? payload.getNachname().trim() : null);
		resourceOwner.setVorname(payload.getVorname() != null ? payload.getVorname().trim() : null);

		try {

			ResourceOwner persisted = resourceOwnerDao.save(resourceOwner);

			LOG.info("{}: Daten geaendert", resourceOwner);

			ResourceOwnerEventPayload roPayload = ResourceOwnerEventPayload.createFromResourceOwner(resourceOwner);
			UserChanged eventPayload = new UserChanged(roPayload);

			// Machen wir synchron wegen des ExceptionHandlings
			if (this.eventHandler != null) {

				this.eventHandler.handleEvent(eventPayload);
			} else {

				new LoggableEventDelegate().fireAuthProviderEvent(eventPayload, authproviderEvent);
			}

			return User.fromResourceOwner(persisted);

		} catch (PersistenceException e) {

			LOG.error("Fehler beim Aendern der Benutzerdaten zu UUID={}, {}: {}", uuid, payload, e.getMessage(), e);
			throw new AuthRuntimeException("Benutzerdaten konnten nicht geändert werden: " + e.getMessage(), e);

		}
	}

	/**
	 * Einfache Heuristik: wenn resourceOwner eine banned Mailadresse hat sich die neue Mailadresse nach dem letzten @
	 * von dem Teil der alten Mailadresse nach dem letzten @ unterscheidet, hat sich die Domain geändert und man kann
	 * das Entbannen versuchen.
	 *
	 * @param resourceOwner ResourceOwner
	 * @param email String
	 * @return boolean
	 */
	boolean getResetBanFlag(ResourceOwner resourceOwner, String email) {

		if (!resourceOwner.isBannedForMails()) {
			return false;
		}

		if (resourceOwner.getEmail().trim().equalsIgnoreCase(email.trim())) {
			return false;
		}

		String oldEmail = resourceOwner.getEmail();
		String[] oldEmailTokens = StringUtils.splitPreserveAllTokens(oldEmail, "@");
		String[] newEmailTokens = StringUtils.splitPreserveAllTokens(email, "@");

		String oldDomain = oldEmailTokens[oldEmailTokens.length - 1].trim();
		String newDomain = newEmailTokens[newEmailTokens.length - 1].trim();

		if (oldDomain.equalsIgnoreCase(newDomain)) {
			return false;
		}

		LOG.info("USER {}: oldDomain={}, newDomain={} => reset the ban flag", StringUtils.abbreviate(resourceOwner.getUuid(), 11),
			oldDomain, newDomain);

		return true;
	}
}
