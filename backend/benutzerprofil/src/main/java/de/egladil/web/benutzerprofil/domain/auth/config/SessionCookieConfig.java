//=====================================================
// Project: benutzerprofil
// (c) Heike Winkelvoß
//=====================================================
package de.egladil.web.benutzerprofil.domain.auth.config;

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