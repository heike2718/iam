// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import de.egladil.web.commons_validation.SecUtils;
import de.egladil.web.commons_validation.annotations.UuidString;
import de.egladil.web.commons_validation.annotations.ValidPasswords;
import de.egladil.web.commons_validation.payload.TwoPasswords;

/**
 * TempPasswordCredentials
 */
public class ChangeTempPasswordPayload {

	@NotNull
	@UuidString
	@Size(min = 1, max = 40)
	private String tokenId;

	@NotNull
	@Email
	@Size(min = 1, max = 255)
	private String email;

	@NotNull
	@UuidString
	@Size(min = 1, max = 40)
	private String tempPassword;

	@NotNull
	@ValidPasswords
	private TwoPasswords twoPasswords;

	/**
	 * Entfernt alle sensiblen Infos: also password, passwordWdh und tempPassword.
	 */
	public void clean() {

		if (twoPasswords != null) {

			twoPasswords.clean();
		}
		tempPassword = SecUtils.wipe(tempPassword);
	}

	@Override
	public String toString() {

		return "ChangeTempPasswordPayload [tokenId=" + tokenId + ", email=" + email + "]";
	}

	public String getEmail() {

		return email;
	}

	public void setEmail(final String email) {

		this.email = email;
	}

	public String getTempPassword() {

		return tempPassword;
	}

	public void setTempPassword(final String tempPassword) {

		this.tempPassword = tempPassword;
	}

	public String getTokenId() {

		return tokenId;
	}

	public void setTokenId(final String tokenId) {

		this.tokenId = tokenId;
	}

	public TwoPasswords getTwoPasswords() {

		return twoPasswords;
	}

	public void setTwoPasswords(final TwoPasswords twoPasswords) {

		this.twoPasswords = twoPasswords;
	}
}
