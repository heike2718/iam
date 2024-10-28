// =====================================================
// Project: profil-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_api.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * ResourceOwner
 */
@Entity
@Table(name = "USERS")
@NamedQueries({
	@NamedQuery(name = "FIND_BY_UUID", query = "select o from ResourceOwner o where o.uuid = :uuid"),
	@NamedQuery(
		name = "FIND_OTHER_BY_EMAIL", query = "select o from ResourceOwner o where o.uuid != :uuid and lower(o.email) = :email"),
	@NamedQuery(
		name = "FIND_OTHER_BY_LOGINNAME",
		query = "select o from ResourceOwner o where o.uuid != :uuid and o.loginName = :loginName"),
})
public class ResourceOwner {

	public static final String FIND_BY_UUID = "FIND_BY_UUID";

	public static final String FIND_OTHER_BY_EMAIL = "FIND_OTHER_BY_EMAIL";

	public static final String FIND_OTHER_BY_LOGINNAME = "FIND_OTHER_BY_LOGINNAME";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@NotBlank
	@Size(min = 1, max = 40)
	@Column(name = "UUID")
	private String uuid;

	@NotBlank
	@Size(min = 1, max = 255)
	@Column(name = "LOGINNAME")
	private String loginName;

	@NotBlank
	@Size(min = 1, max = 255)
	@Column(name = "EMAIL")
	private String email;

	@Version
	@Column(name = "VERSION")
	private int version;

	@Override
	public String toString() {

		return "ResourceOwner [id=" + id + ", uuid=" + uuid + "]";
	}
}
