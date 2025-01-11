// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.infrastructure.filters;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.bv_admin.domain.auth.session.AuthenticatedUser;
import de.egladil.web.bv_admin.domain.auth.session.Session;
import de.egladil.web.bv_admin.domain.auth.session.SessionService;
import de.egladil.web.bv_admin.domain.auth.session.SessionUtils;
import de.egladil.web.bv_admin.domain.exceptions.AuthAdminAPIRuntimeException;
import de.egladil.web.bv_admin.infrastructure.cdi.AuthenticationContextImpl;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

/**
 * InitSecurityContextFilter
 */
@ApplicationScoped
@Provider
@PreMatching
@Priority(Priorities.AUTHORIZATION)
public class InitSecurityContextFilter implements ContainerRequestFilter {

	private static final String STAGE_PROD = "prod";

	private static final Logger LOGGER = LoggerFactory.getLogger(InitSecurityContextFilter.class);

	private static List<String> OPEN_DATA_PATHS = Arrays.asList(new String[] { "/auth-admin-api/version" });

	@ConfigProperty(name = "stage")
	String stage;

	@ConfigProperty(name = "mock.session")
	boolean mockSession;

	@ConfigProperty(name = "mock.benutzerid")
	String mockBenutzerid;

	@ConfigProperty(name = "mock.benutzer.fullname")
	String mockBenutzerFullName;

	@ConfigProperty(name = "mock.role")
	String mockRole;

	@Inject
	SessionService sessionService;

	@Inject
	AuthenticationContextImpl authCtx; // Injecting the implementation, not the interface!!!

	@Override
	public void filter(final ContainerRequestContext requestContext) throws IOException {

		// https://quarkus.io/guides/context-propagation

		String method = requestContext.getMethod();

		if ("OPTIONS".equals(method)) {

			LOGGER.debug("keine Auth bei OPTIONS");

			return;
		}

		String path = requestContext.getUriInfo().getPath();

		boolean noSessionRequired = this.noSessionRequired(path);

		LOGGER.debug("stage={}, mockSession={}, path={}, noSessionRequired={}", stage, mockSession, path, noSessionRequired);

		if (noSessionRequired) {

			this.addUserToAuthAndSecurityContext(AuthenticatedUser.createAnonymousUser(), requestContext);
			return;
		}

		try {

			if (!stage.toLowerCase().startsWith(STAGE_PROD) && mockSession) {

				LOGGER.warn("Achtung: mock-Session!!! check properties 'stage' und 'mock.session' [stage={}, mockSession=", stage,
					mockSession);

				initMockSecurityContext(requestContext);
			} else {

				String sessionId = SessionUtils.getSessionId(requestContext, stage);

				LOGGER.debug("path={}, sessionId={}", path, sessionId);

				if (sessionId != null) {

					Session session = sessionService.getAndRefreshSessionIfValid(sessionId);

					if (session != null) {

						AuthenticatedUser user = session.getUser();

						if (user != null) {

							addUserToAuthAndSecurityContext(user, requestContext);
						} else {

							LOGGER.warn("path={}, user ist null, die Anwendung wird nicht funktionieren!", path);
						}

					}
				}
			}
		} catch (Exception e) {

			LOGGER.error("{}: {}", path, e.getMessage(), e);
			throw new AuthAdminAPIRuntimeException("Unerwarterer Fehler bei Request " + method + " path=" + path);
		}
	}

	/**
	 * @param requestContext
	 */
	private void addUserToAuthAndSecurityContext(final AuthenticatedUser user, final ContainerRequestContext requestContext) {

		authCtx.setUser(user);

		requestContext.setSecurityContext(new SecurityContext() {

			@Override
			public boolean isUserInRole(final String role) {

				Optional<String> opt = Arrays.stream(user.getRoles()).filter(r -> role.equalsIgnoreCase(r)).findFirst();
				return opt.isPresent();
			}

			@Override
			public boolean isSecure() {

				return true;
			}

			@Override
			public Principal getUserPrincipal() {

				return new Principal() {

					@Override
					public String getName() {

						return user.getUuid();
					}
				};
			}

			@Override
			public String getAuthenticationScheme() {

				return null;
			}
		});

		LOGGER.debug("admin {} added to AuthenticationContext and SecurityContext", user);

	}

	private void initMockSecurityContext(final ContainerRequestContext requestContext) {

		AuthenticatedUser user = new AuthenticatedUser(mockBenutzerid).withFullName(mockBenutzerFullName).withIdReference("bla")
			.withRoles(new String[] { mockRole });

		authCtx.setUser(user);
		LOGGER.warn("config property 'mock.session' is true => authCtx with mocked admin: ");
		LOGGER.info("====> user={}", user);
	}

	boolean noSessionRequired(final String path) {

		return OPEN_DATA_PATHS.stream().filter(p -> path.startsWith(p)).findFirst().isPresent();

	}
}
