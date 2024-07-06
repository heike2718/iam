// =====================================================
// Projekt: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.auth_admin_api.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

/**
 * Salt
 */
@Entity()
@Table(name = "SLZ")
public class Salt {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Version
	@Column(name = "VERSION")
	private int version;

	public Long getId() {

		return this.id;
	}

	public void setId(final Long id) {

		this.id = id;
	}

}
