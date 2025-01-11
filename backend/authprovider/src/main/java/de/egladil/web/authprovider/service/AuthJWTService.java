// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.service;

import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import de.egladil.web.authprovider.auth_code_store.OAuthFlowType;
import de.egladil.web.authprovider.auth_code_store.OneTimeTokenJwtData;
import de.egladil.web.authprovider.auth_code_store.OneTimeTokenJwtRepository;
import de.egladil.web.authprovider.crypto.JWTService;
import de.egladil.web.authprovider.entities.Client;
import de.egladil.web.authprovider.entities.ResourceOwner;
import de.egladil.web.authprovider.error.AuthRuntimeException;
import de.egladil.web.authprovider.error.ClientAccessTokenNotFoundException;
import de.egladil.web.authprovider.payload.ClientCredentials;
import de.egladil.web.authprovider.payload.JWTPayload;
import de.egladil.web.authprovider.payload.SignUpLogInResponseData;
import de.egladil.web.authprovider.payload.SignUpLogInResponseDataBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * AuthJWTService produziert ein JWT und hält es zum Abholen mit einem auth-token im Heap.
 */
@ApplicationScoped
public class AuthJWTService {

	@Inject
	ClientService clientService;

	@Inject
	JWTService jwtService;

	@Inject
	OneTimeTokenJwtRepository oneTimeTokenJwtRepository;

	/**
	 * Erzeugt eine Instanz von AuthproviderSessionService
	 */
	public AuthJWTService() {

	}

	/**
	 * Erzeugt eine Instanz von AuthproviderSessionService zu Testzewecken ohne CDI.
	 */
	public AuthJWTService(final ClientService clientService, final JWTService jwtService) {

		this.clientService = clientService;
		this.jwtService = jwtService;
	}

	/**
	 * Erzeugt ein JWT. Der Vor-und Nachname wird nur für Clients verpackt, die diesen benötigen, sofern der
	 * ResourceOwner einen hat.
	 *
	 * @param resourceOwner
	 * @param client
	 * @return String
	 */
	public SignUpLogInResponseData createAuthorization(final ResourceOwner resourceOwner, final ClientCredentials clientCredentials,
		final String nonce) {

		try {

			Client client = clientService.findAndCheckClient(clientCredentials);

			JWTPayload jwt = jwtService.createJWT(resourceOwner, client);

		// @formatter:off
		return SignUpLogInResponseDataBuilder.instance()
			.withIdToken(jwt.getJwt())
			.withState(clientCredentials.getState())
			.withNonce(nonce)
			.build();
		// @formatter:on

		} catch (ClientAccessTokenNotFoundException e) {

			throw new AuthRuntimeException("ClientAccessToken mit accessToken='" + clientCredentials.getAccessToken()
				+ "' müsste an diesere Stelle vorhanden sein");
		}

	}

	/**
	 * Erzeugt ein JWT mit uuid und email des ResourceOwners und hinterlegt sie mit einer UUID im OneTimeTokenJwtRepo.
	 *
	 * @param resourceOwner
	 * @param clientCredentials
	 * @param nonce
	 * @return SignUpLogInResponseData
	 */
	public SignUpLogInResponseData createAndStoreAuthorization(final ResourceOwner resourceOwner,
		final ClientCredentials clientCredentials, final String nonce) {

		try {

			Client client = clientService.findAndCheckClient(clientCredentials);

			JWTPayload jwt = jwtService.createJWTWithEmail(resourceOwner, client);

			String oneTimeToken = UUID.randomUUID().toString();
			OneTimeTokenJwtData oneTimeTokenData = new OneTimeTokenJwtData(oneTimeToken, client.getClientId(), jwt.getJwt());
			oneTimeTokenJwtRepository.addToken(oneTimeTokenData);

		// @formatter:off
		return SignUpLogInResponseDataBuilder.instance()
			.withIdToken(oneTimeToken)
			.withState(clientCredentials.getState())
			.withNonce(nonce)
			.withOauthFlowType(OAuthFlowType.AUTHORIZATION_TOKEN_GRANT)
			.build();
		// @formatter:on

		} catch (ClientAccessTokenNotFoundException e) {

			throw new AuthRuntimeException("ClientAccessToken mit accessToken='" + clientCredentials.getAccessToken()
				+ "' müsste an diesere Stelle vorhanden sein");
		}
	}

	/**
	 * Tauscht das oneTimeToken gegen das JWT.
	 *
	 * @param oneTimeToken
	 * @param client Client
	 * @return String nicht null, das JWT
	 */
	public String exchangeTheOneTimeToken(final String oneTimeToken, final Client client)
		throws AuthRuntimeException, SecurityException {

		Optional<OneTimeTokenJwtData> optData = oneTimeTokenJwtRepository.getAndRemoveWithOneTimeToken(oneTimeToken);

		if (optData.isPresent()) {

			OneTimeTokenJwtData data = optData.get();

			if (!client.getClientId().equals(data.clientId())) {

				String message = "[exchangeTheOneTimeToken]: data.clientId stimmt nicht: expected="
					+ StringUtils.abbreviate(client.getClientId(), 11) + ", actual=" + data.clientId();

				throw new SecurityException(message);
			}

			return data.jwt();
		}

		String message = "[exchangeTheOneTimeToken]: Unbekanntes one time token oder weiterer Zugriffsversuch auf die OneTimeTokenJwtData: clientId="
			+ StringUtils.abbreviate(client.getClientId(), 11);

		throw new SecurityException(message);

		// return "fake-token-for-testing-purposes";
	}

}
