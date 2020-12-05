// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.event;

import java.time.LocalDateTime;

import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.commons_net.time.CommonTimeUtils;

/**
 * LoginversuchInaktiverUser
 */
public class LoginversuchInaktiverUser implements AuthproviderEvent {

	private final LocalDateTime occuredOn;

	private ResourceOwner resourceOwner;

	LoginversuchInaktiverUser() {

		this.occuredOn = CommonTimeUtils.now();
	}

	public LoginversuchInaktiverUser(final ResourceOwner resourceOwner) {

		this();
		this.resourceOwner = resourceOwner;
	}

	@Override
	public boolean writeToEventStore() {

		return true;
	}

	@Override
	public boolean propagateToListeners() {

		return true;
	}

	@Override
	public AuthproviderEventType eventType() {

		return AuthproviderEventType.LOGINVERSUCH_INAKTIVER_USER;
	}

	@Override
	public Object payload() {

		return resourceOwner;
	}

	@Override
	public String serializePayload() {

		return new ResourceOwnerSerializer().apply(resourceOwner);
	}

	@Override
	public LocalDateTime occuredOn() {

		return this.occuredOn;
	}

}
