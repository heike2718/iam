// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * PersistenterUserReadOnly
 */
@Entity
@Table(name = "VW_USERS_SUCHE")
@NamedQueries({
	@NamedQuery(
		name = "PersistenterUserReadOnly.FIND_BY_UUID",
		query = "select b from PersistenterUserReadOnly b where b.uuid = :uuid")
})
public class PersistenterUserReadOnly {

	public static final String FIND_BY_UUID = "PersistenterUserReadOnly.FIND_BY_UUID";

	@Id
	@Column(name = "ID")
	public Long id;

	@Column(name = "UUID")
	public String uuid;

	@Column(name = "VORNAME")
	public String vorname;

	@Column(name = "NACHNAME")
	public String nachname;

	@Column(name = "EMAIL")
	public String email;

	@Column(name = "AKTIVIERT")
	public boolean aktiviert;

	@Column(name = "ROLLEN")
	public String rollen;

	@Column(name = "DATE_MODIFIED_STRING")
	public String datumGeaendert;

	@Column(name = "SLZ_ID")
	public Long saltId;

	public PersistenterUserReadOnly(final Long id, final String uuid, final String vorname, final String nachname, final String email, final boolean aktiviert, final String rollen, final String datumGeaendert) {

		super();
		this.id = id;
		this.uuid = uuid;
		this.vorname = vorname;
		this.nachname = nachname;
		this.email = email;
		this.aktiviert = aktiviert;
		this.rollen = rollen;
		this.datumGeaendert = datumGeaendert;
	}

}
