// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.AuthProviderApp;
import de.egladil.web.authprovider.config.ConfigService;
import de.egladil.web.authprovider.domain.AuthSession;
import de.egladil.web.authprovider.error.AuthException;
import de.egladil.web.authprovider.error.LogmessagePrefixes;
import de.egladil.web.authprovider.service.AuthproviderSessionService;
import de.egladil.web.commons_net.exception.SessionExpiredException;
import de.egladil.web.commons_net.utils.CommonHttpUtils;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;

/**
 * AuthorizationFilter
 */
// @ApplicationScoped
// @Provider
// @Priority(Priorities.AUTHORIZATION)
// @PreMatching
public class AuthorizationFilter implements ContainerRequestFilter {

	private static final String CSRF_TOKEN_HEADER = "X-XSRF-TOKEN";

	private static final Logger LOG = LoggerFactory.getLogger(AuthorizationFilter.class);

	private static final List<String> AUTHORIZED_PATHS = Arrays
		.asList(new String[] { "/auth/sessions", "/users/signup", "/temppwd", "/validators" });

	@Inject
	ConfigService configService;

	@Inject
	AuthproviderSessionService sessionService;

	@Override
	public void filter(final ContainerRequestContext requestContext) throws IOException {

		String path = requestContext.getUriInfo().getPath();
		String method = requestContext.getMethod();

		if (!"OPTIONS".equals(method)) {

			LOG.debug("entering AuthorizationFilter: path={}", path);

			if (needsSession(path)) {

				String csrfToken = requestContext.getHeaderString(CSRF_TOKEN_HEADER);

				if (csrfToken == null) {

					LOG.warn(LogmessagePrefixes.BOT + "Aufruf ohne CSRF-Token: path=", path);

					// FIXME: CSRF-Protection ist clientseitig noch nicht eingebaut, daher ersteinmal keine AuthException
					// throw new AuthException("Keine Berechtigung");
				}

				String sessionId = CommonHttpUtils.getSessionId(requestContext, configService.getStage(),
					AuthProviderApp.CLIENT_COOKIE_PREFIX);

				if (sessionId != null) {

					if (AuthProviderApp.STAGE_DEV.equals(configService.getStage())) {

						LOG.info("path={}, sessionId={}", path, sessionId);
					}

					AuthSession userSession = sessionService.getSession(sessionId);

					if (userSession == null) {

						LOG.warn("sessionId {} nicht bekannt oder abgelaufen", sessionId);
						throw new SessionExpiredException("keine gültige Session vorhanden");
					}

					if (!csrfToken.equals(userSession.getCsrfToken())) {

						// FIXME: CSRF-Protection ist clientseitig noch nicht eingebaut, daher ersteinmal keine AuthException
						LOG.warn(LogmessagePrefixes.BOT + "Aufruf mit falschem CSRF-Token: path=", path);
						// throw new AuthException("Keine Berechtigung");
					}

				} else {

					LOG.warn("sessionId null: path={}", path);

					throw new AuthException("Keine Berechtigung");
				}
			} else {

				LOG.debug("alles ok");
			}
		}

	}

	private boolean needsSession(final String path) {

		Optional<String> optPath = AUTHORIZED_PATHS.stream().filter(p -> path.toLowerCase().startsWith(p)).findFirst();

		LOG.debug("path={} - present:{}", path, optPath.isPresent());
		return optPath.isPresent();
	}

}
