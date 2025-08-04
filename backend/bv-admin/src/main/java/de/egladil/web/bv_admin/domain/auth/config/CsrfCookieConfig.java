//=====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
//=====================================================
package de.egladil.web.bv_admin.domain.auth.config;

import io.smallrye.config.ConfigMapping;

/**
 *
 */
@ConfigMapping(prefix = "csrf-cookie")
public interface CsrfCookieConfig {

	String name();

	String sameSite();

	boolean secure();

	String path();

	default String toLog() {

		return "CsrfCookieConfig=[name=" + name() + ", path=" + path() + ", sameSite=" + sameSite() + ", secure=" + secure() + "]";

	}

}
