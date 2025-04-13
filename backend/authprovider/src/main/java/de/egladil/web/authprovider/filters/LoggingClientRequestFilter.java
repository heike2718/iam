// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.filters;

import java.io.IOException;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.ext.Provider;

@ApplicationScoped
@Provider
@PreMatching
public class LoggingClientRequestFilter implements ClientRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingClientRequestFilter.class);

	@Override
	public void filter(final ClientRequestContext requestContext) throws IOException {

		URI uri = requestContext.getUri();
		LOGGER.debug(">>>>> Request URL: {} <<<<<", uri);
	}
}
