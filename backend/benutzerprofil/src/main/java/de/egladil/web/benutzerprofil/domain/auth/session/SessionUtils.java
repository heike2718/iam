// =====================================================
// Project: benutzerprofil
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.benutzerprofil.domain.auth.session;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.benutzerprofil.domain.auth.config.SessionCookieConfig;
import de.egladil.web.benutzerprofil.domain.exceptions.ProfilAPIRuntimeException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.NewCookie.SameSite;

/**
 * SessionUtils
 */
public final class SessionUtils {

	public static final String ANONYME_UUID = "Anonym";

	private static final String DEFAULT_ENCODING = "UTF-8";

	private static final Logger LOGGER = LoggerFactory.getLogger(SessionUtils.class);

	/**
	 *
	 */
	private SessionUtils() {

	}

	public static byte[] getPublicKey() {

		try (InputStream in = SessionUtils.class.getResourceAsStream("/META-INF/authprov_public_key.pem");
			StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, Charset.forName(DEFAULT_ENCODING));

			return sw.toString().getBytes();
		} catch (IOException e) {

			throw new ProfilAPIRuntimeException("Konnte jwt-public-key nicht lesen: " + e.getMessage());
		}

	}

	public static String createIdReference() {

		return "" + UUID.randomUUID().getMostSignificantBits();
	}

	/**
	 * Berechnet den expiresAt-Zeitpunkt mit dem gegebenen idle timout.
	 *
	 * @param sessionIdleTimeoutMinutes int Anzahl Minuten, nach denen eine Session als idle weggeräumt wird.
	 * @return long
	 */
	public static long getExpiresAt(final int sessionIdleTimeoutMinutes) {

		ZoneId zoneId = ZoneId.systemDefault();
		Instant instant = LocalDateTime.now(zoneId).plus(sessionIdleTimeoutMinutes, ChronoUnit.MINUTES).atZone(zoneId).toInstant();
		return Date.from(instant).getTime();

	}

	/**
	 *
	 * @param cookieConfig SessionCookieConfig
	 * @param value String
	 * @return NewCookie
	 */
	public static NewCookie createSessionCookie(SessionCookieConfig cookieConfig, final String value) {

		// @formatter:off
		return new NewCookie.Builder(cookieConfig.name())
			.path(cookieConfig.path())
			.sameSite(SameSite.valueOf(cookieConfig.sameSite()))
			.httpOnly(true)
			.secure(cookieConfig.secure())
			.value(value)
			.build();
		// @formatter:on
	}

	public static NewCookie createInvalidatedSessionCookie(SessionCookieConfig cookieConfig) {

		long dateInThePast = LocalDateTime.now(ZoneId.systemDefault()).minus(10, ChronoUnit.YEARS).toEpochSecond(ZoneOffset.UTC);

		// @formatter:off
		return new NewCookie.Builder(cookieConfig.name())
			.path(cookieConfig.path())
			.maxAge(0)
			.sameSite(SameSite.valueOf(cookieConfig.sameSite()))
			.httpOnly(true)
			.secure(cookieConfig.secure())
			.expiry(new Date(dateInThePast))
			.value("")
			.build();
		// @formatter:on

	}

	public static String getSessionId(final ContainerRequestContext requestContext, SessionCookieConfig cookieConfig) {
		String sessionIdFromCookie = getSessionIdFromCookie(requestContext, cookieConfig);
		LOGGER.debug("sessionIdFromCookie={}", sessionIdFromCookie);

		return sessionIdFromCookie;

	}

	/**
	 * @param requestContext
	 * @param clientPrefix
	 * @return String oder null
	 */
	private static String getSessionIdFromCookie(final ContainerRequestContext requestContext, SessionCookieConfig cookieConfig) {

		Map<String, Cookie> cookies = requestContext.getCookies();

		Cookie sessionCookie = cookies.get(cookieConfig.name());

		if (sessionCookie != null) {

			return sessionCookie.getValue();
		}

		String path = requestContext.getUriInfo().getPath();
		LOGGER.debug("{}: Request ohne {}-Cookie", path, cookieConfig.name());

		return null;
	}
}
