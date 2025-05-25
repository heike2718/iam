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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PersistenterUser
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

	@Column(name = "BANNED_FOR_MAILS")
	private boolean bannedForMails;

	@Column(name = "PERMANENT")
	private boolean darfNichtGeloeschtWerden;

	@Version
	@Column(name = "VERSION")
	private int version;

}
