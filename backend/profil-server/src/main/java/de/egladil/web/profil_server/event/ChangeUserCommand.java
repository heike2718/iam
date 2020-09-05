// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_server.event;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ChangeUserCommand
 */
public class ChangeUserCommand {

	@JsonProperty
	private String sendingClientId;

	@JsonProperty
	private String uuid;

	@JsonProperty
	private String vorname;

	@JsonProperty
	private String nachname;

	@JsonProperty
	private String email;

	ChangeUserCommand() {

	}

	public ChangeUserCommand(final String clientId, final UserChanged event) {

		this.sendingClientId = clientId;
		this.uuid = event.uuid();
		this.email = event.email();
		this.vorname = event.vorname();
		this.nachname = event.nachname();
	}

}
