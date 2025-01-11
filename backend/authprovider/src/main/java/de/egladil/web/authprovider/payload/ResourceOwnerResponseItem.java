// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ResourceOwnerResponseItem
 */
public class ResourceOwnerResponseItem {

	@JsonProperty
	private String uuid;

	@JsonProperty
	private String vorname;

	@JsonProperty
	private String nachname;

	@JsonProperty
	private String fullName;

	@JsonProperty
	private String loginName;

	@JsonProperty
	private String email;

	@JsonProperty
	private boolean aktiviert;

	@JsonProperty
	private boolean existend;

	@JsonProperty
	private String roles;

	public static ResourceOwnerResponseItem create(final String uuid, final boolean existend, final String vorname,
		final String nachname, final String fullName, final String loginName, final String email, final boolean aktiviert,
		final String roles) {

		ResourceOwnerResponseItem result = new ResourceOwnerResponseItem();
		result.uuid = uuid;
		result.existend = existend;
		result.vorname = vorname;
		result.nachname = nachname;
		result.fullName = fullName;
		result.loginName = loginName;
		result.email = email;
		result.aktiviert = aktiviert;
		result.roles = roles;
		return result;

	}

	/**
	 *
	 */
	ResourceOwnerResponseItem() {

	}

	public String getUuid() {

		return uuid;
	}

	public void setUuid(final String uuid) {

		this.uuid = uuid;
	}

	public String getFullName() {

		return fullName;
	}

	public void setFullName(final String fullName) {

		this.fullName = fullName;
	}

	public String getLoginName() {

		return loginName;
	}

	public String getEmail() {

		return email;
	}

	public boolean isAktiviert() {

		return aktiviert;
	}

	public String getRoles() {

		return roles;
	}

	public boolean isExistend() {

		return existend;
	}

	public String getVorname() {

		return vorname;
	}

	public String getNachname() {

		return nachname;
	}

}
