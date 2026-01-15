// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.auth.clientauth;

import jakarta.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.egladil.web.bv_admin.domain.auth.dto.OAuthClientCredentials;

/**
 * OAuthClientCredentialsProvider
 */
@ApplicationScoped
public class OAuthClientCredentialsProvider {

    @ConfigProperty(name = "public-client-id")
    String publicClientId;

    @ConfigProperty(name = "public-client-secret")
    String publicClientSecret;

    /**
     * @param nonce String, darf manchmal null sein.
     * @return
     */
    public OAuthClientCredentials getClientCredentials(final String nonce) {

        return OAuthClientCredentials.create(publicClientId, publicClientSecret, nonce);
    }

}
