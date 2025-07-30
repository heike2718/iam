// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.benutzerprofil.domain.auth.session;

import java.util.Map;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.benutzerprofil.domain.auth.config.CsrfCookieConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.NewCookie;;

/**
 * CsrfCookieService
 */
@ApplicationScoped
public class CsrfCookieService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CsrfCookieService.class);

	@Inject
	SecureTokenService csrfTokenService;

	@Inject
	CsrfCookieConfig csrfCookieConfig;

	/**
	 * Erzeugt ein neues CsrfToken.
	 *
	 * @return
	 */
	public NewCookie createCsrfTokenCookie() {

		String csrfToken = csrfTokenService.createRandomToken().replaceAll("\"", "");

		LOGGER.debug("csrfToken={}", csrfToken);

		return new NewCookie.Builder(csrfCookieConfig.name()).value(csrfToken).path(csrfCookieConfig.path()).comment("csrf")
			.maxAge(-1).httpOnly(false).secure(csrfCookieConfig.secure()).build();
	}

	/**
	 * @param requestContext
	 * @param clientPrefix
	 * @return String oder null
	 */
	public String getXsrfTokenFromCookie(final ContainerRequestContext requestContext) {

		Map<String, Cookie> cookies = requestContext.getCookies();

		Cookie csrfTokenCookie = cookies.get(csrfCookieConfig.name());

		if (csrfTokenCookie != null) {

			return csrfTokenCookie.getValue();
		}

		String path = requestContext.getUriInfo().getPath();
		LOGGER.debug("{}: Request ohne {}-Cookie", path, csrfCookieConfig.name());

		return null;
	}
}
