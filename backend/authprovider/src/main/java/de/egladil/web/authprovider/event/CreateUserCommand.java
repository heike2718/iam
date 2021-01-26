// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.event;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * CreateUserCommand
 */
public class CreateUserCommand {

	@JsonProperty
	private String syncToken;

	@JsonProperty
	private String uuid;

	@JsonProperty
	private String fullName;

	@JsonProperty
	private String email;

	@JsonProperty
	private String nonce;

	@JsonProperty
	private String clientId;

	@Override
	public String toString() {

		return "CreateUserCommand [uuid=" + StringUtils.abbreviate(uuid, 11) + ", fullName=" + fullName + ", nonce=" + nonce
			+ ", clientId=" + StringUtils.abbreviate(clientId, 8) + "]";
	}

	public String getSyncToken() {

		return syncToken;
	}

	public CreateUserCommand withSyncToken(final String syncToken) {

		this.syncToken = syncToken;
		return this;
	}

	public String getUuid() {

		return uuid;
	}

	public CreateUserCommand withUuid(final String uuid) {

		this.uuid = uuid;
		return this;
	}

	public String getFullName() {

		return fullName;
	}

	public CreateUserCommand withFullName(final String fullName) {

		this.fullName = fullName;
		return this;
	}

	public String getEmail() {

		return email;
	}

	public CreateUserCommand withEmail(final String email) {

		this.email = email;
		return this;
	}

	public String getNonce() {

		return nonce;
	}

	public CreateUserCommand withNonce(final String nonce) {

		this.nonce = nonce;
		return this;
	}

	public String getClientId() {

		return clientId;
	}

	public CreateUserCommand withClientId(final String abbreviatedClientId) {

		this.clientId = abbreviatedClientId;
		return this;
	}

}
