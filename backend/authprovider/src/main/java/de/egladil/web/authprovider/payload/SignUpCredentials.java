// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.payload;

import de.egladil.web.auth_validations.annotations.InputSecured;
import de.egladil.web.auth_validations.annotations.LoginName;
import de.egladil.web.auth_validations.annotations.ValidPasswords;
import de.egladil.web.auth_validations.dto.ZweiPassworte;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
	@Size(max = 255)
	private String email;

	@LoginName(message = "loginName enthält ungueltige Zeichen")
	@Size(max = 255)
	private String loginName;

	@InputSecured(message = "vorname enthält ungültige Zeichen")
	@Size(max = 100)
	private String vorname;

	@InputSecured(message = "nachname enthält ungültige Zeichen")
	@Size(max = 100)
	private String nachname;

	@InputSecured(message = "nonce enthält ungültige Zeichen")
	@Size(max = 60)
	private String nonce;

	@NotNull
	@ValidPasswords(message = "zweiPassworte ist nicht valid")
	private ZweiPassworte zweiPassworte;

	@AssertTrue(message = "Bitte stimmen Sie den Datenschutzhinweisen zu.")
	private boolean agbGelesen;

	@NotNull(message = "clientCredentials ist erforderlich.")
	private ClientCredentials clientCredentials;

	// keine Annotation, damit das geloggt wird!
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

	public ZweiPassworte getZweiPassworte() {

		return zweiPassworte;
	}

	public void setZweiPassworte(final ZweiPassworte zweiPassworte) {

		this.zweiPassworte = zweiPassworte;
	}

	public void clean() {

		if (zweiPassworte != null) {

			zweiPassworte.clean();
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
