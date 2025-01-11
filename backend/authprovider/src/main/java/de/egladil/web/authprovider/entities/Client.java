// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.entities;

import de.egladil.web.auth_validations.annotations.ClientId;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Ein Client ist eine Anwendung, die diesen AuthProvider für die Authentisierung Ihrer Benutzer verwendet.<br>
 * <br>
 * <code>Beispiel:</code>die Minikänguru-Anwendung, die Minikänguru-Admin-Anwendung, die Checklistenapp.
 */
@Entity
@Table(name = "CLIENTS")
public class Client implements AuthProviderEntity {

	/* serialVersionUID */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "CLIENT_ID")
	@ClientId
	@NotBlank
	@Size(max = 50)
	/** Mit der clientId identifiziert sich die Client-Anwendung gegenüber dem Identity-Provider */
	private String clientId;

	@Column(name = "NAME")
	@NotBlank
	@Size(max = 100)
	private String name;

	@Column(name = "ZURUECK_TEXT")
	@NotBlank
	@Size(max = 150)
	private String zurueckText;

	@Column(name = "BASE_URL")
	private String baseUrl;

	@Column(name = "REDIRECT_URLS")
	@Size(max = 500)
	private String redirectUrls;

	@Column(name = "LOGIN_MIT_LOGINNAME")
	private boolean loginWithLoginnameSupported;

	@Column(name = "NAMEN_REQUIRED")
	private boolean vornameNachnameRequired;

	@Column(name = "AGB_URL")
	private String agbUrl;

	@Column(name = "ACCESSTOKEN_EXPIRATION_MINUTES")
	private int accessTokenExpirationMinutes;

	@Column(name = "JWT_EXPIRATION_MINUTES")
	private int jwtExpirationMinutes;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JoinColumn(name = "PID", referencedColumnName = "ID")
	private LoginSecrets loginSecrets;

	@Version
	@Column(name = "VERSION")
	private int version;

	@Override
	public Long getId() {

		return this.id;
	}

	public String getClientId() {

		return clientId;
	}

	public void setClientId(final String clientId) {

		this.clientId = clientId;
	}

	public String getName() {

		return name;
	}

	public void setName(final String name) {

		this.name = name;
	}

	public String getRedirectUrls() {

		return redirectUrls;
	}

	public void setRedirectUrls(final String redirectUrl) {

		this.redirectUrls = redirectUrl;
	}

	public int getVersion() {

		return version;
	}

	public void setVersion(final int version) {

		this.version = version;
	}

	public void setId(final Long id) {

		this.id = id;
	}

	public boolean isLoginWithLoginnameSupported() {

		return loginWithLoginnameSupported;
	}

	public void setLoginWithLoginnameSupported(final boolean loginWithLoginnameProvided) {

		this.loginWithLoginnameSupported = loginWithLoginnameProvided;
	}

	public String getAgbUrl() {

		return agbUrl;
	}

	public void setAgbUrl(final String agbUrl) {

		this.agbUrl = agbUrl;
	}

	public int getJwtExpirationMinutes() {

		return jwtExpirationMinutes;
	}

	public void setJwtExpirationMinutes(final int jwtExpirationMinutes) {

		this.jwtExpirationMinutes = jwtExpirationMinutes;
	}

	public String getZurueckText() {

		return zurueckText;
	}

	public void setZurueckText(final String zurueckText) {

		this.zurueckText = zurueckText;
	}

	public boolean isVornameNachnameRequired() {

		return vornameNachnameRequired;
	}

	public void setVornameNachnameRequired(final boolean vornameNachnameRequired) {

		this.vornameNachnameRequired = vornameNachnameRequired;
	}

	public String getBaseUrl() {

		return baseUrl;
	}

	public void setBaseUrl(final String baseUrl) {

		this.baseUrl = baseUrl;
	}

	public LoginSecrets getLoginSecrets() {

		return loginSecrets;
	}

	public void setLoginSecrets(final LoginSecrets loginSecrets) {

		this.loginSecrets = loginSecrets;
	}

	@Override
	public String toString() {

		return "Client [clientId=" + clientId + "]";
	}

	public int getAccessTokenExpirationMinutes() {

		return accessTokenExpirationMinutes;
	}

	public void setAccessTokenExpirationMinutes(final int accessTokenExpirationMinutes) {

		this.accessTokenExpirationMinutes = accessTokenExpirationMinutes;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((clientId == null) ? 0 : clientId.hashCode());
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
		Client other = (Client) obj;

		if (clientId == null) {

			if (other.clientId != null)
				return false;
		} else if (!clientId.equals(other.clientId))
			return false;
		return true;
	}
}
