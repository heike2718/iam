// =====================================================
// Project: benutzerprofil
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.benutzerprofil.domain.auth.clientauth;

import org.eclipse.microprofile.rest.client.RestClientDefinitionException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.auth_validations.dto.OAuthClientCredentials;
import de.egladil.web.benutzerprofil.domain.auth.dto.ResponsePayload;
import de.egladil.web.benutzerprofil.domain.exceptions.BenutzerprofilRuntimeException;
import de.egladil.web.benutzerprofil.infrastructure.restclient.AuthproviderRestClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

/**
 * InitAccessTokenDelegate kapselt den Aufruf des RestClients.
 */
@ApplicationScoped
public class InitAccessTokenDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(InitAccessTokenDelegate.class);

	@Inject
	@RestClient
	AuthproviderRestClient authproviderRestClient;

	/**
	 * @param clientSecrets
	 * @return
	 */
	public ResponsePayload authenticateClient(final OAuthClientCredentials credentials) {

		try (Response authResponse = authproviderRestClient.authenticateClient(credentials);) {

			ResponsePayload responsePayload = authResponse.readEntity(ResponsePayload.class);

			return responsePayload;
		} catch (IllegalStateException | RestClientDefinitionException | WebApplicationException | ProcessingException e) {

			String msg = "Unerwarteter Fehler beim Anfordern eines client-accessTokens: " + e.getMessage();
			LOGGER.error(msg, e);
			throw new BenutzerprofilRuntimeException(msg, e);
		}
	}
}
