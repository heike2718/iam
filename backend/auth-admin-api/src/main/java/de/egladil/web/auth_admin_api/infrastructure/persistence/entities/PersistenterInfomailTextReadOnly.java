// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.infrastructure.persistence.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * PersistenterInfomailTextReadOnly
 */
@Entity
@Table(name = "VW_INFOMAIL_TEXTE")
@NamedQueries({
	@NamedQuery(
		name = "PersistenterInfomailTextReadOnly.LOAD_ALL",
		query = "select i from PersistenterInfomailTextReadOnly i order by i.betreff"),
	@NamedQuery(
		name = "PersistenterInfomailTextReadOnly.FIND_BY_UUID",
		query = "select i from PersistenterInfomailTextReadOnly i where i.uuid = :uuid")
})
public class PersistenterInfomailTextReadOnly {

	public static final String LOAD_ALL = "PersistenterInfomailTextReadOnly.LOAD_ALL";

	public static final String FIND_BY_UUID = "PersistenterInfomailTextReadOnly.FIND_BY_UUID";

	@Id
	@Column(name = "UUID")
	public String uuid;

	@Column(name = "BETREFF")
	public String betreff;

	@Column(name = "MAILTEXT")
	public String mailtext;

	@Column(name = "DATE_MODIFIED")
	public Date geaendertAm;

	@Column(name = "UUIDS_MAILVERSANDAUFTRAEGE")
	public String uuidsMailversandauftraege;

}
