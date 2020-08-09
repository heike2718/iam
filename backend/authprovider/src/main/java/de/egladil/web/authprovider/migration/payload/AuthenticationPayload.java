// =====================================================
// Project: authprovoder
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.migration.payload;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * AuthenticationPayload
 */
public class AuthenticationPayload implements Serializable {

	@JsonIgnore
	private static final long serialVersionUID = 1L;

	@JsonProperty
	private String secret;

	@JsonProperty
	private String uuid;

	@JsonProperty
	private String rolle;

	@JsonProperty
	private String email;

	@JsonProperty
	private String loginname;

	@JsonProperty
	private String vorname;

	@JsonProperty
	private String nachname;

	@JsonProperty
	private String pw;

	@JsonProperty
	private String alg;

	@JsonProperty
	private String slt;

	@JsonProperty
	private int rounds;

	@JsonProperty
	private boolean mailbenachrichtigung;

	@JsonProperty
	private String schulkuerzel;

	@JsonProperty
	private boolean anonym;

	public String getSecret() {

		return secret;
	}

	public String getUuid() {

		return uuid;
	}

	public String getEmail() {

		return email;
	}

	public String getLoginname() {

		return loginname;
	}

	public String getVorname() {

		return vorname;
	}

	public String getNachname() {

		return nachname;
	}

	public String getPw() {

		return pw;
	}

	public String getAlg() {

		return alg;
	}

	public String getSlt() {

		return slt;
	}

	public int getRounds() {

		return rounds;
	}

	public String getRolle() {

		return rolle;
	}

	@Override
	public String toString() {

		return "AuthenticationPayload [uuid=" + uuid + ", rolle=" + rolle + ", email=" + email + ", vorname=" + vorname
			+ ", nachname=" + nachname + "]";
	}

	public String getSchulkuerzel() {

		return schulkuerzel;
	}

	public boolean isAnonym() {

		return anonym;
	}
}
