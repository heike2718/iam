// =====================================================
// Project: benutzerprofil
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.benutzerprofil.infrastructure.error;

import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.benutzerprofil.domain.auth.dto.MessagePayload;
import de.egladil.web.benutzerprofil.domain.exceptions.CommunicationException;
import de.egladil.web.benutzerprofil.domain.exceptions.ConcurrentModificationException;
import de.egladil.web.benutzerprofil.domain.exceptions.ProfilAPIRuntimeException;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;

/**
 * AuthproviderResponseExceptionMapper
 */
public class AuthproviderResponseExceptionMapper implements ResponseExceptionMapper<Throwable> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthproviderResponseExceptionMapper.class);

	@Override
	public Throwable toThrowable(final Response response) {

		int status = response.getStatus();

		if (status < 300) {

			return null;
		}

		switch (status) {

		case 401: {

			String message = "401 bei Kommunikation mit dem authprovider: Konfiguration der Infrastruktur prüfen (client-id, client-secret, authprovider-rest-client-url,...";
			// wird im BenutzerverwaltungExceptionMapper geloggt
			return new CommunicationException(new ProfilAPIRuntimeException(message));
		}

		case 404: {

			LOGGER.warn(
				"Das Benutzerkonto wurde zwischenzeitlich vermutlich geloescht: auth-admin-api-log pruefen");

			return new CommunicationException(new ConcurrentModificationException("404"));
		}

		case 400: {

			MessagePayload errorEntity = response.readEntity(new GenericType<MessagePayload>() {
			});
			String message = "BadRequest-Response vom authprovider. Input-Validierung: " + errorEntity.getMessage();
			LOGGER.error(message);
			return new CommunicationException(new ProfilAPIRuntimeException(message));
		}

		default:
			String message = "Unerwarteter http-Status vom authprovider: status=" + status;
			LOGGER.error(message);
			return new CommunicationException(new ProfilAPIRuntimeException(message));
		}
	}
}
