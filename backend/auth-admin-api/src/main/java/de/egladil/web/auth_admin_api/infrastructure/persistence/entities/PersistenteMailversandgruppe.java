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
 * PersistenteMailversandgruppe
 */
@Entity
@Table(name = "MAILVERSAND_GRUPPEN")
public class PersistenteMailversandgruppe {

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

	public String getIdVersandauftrag() {

		return idVersandauftrag;
	}

	public void setIdVersandauftrag(final String idVersandauftrag) {

		this.idVersandauftrag = idVersandauftrag;
	}

	public String getEmpfaengerEmails() {

		return empfaengerEmails;
	}

	public void setEmpfaengerEmails(final String empfaengerEmails) {

		this.empfaengerEmails = empfaengerEmails;
	}

	public Jobstatus getStatus() {

		return status;
	}

	public void setStatus(final Jobstatus status) {

		this.status = status;
	}

	public int getSortnr() {

		return sortnr;
	}

	public void setSortnr(final int sortnr) {

		this.sortnr = sortnr;
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

}
