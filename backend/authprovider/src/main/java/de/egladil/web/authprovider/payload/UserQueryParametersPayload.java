// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.payload;

import de.egladil.web.auth_validations.annotations.UuidString;
import de.egladil.web.auth_validations.dto.OAuthClientCredentials;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * UserQueryParametersPayload
 */
public class UserQueryParametersPayload {

	@UuidString
	private String adminUUID;

	@NotNull
	private OAuthClientCredentials clientCredentials;

	@NotNull
	private BenutzerSuchmodus parameterTyp;

	@NotBlank
	private String query;

	public String getAdminUUID() {

		return adminUUID;
	}

	public void setAdminUUID(final String adminUUID) {

		this.adminUUID = adminUUID;
	}

	public OAuthClientCredentials getClientCredentials() {

		return clientCredentials;
	}

	public void setClientCredentials(final OAuthClientCredentials clientCredentials) {

		this.clientCredentials = clientCredentials;
	}

	public BenutzerSuchmodus getParameterTyp() {

		return parameterTyp;
	}

	public void setParameterTyp(final BenutzerSuchmodus parameterTyp) {

		this.parameterTyp = parameterTyp;
	}

	public String getQuery() {

		return query;
	}

	public void setQuery(final String query) {

		this.query = query;
	}

}
