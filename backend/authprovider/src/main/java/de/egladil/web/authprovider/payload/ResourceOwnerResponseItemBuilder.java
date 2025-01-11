// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.payload;

/**
 * ResourceOwnerResponseItemBuilder
 */
public class ResourceOwnerResponseItemBuilder {

	private final String uuid;

	private final boolean existend;

	private String vorname;

	private String nachname;

	private final String fullName;

	private String loginName;

	private String email;

	private final boolean aktiviert;

	private String roles;

	/**
	 * @param uuid
	 * @param fullName
	 */
	public ResourceOwnerResponseItemBuilder(final String uuid, final boolean existend, final String fullName,
		final boolean aktiviert) {

		this.uuid = uuid;
		this.existend = existend;
		this.fullName = existend && aktiviert ? fullName : null;
		this.aktiviert = aktiviert;
	}

	public ResourceOwnerResponseItemBuilder withLoginName(final String loginName) {

		this.loginName = loginName;
		return this;
	}

	public ResourceOwnerResponseItemBuilder withEmail(final String email) {

		this.email = email;

		return this;
	}

	public ResourceOwnerResponseItemBuilder withRoles(final String roles) {

		this.roles = roles;
		return this;
	}

	public ResourceOwnerResponseItemBuilder withVorname(final String vorname) {

		this.vorname = vorname;
		return this;
	}

	public ResourceOwnerResponseItemBuilder withNachname(final String nachname) {

		this.nachname = nachname;
		return this;
	}

	public ResourceOwnerResponseItem build() {

		return ResourceOwnerResponseItem.create(uuid, existend, vorname, nachname, fullName, loginName, email, aktiviert, roles);
	}
}
