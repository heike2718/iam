// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.infrastructure.persistence.entities;

import java.time.LocalDateTime;

import de.egladil.web.auth_admin_api.domain.Jobstatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * PersistenterMailversandauftragReadOnly
 */
@Entity
@Table(name = "VW_MAILVERSANDAUFTRAEGE")
@NamedQueries({
	@NamedQuery(
		name = "PersistenterMailversandauftragReadOnly.LOAD_ALL",
		query = "select v from PersistenterMailversandauftragReadOnly v order by v.betreff asc, v.erfasstAm desc, v.uuid"),
	@NamedQuery(
		name = "PersistenterMailversandauftragReadOnly.FIND_WITH_INFOMAILTEXT_AND_JAHR_MONAT",
		query = "select v from PersistenterMailversandauftragReadOnly v where v.idInfomailtext = :idInfomailtext and v.versandJahrMonat = :versandJahrMonat order by v.erfasstAm desc, v.uuid"),
})
public class PersistenterMailversandauftragReadOnly {

	public static final String LOAD_ALL = "PersistenterMailversandauftragReadOnly.LOAD_ALL";

	public static final String FIND_WITH_INFOMAILTEXT_AND_JAHR_MONAT = "PersistenterMailversandauftragReadOnly.FIND_WITH_INFOMAILTEXT_AND_JAHR_MONAT";

	@Id
	@Column
	public String uuid;

	@Column(name = "UUID_INFOMAIL_TEXT")
	public String idInfomailtext;

	@Column(name = "BETREFF")
	public String betreff;

	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	public Jobstatus status;

	@Column(name = "ANZAHL_EMPFAENGER")
	public long anzahlEmpfaenger;

	@Column(name = "ANZAHL_GRUPPEN")
	public long anzahlGruppen;

	@Column(name = "ERFASST_AM")
	public LocalDateTime erfasstAm;

	@Column(name = "VERSAND_JAHR_MONAT")
	public String versandJahrMonat;
}
