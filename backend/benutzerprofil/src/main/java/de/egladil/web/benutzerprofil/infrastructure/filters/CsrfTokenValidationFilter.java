// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.benutzerprofil.infrastructure.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.benutzerprofil.domain.auth.dto.MessagePayload;
import de.egladil.web.benutzerprofil.domain.auth.session.Session;
import de.egladil.web.benutzerprofil.domain.auth.session.SessionService;
import de.egladil.web.benutzerprofil.domain.auth.session.SessionUtils;
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

	@ConfigProperty(name = "stage")
	String stage;

	@ConfigProperty(name = "csrf.enabled")
	boolean csrfEnabled;

	@Inject
	SessionService sessionservice;

	@Override
	public void filter(final ContainerRequestContext requestContext) throws IOException {

		LOGGER.debug("entering filter");

		if (!csrfEnabled) {

			LOGGER.warn("Achtung: keine csrf protection: check property 'csrf.enabled' [csrfEnabled={}]",
				csrfEnabled);
			return;
		}

		String path = requestContext.getUriInfo().getPath();
		String method = requestContext.getMethod();

		if (SECURE_HTTP_METHODS.contains(method) || SECURE_PATHS.contains(path)) {

			return;
		}

		String xsrfTokenCookieValue = SessionUtils.getXsrfTokenFromCookie(requestContext);
		String sessionId = SessionUtils.getSessionId(requestContext, stage);

		LOGGER.info("sessionId={}, xsrfTokenCookieValue={}", sessionId, xsrfTokenCookieValue);

		if (xsrfTokenCookieValue != null && sessionId != null) {

			Session session = sessionservice.getSessionNullSave(sessionId);

			if (session != null && !xsrfTokenCookieValue.equals(session.getXsrfToken())) {

				requestContext.abortWith(Response.status(400).entity(MessagePayload.error(INVALID_CSRF_RESPONSE_PAYLOD)).build());
			}
		}
	}
}
