// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.infrastructure.persistence.entities;

import de.egladil.web.bv_admin.domain.benutzer.CryptoAlgorithm;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PersistenterUserReadOnly
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "VW_USERS_SUCHE")
@NamedQueries({
	@NamedQuery(name = PersistenterUserReadOnly.FIND_BY_UUID, query = "select u from PersistenterUserReadOnly u where u.uuid = :uuid"),
	@NamedQuery(name = PersistenterUserReadOnly.FIND_BY_UUID_LIST, query = "select u from PersistenterUserReadOnly u where u.uuid in :uuids"),
	@NamedQuery(name = PersistenterUserReadOnly.FIND_FOR_MAILVERSAND_BY_UUID_LIST, query = "select u from PersistenterUserReadOnly u where u.aktiviert = :aktiviert and u.bannedForMails = :bannedForMails and u.uuid in :uuids"), })
public class PersistenterUserReadOnly {

	public static final String FIND_BY_UUID = "PersistenterUserReadOnly.FIND_BY_UUID";

	public static final String FIND_BY_UUID_LIST = "PersistenterUserReadOnly.FIND_BY_UUID_LIST";

	public static final String FIND_FOR_MAILVERSAND_BY_UUID_LIST = "PersistenterUserReadOnly.FIND_FOR_MAILVERSAND_BY_UUID_LIST";

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "UUID")
	private String uuid;

	@Column(name = "VORNAME")
	private String vorname;

	@Column(name = "NACHNAME")
	private String nachname;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "AKTIVIERT")
	private boolean aktiviert;

	@Column(name = "ROLLEN")
	private String rollen;

	@Column(name = "CRYPTO_ALGORITHM")
	@Enumerated(EnumType.STRING)
	private CryptoAlgorithm cryptoAlgorithm;

	@Column(name = "DATE_MODIFIED_STRING")
	private String aenderungsdatum;

	@Column(name = "SLZ_ID")
	private Long saltId;

	@Column(name = "ANZAHL_LOGINS")
	private int anzahlLogins;

	@Column(name = "BANNED_FOR_MAILS")
	private boolean bannedForMails;

	@Column(name = "PERMANENT")
	private boolean darfNichtGeloeschtWerden;
}
