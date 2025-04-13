// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.benutzerprofil.infrastructure.filters;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.benutzerprofil.domain.auth.session.CsrfCookieService;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

/**
 * CsrfTokenProviderFilter
 */
@Provider
public class CsrfTokenProviderFilter implements ContainerResponseFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(CsrfTokenProviderFilter.class);

	@Inject
	CsrfCookieService csrfCookieService;

	@Override
	public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext)
		throws IOException {

		LOGGER.info("==> Set-Cookie");
		responseContext.getHeaders().add("Set-Cookie", csrfCookieService.createCsrfTokenCookie());
	}
}
