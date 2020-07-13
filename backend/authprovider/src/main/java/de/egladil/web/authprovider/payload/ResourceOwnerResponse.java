// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ResourceOwnerResponse
 */
public class ResourceOwnerResponse {

	@JsonProperty
	private String nonce;

	@JsonProperty
	private List<ResourceOwnerResponseItem> items;

	/**
	 *
	 */
	public ResourceOwnerResponse() {

	}

	/**
	 * @param nonce
	 * @param items
	 */
	public ResourceOwnerResponse(final String nonce, final List<ResourceOwnerResponseItem> items) {

		this.nonce = nonce;
		this.items = items;
	}

}
