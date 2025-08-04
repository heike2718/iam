// =====================================================
// Projekt: commons
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.utils;

import org.apache.commons.lang3.StringUtils;

import jakarta.ws.rs.container.ContainerRequestContext;

/**
 * AuthHttpUtils
 */
public final class AuthHttpUtils {

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
	 * @param requestContext
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
}
