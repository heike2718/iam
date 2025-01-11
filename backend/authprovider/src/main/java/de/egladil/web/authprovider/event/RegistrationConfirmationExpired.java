// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.event;

import de.egladil.web.authprovider.entities.ResourceOwner;

/**
 * RegistrationConfirmationExpired
 */
public class RegistrationConfirmationExpired extends AbstractAuthproviderEvent {

	/**
	 * @param resourceOwner
	 */
	public RegistrationConfirmationExpired(final ResourceOwner resourceOwner) {

		super(resourceOwner);
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
}
