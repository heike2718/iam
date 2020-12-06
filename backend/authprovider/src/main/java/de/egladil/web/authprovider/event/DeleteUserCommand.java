// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.event;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DeleteUserCommand
 */
public class DeleteUserCommand {

	@JsonProperty
	private String syncToken;

	@JsonProperty
	private String uuid;

	public static DeleteUserCommand create(final String uuid) {

		DeleteUserCommand result = new DeleteUserCommand();
		result.uuid = uuid;
		return result;
	}

	public DeleteUserCommand withSyncToken(final String syncToken) {

		this.syncToken = syncToken;
		return this;

	}

}
