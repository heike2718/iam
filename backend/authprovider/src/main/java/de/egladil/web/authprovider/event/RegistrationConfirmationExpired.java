// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.event;

import java.time.LocalDateTime;

import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.commons_net.time.CommonTimeUtils;

/**
 * RegistrationConfirmationExpired
 */
public class RegistrationConfirmationExpired implements AuthproviderEvent {

	private final LocalDateTime occuredOn;

	private ResourceOwnerEventPayload resourceOwner;

	RegistrationConfirmationExpired() {

		this.occuredOn = CommonTimeUtils.now();
	}

	public static RegistrationConfirmationExpired create(final ResourceOwner resourceOwner) {

		RegistrationConfirmationExpired result = new RegistrationConfirmationExpired();
		result.resourceOwner = ResourceOwnerEventPayload.createFromResourceOwner(resourceOwner);
		return result;
	}

	@Override
	public boolean propagateToListeners() {

		return true;
	}

	@Override
	public boolean writeToEventStore() {

		return true;
	}

	@Override
	public AuthproviderEventType eventType() {

		return AuthproviderEventType.REGISTRATION_CONFIRMATION_EXPIRED;
	}

	@Override
	public Object payload() {

		return this.resourceOwner;
	}

	@Override
	public String serializePayload() {

		return new ResourceOwnerEventPayloadSerializer().apply(resourceOwner);
	}

	@Override
	public LocalDateTime occuredOn() {

		return occuredOn;
	}

}
