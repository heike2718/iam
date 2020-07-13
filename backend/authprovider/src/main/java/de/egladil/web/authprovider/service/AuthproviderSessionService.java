// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.NewCookie;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.AuthProviderApp;
import de.egladil.web.authprovider.domain.AuthSession;
import de.egladil.web.commons_crypto.CryptoService;
import de.egladil.web.commons_net.exception.SessionExpiredException;
import de.egladil.web.commons_net.time.CommonTimeUtils;
import de.egladil.web.commons_net.utils.CommonHttpUtils;

/**
 * AuthproviderSessionService hält eine anonyme Session
 */
@ApplicationScoped
public class AuthproviderSessionService {

	private static final int SESSION_IDLE_TIMEOUT_MINUTES = 30;

	private static final Logger LOG = LoggerFactory.getLogger(AuthproviderSessionService.class);

	private ConcurrentHashMap<String, AuthSession> sessions = new ConcurrentHashMap<>();

	@Inject
	CryptoService cryptoService;

	public AuthSession createAnonymousSession() {

		String sessionId = new String(Base64.getEncoder().encode(cryptoService.generateSessionId().getBytes()));
		String uuid = new String(Base64.getEncoder().encode(cryptoService.generateSessionId().getBytes()));

		String csrfToken = new String(Base64.getEncoder().encode(cryptoService.generateSessionId().getBytes()));

		AuthSession userSession = AuthSession.create(sessionId, uuid);
		userSession.setExpiresAt(getSessionTimeout());
		userSession.setCsrfToken(csrfToken);

		sessions.put(sessionId, userSession);

		return userSession;
	}

	public void invalidate(final String sessionId) {

		AuthSession authSession = sessions.remove(sessionId);

		if (authSession != null) {

			LOG.info("Session {} invalidated", StringUtils.abbreviate(sessionId, 11));
		}
	}

	/**
	 * Gibt die Session mit der gegebenen sessionId zurück.
	 *
	 * @param  sessionId
	 *                   String
	 * @return           AuthSession oder null.
	 */
	public AuthSession getSession(final String sessionId) throws SessionExpiredException {

		AuthSession session = sessions.get(sessionId);

		if (session != null) {

			LocalDateTime expireDateTime = CommonTimeUtils.transformFromDate(new Date(session.getExpiresAt()));
			LocalDateTime now = CommonTimeUtils.now();

			if (now.isAfter(expireDateTime)) {

				sessions.remove(sessionId);
				throw new SessionExpiredException("Ihre Session ist abgelaufen. Bitte loggen Sie sich erneut ein.");
			}
		}

		return session;
	}

	public NewCookie createSessionCookie(final String sessionId) {

		final String name = AuthProviderApp.CLIENT_COOKIE_PREFIX + CommonHttpUtils.NAME_SESSIONID_COOKIE;

		LOG.debug("Erzeugen Cookie mit name={}", name);

		// @formatter:off
		NewCookie sessionCookie = new NewCookie(name,
			sessionId,
			"/", // path
			null, // domain muss null sein, wird vom Browser anhand des restlichen Responses abgeleitet. Sonst wird das Cookie nicht gesetzt.
			1,  // version
			null, // comment
			7200, // expires (minutes)
			null,
			true, // secure
			true  // httpOnly
			);
		// @formatter:on

		return sessionCookie;
	}

	private long getSessionTimeout() {

		return CommonTimeUtils.getInterval(CommonTimeUtils.now(), SESSION_IDLE_TIMEOUT_MINUTES,
			ChronoUnit.MINUTES).getEndTime().getTime();
	}

}
