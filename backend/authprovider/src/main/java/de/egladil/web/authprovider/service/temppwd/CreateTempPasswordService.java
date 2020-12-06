// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.service.temppwd;

import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.authprovider.config.PasswordConfig;
import de.egladil.web.authprovider.dao.ResourceOwnerDao;
import de.egladil.web.authprovider.dao.TempPasswordDao;
import de.egladil.web.authprovider.domain.Client;
import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.authprovider.domain.TempPassword;
import de.egladil.web.authprovider.error.AccountDeactivatedException;
import de.egladil.web.authprovider.utils.AuthUtils;
import de.egladil.web.commons_crypto.CryptoService;
import de.egladil.web.commons_net.time.CommonTimeUtils;
import de.egladil.web.commons_net.time.TimeInterval;

/**
 * TempPasswordService
 */
@RequestScoped
public class CreateTempPasswordService {

	private static final Logger LOG = LoggerFactory.getLogger(CreateTempPasswordService.class);

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	@ConfigProperty(name = "tempPasswordExpireMinutes", defaultValue = "30")
	int tempPasswordExpireMinutes;

	@Inject
	TempPasswordDao tempPasswordDao;

	@Inject
	ResourceOwnerDao ressourceOwnerDao;

	@Inject
	TempPasswordMailService mailService;

	@Inject
	CryptoService cryptoService;

	@Inject
	PasswordConfig passwordConfig;

	/**
	 * Erzeugt eine TempPassword-Entity mit einem expireDate und versendet asynchron eine Mail an die gegebene
	 * Mailadresse, sofern diese gültig ist.
	 *
	 * @param  payload
	 *                           OrderTempPasswordPayload
	 * @throws NotFoundException
	 */
	public void orderTempPassword(final String email, final Client client) throws NotFoundException {

		if (email == null) {

			throw new IllegalArgumentException("email null");
		}

		Optional<ResourceOwner> optUser = ressourceOwnerDao.findByEmail(email);

		if (optUser.isEmpty()) {

			LOG.warn("Anforderung temporäres Passwort ungekannte Mailadresse '{}'", email);
			throw new AccountDeactivatedException(applicationMessages.getString("CreateTempPwd.unknownOrDeactivated"));
		}

		ResourceOwner resourceOwner = optUser.get();

		if (!resourceOwner.isAktiviert()) {

			LOG.warn("Konto {} noch nicht aktiviert", resourceOwner);

			throw new AccountDeactivatedException(applicationMessages.getString("CreateTempPwd.unknownOrDeactivated"));
		}

		String password = cryptoService.generateRandomString(passwordConfig.getRandomAlgorithm(),
			passwordConfig.getTempPwdLength(), passwordConfig.getTempPwdCharPool().toCharArray());

		String tokenId = AuthUtils.newTokenId();

		int expirationMinutes = Integer.valueOf(tempPasswordExpireMinutes);
		TimeInterval timeInterval = CommonTimeUtils.getInterval(CommonTimeUtils.now(), expirationMinutes,
			ChronoUnit.MINUTES);

		TempPassword tempPassword = new TempPassword();
		tempPassword.setExpiresAt(timeInterval.getEndTime());
		tempPassword.setPassword(password);
		tempPassword.setTokenId(tokenId);
		tempPassword.setResourceOwner(optUser.get());
		tempPassword.setClient(client);

		TempPassword persisted = tempPasswordDao.save(tempPassword);
		mailService.versendeTempPasswordMail(email, persisted);

		LOG.info("temp password ordered for {}", optUser.get());
	}

}
