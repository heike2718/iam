//=====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
//=====================================================
package de.egladil.web.bv_admin.infrastructure.persistence.entities;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

/**
 *
 */
@Entity()
@Table(name = "PW")
public class PersistentesPassword {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Version
	@Column(name = "VERSION")
	private int version;

	@OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "slz", referencedColumnName = "id")
	@Fetch(FetchMode.JOIN)
	private Salt salt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Salt getSalt() {
		return salt;
	}

	public void setSalt(Salt salt) {
		this.salt = salt;
	}
}
