// =====================================================
// Projekt: de.egladil.mkv.adminservice
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.benutzerprofil.infrastructure.filters;

import java.io.IOException;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.benutzerprofil.BenutzerprofilApplication;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.Provider;

/**
 * SecureHeadersFilter
 */
@Provider
public class SecureHeadersFilter implements ContainerResponseFilter {

	private static final String CONTENT_SECURITY_POLICY = "Content-Security-Policy";

	private static final Logger LOGGER = LoggerFactory.getLogger(SecureHeadersFilter.class);

	@ConfigProperty(name = "stage")
	String stage;

	@Override
	public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext)
		throws IOException {

		final MultivaluedMap<String, Object> headers = responseContext.getHeaders();

		if (headers.get("Cache-Control") == null) {

			headers.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
		}

		if (headers.get("Pragma") == null) {

			headers.add("Pragma", "no-cache");
		}

		if (headers.get("X-Content-Type-Options") == null) {

			headers.add("X-Content-Type-Options", "nosniff");
		}

		if (headers.get("X-Frame-Options") == null) {

			headers.add("X-Frame-Options", "DENY");
		}

		if (headers.get("Server") == null) {

			headers.add("Server", "Hex");
		}

		if (headers.get("X-Powered-By") == null) {

			headers.add("X-Powered-By", "Ponder Stibbons");
		}

		if (headers.get("Vary") == null) {

			headers.add("Vary", "Origin");
		}

		if (headers.get(CONTENT_SECURITY_POLICY) == null) {

			responseContext.getHeaders().add(CONTENT_SECURITY_POLICY, "default-src 'self'; ");
		}

		if (!BenutzerprofilApplication.STAGE_DEV.equals(stage) && headers.get("Strict-Transport-Security") == null) {

			headers.add("Strict-Transport-Security", "max-age=63072000; includeSubdomains");

		} else {

			LOGGER.info("stage={}", stage);
		}

		if (headers.get("X-XSS-Protection") == null) {

			headers.add("X-XSS-Protection", "1; mode=block");
		}

		if (headers.get("X-Frame-Options") == null) {

			headers.add("X-Frame-Options", "deny");
		}
	}
}
