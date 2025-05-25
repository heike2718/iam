// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.infrastructure.persistence.entities;

import java.time.LocalDateTime;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PersistenteMailversandgruppe
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "MAILVERSAND_GRUPPEN")
@NamedQueries({
	@NamedQuery(name = "PersistenteMailversandgruppe.FIND_BY_VERSANDAUFTAG", query = "select g from PersistenteMailversandgruppe g where g.idVersandauftrag = :idVersandauftrag order by g.sortnr") })
public class PersistenteMailversandgruppe {

	public static final String FIND_BY_VERSANDAUFTAG = "PersistenteMailversandgruppe.FIND_BY_VERSANDAUFTAG";

	@Id
	@UuidGenerator(style = Style.RANDOM)
	@Column(name = "UUID", insertable = false, nullable = false, unique = true, updatable = false)
	private String uuid;

	@Column(name = "VERSANDAUFTRAG_UUID")
	private String idVersandauftrag;

	@Column(name = "EMPFAENGER_UUIDS")
	private String empfaengerUUIDs;

	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	private Jobstatus status;

	@Column(name = "SORTNR")
	private int sortnr;

	@Column(name = "DATE_MODIFIED")
	private LocalDateTime geaendertAm;

	@Version
	@Column(name = "VERSION")
	private int version;

}
