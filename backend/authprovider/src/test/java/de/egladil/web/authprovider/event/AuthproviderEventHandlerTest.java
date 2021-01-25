// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.authprovider.restclient.MkGatewayRestClient;
import de.egladil.web.authprovider.service.AuthMailService;
import de.egladil.web.authprovider.service.mail.MinikaengurukontenInfoStrategie;
import de.egladil.web.authprovider.service.mail.MinikaengurukontenInfoStrategie.MinikaengurukontenMailKontext;
import de.egladil.web.commons_mailer.DefaultEmailDaten;

/**
 * AuthproviderEventHandlerTest
 */
public class AuthproviderEventHandlerTest {

	private EventRepository eventRepository;

	private MkGatewayRestClient mkGateway;

	private AuthMailService mailService;

	private AuthproviderEventHandler handler;

	@BeforeEach
	void setUp() {

		eventRepository = Mockito.mock(EventRepository.class);
		mkGateway = Mockito.mock(MkGatewayRestClient.class);
		mailService = Mockito.mock(AuthMailService.class);

		handler = AuthproviderEventHandler.createForTest(eventRepository, mailService, mkGateway);

	}

	@Test
	void should_handleEventHandleProperly_when_LoginvesrsuchInaktiverUser() {

		// Act
		ResourceOwner resourceOwner = createTestData();
		LoginversuchInaktiverUser eventPayload = new LoginversuchInaktiverUser(resourceOwner);
		DefaultEmailDaten emailDaten = createEmailDaten(MinikaengurukontenMailKontext.LOGIN_INAKTIV, resourceOwner);

		Mockito.when(mailService.sendMail(emailDaten)).thenReturn(Boolean.TRUE);

		// Act
		handler.handleEvent(eventPayload);

		Mockito.verify(mailService, Mockito.times(1)).sendMail(emailDaten);

	}

	@Test
	void should_handleEventHandleProperly_when_UserCreated() {

		// Act
		ResourceOwner resourceOwner = createTestData();
		UserCreated eventPayload = new UserCreated(resourceOwner);
		DefaultEmailDaten emailDaten = createEmailDaten(MinikaengurukontenMailKontext.USER_CREATED, resourceOwner);

		Mockito.when(mailService.sendMail(emailDaten)).thenReturn(Boolean.TRUE);

		// Act
		handler.handleEvent(eventPayload);

		Mockito.verify(mailService, Mockito.times(1)).sendMail(emailDaten);

	}

	@Test
	void should_handleEventHandleProperly_when_RegistrationConfirmationExpired() {

		// Act
		ResourceOwner resourceOwner = createTestData();
		RegistrationConfirmationExpired eventPayload = new RegistrationConfirmationExpired(resourceOwner);
		DefaultEmailDaten emailDaten = createEmailDaten(MinikaengurukontenMailKontext.CONFIRMATION_EXPIRED, resourceOwner);

		Mockito.when(mailService.sendMail(emailDaten)).thenReturn(Boolean.TRUE);

		// Act
		handler.handleEvent(eventPayload);

		Mockito.verify(mailService, Mockito.times(1)).sendMail(emailDaten);
	}

	private ResourceOwner createTestData() {

		ResourceOwner resourceOwner = new ResourceOwner();
		resourceOwner.setAktiviert(false);
		resourceOwner.setAnonym(false);
		resourceOwner.setEmail("mail@web.de");
		resourceOwner.setLoginName("mail@web.de");
		resourceOwner.setNachname("Log");
		resourceOwner.setRoles("STANDARD");
		resourceOwner.setUuid("ahkgd-eufwhgo");
		resourceOwner.setVorname("Anna");

		return resourceOwner;
	}

	private DefaultEmailDaten createEmailDaten(final MinikaengurukontenMailKontext kontext, final ResourceOwner resourceOwner) {

		return new MinikaengurukontenInfoStrategie(
			ResourceOwnerEventPayload.createFromResourceOwner(resourceOwner), kontext, "DEV").createEmailDaten();
	}
}
