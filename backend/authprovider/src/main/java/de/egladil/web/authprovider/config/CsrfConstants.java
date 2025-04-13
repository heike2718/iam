package de.egladil.web.authprovider.config;

public interface CsrfConstants {

	final String CSRF_TOKEN_COOKIE_NAME = "XSRF-TOKEN";

	final String CSRF_TOKEN_HEADER_NAME = "X-XSRF-TOKEN";

	/**
	 * Dient zur Unterscheidung im Browser: Cookie wird nur an diese Sub-URL gesendet.
	 */
	final String COOKIE_PATH = "/authprovider";


}
