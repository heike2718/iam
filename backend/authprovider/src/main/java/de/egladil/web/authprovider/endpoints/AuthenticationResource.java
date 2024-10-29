// =====================================================
// Projekt: authenticationprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.endpoints;

import java.util.Locale;
import java.util.ResourceBundle;

import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.authprovider.payload.LoginCredentials;
import de.egladil.web.authprovider.payload.MessagePayload;
import de.egladil.web.authprovider.payload.ResponsePayload;
import de.egladil.web.authprovider.payload.SignUpLogInResponseData;
import de.egladil.web.authprovider.service.AuthJWTService;
import de.egladil.web.authprovider.service.AuthenticationService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * AuthenticationResource stellt REST-Endpoints zur Authentisierung von ResourceOwnern zur Verfügung.
 */
@RequestScoped
@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationResource {

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	@Inject
	AuthenticationService authenticationService;

	@Inject
	AuthJWTService authJWTService;

	/**
	 * Authentisiert den Benutzer mit Loginname / Email und Paaswort. Gibt als SignUpLogInResponseData.idToken ein oneTimeToken
	 * zurück, mit dem der Server des anfragenden Clients das JWT abholen kann.
	 *
	 * @param  credentials
	 * @return             Resonse mit SignUpLogInResponseData
	 */
	@POST
	@Path("/sessions/auth-token-grant")
	public Response authenticateUserWithTokenExchangeTypeAuthTokenGrant(@Valid final LoginCredentials credentials) {

		try {

			if (honeypot(credentials.getAuthorizationCredentials().getKleber())) {

				ResponsePayload responsePayload = ResponsePayload.messageOnly(
					MessagePayload
						.error(applicationMessages.getString("general.notAuthenticated")));
				return Response.status(401).entity(responsePayload).build();
			}

			ResourceOwner resourceOwner = authenticationService
				.authenticateResourceOwner(credentials.getAuthorizationCredentials());

			SignUpLogInResponseData data = authJWTService
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
