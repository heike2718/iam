// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.event;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * BotAttackEventPayload
 */
public class BotAttackEventPayload {

	@JsonProperty
	private String path;

	@JsonProperty
	private String loginName;

	@JsonProperty
	private String passwort;

	@JsonProperty
	private String kleber;

	@JsonProperty
	private String redirectUrl;

	public String getLoginName() {

		return loginName;
	}

	public BotAttackEventPayload withLoginName(final String loginName) {

		this.loginName = loginName;
		return this;
	}

	public String getPasswort() {

		return passwort;
	}

	public BotAttackEventPayload withPasswort(final String passwort) {

		this.passwort = passwort;
		return this;
	}

	public String getKleber() {

		return kleber;
	}

	public BotAttackEventPayload withKleber(final String kleber) {

		this.kleber = kleber;
		return this;
	}

	public String getRedirectUrl() {

		return redirectUrl;
	}

	public BotAttackEventPayload withRedirectUrl(final String redirectUrl) {

		this.redirectUrl = redirectUrl;
		return this;
	}

	public String getPath() {

		return path;
	}

	public BotAttackEventPayload withPath(final String path) {

		this.path = path;
		return this;
	}

	@Override
	public String toString() {

		return "BotAttackEventPayload [path=" + path + ", loginName=" + loginName + ", kleber=" + kleber + ", redirectUrl="
			+ redirectUrl + "]";
	}

}
