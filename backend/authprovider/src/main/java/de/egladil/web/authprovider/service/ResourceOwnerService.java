// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.crypto.AuthCryptoService;
import de.egladil.web.authprovider.dao.ResourceOwnerDao;
import de.egladil.web.authprovider.domain.LoginSecrets;
import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.authprovider.domain.Salt;
import de.egladil.web.authprovider.error.AuthPersistenceException;
import de.egladil.web.authprovider.error.ConcurrentUpdateException;
import de.egladil.web.authprovider.error.DuplicateEntityException;
import de.egladil.web.authprovider.payload.BenutzerSuchmodus;
import de.egladil.web.authprovider.payload.ResourceOwnerResponseItem;
import de.egladil.web.authprovider.payload.ResourceOwnerResponseItemBuilder;
import de.egladil.web.authprovider.payload.SignUpCredentials;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

/**
 * ResourceOwnerService
 */
@RequestScoped
public class ResourceOwnerService {

	/**
	 *
	 */
	private static final String ROLE_STANDARD = "STANDARD";

	private Logger LOG = LoggerFactory.getLogger(ResourceOwnerService.class);

	@Inject
	ResourceOwnerDao resourceOwnerDao;

	@Inject
	AuthCryptoService authCryptoService;

	/**
	 * Erzeugt eine Instanz von ResourceOwnerService
	 */
	public ResourceOwnerService() {

	}

	/**
	 * Erzeugt eine Instanz von ResourceOwnerService für Tests ohne CDI.
	 */
	public ResourceOwnerService(final ResourceOwnerDao resourceOwnerDao, final AuthCryptoService cryptoService) {

		this.resourceOwnerDao = resourceOwnerDao;
		this.authCryptoService = cryptoService;
	}

	/**
	 * Erzeugt einen neuen ResourceOwner und gibt diesen zurück.<br>
	 * <br>
	 * <strong>Achtung: </strong> Nach dem Aufruf sind die Passwörter entfernt.
	 *
	 * @param  credentials
	 *                     SignUpCredentials
	 * @return             ResourceOwner
	 */
	public ResourceOwner createNewResourceOwner(final SignUpCredentials credentials) {

		ResourceOwner resourceOwner = createResourceOwner(credentials);

		try {

			LoginSecrets loginSecrets = createLoginSecrets(credentials);
			resourceOwner.setLoginSecrets(loginSecrets);
			ResourceOwner persistentResourceOwner = resourceOwnerDao.save(resourceOwner);
			return persistentResourceOwner;

		} catch (ConcurrentUpdateException e) {

			LOG.error("ConcurrentUpdateException beim Speichern eines neuen Benutzerkontos: {}", resourceOwner);
			throw new AuthPersistenceException("Fehler beim Anlegen eines ResourceOwners: " + e.getMessage(), e);
		}
	}

	/**
	 * Löscht alle Benutzerkonten mit den gegebenen UUIDs aus der Datenbank
	 *
	 * @param  uuids
	 *               String
	 * @return       int Anzahl der tatsächlich gelöschten Accounts.
	 */
	public int deleteResourceOwnersQuietly(final List<String> uuids) {

		int anzahlGeloescht = 0;

		for (String uuid : uuids) {

			Optional<ResourceOwner> optRO = this.resourceOwnerDao.findByUUID(uuid);

			if (optRO.isPresent()) {

				boolean deleted = deleteResourceOwner(optRO.get());

				if (deleted) {

					anzahlGeloescht++;
				}
			}
		}

		return anzahlGeloescht;

	}

	/**
	 * Löscht das Benutzerkonto mit der gegeben Mailadresse in der Datenbank.
	 *
	 * @param email
	 */
	public void deleteResourceOwnerQuietly(final String email) {

		Optional<ResourceOwner> opt = this.resourceOwnerDao.findByEmail(email);

		if (!opt.isPresent()) {

			return;
		}

		ResourceOwner resourceOwner = opt.get();
		deleteResourceOwner(resourceOwner);
	}

	/**
	 * Löscht alle Daten in der Datenbank.
	 *
	 * @param  resourceOwner
	 *                       ResourceOwner
	 * @return               boolean ob gelöscht oder nicht.
	 */

	@Transactional(value = TxType.REQUIRED)
	public boolean deleteResourceOwner(final ResourceOwner resourceOwner) {

		try {

			LoginSecrets loginSecrets = resourceOwner.getLoginSecrets();
			Salt salt = loginSecrets.getSalt();

			boolean roGeloescht = this.resourceOwnerDao.delete(resourceOwner);
			this.resourceOwnerDao.delete(loginSecrets);
			this.resourceOwnerDao.delete(salt);

			LOG.info("User mit uuid='{}' gelöscht", resourceOwner.getUuid());
			return roGeloescht;

		} catch (Exception e) {

			LOG.error("ResourceOwner mit email='{}' konnte nicht gelöscht werden: {}", resourceOwner.getEmail(),
				e.getMessage());
			return false;
		}
	}

