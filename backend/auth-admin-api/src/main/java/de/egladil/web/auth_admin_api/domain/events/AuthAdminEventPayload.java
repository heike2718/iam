// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.events;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * AuthAdminEventPayload
 */
public class AuthAdminEventPayload {

	@JsonProperty
	private String akteur;

	@JsonProperty
	private String target;

	public AuthAdminEventPayload() {

	}

	public String getAkteur() {

		return akteur;
	}

	public AuthAdminEventPayload withAkteur(final String uuidAkteur) {

		this.akteur = uuidAkteur;
		return this;
	}

	public String getTarget() {

		return target;
	}

	public AuthAdminEventPayload withTarget(final String uuidUser) {

		this.target = uuidUser;
		return this;
	}

}
