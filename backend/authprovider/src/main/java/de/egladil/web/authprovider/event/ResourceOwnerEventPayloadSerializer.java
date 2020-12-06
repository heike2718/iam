// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.event;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ResourceOwnerEventPayloadSerializer
 */
public class ResourceOwnerEventPayloadSerializer implements Function<ResourceOwnerEventPayload, String> {

	private static final Logger LOG = LoggerFactory.getLogger(ResourceOwnerEventPayloadSerializer.class);

	@Override
	public String apply(final ResourceOwnerEventPayload resourceOwner) {

		try {

			return new ObjectMapper().writeValueAsString(resourceOwner);

		} catch (JsonProcessingException e) {

			LOG.error("konnte event nicht serialisieren: " + e.getMessage(), e);

			return null;
		}
	}

}