	/**
	 * Erhöht die Anzahl der Logins und speichert das. Eine PersistenceException wird nur geloggt, da das fürs Login
	 * nicht unbedingt erforderlich ist.
	 *
	 * @param  resourceOwner
	 *                       ResourceOwner
	 * @return               ResourceOwner
	 */
	public ResourceOwner erfolgreichesLoginSpeichern(final ResourceOwner resourceOwner) {

		int anzahlLogins = resourceOwner.getAnzahlLogins();
		resourceOwner.setAnzahlLogins(++anzahlLogins);
		resourceOwner.setDatumGeaendert(new Date());

		try {

			ResourceOwner persisted = resourceOwnerDao.save(resourceOwner);
			return persisted;
		} catch (Exception e) {

			LOG.error("Konnte erfolgreiches Login von {} nicht speichern: {}", resourceOwner, e.getMessage(), e);
			return resourceOwner;
		}

	}

	/**
	 * Sucht zuerst nach dem Loginnamen und anchsließend nach email.
	 *
	 * @param  identifier
	 *                    String email oder loginName
	 * @return            Optional
	 */
	public Optional<ResourceOwner> findByIdentifier(final String identifier) {

		Optional<ResourceOwner> opt = this.findResourceOwnerByLoginName(identifier);

		if (opt.isPresent()) {

			return opt;
		}

		return this.findResourceOwnerByEmail(identifier);

	}

	public Optional<ResourceOwner> findResourceOwnerByEmail(final String email) {

		return this.resourceOwnerDao.findByEmail(email);
	}

	public Optional<ResourceOwner> findResourceOwnerByLoginName(final String loginName) {

		return this.resourceOwnerDao.findByLoginName(loginName);
	}

	/**
	 * Prüft, ob es die Mailadresse, den LoginNamen bereits gibt und beides zusammenpasst. Falls der gegebene LoginName
	 * nicht zur mailadresse passt, wird eine Warnung geloggt. Falls es einen ResourceOwner gibt, muss noch geprüft
	 * werden, ob er aktiviert oder deaktiviert ist?
	 *
	 * @param  loginName
	 *                   String
	 * @param  email
	 *                   String
	 * @return           Optional
	 */
	public Optional<ResourceOwner> checkExiststAndIsConsistent(final String loginName, final String email) {

		Optional<ResourceOwner> optROEmail = findResourceOwnerByEmail(email);
		Optional<ResourceOwner> optROLoginname = findResourceOwnerByLoginName(loginName);

		if (!optROEmail.isPresent() && !optROLoginname.isPresent()) {

			return Optional.empty();
		}

		ResourceOwner roEmail = null;
		ResourceOwner roLoginname = null;

		if (optROEmail.isPresent() && !optROLoginname.isPresent()) {

			roEmail = optROEmail.get();

			if (!roEmail.getEmail().equals(roEmail.getLoginName())) {

				// wäre passender User, aber es muss ein anderer sein, da bei Fehlen einer Mailadresse der Loginname
				// gleich der Mailadresse ist.
				throw new DuplicateEntityException("Diese Mailadresse gibt es schon.");
			}
			return optROEmail;
		}

		if (!optROEmail.isPresent() && optROLoginname.isPresent()) {

			throw new DuplicateEntityException("Diesen Loginnamen gibt es schon.");
		}

		roEmail = optROEmail.get();
		roLoginname = optROLoginname.get();

		if (!roEmail.equals(roLoginname)) {

			throw new DuplicateEntityException("Diesen Loginnamen und diese Mailadresse gibt es schon.");
		}

		return optROEmail;
	}

	/**
	 * Prüft, ob Änderungen mit den als eindeutig erforderlichen Attributen möglich sind.
	 *
	 * @param  loginName
	 *                   String der neue loginName
	 * @param  email
	 *                   String die neue Mailadresse
	 * @param  uuid
	 *                   String die UUID des ReourceOwners, der seine Daten ändern möchte.
	 * @return           String key zur Message in ApplicationMessages oder null
	 */
	public String changeLoginNameAndEmailAllowed(final String loginName, final String email, final String uuid) {

		List<ResourceOwner> gleicherLoginName = resourceOwnerDao.findOtherUsersWithSameLoginName(loginName, uuid);
		List<ResourceOwner> gleicheEmail = new ArrayList<>();

		if (!loginName.equalsIgnoreCase(email)) {

			gleicheEmail = resourceOwnerDao.findOtherUsersWithSameEmail(email, uuid);
		}

		if (gleicherLoginName.size() > 0 && gleicheEmail.size() > 0) {

			return "ProfileResource.data.duplicate.emailAndLoginName";
		}

		if (gleicherLoginName.size() > 0) {

			return "ProfileResource.data.duplicate.loginName";
		}

		if (gleicheEmail.size() > 0) {

			return "ProfileResource.data.duplicate.email";
		}

		return null;
	}

