// =====================================================
// Projekt: commons
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.utils;

import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.NewCookie;

/**
 * AuthHttpUtils
 */
public final class AuthHttpUtils {

	public static final String NAME_SESSIONID_COOKIE = "_SESSIONID";

	private static final String STAGE_DEV = "dev";

	private static final String SESSION_ID_HEADER = "X-SESSIONID";

	private static final Logger LOG = LoggerFactory.getLogger(AuthHttpUtils.class);

	/**
	 * Erzeugt eine Instanz von AuthHttpUtils
	 */
	private AuthHttpUtils() {

	}

	public static String extractOrigin(final String headerValue) {

		if (StringUtils.isBlank(headerValue)) {

			return null;
		}
		final String value = headerValue.replaceAll("http://", "").replaceAll("https://", "");
		final String[] token = StringUtils.split(value, "/");
		final String extractedOrigin = token == null ? value : token[0];
		return extractedOrigin;
	}

	/**
	 * Zu Logzwecken alle Headers dumpen.
	 *
	 * @param  requestContext
	 * @return
	 */
	public static String getRequestInfos(final ContainerRequestContext requestContext) {

		final StringBuffer sb = new StringBuffer();
		sb.append(" <--- Request Headers --- ");

		requestContext.getHeaders().entrySet().stream().forEach(e -> {

			String key = e.getKey();
			sb.append(key);
			sb.append(":");
			e.getValue().stream().forEach(v -> {

				sb.append(v);
				sb.append(", ");
			});
		});

		sb.append(" Headers Request ---> ");
		final String dump = sb.toString();
		return dump;
	}

	/**
	 * Gibt aus dem requestContext die sessionId zurück, falls vorhanden, sonst null.<br>
	 * <ul>
	 * <li>Stage dev: Wert des Headers mit dem Namen X-SESSIONID</li>
	 * <li>Sonst: Wert des Cookies mit dem Namen clientPrefix_SESSIONID
	 * </ul>
	 *
	 * @param  requestContext
	 *                        ContainerRequestContext
	 * @param  stage
	 *                        String Name der Umgebung, also dev oder nicht
	 * @param  clientPrefix
	 * @return                Stringf oder null
	 */
	public static String getSessionId(final ContainerRequestContext requestContext, final String stage, final String clientPrefix) {

		return !STAGE_DEV.equals(stage) ? getSessionIdFromCookie(requestContext, clientPrefix)
			: getSesssionIdFromHeader(requestContext);

	}

	private static String getSessionIdFromCookie(final ContainerRequestContext requestContext, final String clientPrefix) {

		String name = clientPrefix + NAME_SESSIONID_COOKIE;

		LOG.debug("Suchen Cookie mit namen {}", name);

		Map<String, Cookie> cookies = requestContext.getCookies();

		Cookie sessionCookie = cookies.get(name);

		if (sessionCookie != null) {

			return sessionCookie.getValue();
		}

		LOG.warn("{}: Request ohne {}-Cookie", requestContext.getUriInfo(), name);
		return null;
	}

	private static String getSesssionIdFromHeader(final ContainerRequestContext requestContext) {

		String sessionIdHeader = requestContext.getHeaderString(SESSION_ID_HEADER);

		if (sessionIdHeader == null) {

			LOG.debug("{} dev: Request ohne SessonID-Header", requestContext.getUriInfo());

			return null;
		}

		LOG.debug("sessionId={}", sessionIdHeader);
		return sessionIdHeader;
	}

	public static NewCookie createSessionInvalidatedCookie(final String clientPrefix) {

		long dateInThePast = AuthTimeUtils.now().minus(10, ChronoUnit.YEARS).toEpochSecond(ZoneOffset.UTC);

		String name = clientPrefix + NAME_SESSIONID_COOKIE;

		LOG.debug("Erzeugen Cookie mit name={} und Wert null", name);

		// @formatter:off
		return new NewCookie.Builder(name)
			.maxAge(0) // maximum age of the cookie in seconds
			.expiry(new Date(dateInThePast))
			.version(1)
			.httpOnly(true)
			.secure(true)
			.build();
		// @formatter:on
	}

	/**
	 * Erzeugt eine User-ID-Referenz, mit der der User für den Zeitraum der Session identifiziert werden kann.
	 *
	 * @return String
	 */
	public static String createUserIdReference() {

		long msb = UUID.randomUUID().getMostSignificantBits();
		return Long.toHexString(msb);
	}
}
