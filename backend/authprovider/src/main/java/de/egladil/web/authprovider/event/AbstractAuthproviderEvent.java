// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.event;

import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.commons_net.time.CommonTimeUtils;

/**
 * AbstractAuthproviderEvent
 */
public abstract class AbstractAuthproviderEvent implements AuthproviderEvent {

	private final LocalDateTime occuredOn;

	private ResourceOwnerEventPayload resourceOwner;

	public AbstractAuthproviderEvent() {

		this.occuredOn = CommonTimeUtils.now();
	}

	public AbstractAuthproviderEvent(final ResourceOwner resourceOwner) {

		this();
		this.resourceOwner = ResourceOwnerEventPayload.createFromResourceOwner(resourceOwner);

	}

	@Override
	public String serializePayload() {

		return new ResourceOwnerEventPayloadSerializer().apply(resourceOwner);
	}

	@Override
	public Object payload() {

		return resourceOwner;
	}

	@Override
	public LocalDateTime occuredOn() {

		return this.occuredOn;
	}

	@Override
	public void writeToConsoleQuietly() {

		try {

			String body = new ObjectMapper().writeValueAsString(resourceOwner);
			System.out.println(eventType().getLabel() + ": " + body);
		} catch (JsonProcessingException e) {

			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}

	protected void setResourceOwner(final ResourceOwnerEventPayload resourceOwner) {

		this.resourceOwner = resourceOwner;
	}
}
