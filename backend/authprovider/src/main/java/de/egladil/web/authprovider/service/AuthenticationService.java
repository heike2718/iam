// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.service;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.crypto.AuthCryptoService;
import de.egladil.web.authprovider.domain.CryptoAlgorithm;
import de.egladil.web.authprovider.entities.Client;
import de.egladil.web.authprovider.entities.LoginSecrets;
import de.egladil.web.authprovider.entities.ResourceOwner;
import de.egladil.web.authprovider.error.AuthException;
import de.egladil.web.authprovider.event.AuthproviderEvent;
import de.egladil.web.authprovider.event.LoggableEventDelegate;
import de.egladil.web.authprovider.event.LoginversuchInaktiverUser;
import de.egladil.web.authprovider.payload.AuthorizationCredentials;
import de.egladil.web.authprovider.utils.AuthUtils;
import io.vertx.core.http.HttpServerRequest;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * AuthenticationService
 */
@RequestScoped
public class AuthenticationService {

	private static final String MESSAGE_FORMAT_FAILED_LOGIN = "ipAddress={0}, userAgent={1}, loginName={2}";

	private static final Logger LOG = LoggerFactory.getLogger(AuthenticationService.class);

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	@ConfigProperty(name = "stage")
	String stage;

	@Inject
	HttpServerRequest request;

	@Inject
	ResourceOwnerService resourceOwnerService;

	@Inject
	AuthCryptoService authCryptoService;

	@Inject
	Event<AuthproviderEvent> authproviderEvent;

	public static AuthenticationService createForTest(final ResourceOwnerService resourceOwnerService,
		final AuthCryptoService authCryptoService) {

		AuthenticationService result = new AuthenticationService();
		result.authCryptoService = authCryptoService;
		result.resourceOwnerService = resourceOwnerService;
		return result;
	}

	/**
	 * Validiert die Credentials und erzeugt ein JWT verpackt in SignUpLogInResponseData.
	 *
	 * @param authorizationCredentials AuthorizationCredentials
	 * @param client Client
	 * @return SignUpLogInResponseData
	 */
	@Transactional
	public ResourceOwner authenticateResourceOwner(final AuthorizationCredentials authorizationCredentials) {

		LOG.info(">>>>> start");

		if (authorizationCredentials == null) {

			throw new IllegalArgumentException("authorizationCredentials null");
		}

		Optional<ResourceOwner> optOwner = resourceOwnerService.findByIdentifier(authorizationCredentials.getLoginName());

		if (!optOwner.isPresent()) {

			String details = getFailedLoginDetails(authorizationCredentials.getLoginName());
			LOG.warn("Login fehlgeschlagen - unbekannter loginName: {}", details);
			throw new AuthException(applicationMessages.getString("Authentication.incorrectCredentials"));
		}

		ResourceOwner resourceOwner = optOwner.get();

		if (!resourceOwner.isAktiviert()) {

			LoginversuchInaktiverUser eventPayload = new LoginversuchInaktiverUser(resourceOwner);
			new LoggableEventDelegate().fireAuthProviderEvent(eventPayload, authproviderEvent);

			throw new AuthException(applicationMessages.getString("Authentication.incorrectCredentials"));
		}
		authCryptoService.verifyPassword(authorizationCredentials.getPasswort().toCharArray(), resourceOwner);

		ResourceOwner persisted = resourceOwnerService.erfolgreichesLoginSpeichern(resourceOwner);

		return persisted;
	}

	private String getFailedLoginDetails(final String loginname) {

		String ipAddress = AuthUtils.getIPAddress(request);
		String userAgent = AuthUtils.getUserAgent(request);
		return MessageFormat.format(MESSAGE_FORMAT_FAILED_LOGIN,
			new Object[] { ipAddress, userAgent, StringUtils.abbreviate(loginname, 11) });
	}

	void resetPassword(String uuid) {

		if (!"dev".equalsIgnoreCase(stage)) {
			throw new AuthException("Das machst Du bitte nur auf dev!!!");
		}

		Optional<ResourceOwner> opt = resourceOwnerService.findByUUID(uuid);
		if (opt.isPresent()) {

			ResourceOwner resourceOwner = opt.get();

			LoginSecrets loginSecrets = resourceOwner.getLoginSecrets();
			String passwordHash = authCryptoService.hashPassword("start123".toCharArray());
			loginSecrets.setPasswordhash(passwordHash);
			loginSecrets.setCryptoAlgorithm(CryptoAlgorithm.ARGON2);
			loginSecrets.setSalt(null);

			resourceOwnerService.aendern(resourceOwner);
		}
	}
}
