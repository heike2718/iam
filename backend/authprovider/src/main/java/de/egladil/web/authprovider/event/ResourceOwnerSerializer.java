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

import de.egladil.web.authprovider.domain.ResourceOwner;

/**
 * ResourceOwnerSerializer
 */
public class ResourceOwnerSerializer implements Function<ResourceOwner, String> {

	private static final Logger LOG = LoggerFactory.getLogger(ResourceOwnerSerializer.class);

	@Override
	public String apply(final ResourceOwner resourceOwner) {

		try {

			return new ObjectMapper().writeValueAsString(resourceOwner);

		} catch (JsonProcessingException e) {

			LOG.error("konnte event nicht serialisieren: " + e.getMessage(), e);

			return null;
		}
	}

}
