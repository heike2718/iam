// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.domain;

import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.web.auth_validations.annotations.InputSecured;
import de.egladil.web.auth_validations.annotations.UuidString;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * ResourceOwner is the subject of an identity.<br>
 * <br>
 * <code>Beispiel:</code> Ein Lehrer in der Minikänguru-Anwendung, der Admin der Minikänguru-Admin-Anwendung, jemand,
 * der die Checklistenapp verwenden darf,...
 */
@Entity
@Table(name = "USERS")
@NamedQueries({
	@NamedQuery(name = "FIND_BY_EMAIL", query = "select o from ResourceOwner o where lower(o.email) = :email"),
	@NamedQuery(name = "FIND_BY_EMAIL_LIKE", query = "select o from ResourceOwner o where lower(o.email) like :email"),
	@NamedQuery(name = "FIND_BY_LOGINNAME", query = "select o from ResourceOwner o where o.loginName = :loginName"),
	@NamedQuery(name = "FIND_BY_LOGINNAME_LIKE", query = "select o from ResourceOwner o where lower(o.loginName) like :loginName"),
	@NamedQuery(name = "FIND_BY_UUID", query = "select o from ResourceOwner o where o.uuid = :uuid"),
	@NamedQuery(name = "FIND_BY_UUID_LIKE", query = "select o from ResourceOwner o where o.uuid LIKE :uuid"),
	@NamedQuery(
		name = "FIND_OTHER_BY_EMAIL", query = "select o from ResourceOwner o where o.uuid != :uuid and lower(o.email) = :email"),
	@NamedQuery(
		name = "FIND_OTHER_BY_LOGINNAME",
		query = "select o from ResourceOwner o where o.uuid != :uuid and lower(o.loginName) = :loginName"),
})
public class ResourceOwner implements AuthProviderEntity {

	/* serialVersionUID */
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	public static final String FIND_BY_EMAIL = "FIND_BY_EMAIL";

	@JsonIgnore
	public static final String FIND_BY_EMAIL_LIKE = "FIND_BY_EMAIL_LIKE";

	@JsonIgnore
	public static final String FIND_BY_LOGINNAME = "FIND_BY_LOGINNAME";

	@JsonIgnore
	public static final String FIND_BY_UUID = "FIND_BY_UUID";

	@JsonIgnore
	public static final String FIND_BY_LOGINNAME_LIKE = "FIND_BY_LOGINNAME_LIKE";

	@JsonIgnore
	public static final String FIND_BY_UUID_LIKE = "FIND_BY_UUID_LIKE";

	@JsonIgnore
	public static final String FIND_OTHER_BY_EMAIL = "FIND_OTHER_BY_EMAIL";

	@JsonIgnore
	public static final String FIND_OTHER_BY_LOGINNAME = "FIND_OTHER_BY_LOGINNAME";

	public static ResourceOwner createAktiviert(final String uuid, final String loginName, final String email) {

		ResourceOwner result = new ResourceOwner();
		result.uuid = uuid;
		result.loginName = loginName;
		result.email = email;
		result.aktiviert = true;
		return result;
	}

