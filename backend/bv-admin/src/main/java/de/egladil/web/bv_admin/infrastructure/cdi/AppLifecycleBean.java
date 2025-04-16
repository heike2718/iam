// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.infrastructure.cdi;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ConfigUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

/**
 * AppLifecycleBean
 */
@ApplicationScoped
public class AppLifecycleBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppLifecycleBean.class);

	@ConfigProperty(name = "env")
	String env;

	@ConfigProperty(name = "quarkus.datasource.jdbc.url")
	String jdbcUrl;

	@ConfigProperty(name = "quarkus.rest-client.authprovider.url")
	String authproviderRESTUrl;

	@ConfigProperty(name = "quarkus.rest-client.mkgateway.url")
	String mkGatewayRESTUrl;

	@ConfigProperty(name = "quarkus.http.root-path")
	String quarkusRootPath;

	@ConfigProperty(name = "public-redirect-url")
	String loginRedirectUrl;

	@ConfigProperty(name = "quarkus.http.port")
	String port;

	@ConfigProperty(name = "quarkus.http.cors.origins")
	String corsAllowedOrigins;

	@ConfigProperty(name = "session.idle.timeout")
	int sessionIdleTimeoutMinutes = 120;

	@ConfigProperty(name = "csrf.enabled")
	String csrfEnabled;

	@ConfigProperty(name = "mock.session")
	String mockSession;

	@ConfigProperty(name = "target.origin")
	String targetOrigin;

	@ConfigProperty(name = "quarkus.application.version")
	String version;

	@ConfigProperty(name = "mailversand.cron.expr")
	String mailversandCronPattern;

	@ConfigProperty(name = "public-client-id")
	String publicClientId;

	@ConfigProperty(name = "public-client-secret")
	String publicClientSecret;

	void onStartup(@Observes
	final StartupEvent ev) {

		LOGGER.info(" ===========> Version {} of the application is starting with profiles {}", version,
			StringUtils.join(ConfigUtils.getProfiles()));

		LOGGER.info(" ===========>  session timeout nach {} min", sessionIdleTimeoutMinutes);
		LOGGER.info(" ===========>  quarkus.http.cors.origins={}", corsAllowedOrigins);
		LOGGER.info(" ===========>  jdbcUrl={}", jdbcUrl);
		LOGGER.info(" ===========>  authproviderRESTUrl={}", authproviderRESTUrl);
		LOGGER.info(" ===========>  mkGatewayRESTUrl={}", mkGatewayRESTUrl);
		LOGGER.info(" ===========>  targetOrigin={}", targetOrigin);
		LOGGER.info(" ===========>  loginRedirectUrl={}", loginRedirectUrl);
		LOGGER.info(" ===========>  mailversandCronPattern={}", mailversandCronPattern);
		LOGGER.info(" ===========>  csrfEnabled={}", csrfEnabled);
		LOGGER.info(" ===========>  mockSession={}", mockSession);
		LOGGER.info(" ===========>  quarkusRootPath={}", quarkusRootPath);
		LOGGER.info(" ===========>  port={}", port);

		if ("dev".equalsIgnoreCase(env)) {
			LOGGER.info(" ===========>  publicClientId={}", publicClientId);
			LOGGER.info(" ===========>  publicClientSecret={}", publicClientSecret);
		}


	}

}
