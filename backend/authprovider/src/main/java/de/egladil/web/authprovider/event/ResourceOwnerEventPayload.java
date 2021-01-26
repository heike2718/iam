// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.web.authprovider.domain.ResourceOwner;

/**
 * ResourceOwnerEventPayload
 */
public class ResourceOwnerEventPayload {

	@JsonProperty
	private String uuid;

	@JsonProperty
	private String loginName;

	@JsonProperty
	private String vorname;

	@JsonProperty
	private String nachname;

	@JsonProperty
	private String email;

	@JsonProperty
	private String nonce;

	private String clientId;

	ResourceOwnerEventPayload() {

	}

	public static ResourceOwnerEventPayload createFromResourceOwner(final ResourceOwner resourceOwner) {

		ResourceOwnerEventPayload result = new ResourceOwnerEventPayload();
		result.uuid = resourceOwner.getUuid();
		result.loginName = resourceOwner.getLoginName();
		result.vorname = resourceOwner.getVorname();
		result.nachname = resourceOwner.getNachname();
		result.email = resourceOwner.getEmail();
		return result;

	}

	public String getUuid() {

		return uuid;
	}

	public String getLoginName() {

		return loginName;
	}

	public String getVorname() {

		return vorname;
	}

	public String getNachname() {

		return nachname;
	}

	public String getEmail() {

		return email;
	}

	@Override
	public String toString() {

		return "ResourceOwnerEventPayload [uuid=" + uuid + ", vorname=" + vorname + ", nachname=" + nachname + "]";
	}

	public String getNonce() {

		return nonce;
	}

	public ResourceOwnerEventPayload withNonce(final String nonce) {

		this.nonce = nonce;
		return this;
	}

	public String getClientId() {

		return clientId;
	}

	public ResourceOwnerEventPayload withClientId(final String clientId) {

		this.clientId = clientId;
		return this;
	}

}
