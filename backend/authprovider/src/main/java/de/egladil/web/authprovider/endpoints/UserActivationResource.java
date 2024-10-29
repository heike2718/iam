// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.endpoints;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.auth_validations.annotations.UuidString;
import de.egladil.web.authprovider.error.LogmessagePrefixes;
import de.egladil.web.authprovider.service.confirm.ConfirmationService;
import de.egladil.web.authprovider.service.confirm.ConfirmationStatus;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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

	private static final String ACTIVATION_DELETED_RESOURCE = CONTENT_ROOT + "activationDeleted.html";

	@Inject
	ConfirmationService confirmationService;

	/**
	 * Aktiviert das Benutzerkonto anhand eines Confirmation-Codes.
	 *
	 * @return
	 */
	@GET
	@Path("/confirmation")
	public Response activateUser(@UuidString @QueryParam("code") final String confirmationCode) {

		String htmlResource = "";

		try {

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
