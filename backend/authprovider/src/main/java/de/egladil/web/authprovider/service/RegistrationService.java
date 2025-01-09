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

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.dao.ActivationCodeDao;
import de.egladil.web.authprovider.dao.ResourceOwnerDao;
import de.egladil.web.authprovider.domain.ActivationCode;
import de.egladil.web.authprovider.domain.Client;
import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.authprovider.error.AuthException;
import de.egladil.web.authprovider.error.AuthRuntimeException;
import de.egladil.web.authprovider.error.DuplicateEntityException;
import de.egladil.web.authprovider.error.MailversandException;
import de.egladil.web.authprovider.event.AuthproviderEvent;
import de.egladil.web.authprovider.event.AuthproviderEventHandler;
import de.egladil.web.authprovider.event.LoggableEventDelegate;
import de.egladil.web.authprovider.event.ResourceOwnerEventPayload;
import de.egladil.web.authprovider.event.UserCreated;
import de.egladil.web.authprovider.payload.SignUpCredentials;
import de.egladil.web.authprovider.service.mail.AuthMailService;
import de.egladil.web.authprovider.service.mail.DefaultEmailDaten;
import de.egladil.web.authprovider.service.mail.RegistrationMailStrategy;
import de.egladil.web.authprovider.utils.AuthTimeUtils;
import de.egladil.web.authprovider.utils.AuthUtils;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.UriInfo;

/**
 * RegistrationService
 */
@RequestScoped
public class RegistrationService {

	private static final Logger LOG = LoggerFactory.getLogger(ResourceOwnerService.class);

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	@ConfigProperty(name = "registrationKeyExpireHours", defaultValue = "24")
	int registrationKeyExpireHours;

	@ConfigProperty(name = "account.activation.url")
	String accountActivationUrl;

	@Inject
	AuthproviderEventHandler eventHandler;

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
	public ResourceOwner createNewResourceOwner(final Client client, final SignUpCredentials signUpCredentials, final UriInfo uriInfo) throws MailversandException {

		if (uriInfo == null) {

			String msg = "UriInfo is not properly injected into UserResource";
			LOG.error(msg);
			throw new AuthRuntimeException(msg);
		}

		if (signUpCredentials.getLoginName() == null) {

			signUpCredentials.setLoginName(signUpCredentials.getEmail());
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
				persistierter, accountActivationUrl)
					.createEmailDaten("RegistrationService");

			mailService.sendMail(maildaten);

			LOG.debug("Mail mit Aktivierungscode versendet");
			LOG.info("{} angelegt", resourceOwner.toString());

			ResourceOwnerEventPayload roPayload = ResourceOwnerEventPayload.createFromResourceOwner(resourceOwner)
				.withNonce(signUpCredentials.getNonce()).withClientId(client.getClientId());
			UserCreated eventPayload = new UserCreated(roPayload);

			// Machen wir synchron wegen des ExceptionHandlings
			if (this.eventHandler != null) {

				this.eventHandler.handleEvent(eventPayload);
			} else {

				new LoggableEventDelegate().fireAuthProviderEvent(eventPayload, authproviderEvent);
			}

			return resourceOwner;

		} catch (MailversandException e) {

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
		LocalDateTime now = AuthTimeUtils.now();
		Date expiresAt = Date.from(now.plus(hours, ChronoUnit.HOURS).atZone(ZoneId.systemDefault()).toInstant());

		result.setExpirationTime(expiresAt);
		result.setConfirmed(false);
		result.setResourceOwner(resourceOwner);
		return result;
	}
}
