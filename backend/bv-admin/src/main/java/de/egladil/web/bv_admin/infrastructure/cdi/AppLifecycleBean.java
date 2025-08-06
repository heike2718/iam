// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.infrastructure.cdi;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.bv_admin.domain.auth.config.CsrfCookieConfig;
import de.egladil.web.bv_admin.domain.auth.config.SessionCookieConfig;
import de.egladil.web.bv_admin.domain.exceptions.BVAdminAPIRuntimeException;
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

	@ConfigProperty(name = "stage")
	String stage;

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

	@ConfigProperty(name = "quarkus.application.version")
	String version;

	@ConfigProperty(name = "mailversand.cron.expr")
	String mailversandCronPattern;

	@ConfigProperty(name = "public-client-id")
	String publicClientId;

	@ConfigProperty(name = "public-client-secret")
	String publicClientSecret;

	@ConfigProperty(name = "csrf-header-name")
	String csrfHeaderName;

	@Inject
	SessionCookieConfig sessionCookieConfig;

	@Inject
	CsrfCookieConfig csrfCookieConfig;

	void onStartup(@Observes
	final StartupEvent ev) {

		LOGGER.info(" ===========> Version {} of the application is starting with profiles {}", version,
			StringUtils.join(ConfigUtils.getProfiles()));

		LOGGER.info(" ===========>  stage={}", stage);
		LOGGER.info(" ===========>  session timeout nach {} min", sessionIdleTimeoutMinutes);
		LOGGER.info(" ===========>  quarkus.http.cors.origins={}", corsAllowedOrigins);
		LOGGER.info(" ===========>  jdbcUrl={}", jdbcUrl);
		LOGGER.info(" ===========>  authproviderRESTUrl={}", authproviderRESTUrl);
		LOGGER.info(" ===========>  mkGatewayRESTUrl={}", mkGatewayRESTUrl);
		LOGGER.info(" ===========>  loginRedirectUrl={}", loginRedirectUrl);
		LOGGER.info(" ===========>  mailversandCronPattern={}", mailversandCronPattern);
		LOGGER.info(" ===========>  quarkusRootPath={}", quarkusRootPath);
		LOGGER.info(" ===========>  csrfHeaderName={}", csrfHeaderName);
		LOGGER.info(" ===========>  {}", sessionCookieConfig.toLog());
		LOGGER.info(" ===========>  {}", csrfCookieConfig.toLog());
		LOGGER.info(" ===========>  publicClientId={}", StringUtils.abbreviate(publicClientId, 11));
		LOGGER.info(" ===========>  publicClientSecret={}", StringUtils.abbreviate(publicClientSecret, 6));
		LOGGER.info(" ===========>  port={}", port);

		if (csrfCookieConfig.signatureKey() == null || csrfCookieConfig.signatureKey().toLowerCase().startsWith("ueberschreiben")) {
			throw new BVAdminAPIRuntimeException("csrf-cookie.signature-key muss ueberschrieben werden!!!");
		}
	}
}
