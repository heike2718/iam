// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.domain;

import java.util.Date;

import de.egladil.web.auth_validations.annotations.UuidString;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * TempPassword
 */
@Entity
@Table(name = "TEMPPWDS")
@NamedQueries({ @NamedQuery(name = "FIND_BY_TOKEN_ID", query = "SELECT t from TempPassword t where t.tokenId = :tokenId") })
public class TempPassword implements AuthProviderEntity {

	private static final long serialVersionUID = 2L;

	public static final String FIND_BY_TOKEN_ID = "FIND_BY_TOKEN_ID";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "TOKEN_ID")
	@NotNull
	@UuidString
	@Size(min = 1, max = 40)
	private String tokenId;

	@Column(name = "EXPIRES_AT")
	@NotNull
	private Date expiresAt;

	@Column(name = "PWD")
	@NotNull
	@Size(min = 1, max = 40)
	private String password;

	@Column(name = "NUMBER_USES")
	private int numberUses;

	@Column(name = "SENT")
	private boolean sent;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "USER_ID", referencedColumnName = "ID")
	private ResourceOwner resourceOwner;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CLIENT_ID", referencedColumnName = "ID")
	private Client client;

	@Version
	@Column(name = "VERSION")
	private int version;

	@Override
	public Long getId() {

		return this.id;
	}

	public String getTokenId() {

		return tokenId;
	}

	public void setTokenId(final String tokenId) {

		this.tokenId = tokenId;
	}

	public Date getExpiresAt() {

		return expiresAt;
	}

	public void setExpiresAt(final Date expiresAt) {

		this.expiresAt = expiresAt;
	}

	public String getPassword() {

		return password;
	}

	public void setPassword(final String password) {

		this.password = password;
	}

	public int getNumberUses() {

		return numberUses;
	}

	public void setNumberUses(final int numberUses) {

		this.numberUses = numberUses;
	}

	public ResourceOwner getResourceOwner() {

		return resourceOwner;
	}

	public void setResourceOwner(final ResourceOwner resourceOwner) {

		this.resourceOwner = resourceOwner;
	}

	public void setId(final Long id) {

		this.id = id;
	}

	public boolean isSent() {

		return sent;
	}

	public void setSent(final boolean sent) {

		this.sent = sent;
	}

	@Override
	public String toString() {

		return "TempPassword [tokenId=" + tokenId + "]";
	}

	public Client getClient() {

		return client;
	}

	public void setClient(final Client client) {

		this.client = client;
	}

}
