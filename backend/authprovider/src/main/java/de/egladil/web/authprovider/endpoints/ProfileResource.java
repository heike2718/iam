// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.endpoints;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.authprovider.error.AuthException;
import de.egladil.web.authprovider.payload.User;
import de.egladil.web.authprovider.payload.profile.ChangeProfileDataPayload;
import de.egladil.web.authprovider.payload.profile.ChangeProfilePasswordPayload;
import de.egladil.web.authprovider.payload.profile.SelectProfilePayload;
import de.egladil.web.authprovider.service.ClientService;
import de.egladil.web.authprovider.service.ResourceOwnerService;
import de.egladil.web.authprovider.service.profile.ChangeDataService;
import de.egladil.web.authprovider.service.profile.ChangePasswordService;
import de.egladil.web.authprovider.service.profile.DeleteAccountService;
import de.egladil.web.commons_validation.ValidationDelegate;
import de.egladil.web.commons_validation.payload.MessagePayload;
import de.egladil.web.commons_validation.payload.NoncePayload;
import de.egladil.web.commons_validation.payload.OAuthClientCredentials;
import de.egladil.web.commons_validation.payload.ResponsePayload;

/**
 * ProfileResource ist der Endpoint, der vom profileservice verwendet wird, um die Daten eines Users zu ändern.
 */
@RequestScoped
@Path("/profiles")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProfileResource {

	private static final Logger LOG = LoggerFactory.getLogger(ProfileResource.class);

	@ConfigProperty(name = "profilapp.client-id")
	String permittedClientId;

	private ValidationDelegate validationDelegate = new ValidationDelegate();

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	@Inject
	ClientService clientService;

	@Inject
	ChangePasswordService changePasswordService;

	@Inject
	ResourceOwnerService resourceOwnerService;

	@Inject
	ChangeDataService changeDataService;

	@Inject
	DeleteAccountService deleteAccountService;

	/**
	 * @param  uuid
	 * @param  payload
	 * @return         Response mit data User
	 */
	@PUT
	@Path("/profile/password")
	public Response changePassword(final ChangeProfilePasswordPayload payload) {

		try {

			verifyClientId(payload.getClientCredentials());

			validationDelegate.check(payload, ChangeProfilePasswordPayload.class);

			clientService.authorizeClient(payload.getClientCredentials());

			ResponsePayload responsePayload = changePasswordService.changePassword(payload.getUuid(), payload.getPasswordPayload());

			if (responsePayload.isOk()) {

				responsePayload.setData(NoncePayload.create(payload.getClientCredentials().getNonce()));

				LOG.info("Passwort geändert für UUID={}", payload.getUuid().substring(0, 8));
				return Response.ok(responsePayload).build();
			}

			responsePayload.setData(NoncePayload.create(payload.getClientCredentials().getNonce()));
			return Response.status(412).entity(responsePayload).build();
		} finally {

			payload.clean();
		}
	}

	@PUT
	@Path("/profile/data")
	public Response changeData(final ChangeProfileDataPayload payload) {

		try {

			validationDelegate.check(payload, ChangeProfileDataPayload.class);

			verifyClientId(payload.getClientCredentials());

			clientService.authorizeClient(payload.getClientCredentials());

			User user = changeDataService.changeData(payload.getUuid(), payload.getProfileData());
			user.setNonce(payload.getClientCredentials().getNonce());

			ResponsePayload responsePayload = new ResponsePayload(
				MessagePayload.info(applicationMessages.getString("ProfileResource.data.success")), user);

			responsePayload.setData(user);

			return Response.ok(responsePayload).build();
		} finally {

			payload.clean();
		}
	}

	/**
	 * Gibt den User zurück. Die UUID ist im Container ist nicht mehr klar, was ich mit der USER_ID- Property im
	 * ContainerRequestContext vorhatte.
	 *
	 * @param  crc
	 * @param  userId
	 * @return
	 */
	@POST
	@Path("/profile")
	public Response getUserProfile(final SelectProfilePayload selectProfilePayload) {

		try {

			validationDelegate.check(selectProfilePayload, SelectProfilePayload.class);

			verifyClientId(selectProfilePayload.getClientCredentials());

			clientService.authorizeClient(selectProfilePayload.getClientCredentials());

			Optional<ResourceOwner> optRO = this.resourceOwnerService.findByUUID(selectProfilePayload.getUuid());

			if (optRO.isPresent()) {

				User user = User.fromResourceOwner(optRO.get());
				ResponsePayload responsePayload = new ResponsePayload(MessagePayload.info("OK"), user);

				return Response.ok(responsePayload).build();
			}

			throw new NotFoundException();
		} finally {

			selectProfilePayload.clean();
		}
	}

	/**
	 * Gibt den User zurück. Die UUID ist im Container ist nicht mehr klar, was ich mit der USER_ID- Property im
	 * ContainerRequestContext vorhatte.
	 *
	 * @param  crc
	 * @param  userId
	 * @return
	 */
	@DELETE
	@Path("/profile")
	public Response deleteUserProfile(final SelectProfilePayload selectProfilePayload) {

		try {

			validationDelegate.check(selectProfilePayload, SelectProfilePayload.class);

			verifyClientId(selectProfilePayload.getClientCredentials());

			clientService.authorizeClient(selectProfilePayload.getClientCredentials());

			return deleteAccountService.deleteAccount(selectProfilePayload);

		} finally {

			selectProfilePayload.clean();
		}
	}

	private void verifyClientId(final OAuthClientCredentials clientCredentials) {

		if (!permittedClientId.equals(clientCredentials.getClientId())) {

			LOG.warn("Nicht erlaubter Zugriff auf Profil-Resource mit ClientId "
				+ StringUtils.abbreviate(clientCredentials.getClientId(), 8));

			throw new AuthException("Keine Berechtigung für diese Resource");
		}
	}

}
