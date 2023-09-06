// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.service;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.crypto.AuthCryptoService;
import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.authprovider.error.AuthException;
import de.egladil.web.authprovider.event.AuthproviderEvent;
import de.egladil.web.authprovider.event.LoggableEventDelegate;
import de.egladil.web.authprovider.event.LoginversuchInaktiverUser;
import de.egladil.web.authprovider.payload.AuthorizationCredentials;

/**
 * AuthenticationService
 */
@RequestScoped
public class AuthenticationService {

	private static final Logger LOG = LoggerFactory.getLogger(AuthenticationService.class);

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	@Inject
	ResourceOwnerService resourceOwnerService;

	@Inject
	AuthCryptoService authCryptoService;

	@Inject
	Event<AuthproviderEvent> authproviderEvent;

	public static AuthenticationService createForTest(final ResourceOwnerService resourceOwnerService, final AuthCryptoService authCryptoService) {

		AuthenticationService result = new AuthenticationService();
		result.authCryptoService = authCryptoService;
		result.resourceOwnerService = resourceOwnerService;
		return result;
	}

	/**
	 * Validiert die Credentials und erzeugt ein JWT verpackt in SignUpLogInResponseData.
	 *
	 * @param  authorizationCredentials
	 *                                  AuthorizationCredentials
	 * @param  client
	 *                                  Client
	 * @return                          SignUpLogInResponseData
	 */
	public ResourceOwner authenticateResourceOwner(final AuthorizationCredentials authorizationCredentials) {

		if (authorizationCredentials == null) {

			throw new IllegalArgumentException("authorizationCredentials null");
		}

		Optional<ResourceOwner> optOwner = resourceOwnerService.findByIdentifier(authorizationCredentials.getLoginName());

		if (!optOwner.isPresent()) {

			LOG.warn("unbekannter Loginname {}", authorizationCredentials.getLoginName());
			throw new AuthException(
				applicationMessages.getString("Authentication.incorrectCredentials"));
		}

		ResourceOwner resourceOwner = optOwner.get();

		if (!resourceOwner.isAktiviert()) {

			LoginversuchInaktiverUser eventPayload = new LoginversuchInaktiverUser(resourceOwner);
			new LoggableEventDelegate().fireAuthProviderEvent(eventPayload, authproviderEvent);

			throw new AuthException(applicationMessages.getString("Benutzerkonto.deaktiviert"));
		}
		authCryptoService.verifyPassword(authorizationCredentials.getPasswort().toCharArray(), resourceOwner);

		ResourceOwner persisted = resourceOwnerService.erfolgreichesLoginSpeichern(resourceOwner);

		return persisted;
	}

}
