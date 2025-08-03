// =====================================================
// Project: mk-gateway
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.cdi;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

/**
 * AppLifecycleBean
 */
@ApplicationScoped
public class AppLifecycleBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppLifecycleBean.class);

	@ConfigProperty(name = "quarkus.hibernate-orm.log.sql")
	boolean logSql;

	@ConfigProperty(name = "quarkus.hibernate-orm.log.bind-parameters")
	boolean logBindParameters;

	@ConfigProperty(name = "quarkus.log.console.level")
	String consoleLogLevel;

	@ConfigProperty(name = "quarkus.log.file.level")
	String fileLogLevel;

	@ConfigProperty(name = "quarkus.log.level")
	String logLevel;

	@ConfigProperty(name = "quarkus.log.min-level")
	String logMinLevel;

	@ConfigProperty(name = "quarkus.datasource.jdbc.url")
	String jdbcURL;

	@ConfigProperty(name = "stage")
	String stage;

	@ConfigProperty(name = "quarkus.rest-client.mkgateway.url")
	String mkGatewayRoute;

	@ConfigProperty(name = "quarkus.http.cors.origins", defaultValue = "")
	String corsAllowedOrigins;

	@ConfigProperty(name = "quarkus.http.port")
	String port;

	@ConfigProperty(name = "temp-pwd.url")
	String tempPwdUrl;

	@ConfigProperty(name = "account.activation.url")
	String accountActivationUrl;

	void onStartup(@Observes
	final StartupEvent ev) {

		LOGGER.info(" ===========>  logSql={}", logSql);
		LOGGER.info(" ===========>  logBindParameters={}", logBindParameters);
		LOGGER.info(" ===========>  logLevel={}", logLevel);
		LOGGER.info(" ===========>  logMinLevel={}", logMinLevel);
		LOGGER.info(" ===========>  consoleLogLevel={}", consoleLogLevel);
		LOGGER.info(" ===========>  fileLogLevel={}", fileLogLevel);
		LOGGER.info(" ===========>  stage={}", stage);
		LOGGER.info(" ===========>  quarkus.http.cors.origins={}", corsAllowedOrigins);
		LOGGER.info(" ===========>  mkGatewayRoute={}", mkGatewayRoute);
		LOGGER.info(" ===========>  jdbcURL={}", jdbcURL);
		LOGGER.info(" ===========>  tempPwdUrl={}", tempPwdUrl);
		LOGGER.info(" ===========>  accountActivationUrl={}", accountActivationUrl);
		LOGGER.info(" ===========>  quarkus.http.cors.origins={}", corsAllowedOrigins);
		LOGGER.info(" ===========>  port={}", port);

	}

}