	public static ResourceOwner createDeaktiviert(final String uuid, final String loginName, final String email) {

		ResourceOwner result = new ResourceOwner();
		result.uuid = uuid;
		result.loginName = loginName;
		result.email = email;
		result.aktiviert = false;
		return result;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	@JsonIgnore
	private Long id;

	@UuidString
	@NotBlank
	@Size(min = 1, max = 40)
	@Column(name = "UUID")
	@JsonProperty
	private String uuid;

	@NotBlank
	@InputSecured
	@Size(min = 1, max = 255)
	@Column(name = "LOGINNAME")
	@JsonIgnore
	private String loginName;

	@InputSecured
	@Size(min = 1, max = 100)
	@Column(name = "VORNAME")
	@JsonIgnore
	private String vorname;

	@InputSecured
	@Size(min = 1, max = 100)
	@Column(name = "NACHNAME")
	@JsonIgnore
	private String nachname;

	@NotBlank
	@Email
	@Size(min = 1, max = 255)
	@Column(name = "EMAIL")
	@JsonIgnore
	private String email;

	@Column(name = "AKTIVIERT")
	private boolean aktiviert;

	@Column(name = "ANONYM")
	private boolean anonym;

	@Column(name = "ANZAHL_LOGINS")
	@JsonIgnore
	private int anzahlLogins;

	@Column(name = "ROLLEN")
	@JsonProperty
	private String roles;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_MODIFIED")
	@JsonIgnore
	private Date datumGeaendert;

	@Version
	@Column(name = "VERSION")
	@JsonIgnore
	private int version;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JoinColumn(name = "PID", referencedColumnName = "ID")
	private LoginSecrets loginSecrets;

	@Override
	public Long getId() {

		return this.id;
	}

	public String getLoginName() {

		return loginName;
	}

	public void setLoginName(final String loginName) {

		this.loginName = loginName;
	}

	public String getEmail() {

		return email;
	}

	public void setEmail(final String email) {

		this.email = email;
	}

	public boolean isAktiviert() {

		return aktiviert;
	}

	public void setAktiviert(final boolean aktiviert) {

		this.aktiviert = aktiviert;
	}

	public boolean isAnonym() {

		return anonym;
	}

	public void setAnonym(final boolean anonym) {

		this.anonym = anonym;
	}

	public void setId(final Long id) {

		this.id = id;
	}

	public int getAnzahlLogins() {

		return anzahlLogins;
	}

	public void setAnzahlLogins(final int anzahlLogins) {

		this.anzahlLogins = anzahlLogins;
	}

	public Date getDatumGeaendert() {

		return datumGeaendert;
	}

	public void setDatumGeaendert(final Date datumGeaendert) {

		this.datumGeaendert = datumGeaendert;
	}

	public String getUuid() {

		return uuid;
	}

	public void setUuid(final String uuid) {

		this.uuid = uuid;
	}

	public LoginSecrets getLoginSecrets() {

		return loginSecrets;
	}

	public void setLoginSecrets(final LoginSecrets loginSecrets) {

		this.loginSecrets = loginSecrets;
	}

	public String printEmailLogin() {

		return "[email='" + email + "', loginName='" + loginName + "']";
	}

	@Override
	public int hashCode() {

		return Objects.hash(uuid);
	}

	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {

			return true;
		}

		if (obj == null) {

			return false;
		}

		if (getClass() != obj.getClass()) {

			return false;
		}
		ResourceOwner other = (ResourceOwner) obj;
		return Objects.equals(uuid, other.uuid);
	}

	public String getVorname() {

		return vorname;
	}

	public void setVorname(final String vorname) {

		this.vorname = vorname;
	}

	public String getNachname() {

		return nachname;
	}

	public void setNachname(final String nachname) {

		this.nachname = nachname;
	}

	public boolean hasName() {

		return StringUtils.isNotBlank(vorname) || StringUtils.isNotBlank(nachname);
	}

	@JsonProperty
	public String getFullName() {

		if (!hasName()) {

			return "";
		}
		return vorname + " " + nachname;
	}

	public String getRoles() {

		return roles;
	}

	public void setRoles(final String roles) {

		this.roles = roles;
	}

	@Override
	public String toString() {

		return "ResourceOwner [uuid=" + StringUtils.abbreviate(uuid, 11) + ", fullName=" + getFullName() + ", loginName="
			+ loginName + ", email=" + email
			+ "]";
	}

	public String toLogString() {

		return "ResourceOwner [uuid=" + StringUtils.abbreviate(uuid, 11) + ", fullName=" + getFullName() + ", loginName="
			+ loginName + ", email=" + StringUtils.abbreviate(email, 9)
			+ "]";
	}
}
