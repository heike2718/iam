// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * LoginSecrets
 */
@Entity
@Table(name = "PW")
public class LoginSecrets implements AuthProviderEntity {

	/* serialVersionUID */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	@JsonIgnore
	private Long id;

	@Column(name = "PWHASH", length = 2000)
	private String passwordhash;

	@Column(name = "LAST_LOGIN_ATTEMPT")
	private Date lastLoginAttempt;

	@Version
	@Column(name = "VERSION")
	@JsonIgnore
	private int version;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JoinColumn(name = "SLZ")
	private Salt salt;

	@Override
	public Long getId() {

		return id;
	}

	public String getPasswordhash() {

		return passwordhash;
	}

	public void setPasswordhash(final String passwordhash) {

		this.passwordhash = passwordhash;
	}

	public Date getLastLoginAttempt() {

		return lastLoginAttempt;
	}

	public void setLastLoginAttempt(final Date lastLoginAttempt) {

		this.lastLoginAttempt = lastLoginAttempt;
	}

	public int getVersion() {

		return version;
	}

	public void setVersion(final int version) {

		this.version = version;
	}

	public Salt getSalt() {

		return salt;
	}

	public void setSalt(final Salt salt) {

		this.salt = salt;
	}

	public void setId(final Long id) {

		this.id = id;
	}

}
