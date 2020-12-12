// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.service.confirm;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.dao.ActivationCodeDao;
import de.egladil.web.authprovider.dao.ResourceOwnerDao;
import de.egladil.web.authprovider.domain.ActivationCode;
import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.authprovider.error.LogmessagePrefixes;
import de.egladil.web.authprovider.event.AuthproviderEvent;
import de.egladil.web.authprovider.event.RegistrationConfirmationExpired;
import de.egladil.web.authprovider.service.ResourceOwnerService;
import de.egladil.web.commons_crypto.CryptoService;
import de.egladil.web.commons_net.time.CommonTimeUtils;

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

	/**
	 * Erzeugt eine Instanz von ConfirmationServiceImpl
	 */
	public ConfirmationServiceImpl() {

	}

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

			LocalDateTime expiresAt = CommonTimeUtils.transformFromDate(activationCode.getExpirationTime());

			if (CommonTimeUtils.now().isAfter(expiresAt)) {

				resourceOwnerService.deleteResourceOwner(resourceOwner);

				if (this.authproviderEvent != null) {

					authproviderEvent.fire(RegistrationConfirmationExpired.create(resourceOwner));
					LOG.debug("{}: delete user command triggerd", ConfirmationStatus.expiredActivation);
				}

				LOG.warn(LogmessagePrefixes.DATENMUELL + "ActivationCode '{}' zu ResourceOwner UUID='{}' expired", confirmationCode,
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
}
