// =====================================================
// Project: mk-gateway
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.infrastructure.persistence.entities;

import java.util.Date;

import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.UuidGenerator.Style;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

/**
 * PersistenterInfomailText
 */
@Entity
@Table(name = "INFOMAIL_TEXTE")
@NamedQueries({
	@NamedQuery(
		name = "PersistenterInfomailText.FIND_BY_UUID", query = "select t from PersistenterInfomailText t where t.uuid = :uuid")
})
public class PersistenterInfomailText {

	public static final String FIND_BY_UUID = "PersistenterInfomailText.FIND_BY_UUID";

	@Id
	@UuidGenerator(style = Style.RANDOM)
	@Column(name = "UUID", insertable = false, nullable = false, unique = true, updatable = false)
	private String uuid;

	@Column(name = "BETREFF")
	private String betreff;

	@Column(name = "MAILTEXT")
	private String mailtext;

	@Column(name = "DATE_MODIFIED")
	private Date geaendertAm;

	@Version
	@Column(name = "VERSION")
	private int version;

	public String getUuid() {

		return uuid;
	}

	public void setUuid(final String uuid) {

		this.uuid = uuid;
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

	public void setMailtext(final String text) {

		this.mailtext = text;
	}

	public Date getGeaendertAm() {

		return geaendertAm;
	}

	public void setGeaendertAm(final Date geaendertAm) {

		this.geaendertAm = geaendertAm;
	}

}
