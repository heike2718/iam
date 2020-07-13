// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.payload;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import de.egladil.web.commons_validation.annotations.Honeypot;
import de.egladil.web.commons_validation.annotations.LoginName;
import de.egladil.web.commons_validation.annotations.StringLatin;
import de.egladil.web.commons_validation.annotations.ValidPasswords;
import de.egladil.web.commons_validation.payload.TwoPasswords;

/**
 * SignUpCredentials enthalten die Credentials des ResourceOwners:
 * <ul>
 * <li>Mailadresse</li>
 * <li>Loginname - darf null sein. In diesem Fall wird automatisch die Mailadresse verwendet</li>
 * <li>Passwörter</li>
 * <li>Info, dass den Datenschutzbestimmungen zugestimmt wurde</li>
 * </ul>
 * sowie des Clients, von dem aus zur Registrierungsresource redirectet wurde:
 * <ul>
 * <li>clientId</li>
 * <li>redirectUrl (Rücksprung)</li>
 * </ul>
 */
public class SignUpCredentials {

	@NotNull
	@Email
	@Size(min = 1, max = 255)
	private String email;

	@NotNull
	@LoginName
	@Size(max = 255)
	private String loginName;

	@StringLatin
	@Size(min = 1, max = 100)
	private String vorname;

	@StringLatin
	@Size(min = 1, max = 100)
	private String nachname;

	@StringLatin
	@Size(max = 150)
	private String groups;

	@StringLatin
	@Size(max = 60)
	private String nonce;

	@NotNull
	@ValidPasswords
	private TwoPasswords twoPasswords;

	@AssertTrue(message = "Bitte stimmen Sie den Datenschutzhinweisen zu.")
	private boolean agbGelesen;

	@NotNull
	private ClientCredentials clientCredentials;

	@Honeypot(message = "")
	private String kleber;

	public String getEmail() {

		return email;
	}

	public void setEmail(final String email) {

		this.email = email;
	}

	public String getKleber() {

		return kleber;
	}

	public void setKleber(final String kleber) {

		this.kleber = kleber;
	}

	public String getLoginName() {

		return loginName;
	}

	public void setLoginName(final String loginName) {

		this.loginName = loginName;
	}

	public boolean isAgbGelesen() {

		return agbGelesen;
	}

	public void setAgbGelesen(final boolean agbGelesen) {

		this.agbGelesen = agbGelesen;
	}

	public ClientCredentials getClientCredentials() {

		return clientCredentials;
	}

	public void setClientCredentials(final ClientCredentials clientCredentials) {

		this.clientCredentials = clientCredentials;
	}

	public String printEmailLogin() {

		return "[email='" + email + "', loginName='" + loginName + "']";
	}

	public String getVorname() {

		return vorname;
	}

	public void setVorname(final String vorname) {

		this.vorname = vorname;
	}

	public String getNachname() {

		return nachname;
	}

	public void setNachname(final String nachname) {

		this.nachname = nachname;
	}

	public TwoPasswords getTwoPasswords() {

		return twoPasswords;
	}

	public void setTwoPasswords(final TwoPasswords twoPasswords) {

		this.twoPasswords = twoPasswords;
	}

	public String getGroups() {

		return groups;
	}

	public void setGroups(final String groups) {

		this.groups = groups;
	}

	public void clean() {

		if (twoPasswords != null) {

			twoPasswords.clean();
		}
	}

	@Override
	public String toString() {

		return "SignUpCredentials [email=" + email + ", loginName=" + loginName + ", vorname=" + vorname + ", nachname=" + nachname
			+ "]";
	}

	public String getNonce() {

		return nonce;
	}

	public void setNonce(final String nonce) {

		this.nonce = nonce;
	}

}
