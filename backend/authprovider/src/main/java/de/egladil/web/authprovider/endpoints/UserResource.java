// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.endpoints;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.domain.Client;
import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.authprovider.error.AuthException;
import de.egladil.web.authprovider.error.ClientAccessTokenNotFoundException;
import de.egladil.web.authprovider.error.LogmessagePrefixes;
import de.egladil.web.authprovider.payload.ResourceOwnerResponse;
import de.egladil.web.authprovider.payload.ResourceOwnerResponseItem;
import de.egladil.web.authprovider.payload.SignUpCredentials;
import de.egladil.web.authprovider.payload.SignUpLogInResponseData;
import de.egladil.web.authprovider.payload.User;
import de.egladil.web.authprovider.payload.UserQueryParametersPayload;
import de.egladil.web.authprovider.payload.UuidPayloadList;
import de.egladil.web.authprovider.service.AuthorizationService;
import de.egladil.web.authprovider.service.ClientService;
import de.egladil.web.authprovider.service.RegistrationService;
import de.egladil.web.authprovider.service.ResourceOwnerService;
import de.egladil.web.commons_mailer.exception.InvalidMailAddressException;
import de.egladil.web.commons_validation.InvalidProperty;
import de.egladil.web.commons_validation.ValidationDelegate;
import de.egladil.web.commons_validation.exception.InvalidInputException;
import de.egladil.web.commons_validation.payload.MessagePayload;
import de.egladil.web.commons_validation.payload.OAuthClientCredentials;
import de.egladil.web.commons_validation.payload.ResponsePayload;

/**
 * UserResource stellt REST-Endpoints für die Verwaltung von ResourceOwnern nur Verfügung.
 */
