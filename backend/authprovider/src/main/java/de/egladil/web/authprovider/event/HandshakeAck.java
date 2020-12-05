// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.event;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * HandshakeAck
 */
public class HandshakeAck {

	@JsonProperty
	private String syncToken;

	@JsonProperty
	private String nonce;

	public static HandshakeAck fromResponse(final Map<String, Object> data) {

		HandshakeAck result = new HandshakeAck();

		{

			Object obj = data.get("syncToken");

			if (obj != null) {

				result.syncToken = (String) obj;
			}
		}

		{

			Object obj = data.get("nonce");

			if (obj != null) {

				result.nonce = (String) obj;
			}
		}
		return result;

	}

	public String syncToken() {

		return syncToken;
	}

	public String nonce() {

		return nonce;
	}

}
