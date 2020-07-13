// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.error;

import de.egladil.web.authprovider.payload.ClientCredentials;

/**
 * ClientAccessTokenRuntimeException
 */
public class ClientAccessTokenRuntimeException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final ClientCredentials clientCredentials;

	/**
	 * @param clientCredentials
	 */
	public ClientAccessTokenRuntimeException(final ClientCredentials clientCredentials) {

		this.clientCredentials = clientCredentials;
	}

	public ClientCredentials getClientCredentials() {

		return clientCredentials;
	}

}
