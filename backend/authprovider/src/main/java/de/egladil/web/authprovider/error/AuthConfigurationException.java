// =====================================================
// Projekt: authenticationprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.error;

/**
 * AuthConfigurationException
 */
public class AuthConfigurationException extends RuntimeException {

	/* serialVersionUID */
	private static final long serialVersionUID = 1L;

	public AuthConfigurationException(final String arg0) {

		super(arg0);
	}

	public AuthConfigurationException(final String arg0, final Throwable arg1) {

		super(arg0, arg1);
	}
}
