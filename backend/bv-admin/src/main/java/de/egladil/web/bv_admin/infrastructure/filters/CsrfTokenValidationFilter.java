// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.infrastructure.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.bv_admin.domain.auth.dto.MessagePayload;
import de.egladil.web.bv_admin.domain.auth.session.SessionService;
import de.egladil.web.bv_admin.domain.auth.util.CsrfCookieService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

/**
 * CsrfTokenValidationFilter
 */
@ApplicationScoped
@Provider
@PreMatching
public class CsrfTokenValidationFilter implements ContainerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(CsrfTokenValidationFilter.class);

	private static final String INVALID_CSRF_RESPONSE_PAYLOD = "CSRF-Token-Validierung fehlgeschlagen";

	private static final List<String> SECURE_HTTP_METHODS = Arrays.asList(new String[] { "OPTIONS", "GET", "HEAD" });

	private static final List<String> SECURE_PATHS = Arrays.asList(new String[] { "/", "/session/logout" });

	@ConfigProperty(name = "csrf-header-name")
	String csrfHeaderName;

	@Inject
	SessionService sessionservice;

	@Inject
	CsrfCookieService csrfCookieService;

	@Override
	public void filter(final ContainerRequestContext requestContext) throws IOException {

		LOGGER.debug("entering filter");

		String path = requestContext.getUriInfo().getPath();
		String method = requestContext.getMethod();

		if (SECURE_HTTP_METHODS.contains(method) || SECURE_PATHS.contains(path)) {

			return;
		}

		String cookieValue = csrfCookieService.getXsrfTokenFromCookie(requestContext);

		if (cookieValue != null) {

			LOGGER.debug("{} {}", method, path);

			List<String> csrfTokenHeader = requestContext.getHeaders().get(csrfHeaderName);

			if (csrfTokenHeader != null && !csrfTokenHeader.isEmpty()) {

				String headerValue = csrfTokenHeader.get(0);

				if (!identifyAsEquals(headerValue, cookieValue)) {

					LOGGER.warn("cookieValue != headerValue: [headerValue={}, cookieValue={}, path={}", headerValue, cookieValue,
						path);
					requestContext
						.abortWith(Response.status(400).entity(MessagePayload.error(INVALID_CSRF_RESPONSE_PAYLOD)).build());
				}
			} else {

				LOGGER.warn("csrfTokenHeader == null oder csrfTokenHeader.size() != 1 bei path={}", path);
				requestContext.abortWith(Response.status(400).entity(MessagePayload.error(INVALID_CSRF_RESPONSE_PAYLOD)).build());
			}

		} else {

			LOGGER.warn("csrfTokenCookie == null bei {} path={}", method, path);
			requestContext.abortWith(Response.status(400).entity(MessagePayload.error(INVALID_CSRF_RESPONSE_PAYLOD)).build());
		}
	}

	boolean identifyAsEquals(final String headerValue, final String cookieValue) {

		String strippedHeaderValue = headerValue;

		// ist irgendwie komisch, aber der headerValue kommt mit Anführungszeichen.
		if (headerValue.contains("\"")) {

			strippedHeaderValue = headerValue.replaceAll("\"", "");
		}

		return strippedHeaderValue.equals(cookieValue);
	}

}
