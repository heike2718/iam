// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.service.confirm;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.egladil.web.authprovider.dao.ActivationCodeDao;
import de.egladil.web.authprovider.dao.ResourceOwnerDao;
import de.egladil.web.authprovider.domain.ActivationCode;
import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.authprovider.event.AuthproviderEvent;
import de.egladil.web.authprovider.event.AuthproviderEventType;
import de.egladil.web.authprovider.service.ResourceOwnerService;
import de.egladil.web.commons_crypto.CryptoService;
import de.egladil.web.commons_net.time.CommonTimeUtils;

/**
 * ConfirmationServiceImplTest
 */
public class ConfirmationServiceImplTest {

	private ActivationCodeDao activationCodeDao;

	private ResourceOwnerDao resourceOwnerDao;

	private ResourceOwnerService resourceOwnerService;

	private CryptoService cryptoService;

	private ConfirmationServiceImpl service;

	@BeforeEach
	void setUp() {

		activationCodeDao = Mockito.mock(ActivationCodeDao.class);
		resourceOwnerDao = Mockito.mock(ResourceOwnerDao.class);
		resourceOwnerService = Mockito.mock(ResourceOwnerService.class);
		cryptoService = Mockito.mock(CryptoService.class);

		service = ConfirmationServiceImpl.createForTest(activationCodeDao, resourceOwnerDao, resourceOwnerService, cryptoService);
	}

	@Test
	void should_writeToEventLog_when_activationExpired() {

		// Arrange
		String confirmCode = "jkasgdk";
		String uuid = "ahkgd-eufwhgo";

		ActivationCode activationCode = new ActivationCode();
		activationCode.setConfirmationCode(confirmCode);
		activationCode.setConfirmed(false);
		activationCode.setExpirationTime(CommonTimeUtils.transformFromLocalDateTime(LocalDateTime.now().plusHours(-1)));

		ResourceOwner resourceOwner = new ResourceOwner();
		resourceOwner.setAktiviert(false);
		resourceOwner.setAnonym(false);
		resourceOwner.setEmail("mail@web.de");
		resourceOwner.setLoginName("mail@web.de");
		resourceOwner.setNachname("Log");
		resourceOwner.setRoles("STANDARD");
		resourceOwner.setUuid(uuid);
		resourceOwner.setVorname("Anna");

		activationCode.setResourceOwner(resourceOwner);

		Mockito.when(activationCodeDao.findByConfirmationCode(confirmCode)).thenReturn(Optional.of(activationCode));

		// Act
		ConfirmationStatus status = service.confirmCode(confirmCode);

		// Assert
		assertEquals(ConfirmationStatus.expiredActivation, status);
		AuthproviderEvent eventPayload = service.eventPayload();
		assertEquals(AuthproviderEventType.REGISTRATION_CONFIRMATION_EXPIRED, eventPayload.eventType());

	}

}
