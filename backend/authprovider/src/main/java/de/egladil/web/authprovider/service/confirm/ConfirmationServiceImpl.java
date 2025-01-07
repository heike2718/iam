// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.service.confirm;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.crypto.impl.CryptoService;
import de.egladil.web.authprovider.dao.ActivationCodeDao;
import de.egladil.web.authprovider.dao.ResourceOwnerDao;
import de.egladil.web.authprovider.domain.ActivationCode;
import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.authprovider.error.LogmessagePrefixes;
import de.egladil.web.authprovider.event.AuthproviderEvent;
import de.egladil.web.authprovider.event.LoggableEventDelegate;
import de.egladil.web.authprovider.event.RegistrationConfirmationExpired;
import de.egladil.web.authprovider.service.ResourceOwnerService;
import de.egladil.web.authprovider.utils.AuthTimeUtils;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

/**
 * ConfirmationServiceImpl
 */
@RequestScoped
public class ConfirmationServiceImpl implements ConfirmationService {

	private static final Logger LOG = LoggerFactory.getLogger(ConfirmationServiceImpl.class);

	@Inject
	ActivationCodeDao activationCodeDao;

	@Inject
	ResourceOwnerDao resourceOwnerDao;

	@Inject
	ResourceOwnerService resourceOwnerService;

	@Inject
	CryptoService cryptoService;

	@Inject
	Event<AuthproviderEvent> authproviderEvent;

	private AuthproviderEvent eventPayload;

	@Override
	public ConfirmationStatus confirmCode(final String confirmationCode) {

		Optional<ActivationCode> optActivationCode = activationCodeDao.findByConfirmationCode(confirmationCode);

		if (!optActivationCode.isPresent()) {

			LOG.warn(LogmessagePrefixes.DATENMUELL + "kein Eintrag mit CONFIRM_CODE='{}' in Tabelle ACTIVATIONCODES",
				confirmationCode);
			return ConfirmationStatus.deletedActivation;
		}

		ActivationCode activationCode = optActivationCode.get();

		ResourceOwner resourceOwner = activationCode.getResourceOwner();

		if (resourceOwner == null) {

			LOG.warn(
				LogmessagePrefixes.DATENMUELL + "zum confirmationCode '{}' gibt es keinen passenden Eintrag in Tabelle users.",
				confirmationCode);
			return ConfirmationStatus.deletedActivation;
		}

		if (resourceOwner.isAnonym()) {

			LOG.warn(LogmessagePrefixes.DATENMUELL + "Eintrag mit ID='{}' in Tabelle USERS ist anonymisiert",
				resourceOwner.getId());

			return ConfirmationStatus.deletedActivation;
		}

		final boolean bereitsAktiviert = resourceOwner.isAktiviert();

		if (!bereitsAktiviert) {

			LocalDateTime expiresAt = AuthTimeUtils.transformFromDate(activationCode.getExpirationTime());

			if (AuthTimeUtils.now().isAfter(expiresAt)) {

				resourceOwnerService.deleteResourceOwner(resourceOwner);

				eventPayload = new RegistrationConfirmationExpired(resourceOwner);
				new LoggableEventDelegate().fireAuthProviderEvent(eventPayload, authproviderEvent);

				LOG.warn("ActivationCode '{}' zu ResourceOwner UUID='{}' expired - Event propagiert", confirmationCode,
					resourceOwner.getUuid());
				return ConfirmationStatus.expiredActivation;
			}

			aktivieren(resourceOwner);
			ResourceOwner persisted = resourceOwnerDao.save(resourceOwner);
			activationCode.setExpirationTime(new Date(System.currentTimeMillis()));
			activationCode.setConfirmed(true);

			activationCodeDao.save(activationCode);
			LOG.info("Benutzerkonto mit UUID '{}' aktiviert", persisted.getUuid());

		}

		return bereitsAktiviert ? ConfirmationStatus.repeatedActivation : ConfirmationStatus.normalActivation;
	}

	private void aktivieren(final ResourceOwner resourceOwner) {

		resourceOwner.setAktiviert(true);
		resourceOwner.setDatumGeaendert(new Date(System.currentTimeMillis()));
	}

	AuthproviderEvent eventPayload() {

		return eventPayload;
	}
}
