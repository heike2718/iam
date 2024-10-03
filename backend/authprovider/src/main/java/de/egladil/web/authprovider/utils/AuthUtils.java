// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.utils;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import io.vertx.core.http.HttpServerRequest;

/**
 * AuthUtils
 */
public final class AuthUtils {

	public static final String DEFAULT_ROLE = "STANDARD";

	private static final String[] KNOWN_MOBILE_USER_AGENT_PREFIXES = new String[] {
		"w3c ", "w3c-", "acs-", "alav", "alca", "amoi", "audi", "avan", "benq",
		"bird", "blac", "blaz", "brew", "cell", "cldc", "cmd-", "dang", "doco",
		"eric", "hipt", "htc_", "inno", "ipaq", "ipod", "jigs", "kddi", "keji",
		"leno", "lg-c", "lg-d", "lg-g", "lge-", "lg/u", "maui", "maxo", "midp",
		"mits", "mmef", "mobi", "mot-", "moto", "mwbp", "nec-", "newt", "noki",
		"palm", "pana", "pant", "phil", "play", "port", "prox", "qwap", "sage",
		"sams", "sany", "sch-", "sec-", "send", "seri", "sgh-", "shar", "sie-",
		"siem", "smal", "smar", "sony", "sph-", "symb", "t-mo", "teli", "tim-",
		"tosh", "tsm-", "upg1", "upsi", "vk-v", "voda", "wap-", "wapa", "wapi",
		"wapp", "wapr", "webc", "winw", "winw", "xda ", "xda-" };

	private static final String[] KNOWN_MOBILE_USER_AGENT_KEYWORDS = new String[] {
		"blackberry", "webos", "ipod", "lge vx", "midp", "maemo", "mmp", "mobile",
		"netfront", "hiptop", "nintendo DS", "novarra", "openweb", "opera mobi",
		"opera mini", "palm", "psp", "phone", "smartphone", "symbian", "up.browser",
		"up.link", "wap", "windows ce" };

	private static final String[] KNOWN_TABLET_USER_AGENT_KEYWORDS = new String[] {
		"ipad", "playbook", "hp-tablet", "kindle" };

	/**
	 *
	 */
	private AuthUtils() {

	}

	public static String newTokenId() {

		String uuid = UUID.randomUUID().toString();
		String result = uuid.replaceAll("\\-", "");

		return result;
	}

	public static boolean isMobileDevice(final String userAgent) {

		if (userAgent == null) {

			return false;
		}

		Optional<String> opt = Arrays.asList(KNOWN_TABLET_USER_AGENT_KEYWORDS).stream().filter(w -> userAgent.contains(w))
			.findFirst();

		if (opt.isPresent()) {

			return true;
		}

		opt = Arrays.asList(KNOWN_MOBILE_USER_AGENT_KEYWORDS).stream().filter(w -> userAgent.contains(w)).findFirst();

		if (opt.isPresent()) {

			return true;
		}

		opt = Arrays.asList(KNOWN_MOBILE_USER_AGENT_PREFIXES).stream().filter(w -> userAgent.contains(w)).findFirst();

		if (opt.isPresent()) {

			return true;
		}

		return false;
	}

	/**
	 * Ein Kommaseparierter String wird normalisiert.
	 * <ul>
	 * <li>alles toUpperCase</li>
	 * <li>Dubletten entfernt</li>
	 * <li>STANDARD eingefügt, falls erforderlich</li>
	 * <li>alphabetisch sortiert.</li>
	 * </ul>
	 *
	 * @param  roles
	 * @return
	 */
	public static String normalizeRoles(final String roles) {

		if (StringUtils.isBlank(roles)) {

			return DEFAULT_ROLE;
		}

		String[] rolesArray = roles.split(",");

		Set<String> ohneDoubles = Arrays.stream(rolesArray).map(s -> s.toUpperCase()).collect(Collectors.toSet());

		Optional<String> optStandard = ohneDoubles.stream().filter(s -> DEFAULT_ROLE.equals(s)).findFirst();

		if (!optStandard.isPresent()) {

			ohneDoubles.add(DEFAULT_ROLE);
		}

		List<String> sortierbar = new ArrayList<String>(ohneDoubles);
		Collections.sort(sortierbar, Collator.getInstance(Locale.GERMAN));

		return StringUtils.join(sortierbar, ",");
	}

	public static final String getUserAgent(final HttpServerRequest request) {

		return request == null ? null : request.getHeader("User-Agent");
	}

	public static final String getIPAddress(final HttpServerRequest request) {

		if (request == null) {

			return null;
		}

		String ipAddress = request.getHeader("X-Forwarded-For");

		if (ipAddress == null || ipAddress.isEmpty()) {

			ipAddress = request.remoteAddress().host();
		}
		return ipAddress;
	}

}
