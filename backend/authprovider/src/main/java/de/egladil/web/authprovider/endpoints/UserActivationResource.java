// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.endpoints;

import java.net.URI;
import java.net.URISyntaxException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.error.LogmessagePrefixes;
import de.egladil.web.authprovider.payload.UuidPayload;
import de.egladil.web.authprovider.service.confirm.ConfirmationService;
import de.egladil.web.authprovider.service.confirm.ConfirmationStatus;
import de.egladil.web.commons_validation.ValidationDelegate;
import de.egladil.web.commons_validation.exception.InvalidInputException;

/**
 * UserActivationResource stellt REST- Endpoints Aktivieren eines neu angelegten ResourceOwner-Kontos zur Verfügung.
 */
@RequestScoped
@Path("/registration")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.TEXT_HTML)
public class UserActivationResource {

	private static final Logger LOG = LoggerFactory.getLogger(UserActivationResource.class);

	private static final String CONTENT_ROOT = "https://mathe-jung-alt.de/benutzer/";

	private static final String ACTIVATION_EXPIRED_RESOURCE = CONTENT_ROOT + "activationExpired.html";

	private static final String ACTIVATION_SUCCESS_RESOURCE = CONTENT_ROOT + "activationSuccess.html";

	private static final String ACTIVATION_FAILED_RESOURCE = CONTENT_ROOT + "activationFailed.html";

	private static final String ACTIVATION_BAD_REQUEST_RESOURCE = CONTENT_ROOT + "activationBadRequest.html";

	private static final String ACTIVATION_DELETED_RESOURCE = CONTENT_ROOT + "activationDeleted.html";

	private ValidationDelegate validationDelegate = new ValidationDelegate();

	@Inject
	ConfirmationService confirmationService;

	/**
	 * Aktiviert das Benutzerkonto anhand eines Confirmation-Codes.
	 *
	 * @return
	 */
	@GET
	@Path("/confirmation")
	public Response activateUser(@QueryParam("code") final String confirmationCode) {

		String htmlResource = "";

		try {

			validationDelegate.check(new UuidPayload(confirmationCode), UuidPayload.class);

			ConfirmationStatus confirmStatus = this.confirmationService.confirmCode(confirmationCode);

			switch (confirmStatus) {

			case deletedActivation:
				htmlResource = ACTIVATION_DELETED_RESOURCE;
				break;

			case normalActivation:
			case repeatedActivation:
				htmlResource = ACTIVATION_SUCCESS_RESOURCE;
				break;

			case expiredActivation:
				htmlResource = ACTIVATION_EXPIRED_RESOURCE;
				break;

			default:
				LOG.error(LogmessagePrefixes.IMPOSSIBLE + "unerwarteter confirmStatus '" + confirmStatus + "'");
				htmlResource = ACTIVATION_FAILED_RESOURCE;
			}
		} catch (InvalidInputException e) {

			LOG.warn(LogmessagePrefixes.BOT + "confirmationCode ungueltig - [{}]", confirmationCode);
			htmlResource = ACTIVATION_BAD_REQUEST_RESOURCE;
		} catch (Exception e) {

			LOG.error(
				"Unerwartete Exception beim Aktivieren des Benutzerkontos mit confirmationCode '" + confirmationCode + "'': "
					+ e.getMessage(),
				e);
			htmlResource = ACTIVATION_FAILED_RESOURCE;
		}

		try {

			return Response.temporaryRedirect(new URI(htmlResource)).build();
		} catch (URISyntaxException e) {

			LOG.error(e.getMessage(), e);
			return Response.serverError()
				.entity("Leider ist ein Fehler aufgetreten. Bitte senden Sie eine Mail an info@egladil.de").build();
		}
	}
}
