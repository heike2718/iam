// =====================================================
// Projekt: authenticationprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.endpoints;

import java.util.Locale;
import java.util.ResourceBundle;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.authprovider.payload.LoginCredentials;
import de.egladil.web.authprovider.payload.SignUpLogInResponseData;
import de.egladil.web.authprovider.service.AuthenticationService;
import de.egladil.web.authprovider.service.AuthorizationService;
import de.egladil.web.commons_validation.ValidationDelegate;
import de.egladil.web.commons_validation.payload.MessagePayload;
import de.egladil.web.commons_validation.payload.ResponsePayload;

/**
 * AuthenticationResource stellt REST-Endpoints zur Authentisierung von ResourceOwnern zur Verfügung.
 */
@RequestScoped
@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationResource {

	private ValidationDelegate validationDelegate = new ValidationDelegate();

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	@Inject
	AuthenticationService authenticationService;

	@Inject
	AuthorizationService authorizationService;

	/**
	 * Authentisiert den Benutzer mit Loginname / Email und Paaswort. Gibt als SignUpLogInResponseData.idToken ein oneTimeToken
	 * zurück, mit dem der Server des anfragenden Clients das JWT abholen kann.
	 *
	 * @param  credentials
	 * @return             Resonse mit SignUpLogInResponseData
	 */
	@POST
	@Path("/sessions/auth-token-grant")
	public Response authenticateUserWithTokenExchangeTypeAuthTokenGrant(final LoginCredentials credentials) {

		try {

			if (honeypot(credentials.getAuthorizationCredentials().getKleber())) {

				ResponsePayload responsePayload = ResponsePayload.messageOnly(
					MessagePayload
						.error(applicationMessages.getString("general.notAuthenticated")));
				return Response.status(401).entity(responsePayload).build();
			}

			validationDelegate.check(credentials, LoginCredentials.class);

			ResourceOwner resourceOwner = authenticationService
				.authenticateResourceOwner(credentials.getAuthorizationCredentials());

			SignUpLogInResponseData data = authorizationService
				.createAndStoreAuthorization(resourceOwner, credentials.getClientCredentials(), credentials.getNonce());

			ResponsePayload responsePayload = new ResponsePayload(MessagePayload.info("erfolgreich authentisiert"), data);

			return Response.ok(responsePayload).build();

		} finally {

			credentials.getAuthorizationCredentials().clean();
		}
	}

	private boolean honeypot(final String value) {

		if (value == null) {

			return false;
		}

		if (value.isEmpty()) {

			return false;
		}
		return true;
	}

}
