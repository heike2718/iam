// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_server.event;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DeleteUserCommand
 */
public class DeleteUserCommand {

	@JsonProperty
	private String syncToken;

	@JsonProperty
	private String uuid;

	public static DeleteUserCommand fromEvent(final UserDeleted event) {

		DeleteUserCommand result = new DeleteUserCommand();
		result.uuid = event.uuid();
		return result;
	}

	public DeleteUserCommand withSyncToken(final String syncToken) {

		this.syncToken = syncToken;
		return this;

	}

}
