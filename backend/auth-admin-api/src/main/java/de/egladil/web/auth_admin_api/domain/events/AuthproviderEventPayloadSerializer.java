// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.events;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * AuthproviderEventPayloadSerializer
 */
public class AuthproviderEventPayloadSerializer implements Function<AuthAdminEventPayload, String> {

	private static final Logger LOG = LoggerFactory.getLogger(AuthproviderEventPayloadSerializer.class);

	@Override
	public String apply(final AuthAdminEventPayload resourceOwner) {

		try {

			return new ObjectMapper().writeValueAsString(resourceOwner);

		} catch (JsonProcessingException e) {

			LOG.error("konnte event nicht serialisieren: " + e.getMessage(), e);

			return null;
		}
	}

}
