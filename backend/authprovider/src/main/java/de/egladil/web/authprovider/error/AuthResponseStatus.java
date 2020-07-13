//=====================================================
// Projekt: authenticationprovider
// (c) Heike Winkelvoß
//=====================================================

package de.egladil.web.authprovider.error;

/**
 * AuthResponseStatus
 */
public enum AuthResponseStatus {

	SERVER_ERROR(500, Loglevel.ERROR),
	BAD_REQUEST(400, Loglevel.DEBUG),
	UNAUTHORIZED(403, Loglevel.WARN),
	NOT_FOUND(404, Loglevel.WARN),
	UNPROCESSABLE_ENTITY(422, Loglevel.WARN),
	DUPLICATE_ENTRY(900, Loglevel.DEBUG),
	CONCURRENT_MODIFICATION(409, Loglevel.DEBUG),
	VALIDATION_ERRORS(902, Loglevel.DEBUG),
	MAILSERVER_NOT_AVAILABLE(903, Loglevel.DEBUG),
	NOT_SUCCESSFUL(904, Loglevel.DEBUG),
	TEAPOT(418, Loglevel.WARN);

	private final int statusCode;

	private final Loglevel loglevel;

	/**
	 * Erzeugt eine Instanz von AuthResponseStatus
	 */
	private AuthResponseStatus(final int statusCode, final Loglevel loglevel) {
		this.statusCode = statusCode;
		this.loglevel = loglevel;
	}

	public final int getStatusCode() {
		return statusCode;
	}

	public final Loglevel getLoglevel() {
		return loglevel;
	}

}
