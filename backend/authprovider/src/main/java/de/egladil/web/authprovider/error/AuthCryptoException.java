// =====================================================
// Projekt: authenticationprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.error;

/**
 * AuthCryptoException
 */
public class AuthCryptoException extends RuntimeException {

	/* serialVersionUID */
	private static final long serialVersionUID = 1L;

	public AuthCryptoException(final String arg0) {

		super(arg0);
	}

	public AuthCryptoException(final String arg0, final Throwable arg1) {

		super(arg0, arg1);
	}
}
