// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.infrastructure.persistence.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;

/**
 * PersistenterUser
 */
@Entity
@Table(name = "USERS")
@NamedQueries({
	@NamedQuery(name = "PersistenterUser.FIND_BY_UUID", query = "select b from PersistenterUser b where b.uuid = :uuid"),
	@NamedQuery(name = "PersistenterUser.FIND_USERS_BY_UUIDS", query = "select b from PersistenterUser b where b.uuid in :uuids") })
public class PersistenterUser {

	public static final String FIND_BY_UUID = "PersistenterUser.FIND_BY_UUID";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "UUID")
	private String uuid;

	@Column(name = "LOGINNAME")
	private String loginName;

	@Column(name = "VORNAME")
	private String vorname;

	@Column(name = "NACHNAME")
	private String nachname;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "AKTIVIERT")
	private boolean aktiviert;

	@Column(name = "ANONYM")
	private boolean anonym;

	@Column(name = "ANZAHL_LOGINS")
	private int anzahlLogins;

	@Column(name = "ROLLEN")
	private String rollen;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_MODIFIED")
	private Date datumGeaendert;

	@Version
	@Column(name = "VERSION")
	private int version;

	public Long getId() {

		return id;
	}

	public void setId(final Long id) {

		this.id = id;
	}

	public String getUuid() {

		return uuid;
	}

	public void setUuid(final String uuid) {

		this.uuid = uuid;
	}

	public String getLoginName() {

		return loginName;
	}

	public void setLoginName(final String loginName) {

		this.loginName = loginName;
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

	public int getAnzahlLogins() {

		return anzahlLogins;
	}

	public void setAnzahlLogins(final int anzahlLogins) {

		this.anzahlLogins = anzahlLogins;
	}

	public String getRollen() {

		return rollen;
	}

	public void setRollen(final String roles) {

		this.rollen = roles;
	}

	public Date getDatumGeaendert() {

		return datumGeaendert;
	}

	public void setDatumGeaendert(final Date datumGeaendert) {

		this.datumGeaendert = datumGeaendert;
	}

	public int getVersion() {

		return version;
	}

	public void setVersion(final int version) {

		this.version = version;
	}

}