@RequestScoped
@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

	private static final Logger LOG = LoggerFactory.getLogger(UserResource.class);

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	private final ValidationDelegate validationDelegate = new ValidationDelegate();

	@ConfigProperty(name = "admin.clientIds")
	String adminClientIds;

	@Inject
	ClientService clientService;

	@Inject
	RegistrationService registrationService;

	@Inject
	ResourceOwnerService resourceOwnerService;

	@Inject
	AuthorizationService authorizationService;

	@Context
	SecurityContext securityContext;

	/**
	 * Gibt den User zurück. Die UUID ist im securityContext.
	 *
	 * @param  crc
	 * @param  userId
	 * @return
	 */
	@GET
	@Path("/user")
	@RolesAllowed({ "STANDARD" })
	public Response getUserProfile() {

		Principal principal = securityContext.getUserPrincipal();

		Optional<ResourceOwner> optRO = this.resourceOwnerService.findByIdentifier(principal.getName());

		if (optRO.isPresent()) {

			User user = User.fromResourceOwner(optRO.get());
			ResponsePayload responsePayload = new ResponsePayload(MessagePayload.info("OK"), user);

			return Response.ok(responsePayload).build();
		}

		throw new NotFoundException();
	}

	/**
	 * Endpoint, der einen neuen ResourceOwner anlegt. Der Response enthält im body ein JWT.
	 *
	 * @param  signUpCredentials
	 *                           RegistrationCredentials
	 * @return                   Response
	 */
	@POST
	@Path("/signup")
	public Response signUp(final SignUpCredentials signUpCredentials, @Context final UriInfo uriInfo) {

		try {

			this.validationDelegate.check(signUpCredentials, SignUpCredentials.class);

			if (signUpCredentials.getGroups() != null && signUpCredentials.getGroups().toUpperCase().contains("ADMIN")) {

				LOG.warn(LogmessagePrefixes.BOT + "jemand hat versucht, sich zum ADMIN zu machen: email="
					+ signUpCredentials.getEmail());
				throw new SecurityException();
			}

			Client client = clientService.findAndCheckClient(signUpCredentials.getClientCredentials());

			ResourceOwner resourceOwner = registrationService.createNewResourceOwner(client, signUpCredentials, uriInfo);

			SignUpLogInResponseData data = authorizationService.createAndStoreAuthorization(resourceOwner,
				signUpCredentials.getClientCredentials(), signUpCredentials.getNonce());

			ResponsePayload responsePayload = new ResponsePayload(
				MessagePayload.info(applicationMessages.getString("Registration.success")), data);
			URI uri = new URI("https://mathe-jung-alt.de/auth-app/users/" + data.getIdToken());

			return Response.created(uri).entity(responsePayload).build();

		} catch (InvalidMailAddressException e) {

			this.resourceOwnerService.deleteResourceOwnerQuietly(signUpCredentials.getEmail());
			throw new InvalidInputException(
				ResponsePayload.messageOnly(MessagePayload.error(applicationMessages.getString("email.invalid"))));
		} catch (URISyntaxException | ClientAccessTokenNotFoundException e) {

			LOG.error(e.getMessage());
			return Response.serverError().build();
		} finally {

			signUpCredentials.clean();
		}
	}

	@POST
	@Path("/names")
	public Response getUserNames(final UuidPayloadList uuids) {

		OAuthClientCredentials clientCredentials = uuids.getClientCredentials();

		try {

			validationDelegate.check(uuids, UuidPayloadList.class);

			clientService.authorizeClient(clientCredentials);

			List<ResourceOwnerResponseItem> items = resourceOwnerService.getUserNames(uuids.getUuids());

			List<ResourceOwnerResponseItem> payload = items.stream().filter(i -> i.isAktiviert()).collect(Collectors.toList());

			ResponsePayload responsePayload = new ResponsePayload(MessagePayload.info("ok"),
				new ResourceOwnerResponse(clientCredentials.getNonce(), payload));

			return Response.ok(responsePayload).build();
		} finally {

			if (clientCredentials != null) {

				clientCredentials.clean();
			}
		}
	}

	@POST
	@Path("/details")
	public Response getGeschuetzteKontodatenByQuery(final UserQueryParametersPayload requestPayload) {

		OAuthClientCredentials clientCredentials = requestPayload.getClientCredentials();

		try {

			validationDelegate.check(requestPayload, UserQueryParametersPayload.class);

			// Hier noch Whitelist-Validation für die 4 verschiedenen queries

			String adminUuid = requestPayload.getAdminUUID();

			if (StringUtils.isBlank(adminUuid)) {

				LOG.warn(LogmessagePrefixes.BOT + "Zugriff ohne adminUUID");
				throw new AuthException("Zugriff ohne adminUUID");
			}

			clientService.authorizeClient(clientCredentials);

			Optional<String> optAdminClientId = Arrays.stream(adminClientIds.split(","))
				.filter(cid -> clientCredentials.getClientId().equals(cid)).findFirst();

			if (optAdminClientId.isEmpty()) {

				LOG.warn("CLIENT mit clientId={} darf keine Benutzerdetails holen",
					StringUtils.abbreviate(clientCredentials.getClientId(), 11));
				throw new AuthException("CLIENT mit dieser CLIENT_ID darf keine Benutzerdetails holen");
			}

			Optional<ResourceOwner> optAdmin = resourceOwnerService.findByUUID(adminUuid);

			if (!optAdmin.isPresent()) {

				LOG.warn(LogmessagePrefixes.BOT + "kein USER mit UUID={} bekannt", StringUtils.abbreviate(adminUuid, 11));
				throw new AuthException("kein USER mit der UUID bekannt");
			}

			ResourceOwner admin = optAdmin.get();
			Optional<String> optAdminRole = Arrays.stream(StringUtils.split(admin.getRoles(), ","))
				.filter(r -> "ADMIN".equalsIgnoreCase(r)).findFirst();

			if (!optAdminRole.isPresent()) {

				LOG.warn(LogmessagePrefixes.BOT + "USER mit UUID={} ist kein ADMIN", StringUtils.abbreviate(adminUuid, 11));
				throw new AuthException("USER mit dieser UUID ist kein ADMIN");
			}

			List<ResourceOwnerResponseItem> responseItems = resourceOwnerService.findByFilterLike(requestPayload.getParameterTyp(),
				requestPayload.getQuery());

			List<ResourceOwnerResponseItem> trefferliste = responseItems.stream().filter(i -> i.isExistend())
				.collect(Collectors.toList());

			ResponsePayload responsePayload = new ResponsePayload(MessagePayload.info("OK"),
				new ResourceOwnerResponse(clientCredentials.getNonce(), trefferliste));

			return Response.ok(responsePayload).build();
		} finally {

			clientCredentials.clean();
		}
	}

	@POST
	@Path("/user/details")
	public Response getOwnKontodaten(final UuidPayloadList requestPayload) {

		OAuthClientCredentials clientCredentials = requestPayload.getClientCredentials();

		try {

			validationDelegate.check(requestPayload, UuidPayloadList.class);

			if (requestPayload.getUuids().size() != 1) {

				ResponsePayload payload = new ResponsePayload(MessagePayload.error("Die Eingaben sind nicht korrekt."),
					Arrays.asList(new InvalidProperty[] { new InvalidProperty("uuid", "size != 1", 0) }));

				throw new InvalidInputException(payload);
			}

			String adminUuid = requestPayload.getAdminUUID();

			if (StringUtils.isBlank(adminUuid)) {

				LOG.warn(LogmessagePrefixes.BOT + "Zugriff ohne adminUUID");
				throw new AuthException("Zugriff ohne adminUUID");
			}

			clientService.authorizeClient(clientCredentials);

			Optional<String> optAdminClientId = Arrays.stream(adminClientIds.split(","))
				.filter(cid -> clientCredentials.getClientId().equals(cid)).findFirst();

			if (optAdminClientId.isEmpty()) {

				LOG.warn("CLIENT mit clientId={} darf keine Benutzerdetails holen",
					StringUtils.abbreviate(clientCredentials.getClientId(), 11));
				throw new AuthException("CLIENT mit dieser CLIENT_ID darf keine Benutzerdetails holen");
			}

			Optional<ResourceOwner> optAdmin = resourceOwnerService.findByUUID(adminUuid);

			if (!optAdmin.isPresent()) {

				LOG.warn(LogmessagePrefixes.BOT + "kein USER mit UUID={} bekannt", StringUtils.abbreviate(adminUuid, 11));
				throw new AuthException("kein USER mit der UUID bekannt");
			}

			ResourceOwner admin = optAdmin.get();
			Optional<String> optAdminRole = Arrays.stream(StringUtils.split(admin.getRoles(), ","))
				.filter(r -> "ADMIN".equalsIgnoreCase(r)).findFirst();

			if (!optAdminRole.isPresent()) {

				LOG.warn(LogmessagePrefixes.BOT + "USER mit UUID={} ist kein ADMIN", StringUtils.abbreviate(adminUuid, 11));
				throw new AuthException("USER mit dieser UUID ist kein ADMIN");
			}

			final String uuid = requestPayload.getUuids().get(0);
			Optional<ResourceOwner> optRO = this.resourceOwnerService.findByUUID(uuid);

			if (optRO.isPresent()) {

				ResourceOwner resourceOwner = optRO.get();

				ResourceOwnerResponseItem responseItem = ResourceOwnerResponseItem.create(uuid, true, resourceOwner.getVorname(),
					resourceOwner.getNachname(),
					resourceOwner.getFullName(), resourceOwner.getLoginName(), resourceOwner.getEmail(),
					resourceOwner.isAktiviert(), resourceOwner.getRoles());

				ResourceOwnerResponse response = new ResourceOwnerResponse(clientCredentials.getNonce(),
					Arrays.asList(new ResourceOwnerResponseItem[] { responseItem }));

				ResponsePayload responsePayload = new ResponsePayload(MessagePayload.info("OK"), response);

				return Response.ok(responsePayload).build();
			}

			LOG.warn("ResourceOwner mit UUID {} nicht vorhanden", StringUtils.abbreviate(uuid, 11));
			return Response.status(404)
				.entity(ResponsePayload.messageOnly(MessagePayload.error("Die angefragte Resource ist nicht vorhanden"))).build();
		} finally {

			clientCredentials.clean();
		}
	}
}
