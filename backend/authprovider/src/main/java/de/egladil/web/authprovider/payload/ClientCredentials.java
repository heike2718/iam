// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.web.commons_validation.SecUtils;
import de.egladil.web.commons_validation.annotations.UuidString;

/**
 * ClientCredentials<br>
 * <br>
 * https://javaee.github.io/jsonb-spec/users-guide.html
 */
public class ClientCredentials {

	@NotBlank
	@UuidString
	@Size(max = 50)
	@JsonProperty
	private String accessToken;

	@NotBlank
	@URL
	@JsonProperty
	private String redirectUrl;

	@Size(max = 150)
	private String state;

	public static ClientCredentials createWithState(final String accessToken, final String redirectUrl, final String state) {

		ClientCredentials result = new ClientCredentials();
		result.accessToken = accessToken;
		result.redirectUrl = redirectUrl;
		result.state = state;

		return result;

	}

	public ClientCredentials() {

	}

	public String getRedirectUrl() {

		return redirectUrl;
	}

	@Override
	public String toString() {

		return "ClientCredentials [accessToken=" + accessToken + ", redirectUrl=" + redirectUrl + "]";
	}

	public String getAccessToken() {

		return accessToken;
	}

	public String getState() {

		return state;
	}

	public void clean() {

		if (accessToken != null) {

			this.accessToken = SecUtils.wipe(accessToken);
		}
	}

}
