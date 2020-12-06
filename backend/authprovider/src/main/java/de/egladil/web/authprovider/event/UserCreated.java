// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.event;

import java.time.LocalDateTime;

import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.commons_net.time.CommonTimeUtils;

/**
 * UserCreated
 */
public class UserCreated implements AuthproviderEvent {

	private final LocalDateTime occuredOn;

	private ResourceOwner resourceOwner;

	UserCreated() {

		this.occuredOn = CommonTimeUtils.now();
	}

	public UserCreated(final ResourceOwner resourceOwner) {

		this();
		this.resourceOwner = resourceOwner;
	}

	@Override
	public boolean propagateToListeners() {

		return true;
	}

	@Override
	public boolean writeToEventStore() {

		return false;
	}

	@Override
	public AuthproviderEventType eventType() {

		return AuthproviderEventType.USER_CREATED;
	}

	@Override
	public Object payload() {

		return resourceOwner;
	}

	@Override
	public String serializePayload() {

		return resourceOwner.getUuid();
	}

	@Override
	public LocalDateTime occuredOn() {

		return occuredOn;
	}

}
