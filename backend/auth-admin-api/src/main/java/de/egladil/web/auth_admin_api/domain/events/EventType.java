// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.events;

/**
 * EventType
 */
public enum EventType {

	LOGINVERSUCH_INAKTIVER_USER("LoginversuchInaktiverUser"),
	REGISTRATION_CONFIRMATION_EXPIRED("RegistrationConfirmationExpired"),
	USER_CREATED("UserCreated"),
	USER_ACTIVATED("UserActivated"),
	USER_DEACTIVATED("UserDeactivated"),
	USER_CHANGED("UserChanged"),
	USER_DELETED("UserDeleted");

	private final String label;

	private EventType(final String label) {

		this.label = label;
	}

	public String getLabel() {

		return label;
	}

}
