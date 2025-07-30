// =====================================================
// Project: benutzerprofil
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.benutzerprofil.infrastructure.cdi;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.benutzerprofil.domain.auth.config.CsrfCookieConfig;
import de.egladil.web.benutzerprofil.domain.auth.config.SessionCookieConfig;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ConfigUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

/**
 * AppLifecycleBean
 */
@ApplicationScoped
public class AppLifecycleBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppLifecycleBean.class);

	@ConfigProperty(name = "env")
	String env;

	@ConfigProperty(name = "quarkus.rest-client.authprovider.url")
	String authproviderRESTUrl;

	@ConfigProperty(name = "quarkus.http.root-path")
	String quarkusRootPath;

	@ConfigProperty(name = "quarkus.http.port")
	String port;

	@ConfigProperty(name = "quarkus.http.cors.origins")
	String corsAllowedOrigins;

	@ConfigProperty(name = "session.idle.timeout")
	int sessionIdleTimeoutMinutes = 120;

	@ConfigProperty(name = "target.origin")
	String targetOrigin;

	@ConfigProperty(name = "quarkus.application.version")
	String version;

	@ConfigProperty(name = "public-client-id")
	String clientId;

	@ConfigProperty(name = "public-redirect-url")
	String redirectUrl;

	@Inject
	SessionCookieConfig sessionCookieConfig;

	@Inject
	CsrfCookieConfig csrfCookieConfig;

	void onStartup(@Observes
	final StartupEvent ev) {

		LOGGER.info(" ===========> Version {} of the application is starting with profiles {}", version,
			StringUtils.join(ConfigUtils.getProfiles()));

		LOGGER.info(" ===========>  session timeout nach {} min", sessionIdleTimeoutMinutes);
		LOGGER.info(" ===========>  quarkus.http.cors.origins={}", corsAllowedOrigins);
		LOGGER.info(" ===========>  authproviderRESTUrl={}", authproviderRESTUrl);
		LOGGER.info(" ===========>  targetOrigin={}", targetOrigin);
		LOGGER.info(" ===========>  quarkusRootPath={}", quarkusRootPath);
		LOGGER.info(" ===========>  port={}", port);
		LOGGER.info(" ===========>  redirectUrl={}", redirectUrl);
		LOGGER.info(" ===========>  {}", sessionCookieConfig.toLog());
		LOGGER.info(" ===========>  {}", csrfCookieConfig.toLog());

		if ("dev".equalsIgnoreCase(env)) {

			LOGGER.info(" ===========>  clientId={}", clientId);
		}
	}

}
