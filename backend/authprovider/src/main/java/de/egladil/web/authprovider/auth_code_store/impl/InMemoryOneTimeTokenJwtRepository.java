// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.auth_code_store.impl;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.enterprise.context.ApplicationScoped;

import de.egladil.web.authprovider.auth_code_store.OneTimeTokenJwtData;
import de.egladil.web.authprovider.auth_code_store.OneTimeTokenJwtRepository;

/**
 * InMemoryOneTimeTokenJwtRepository
 */
@ApplicationScoped
public class InMemoryOneTimeTokenJwtRepository implements OneTimeTokenJwtRepository {

	private ConcurrentHashMap<String, OneTimeTokenJwtData> jwts = new ConcurrentHashMap<>();

	@Override
	public void addToken(final OneTimeTokenJwtData data) {

		if (data == null) {

			throw new IllegalArgumentException("data darf nicht null sein");
		}
		this.jwts.put(data.oneTimeToken(), data);

	}

	@Override
	public Optional<OneTimeTokenJwtData> getAndRemoveWithOneTimeToken(final String oneTimeToken) {

		OneTimeTokenJwtData data = this.jwts.get(oneTimeToken);

		if (data != null) {

			this.jwts.remove(oneTimeToken);
			return Optional.of(data);
		}

		return Optional.empty();
	}

}
