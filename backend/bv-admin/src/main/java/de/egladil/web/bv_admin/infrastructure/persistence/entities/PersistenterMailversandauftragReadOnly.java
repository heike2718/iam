// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.infrastructure.persistence.entities;

import java.time.LocalDateTime;

import de.egladil.web.bv_admin.domain.Jobstatus;
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
 * PersistenterMailversandauftragReadOnly
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "VW_MAILVERSANDAUFTRAEGE")
@NamedQueries({
	@NamedQuery(name = "PersistenterMailversandauftragReadOnly.LOAD_ALL", query = "select v from PersistenterMailversandauftragReadOnly v order by v.erfasstAm desc, v.uuid"),
	@NamedQuery(name = "PersistenterMailversandauftragReadOnly.FIND_WITH_INFOMAILTEXT_AND_JAHR_MONAT", query = "select v from PersistenterMailversandauftragReadOnly v where v.idInfomailtext = :idInfomailtext and v.versandJahrMonat = :versandJahrMonat order by v.erfasstAm desc, v.uuid"), })
public class PersistenterMailversandauftragReadOnly {

	public static final String LOAD_ALL = "PersistenterMailversandauftragReadOnly.LOAD_ALL";

	public static final String FIND_WITH_INFOMAILTEXT_AND_JAHR_MONAT = "PersistenterMailversandauftragReadOnly.FIND_WITH_INFOMAILTEXT_AND_JAHR_MONAT";

	@Id
	@Column
	private String uuid;

	@Column(name = "UUID_INFOMAIL_TEXT")
	private String idInfomailtext;

	@Column(name = "BETREFF")
	private String betreff;

	@Column(name = "MAILTEXT")
	private String mailtext;

	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	private Jobstatus status;

	@Column(name = "ANZAHL_EMPFAENGER")
	private long anzahlEmpfaenger;

	@Column(name = "ANZAHL_GRUPPEN")
	private long anzahlGruppen;

	@Column(name = "ERFASST_AM")
	private LocalDateTime erfasstAm;

	@Column(name = "VERSAND_JAHR_MONAT")
	private String versandJahrMonat;
}