	ResourceOwner createResourceOwner(final SignUpCredentials credentials) throws SecurityException {

		ResourceOwner resourceOwner = new ResourceOwner();
		resourceOwner.setEmail(credentials.getEmail());

		String loginName = StringUtils.isBlank(credentials.getLoginName()) ? credentials.getEmail() : credentials.getLoginName();
		resourceOwner.setLoginName(loginName);
		resourceOwner.setUuid(UUID.randomUUID().toString());
		resourceOwner.setVorname(credentials.getVorname());
		resourceOwner.setNachname(credentials.getNachname());

		String normalizedRoles = ROLE_STANDARD;

		resourceOwner.setRoles(normalizedRoles);
		return resourceOwner;
	}

	LoginSecrets createLoginSecrets(final SignUpCredentials credentials) {

		Hash hash = authCryptoService.hashPassword(credentials.getZweiPassworte().getPasswort().toCharArray());
		LoginSecrets loginSecrets = new LoginSecrets();
		loginSecrets.setPasswordhash(Base64.getEncoder().encodeToString(hash.getBytes()));

		Salt salt = new Salt();
		salt.setAlgorithmName(hash.getAlgorithmName());
		salt.setIterations(hash.getIterations());
		salt.setWert(hash.getSalt().toBase64());

		loginSecrets.setSalt(salt);

		return loginSecrets;
	}

	/**
	 * Sucht den Eintrag in USERS mit der gegebenen UUID.
	 *
	 * @param  uuid
	 *              String
	 * @return      Optional
	 */
	public Optional<ResourceOwner> findByUUID(final String uuid) {

		return resourceOwnerDao.findByUUID(uuid);
	}

	/**
	 * Sucht alle ResourceOwner, auf die der Filter zutrifft.
	 *
	 * @param  queryTyp
	 * @param  query
	 * @return
	 */
	public List<ResourceOwnerResponseItem> findByFilterLike(final BenutzerSuchmodus queryTyp, final String query) {

		List<ResourceOwner> trefferliste = null;

		switch (queryTyp) {

		case EMAIL:
			trefferliste = resourceOwnerDao.findByEmailLike(query);
			break;

		case LOGINNAME:
			trefferliste = resourceOwnerDao.findByLoginnameLike(query);
			break;

		case NAME:
			trefferliste = resourceOwnerDao.findByNamenLike(query);
			break;

		case UUID:
			trefferliste = resourceOwnerDao.findByUuidLike(query);
			break;

		default:
			throw new IllegalArgumentException("Unbekannter BenutzerSuchmodus " + queryTyp);
		}

		final List<ResourceOwnerResponseItem> result = new ArrayList<>();

		trefferliste.forEach(ro -> {

			ResourceOwnerResponseItem item = null;
			item = new ResourceOwnerResponseItemBuilder(ro.getUuid(), true, ro.getFullName(), ro.isAktiviert())
				.withEmail(ro.getEmail())
				.withLoginName(ro.getLoginName())
				.withVorname(ro.getVorname())
				.withNachname(ro.getNachname())
				.withRoles(ro.getRoles()).build();
			result.add(item);
		});

		return result;
	}

	/**
	 * Läd die Namen der gegebenen User. Wenn es den User mit derUUID nicht gibt oder er anonymisiert oder nicht aktiviert ist, ist
	 * fullName null.
	 *
	 * @param  uuidPayload
	 *                     UuidPayloadList
	 * @return             List
	 */
	public List<ResourceOwnerResponseItem> getUserNames(final List<String> uuids) {

		final List<ResourceOwnerResponseItem> result = new ArrayList<>();

		uuids.forEach(uuid -> {

			ResourceOwnerResponseItem item = null;
			Optional<ResourceOwner> optRo = resourceOwnerDao.findByUUID(uuid);

			if (optRo.isPresent()) {

				ResourceOwner ro = optRo.get();

				item = new ResourceOwnerResponseItemBuilder(uuid, true, ro.getFullName(), ro.isAktiviert()).build();
			} else {

				item = new ResourceOwnerResponseItemBuilder(uuid, false, null, false).build();
			}
			result.add(item);
		});

		return result;
	}
}
