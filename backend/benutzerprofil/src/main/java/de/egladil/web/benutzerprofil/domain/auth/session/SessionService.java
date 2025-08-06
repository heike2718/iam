// =====================================================
// Project: benutzerprofil
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.benutzerprofil.domain.auth.session;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import de.egladil.web.benutzerprofil.domain.auth.jwt.JWTService;
import de.egladil.web.benutzerprofil.domain.auth.jwt.impl.DecodedJWTReader;
import de.egladil.web.benutzerprofil.domain.exceptions.AuthRuntimeException;
import de.egladil.web.benutzerprofil.domain.exceptions.SessionExpiredException;
import de.egladil.web.benutzerprofil.infrastructure.cdi.AuthenticationContext;
import de.egladil.web.egladil_secure_tokens.SecureRandomGenerator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;

@ApplicationScoped
public class SessionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SessionService.class);

	private ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

	@ConfigProperty(name = "session.idle.timeout")
	int sessionIdleTimeoutMinutes;

	@Context
	ContainerRequestContext requestContext;

	@Inject
	AuthenticationContext authCtx;

	@Inject
	JWTService jwtService;

	private final SecureRandomGenerator secureRandomGenerator = new SecureRandomGenerator();

	/**
	 * Wenn das JWT sagt, ist kein Admin, dann wird eine anonyme Session angelegt.
	 *
	 * @param jwt
	 * @return
	 */
	public Session initSession(final String jwt) {

		LOGGER.debug(jwt);

		try {

			DecodedJWT decodedJWT = jwtService.verify(jwt, SessionUtils.getPublicKey());

			final DecodedJWTReader jwtReader = new DecodedJWTReader(decodedJWT);

			String uuid = decodedJWT.getSubject();

			String fullName = jwtReader.getFullName();

			String userIdReference = uuid.substring(0, 8) + "_" + secureRandomGenerator.generateSecureRandomHex(32);

			AuthenticatedUser authenticatedUser = new AuthenticatedUser(uuid).withFullName(fullName)
				.withIdReference(userIdReference).withUuid(decodedJWT.getSubject());

			Session session = this.internalCreateAnonymousSession().withUser(authenticatedUser);

			if (sessionIdleTimeoutMinutes == 0) {

				LOGGER.warn("session.idle.timeout=0 => verwenden default 120 min");
				session.setExpiresAt(SessionUtils.getExpiresAt(120));
			} else {

				session.setExpiresAt(SessionUtils.getExpiresAt(sessionIdleTimeoutMinutes));
			}

			session.setSessionActive(true);
			sessions.put(session.getSessionId(), session);

			LOGGER.info("Benutzer eingeloggt: {}", session.getUser().toString());

			return session;
		} catch (TokenExpiredException e) {

			LOGGER.error("JWT expired");
			throw new AuthRuntimeException("JWT expired");
		} catch (JWTVerificationException e) {

			String msg = "Security Thread: JWT " + StringUtils.abbreviate(jwt, 20) + " invalid: " + e.getMessage();
			LOGGER.warn(msg);
			throw new AuthRuntimeException("JWT invalid");
		}
	}

	/**
	 * @param sessionId
	 * @return Session
	 */
	public Session getAndRefreshSessionIfValid(final String sessionId) {

		Session session = sessions.get(sessionId);

		if (session == null) {

			return null;
		}

		LocalDateTime expireDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(session.getExpiresAt()), ZoneId.systemDefault())
			.plusSeconds(5); // bissel Toleranz lassen, oder?
		LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());

		if (now.isAfter(expireDateTime)) {

			sessions.remove(sessionId);
			throw new SessionExpiredException("Die Session ist abgelaufen. Bitte neu einloggen.");
		}

		session.setExpiresAt(SessionUtils.getExpiresAt(sessionIdleTimeoutMinutes));

		return session;
	}

	public Session getSessionNullSave(final String sessionId) {

		return sessionId == null ? null : sessions.get(sessionId);
	}

	public void invalidateSession(final String sessionId) {

		if (sessionId == null) {

			LOGGER.debug("invalidateSession ohne sessionId aufgerufen");
			return;
		}

		Session session = this.sessions.remove(sessionId);

		if (session != null && !session.isAnonym()) {

			LOGGER.info("BenutzerDto ausgeloggt: {}", session.getUser().toString());
		}
	}

	private Session internalCreateAnonymousSession() {

		String sessionId = secureRandomGenerator.generateSecureRandomHex(32);
		return Session.createAnonymous(sessionId);
	}
}
