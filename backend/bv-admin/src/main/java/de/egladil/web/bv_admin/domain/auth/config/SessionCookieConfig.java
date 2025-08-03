//=====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
//=====================================================
package de.egladil.web.bv_admin.domain.auth.config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "session-cookie")
public interface SessionCookieConfig {

	String name();

	String sameSite();

	boolean secure();

	String path();

	default String toLog() {

		return "SessionCookieConfig=[name=" + name() + ", path=" + path() + ", sameSite=" + sameSite() + ", secure=" + secure()
		+ "]";

	}
}