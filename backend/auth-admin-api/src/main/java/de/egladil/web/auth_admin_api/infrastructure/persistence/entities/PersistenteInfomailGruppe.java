// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.infrastructure.persistence.entities;

import java.util.Date;

import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.UuidGenerator.Style;

import de.egladil.web.auth_admin_api.domain.Jobstatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

/**
 * PersistenteInfomailGruppe
 */
@Entity
@Table(name = "INFOMAIL_GRUPPEN")
public class PersistenteInfomailGruppe {

	@Id
	@UuidGenerator(style = Style.RANDOM)
	@Column(name = "UUID", insertable = false, nullable = false, unique = true, updatable = false)
	private String uuid;

	@Column(name = "VERSANDAUFTRAG_UUID")
	private String idVersandauftrag;

	@Column(name = "EMPFAENGER")
	private String empfaengerEmails;

	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	private Jobstatus status;

	@Column(name = "SORTNR")
	private int sortnr;

	@Column(name = "DATE_MODIFIED")
	private Date geaendertAm;

	@Version
	@Column(name = "VERSION")
	private int version;

}
