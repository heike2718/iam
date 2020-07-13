// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

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

/**
 * AuthorizationFilter
 */
@ApplicationScoped
@Provider
@Priority(Priorities.AUTHORIZATION)
@PreMatching
public class AuthorizationFilter implements ContainerRequestFilter {

	private static final String CSRF_TOKEN_HEADER = "X-XSRF-TOKEN";

	private static final Logger LOG = LoggerFactory.getLogger(AuthorizationFilter.class);

	private static final List<String> AUTHORIZED_PATHS = Arrays
		.asList(new String[] { "/auth/sessions", "/users/signup", "/temppwd", "/validators" });

	@Inject
	ConfigService configService;

	@Context
	ResourceInfo resourceInfo;

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
					throw new AuthException("Keine Berechtigung");
				}

				String sessionId = CommonHttpUtils.getSessionId(requestContext, configService.getStage(),
					AuthProviderApp.CLIENT_COOKIE_PREFIX);

				if (sessionId != null) {

					AuthSession userSession = sessionService.getSession(sessionId);

					if (userSession == null) {

						LOG.warn("sessionId {} nicht bekannt oder abgelaufen", sessionId);
						throw new SessionExpiredException("keine gültige Session vorhanden");
					}

					if (!csrfToken.equals(userSession.getCsrfToken())) {

						LOG.warn(LogmessagePrefixes.BOT + "Aufruf mit falshem CSRF-Token: path=", path);
						throw new AuthException("Keine Berechtigung");
					}

				} else {

					throw new AuthException("Keine Berechtigung");
				}
			} else {

				LOG.debug("alles ok");
			}
		}

	}

	private boolean needsSession(final String path) {

		Optional<String> optPath = AUTHORIZED_PATHS.stream().filter(p -> path.toLowerCase().startsWith(p)).findFirst();

		return optPath.isPresent();
	}

}
