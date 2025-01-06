// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.event;

/**
 * AuthproviderEventType
 */
public enum AuthproviderEventType {

	BOT_ATTACK("Bot-Attacke: kleber war gefüllt"),
	LOGINVERSUCH_INAKTIVER_USER("LoginversuchInaktiverUser"),
	REGISTRATION_CONFIRMATION_EXPIRED("RegistrationConfirmationExpired"),
	USER_CREATED("UserCreated"),
	USER_ACTIVATED("UserActivated"),
	USER_DEACTIVATED("UserDeactivated"),
	USER_CHANGED("UserChanged"),
	USER_DELETED("UserDeleted");

	private final String label;

	private AuthproviderEventType(final String label) {

		this.label = label;
	}

	public String getLabel() {

		return label;
	}

}
