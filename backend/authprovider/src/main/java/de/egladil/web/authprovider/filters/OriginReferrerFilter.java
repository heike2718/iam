// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.config.ConfigService;
import de.egladil.web.authprovider.error.AuthException;
import de.egladil.web.authprovider.utils.AuthHttpUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.NoContentException;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;

/**
 * OriginReferrerFilter
 */
@ApplicationScoped
@Provider
@PreMatching
public class OriginReferrerFilter implements ContainerRequestFilter {

	private static final Logger LOG = LoggerFactory.getLogger(OriginReferrerFilter.class);

	private static final List<String> NO_CONTENT_PATHS = Arrays.asList(new String[] { "/favicon.ico" });

	@Inject
	ConfigService configService;

	@Override
	public void filter(final ContainerRequestContext requestContext) throws IOException {

		String path = requestContext.getUriInfo().getPath();
		String method = requestContext.getMethod();

		LOG.info("{} : {}", method, path);

		UriInfo uriInfo = requestContext.getUriInfo();
		String pathInfo = uriInfo.getPath();

		if (NO_CONTENT_PATHS.contains(pathInfo) || "OPTIONS".equals(requestContext.getMethod())) {

			throw new NoContentException(pathInfo);
		}

		validateOriginAndRefererHeader(requestContext);
	}

	private void validateOriginAndRefererHeader(final ContainerRequestContext requestContext) throws IOException {

		final String origin = requestContext.getHeaderString("Origin");
		final String referer = requestContext.getHeaderString("Referer");

		LOG.debug("Origin = [{}], Referer=[{}]", origin, referer);

		if (StringUtils.isBlank(origin) && StringUtils.isBlank(referer)) {

			final String details = "Header Origin UND Referer fehlen";

			if (configService.isBlockOnMissingOriginReferer()) {

				logErrorAndThrow(details, requestContext);
			}
		}

		if (!StringUtils.isBlank(origin)) {

			checkHeaderTarget(origin, requestContext);
		}

		if (!StringUtils.isBlank(referer)) {

			checkHeaderTarget(referer, requestContext);
		}
	}

	private void checkHeaderTarget(final String headerValue, final ContainerRequestContext requestContext) throws IOException {

		final String extractedValue = AuthHttpUtils.extractOrigin(headerValue);

		if (extractedValue == null) {

			return;
		}

		final String targetOrigin = configService.getTargetOrigin();

		if (targetOrigin != null) {

			List<String> allowedOrigins = Arrays.asList(targetOrigin.split(","));

			if (!allowedOrigins.contains(extractedValue)) {

				final String details = "targetOrigin != extractedOrigin: [targetOrigin=" + targetOrigin
					+ ", extractedOriginOrReferer=" + extractedValue + ", allowedOrigins=" + StringUtils.join(allowedOrigins) + "]";
				logErrorAndThrow(details, requestContext);
			}
		}
	}

	private void logErrorAndThrow(final String details, final ContainerRequestContext requestContext) throws IOException {

		final String dump = AuthHttpUtils.getRequestInfos(requestContext);
		LOG.warn("Possible Attack: {} {}", details, dump);
		throw new AuthException();
	}

}
