// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.event;

/**
 * AuthproviderEventType
 */
public enum AuthproviderEventType {

	LOGINVERSUCH_INAKTIVER_USER("LoginversuchInaktiverUser"),
	REGISTRATION_CONFIRMATION_EXPIRED("RegistrationConfirmationExpired"),
	USER_CREATED("UserCreated");

	private final String label;

	private AuthproviderEventType(final String label) {

		this.label = label;
	}

	public String getLabel() {

		return label;
	}

}
