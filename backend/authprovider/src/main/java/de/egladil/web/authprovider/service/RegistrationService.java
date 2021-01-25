// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.core.UriInfo;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.dao.ActivationCodeDao;
import de.egladil.web.authprovider.dao.ResourceOwnerDao;
import de.egladil.web.authprovider.domain.ActivationCode;
import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.authprovider.error.AuthException;
import de.egladil.web.authprovider.error.AuthRuntimeException;
import de.egladil.web.authprovider.error.DuplicateEntityException;
import de.egladil.web.authprovider.event.AuthproviderEvent;
import de.egladil.web.authprovider.event.LoggableEventDelegate;
import de.egladil.web.authprovider.event.UserCreated;
import de.egladil.web.authprovider.payload.SignUpCredentials;
import de.egladil.web.authprovider.service.mail.RegistrationMailStrategy;
import de.egladil.web.authprovider.utils.AuthUtils;
import de.egladil.web.commons_mailer.DefaultEmailDaten;
import de.egladil.web.commons_mailer.exception.InvalidMailAddressException;
import de.egladil.web.commons_net.time.CommonTimeUtils;

/**
 * RegistrationService
 */
@RequestScoped
public class RegistrationService {

	private static final Logger LOG = LoggerFactory.getLogger(ResourceOwnerService.class);

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	@ConfigProperty(name = "registrationKeyExpireHours", defaultValue = "24")
	int registrationKeyExpireHours;

	@Inject
	ResourceOwnerService resourceOwnerService;

	@Inject
	ActivationCodeDao activationCodeDao;

	@Inject
	ResourceOwnerDao resourceOwnerDao;

	@Inject
	AuthMailService mailService;

	@Inject
	Event<AuthproviderEvent> authproviderEvent;

	/**
	 * Erzeugt einen neuen ResourceOwner und ein Aktivierungstoken.
	 *
	 * @param  credentials
	 * @return             SignUpLogInResponseData
	 */
	public ResourceOwner createNewResourceOwner(final SignUpCredentials signUpCredentials, final UriInfo uriInfo) throws InvalidMailAddressException {

		if (uriInfo == null) {

			String msg = "UriInfo is not properly injected into UserResource";
			LOG.error(msg);
			throw new AuthRuntimeException(msg);
		}

		Optional<ResourceOwner> optRo = resourceOwnerService.checkExiststAndIsConsistent(signUpCredentials.getLoginName(),
			signUpCredentials.getEmail());

		if (optRo.isPresent()) {

			if (!optRo.get().isAktiviert()) {

				throw new AuthException(applicationMessages.getString("Benutzerkonto.deaktiviert"));
			}
			throw new DuplicateEntityException(applicationMessages.getString("Registration.exists"));
		}

		ResourceOwner resourceOwner = resourceOwnerService.createNewResourceOwner(signUpCredentials);

		try {

			ActivationCode activationCode = createActivationCode(
				resourceOwnerDao.findById(ResourceOwner.class, resourceOwner.getId()));
			ActivationCode persistierter = activationCodeDao.save(activationCode);

			DefaultEmailDaten maildaten = new RegistrationMailStrategy(signUpCredentials.getEmail(),
				signUpCredentials.getLoginName(),
				persistierter, uriInfo)
					.createEmailDaten();

			mailService.sendMail(maildaten);

			LOG.debug("Mail mit Aktivierungscode versendet");
			LOG.info("{} angelegt", resourceOwner.toString());

			UserCreated eventPayload = new UserCreated(resourceOwner);
			new LoggableEventDelegate().fireAuthProviderEvent(eventPayload, authproviderEvent);

			return resourceOwner;

		} catch (InvalidMailAddressException e) {

			throw e;

		} catch (Exception e) {

			LOG.error("Exception beim Anlegen eines Users oder versenden des ActivationCodes: {}", e.getMessage(), e);
			throw new AuthRuntimeException("Fehler beim Anlegen oder Versenden eines ActivationCodes: " + e.getMessage(), e);
		}
	}

	ActivationCode createActivationCode(final ResourceOwner resourceOwner) {

		ActivationCode result = new ActivationCode();
		String code = AuthUtils.newTokenId();
		result.setConfirmationCode(code);

		int hours = Integer.valueOf(registrationKeyExpireHours);
		LocalDateTime now = CommonTimeUtils.now();
		Date expiresAt = Date.from(now.plus(hours, ChronoUnit.HOURS).atZone(ZoneId.systemDefault()).toInstant());

		result.setExpirationTime(expiresAt);
		result.setConfirmed(false);
		result.setResourceOwner(resourceOwner);
		return result;
	}
}
