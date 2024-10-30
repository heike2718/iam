// =====================================================
// Project: profil-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_api.domain.exceptions;

/**
 * CommunicationException
 */
public class CommunicationException extends RuntimeException {

	private static final long serialVersionUID = 7188998485003545930L;

	private final RuntimeException exceptionToPropagate;

	public CommunicationException(final RuntimeException exceptionToPropagate) {

		super();
		this.exceptionToPropagate = exceptionToPropagate;
	}

	public RuntimeException getExceptionToPropagate() {

		return exceptionToPropagate;
	}

}
