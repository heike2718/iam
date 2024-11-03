// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.service.profile;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.authprovider.payload.MessagePayload;
import de.egladil.web.authprovider.payload.ResponsePayload;
import de.egladil.web.authprovider.payload.profile.SelectProfilePayload;
import de.egladil.web.authprovider.service.AuthMailService;
import de.egladil.web.authprovider.service.ClientService;
import de.egladil.web.authprovider.service.ResourceOwnerService;
import de.egladil.web.authprovider.service.mail.BenutzerkontoGeloeschtMailStrategie;
import de.egladil.web.authprovider.service.mail.CreateDefaultMailDatenStrategy;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

/**
 * DeleteAccountService
 */
@RequestScoped
public class DeleteAccountService {

	private static final Logger LOG = LoggerFactory.getLogger(DeleteAccountService.class);

	@ConfigProperty(name = "account.deleted.subject")
	String mailSubject;

	@ConfigProperty(name = "account.deleted.to")
	String mailTo;

	@Inject
	ClientService clientService;

	@Inject
	ResourceOwnerService resourceOwnerService;

	@Inject
	AuthMailService mailService;

	public Response deleteAccount(final SelectProfilePayload selectProfilePayload) {

		Optional<ResourceOwner> optRO = this.resourceOwnerService.findByUUID(selectProfilePayload.getUuid());

		if (optRO.isPresent()) {

			ResourceOwner resourceOwner = optRO.get();
			resourceOwnerService.deleteResourceOwner(resourceOwner);

			sendInfoMailQuietly(resourceOwner);

			String nonce = selectProfilePayload.getClientCredentials().getNonce();

			LOG.info(">>>>> nonce={}", nonce);

			ResponsePayload responsePayload = new ResponsePayload(MessagePayload.info("Benutzerkonto gelöscht"),
				nonce);

			return Response.ok(responsePayload).build();
		}

		throw new NotFoundException();

	}

	private void sendInfoMailQuietly(final ResourceOwner resourceOwner) {

		try {

			CreateDefaultMailDatenStrategy mailStrategy = new BenutzerkontoGeloeschtMailStrategie(mailSubject, mailTo,
				resourceOwner);

			mailService.sendMail(mailStrategy.createEmailDaten(mailSubject));

		} catch (Exception e) {

			LOG.error("Infomail über Löschung des Benutzerkontos " + StringUtils.abbreviate(resourceOwner.getUuid(), 11)
				+ " konnte nicht versendet werden: " + e.getMessage());
		}
	}
}
