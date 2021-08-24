// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.endpoints;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.AuthProviderApp;
import de.egladil.web.authprovider.domain.AuthSession;
import de.egladil.web.authprovider.error.AuthException;
import de.egladil.web.authprovider.service.AuthproviderSessionService;
import de.egladil.web.commons_net.utils.CommonHttpUtils;
import de.egladil.web.commons_validation.payload.MessagePayload;
import de.egladil.web.commons_validation.payload.ResponsePayload;

/**
 * AnonymousSessionResource
 */
@RequestScoped
@Path("/session")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AnonymousSessionResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(AnonymousSessionResource.class);

	@ConfigProperty(name = "stage")
	String stage;

	@Inject
	AuthproviderSessionService sessionService;

	@GET
	public Response createSession() {

		AuthSession session = sessionService.createAnonymousSession();

		if (AuthProviderApp.STAGE_DEV.equals(stage)) {

			LOGGER.info("sessionId={}", session.getSessionId());
		}

		NewCookie sessionCookie = sessionService.createSessionCookie(session.getSessionId());

		if (!AuthProviderApp.STAGE_DEV.equals(stage)) {

			session.clearSessionId();
		}

		ResponsePayload responsePayload = new ResponsePayload(MessagePayload.info("OK"), session);

		return Response.ok(responsePayload).cookie(sessionCookie).build();
	}

	@DELETE
	@Path("/invalidate")
	public Response clearSession(@CookieParam(
		value = AuthProviderApp.CLIENT_COOKIE_PREFIX + CommonHttpUtils.NAME_SESSIONID_COOKIE) final String sessionId) {

		if (sessionId != null) {

			sessionService.invalidate(sessionId);
		}
		return Response.ok(ResponsePayload.messageOnly(MessagePayload.info("OK")))
			.cookie(CommonHttpUtils.createSessionInvalidatedCookie(AuthProviderApp.CLIENT_COOKIE_PREFIX)).build();

	}

	@DELETE
	@Path("/dev/invalidate/{sessionid}")
	public Response clearSessionDev(@PathParam(value = "sessionid") final String sessionId) {

		if (!AuthProviderApp.STAGE_DEV.equals(stage)) {

			throw new AuthException("Diese URL darf nur im DEV-Mode aufgerufen werden");
		}

		sessionService.invalidate(sessionId);
		return Response.ok(ResponsePayload.messageOnly(MessagePayload.info("OK"))).build();

	}

}
