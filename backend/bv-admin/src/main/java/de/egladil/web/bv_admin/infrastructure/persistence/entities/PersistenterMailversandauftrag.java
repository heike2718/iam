// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.infrastructure.persistence.entities;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.UuidGenerator.Style;

import de.egladil.web.bv_admin.domain.Jobstatus;
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
	@NamedQuery(name = "PersistenterMailversandauftrag.FIND_BY_UUID", query = "select v from PersistenterMailversandauftrag v where v.uuid = :uuid"),
	@NamedQuery(name = "PersistenterMailversandauftrag.FIND_NOT_COMPLETED", query = "select v from PersistenterMailversandauftrag v where v.status = :statusWaiting or v.status = :statusInProgress order by v.erfasstAm") })
public class PersistenterMailversandauftrag {

	public static final String FIND_BY_UUID = "PersistenterMailversandauftrag.FIND_BY_UUID";

	public static final String FIND_NOT_COMPLETED = "PersistenterMailversandauftrag.FIND_NOT_COMPLETED";

	@Id
	@UuidGenerator(style = Style.RANDOM)
	@Column(name = "UUID", insertable = false, nullable = false, unique = true, updatable = false)
	private String uuid;

	@Column(name = "UUID_INFOMAIL_TEXT")
	private String idInfomailtext;

	@Column(name = "BETREFF")
	public String betreff;

	@Column(name = "MAILTEXT")
	public String mailtext;

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
	private long anzahlEmpfaenger;

	@Column(name = "ANZAHL_VERSENDET")
	private long anzahlVersendet;

	@Column(name = "MIT_FEHLERN")
	private boolean versandMitFehlern;

	@Column(name = "DATE_MODIFIED")
	private Date geaendertAm;

	@Version
	@Column(name = "VERSION")
	private int version;

	public String getIdInfomailtext() {

		return idInfomailtext;
	}

	public void setIdInfomailtext(final String idInfomailtext) {

		this.idInfomailtext = idInfomailtext;
	}

	public String getChecksumEmpfaengerIDs() {

		return checksumEmpfaengerIDs;
	}

	public void setChecksumEmpfaengerIDs(final String checksumEmpfaengerIDs) {

		this.checksumEmpfaengerIDs = checksumEmpfaengerIDs;
	}

	public String getVersandJahrMonat() {

		return versandJahrMonat;
	}

	public void setVersandJahrMonat(final String versandJahrMonat) {

		this.versandJahrMonat = versandJahrMonat;
	}

	public Jobstatus getStatus() {

		return status;
	}

	public void setStatus(final Jobstatus status) {

		this.status = status;
	}

	public LocalDateTime getErfasstAm() {

		return erfasstAm;
	}

	public void setErfasstAm(final LocalDateTime erfasstAm) {

		this.erfasstAm = erfasstAm;
	}

	public LocalDateTime getVersandBegonnenAm() {

		return versandBegonnenAm;
	}

	public void setVersandBegonnenAm(final LocalDateTime versandBegonnenAm) {

		this.versandBegonnenAm = versandBegonnenAm;
	}

	public LocalDateTime getVersandBeendetAm() {

		return versandBeendetAm;
	}

	public void setVersandBeendetAm(final LocalDateTime versandBeendetAm) {

		this.versandBeendetAm = versandBeendetAm;
	}

	public boolean isVersandMitFehlern() {

		return versandMitFehlern;
	}

	public void setVersandMitFehlern(final boolean versandMitFehlern) {

		this.versandMitFehlern = versandMitFehlern;
	}

	public Date getGeaendertAm() {

		return geaendertAm;
	}

	public void setGeaendertAm(final Date geaendertAm) {

		this.geaendertAm = geaendertAm;
	}

	public String getUuid() {

		return uuid;
	}

	public void setUuid(final String uuid) {

		this.uuid = uuid;
	}

	public long getAnzahlEmpfaenger() {

		return anzahlEmpfaenger;
	}

	public void setAnzahlEmpfaenger(final long anzahlEmpfaenger) {

		this.anzahlEmpfaenger = anzahlEmpfaenger;
	}

	public long getAnzahlVersendet() {

		return anzahlVersendet;
	}

	public void setAnzahlVersendet(final long anzahlVersendet) {

		this.anzahlVersendet = anzahlVersendet;
	}

	public String getBetreff() {

		return betreff;
	}

	public void setBetreff(final String betreff) {

		this.betreff = betreff;
	}

	public String getMailtext() {

		return mailtext;
	}

	public void setMailtext(final String mailtext) {

		this.mailtext = mailtext;
	}

}
