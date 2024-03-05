// =====================================================
// Project: mk-gateway
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_server.cdi;

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

	@ConfigProperty(name = "quarkus.http.cors.origins", defaultValue = "")
	String corsAllowedOrigins;

	@ConfigProperty(name = "de.egladil.web.profil_server.restclient.MkGatewayRestClient/mp-rest/url")
	String mkGatewayRoute;

	@ConfigProperty(name = "de.egladil.web.profil_server.restclient.InitAccessTokenRestClient/mp-rest/url")
	String accessTokenRoute;

	@ConfigProperty(name = "de.egladil.web.profil_server.restclient.TokenExchangeRestClient/mp-rest/url")
	String tokenExchangeRoute;

	@ConfigProperty(name = "de.egladil.web.profil_server.restclient.ProfileRestClient/mp-rest/url")
	String profileRoute;

	void onStartup(@Observes final StartupEvent ev) {

		LOGGER.info(" ===========>  quarkus.http.cors.origins={}", corsAllowedOrigins);
		LOGGER.info(" ===========>  mkGatewayRoute={}", mkGatewayRoute);
		LOGGER.info(" ===========>  accessTokenRoute={}", accessTokenRoute);
		LOGGER.info(" ===========>  tokenExchangeRoute={}", tokenExchangeRoute);
		LOGGER.info(" ===========>  profileRoute={}", profileRoute);
	}

}
