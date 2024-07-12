// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.infrastructure.persistence.entities;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.UuidGenerator.Style;

import de.egladil.web.auth_admin_api.domain.Jobstatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

/**
 * PersistenterMailversandauftrag
 */
@Entity
@Table(name = "MAILVERSANDAUFTRAEGE")
@NamedQueries({
	@NamedQuery(
		name = "PersistenterMailversandauftrag.FIND_FOR_NEWSLETTER",
		query = "select v from PersistenterMailversandauftrag v where v.idInfomailtext = :idInfomailtext order by v.erfasstAm"),
	@NamedQuery(
		name = "PersistenterMailversandauftrag.FIND_BY_UUID",
		query = "select v from PersistenterMailversandauftrag v where v.uuid = :uuid"),
	@NamedQuery(
		name = "PersistenterMailversandauftrag.LOAD_ALL",
		query = "select v from PersistenterMailversandauftrag v order by v.erfasstAm"),
	@NamedQuery(
		name = "PersistenterMailversandauftrag.FIND_NOT_COMPLETED",
		query = "select v from PersistenterMailversandauftrag v where v.status = :statusWaiting or v.status = :statusInProgress order by v.erfasstAm")
})
public class PersistenterMailversandauftrag {

	public static final String FIND_FOR_NEWSLETTER = "PersistenterMailversandauftrag.FIND_FOR_NEWSLETTER";

	public static final String FIND_BY_UUID = "PersistenterMailversandauftrag.FIND_BY_UUID";

	public static final String LOAD_ALL = "PersistenterMailversandauftrag.LOAD_ALL";

	public static final String FIND_NOT_COMPLETED = "PersistenterMailversandauftrag.FIND_NOT_COMPLETED";

	@Id
	@UuidGenerator(style = Style.RANDOM)
	@Column(name = "UUID", insertable = false, nullable = false, unique = true, updatable = false)
	private String uuid;

	@Column(name = "UUID_INFOMAIL_TEXT")
	private String idInfomailtext;

	@Column(name = "EMPFAENGER_IDS")
	private String empgaengerIDs;

	@Column(name = "EMPFAENGER_CHECKSUM")
	private String checksumEmpfaengerIDs;

	@Column(name = "VERSAND_JAHR_MONAT")
	private String versandJahrMonat;

	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	private Jobstatus status;

	@Column(name = "ERFASST_AM")
	private LocalDateTime erfasstAm;

	@Column(name = "VERSAND_BEGONNEN_AM")
	private LocalDateTime versandBegonnenAm;

	@Column(name = "VERSAND_BEENDET_AM")
	private LocalDateTime versandBeendetAm;

	@Column(name = "ANZAHL_EMPFAENGER")
	private int anzahlEmpfaenger;

	@Column(name = "ANZAHL_VERSENDET")
	private int anzahlVersendet;

	@Column(name = "MIT_FEHLERN")
	private boolean versandMitFehlern;

	@Column(name = "DATE_MODIFIED")
	private Date geaendertAm;

	@Version
	@Column(name = "VERSION")
	private int version;

}
