// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.mailversand.api;

import java.util.ArrayList;
import java.util.List;

/**
 * AuthAdminMailDto
 */
public class AuthAdminMailDto {

	private String betreff;

	private String body;

	private boolean attachSpammailhinweis;

	private List<String> emfaenger = new ArrayList<>();

	private List<String> bccEmpfaenger = new ArrayList<>();

	public String getBetreff() {

		return betreff;
	}

	public AuthAdminMailDto withBetreff(final String betreff) {

		this.betreff = betreff;
		return this;
	}

	public String getBody() {

		return body;
	}

	public AuthAdminMailDto withBody(final String body) {

		this.body = body;
		return this;
	}

	public boolean isAttachSpammailhinweis() {

		return attachSpammailhinweis;
	}

	public AuthAdminMailDto withAttachSpammailhinweis(final boolean attachSpammailhinweis) {

		this.attachSpammailhinweis = attachSpammailhinweis;
		return this;
	}

	public List<String> getEmfaenger() {

		return emfaenger;
	}

	public AuthAdminMailDto withEmfaenger(final List<String> emfaenger) {

		this.emfaenger = emfaenger;
		return this;
	}

	public List<String> getBccEmpfaenger() {

		return bccEmpfaenger;
	}

	public AuthAdminMailDto withBccEmpfaenger(final List<String> bccEmpfaenger) {

		this.bccEmpfaenger = bccEmpfaenger;
		return this;
	}

}
