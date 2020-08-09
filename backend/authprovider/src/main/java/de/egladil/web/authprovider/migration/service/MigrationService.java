// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.migration.service;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.dao.ResourceOwnerDao;
import de.egladil.web.authprovider.domain.LoginSecrets;
import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.authprovider.domain.Salt;
import de.egladil.web.authprovider.migration.payload.AuthenticationPayload;
import de.egladil.web.commons_validation.payload.MessagePayload;
import de.egladil.web.commons_validation.payload.ResponsePayload;

/**
 * MigrationService
 */
@ApplicationScoped
public class MigrationService {

	private static final String ROLE_STANDARD = "STANDARD,";

	private static final Logger LOG = LoggerFactory.getLogger(MigrationService.class);

	@Inject
	ResourceOwnerDao resourceOwnerDao;

	/**
	 * Erzeugt die erforderlichen Einträge in der Datenbank.
	 *
	 * @param  payload
	 * @return
	 */
	public ResponsePayload importiereBenutzer(final AuthenticationPayload payload) {

		if (payload.isAnonym()) {

			LOG.info("{} ist anonym und wird nicht uebernommen.", payload);
			return ResponsePayload.messageOnly(MessagePayload.warn("Benutzer ist anonym und wird ignoriert"));
		}

		ResourceOwner resourceOwner = ResourceOwner.createAktiviert(payload.getUuid(), payload.getLoginname(), payload.getEmail());
		resourceOwner.setNachname(payload.getNachname());
		resourceOwner.setVorname(payload.getVorname());
		resourceOwner.setRoles(ROLE_STANDARD + payload.getRolle());

		LoginSecrets loginSecrets = createLoginSecrets(payload);
		resourceOwner.setLoginSecrets(loginSecrets);

		if (canImport(resourceOwner)) {

			try {

				resourceOwnerDao.save(resourceOwner);

				String msg = "Benutzer importiert: " + resourceOwner.toLogString();

				return ResponsePayload.messageOnly(MessagePayload.info(msg));
			} catch (PersistenceException e) {

				String msg = "Benutzer konnte nicht gespeichert werden: " + e.getMessage();
				LOG.error("{} konnte nicht gespeichert werden: {}", payload, e.getMessage(), e);
				return ResponsePayload.messageOnly(MessagePayload.error(msg));
			}
		} else {

			LOG.warn("{} konnte nicht importiert werden: UUID oder email oder loginname bereits vergeben.", payload);
			return ResponsePayload
				.messageOnly(MessagePayload.warn("Benutzer konnte nicht importiert werden: wurde übersprungen - siehe log."));
		}
	}

	/**
	 * @param  payload
	 * @return
	 */
	private LoginSecrets createLoginSecrets(final AuthenticationPayload payload) {

		LoginSecrets result = new LoginSecrets();
		result.setPasswordhash(payload.getPw());

		Salt salt = new Salt();
		salt.setAlgorithmName(payload.getAlg());
		salt.setIterations(payload.getRounds());
		salt.setWert(payload.getSlt());

		result.setSalt(salt);

		return result;
	}

	boolean canImport(final ResourceOwner resourceOwner) {

		Optional<ResourceOwner> optRO = resourceOwnerDao.findByUUID(resourceOwner.getUuid());

		if (optRO.isPresent()) {

			LOG.info("USERS enthält bereits einen Eintrag mit UUID='{}'", resourceOwner.getUuid());
			return false;
		}

		optRO = resourceOwnerDao.findByLoginName(resourceOwner.getEmail());

		if (optRO.isPresent()) {

			LOG.info("USERS enthält bereits einen Eintrag mit email='{}'", resourceOwner.getEmail());
			return false;
		}

		optRO = resourceOwnerDao.findByLoginName(resourceOwner.getLoginName());

		if (optRO.isPresent()) {

			LOG.info("USERS enthält bereits einen Eintrag mit loginName='{}'", resourceOwner.getLoginName());
			return false;
		}

		return true;
	}

}
