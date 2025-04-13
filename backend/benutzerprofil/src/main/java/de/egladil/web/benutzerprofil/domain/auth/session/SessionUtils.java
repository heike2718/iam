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

import de.egladil.web.benutzerprofil.domain.auth.config.AuthConstants;
import de.egladil.web.benutzerprofil.domain.exceptions.ProfilAPIRuntimeException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.NewCookie;

/**
 * SessionUtils
 */
public final class SessionUtils {

	public static final String ANONYME_UUID = "Anonym";

	private static final String DEFAULT_ENCODING = "UTF-8";

	private static final String SESSION_ID_HEADER = "X-SESSIONID";

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

	public static NewCookie createCookie(final String cookieName, final String cookieValue, final String path, final boolean cookiesSecure,
		final boolean httpOnly) {

		return new NewCookie.Builder(cookieName).value(cookieValue).path(path).domain(null).comment(null).maxAge(360000) // maximum
																														// age
																														// of
																														// the
																														// cookie
																														// in
																														// seconds
			.httpOnly(httpOnly).secure(cookiesSecure).build();
	}

	public static String getSessionId(final ContainerRequestContext requestContext, final String stage) {

		String sessionIdFromHeader = getSesssionIdFromHeader(requestContext);

		if (sessionIdFromHeader != null) {

			LOGGER.debug("sessionIdFromHeader={}", sessionIdFromHeader);
			return sessionIdFromHeader;
		}

		LOGGER.debug("sessionIdFromHeader was null, try to get SessionId from Cookie");
		String sessionIdFromCookie = getSessionIdFromCookie(requestContext);
		LOGGER.debug("sessionIdFromCookie={}", sessionIdFromCookie);

		return sessionIdFromCookie;

	}

	/**
	 * @param requestContext
	 * @return
	 */
	private static String getSesssionIdFromHeader(final ContainerRequestContext requestContext) {

		String sessionIdHeader = requestContext.getHeaderString(SESSION_ID_HEADER);

		if (sessionIdHeader == null) {

			LOGGER.debug("{} dev: Request ohne SessonID-Header", requestContext.getUriInfo());

			return null;
		}

		LOGGER.debug("sessionId={}", sessionIdHeader);
		return sessionIdHeader;
	}

	/**
	 * @param requestContext
	 * @param clientPrefix
	 * @return String oder null
	 */
	private static String getSessionIdFromCookie(final ContainerRequestContext requestContext) {

		Map<String, Cookie> cookies = requestContext.getCookies();

		Cookie sessionCookie = cookies.get(AuthConstants.SESSION_COOKIE_NAME);

		if (sessionCookie != null) {

			return sessionCookie.getValue();
		}

		String path = requestContext.getUriInfo().getPath();
		LOGGER.debug("{}: Request ohne {}-Cookie", path, AuthConstants.SESSION_COOKIE_NAME);

		return null;
	}

	/**
	 * @param requestContext
	 * @param clientPrefix
	 * @return String oder null
	 */
	public static String getXsrfTokenFromCookie(final ContainerRequestContext requestContext) {

		Map<String, Cookie> cookies = requestContext.getCookies();

		Cookie csrfTokenCookie = cookies.get(AuthConstants.CSRF_TOKEN_COOKIE_NAME);

		if (csrfTokenCookie != null) {

			return csrfTokenCookie.getValue();
		}

		String path = requestContext.getUriInfo().getPath();
		LOGGER.debug("{}: Request ohne {}-Cookie", path, AuthConstants.CSRF_TOKEN_COOKIE_NAME);

		return null;
	}

	public static NewCookie createInvalidatedCookie(final String cookieName, final boolean cookiesSecure) {

		long dateInThePast = LocalDateTime.now(ZoneId.systemDefault()).minus(10, ChronoUnit.YEARS).toEpochSecond(ZoneOffset.UTC);

		return new NewCookie.Builder(cookieName).value("").path(AuthConstants.COOKIE_PATH).maxAge(0) // maximum age of the cookie in seconds
			.expiry(new Date(dateInThePast)).version(1).httpOnly(true).secure(cookiesSecure).build();

	}
}
