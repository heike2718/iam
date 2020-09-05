// =====================================================
// Projekt: authenticationprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.endpoints;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private static final Logger LOG = LoggerFactory.getLogger(AuthenticationResource.class);

	private ValidationDelegate validationDelegate = new ValidationDelegate();

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

		LOG.info("Use AUTHORIZATION_TOKEN_GRANT");

		try {

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

}
