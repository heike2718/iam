// Project: profil-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_api.domain.auth.clientauth;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.auth_validations.dto.OAuthClientCredentials;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * OAuthClientCredentialsProvider
 */
@ApplicationScoped
public class OAuthClientCredentialsProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(OAuthClientCredentialsProvider.class);

	@ConfigProperty(name = "public-client-id")
	String publicClientId;

	@ConfigProperty(name = "public-client-secret")
	String publicClientSecret;

	/**
	 * @param  nonce
	 *               String, darf manchmal null sein.
	 * @return
	 */
	public OAuthClientCredentials getClientCredentials(final String nonce) {

		LOGGER.debug(">>>>>>>> publicClientId={}, publicClientSecret={}  <<<<<<<<", publicClientId, publicClientSecret);

		return OAuthClientCredentials.create(publicClientId, publicClientSecret, nonce);
	}

}
