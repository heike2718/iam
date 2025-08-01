// =====================================================
// Project: benutzerprofil
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.benutzerprofil.infrastructure.filters;

import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.vertx.web.RouteFilter;
import io.vertx.ext.web.RoutingContext;

/**
 * SPARouteFilter. Das Backend served die Angular-Anwendung. Daher müssen statischen Webseiten woanders gesucht werden,
 * als die Quarkus-app.
 */
public class SPARouteFilter {

	private static final Predicate<String> FILE_NAME_PREDICATE = Pattern.compile(".*[.][a-zA-Z\\d]+").asMatchPredicate();

	private static final String API_PREFIX = "/api/";

	private static final String APP_PLUS_API_PREFIX = "/benutzerprofil" + API_PREFIX;

	private static final String DEFAULT_APP = "/benutzerprofil/";

	private static final String[] PATH_PREFIXES = { DEFAULT_APP };

	private static final Logger LOGGER = LoggerFactory.getLogger(SPARouteFilter.class);

	@RouteFilter(100)
	void apiFilter(final RoutingContext rc) {

		final String path = rc.normalizedPath();
		LOGGER.info("Check reroute with path: " + path);

		if (path.startsWith(APP_PLUS_API_PREFIX)) {

			// reroute to REST-API
			String rerouted = path.replaceFirst(DEFAULT_APP, "/") + getQueryParameters(rc);
			LOGGER.info("(2) rc.reroute: " + rerouted);
			rc.reroute(rerouted);
		} else {

			LOGGER.info("(3)");

			if (this.doesNotNeedRedirect(path)) {

				LOGGER.info("(4)");
				rc.next();
			} else {

				LOGGER.info("(5)");

				if (path.startsWith(DEFAULT_APP)) {

					// I0094: deep-Angular-Router-Links (z.B. /benutzerprofil/user/) müssen zur SPA Grund-URL
					// (/benutzerprofil/) umgeleitet werden. Danach übernimmt wieder das Angular-Routing
					// Jetzt funktionieren Bookmarking, Back-Button sowie F5 ohne dass es ein 404 gibt.
					String[] tokens = path.split("/");
					LOGGER.info("(6) Anzahl token = {}", tokens.length);

					if (tokens.length > 2) {

						// /profil-app/ => 2 tokens!
						String rerouted = "/" + tokens[1] + "/";
						LOGGER.info("(7) Umleiten von deep Angular router links: {} nach {} ", path, rerouted);
						rc.reroute(rerouted);
					} else {

						LOGGER.info("(8) kein Umleiten der SPA-Grund-URL {} ", path);
						rc.next();
					}

				} else {

					LOGGER.info("(9) global else => rc.next()");
					rc.next();
				}
			}
		}
	}

	boolean doesNotNeedRedirect(final String path) {

		if (path.equals("/")) {

			LOGGER.info("(3-1) kein Umleiten von /");
			return true;
		}

		if (FILE_NAME_PREDICATE.test(path)) {

			LOGGER.info("(3-2) kein Umleiten von statischen files aus src/main/resources/META-INF/resources/benutzerprofil/");
			return true;
		}

		if (Stream.of(PATH_PREFIXES).noneMatch(path::startsWith)) {

			LOGGER.info("(3-3) kein Umleiten von Pfaden, die nicht mit {} beginnen", DEFAULT_APP);
			return true;
		}

		LOGGER.info("(3-4)");
		return false;
	}

	String getQueryParameters(final RoutingContext rc) {

		Map<String, String> queryParams = rc.queryParams().entries().stream()
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		if (queryParams.isEmpty()) {

			return "";
		}

		StringBuffer sb = new StringBuffer("?");
		queryParams.forEach((key, value) -> sb.append(key).append("=").append(value).append("&"));

		sb.deleteCharAt(sb.length() - 1);

		return sb.toString();

	}

}
