// =====================================================
// Projekt: de.egladil.bv.aas
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import de.egladil.web.authprovider.validation.PeriodChecker;
import de.egladil.web.commons_validation.annotations.UuidString;

/**
 * Kapselt die Attribute, die für die Aktivierung eines ResourceOwners erforderlich sind.
 *
 * @author heike
 */
@Entity
@Table(name = "ACTIVATIONCODES")
@NamedQueries({
	@NamedQuery(
		name = "findActivationCodeByConfirmationCode",
		query = "SELECT a FROM ActivationCode a WHERE a.confirmationCode = :confirmationCode") })
public class ActivationCode implements Serializable, AuthProviderEntity {

	private static final long serialVersionUID = 2L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "CONFIRM_CODE", length = 40)
	@NotNull
	@UuidString
	@Size(min = 1, max = 40)
	private String confirmationCode;

	@Column(name = "CONFIRM_EXPIRETIME")
	private Date expirationTime;

	@Column(name = "CONFIRMED")
	private boolean confirmed;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "USER_ID", referencedColumnName = "ID")
	private ResourceOwner resourceOwner;

	@Version
	@Column(name = "VERSION")
	private int version;

	public ActivationCode() {

	}

	@Override
	public Long getId() {

		return this.id;
	}

	public String getConfirmationCode() {

		return this.confirmationCode;
	}

	public void setConfirmationCode(final String code) {

		this.confirmationCode = code;
	}

	public Date getExpirationTime() {

		return this.expirationTime;
	}

	public void setExpirationTime(final Date date) {

		this.expirationTime = date;
	}

	/**
	 * Zwischen jetzt und expiration gibt es eine Kulanzzeitspanne von 60s.
	 *
	 * @param  now
	 * @return
	 */
	public boolean isExpired(final Date now) {

		final boolean result = !new PeriodChecker().isPeriodLessEqualExpectedPeriod(expirationTime, now, 60000);
		return result;
	}

	public boolean isConfirmed() {

		return confirmed;
	}

	public void setConfirmed(final boolean confirmed) {

		this.confirmed = confirmed;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((confirmationCode == null) ? 0 : confirmationCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ActivationCode other = (ActivationCode) obj;

		if (confirmationCode == null) {

			if (other.confirmationCode != null)
				return false;
		} else if (!confirmationCode.equals(other.confirmationCode))
			return false;
		return true;
	}

	public ResourceOwner getResourceOwner() {

		return resourceOwner;
	}

	public void setResourceOwner(final ResourceOwner resourceOwner) {

		this.resourceOwner = resourceOwner;
		// resourceOwner.setActivationCode(this);
	}

}
