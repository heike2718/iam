// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.event;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.authprovider.restclient.MkGatewayRestClientDelegate;
import de.egladil.web.authprovider.service.AuthMailService;
import de.egladil.web.authprovider.service.mail.MinikaengurukontenInfoStrategie;
import de.egladil.web.authprovider.service.mail.MinikaengurukontenInfoStrategie.MinikaengurukontenMailKontext;
import de.egladil.web.commons_mailer.DefaultEmailDaten;
import de.egladil.web.commons_validation.payload.MessagePayload;
import de.egladil.web.commons_validation.payload.ResponsePayload;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

/**
 * AuthproviderEventHandlerTest
 */
@QuarkusTest
public class AuthproviderEventHandlerTest {

	@InjectMock
	EventRepository eventRepository;

	@InjectMock
	MkGatewayRestClientDelegate mkGateway;

	@InjectMock
	AuthMailService mailService;

	@Inject
	AuthproviderEventHandler handler;

	@Test
	void should_handleEventHandleProperly_when_LoginvesrsuchInaktiverUser() {

		// Act
		ResourceOwner resourceOwner = createTestData();
		LoginversuchInaktiverUser eventPayload = new LoginversuchInaktiverUser(resourceOwner);
		DefaultEmailDaten emailDaten = createEmailDaten(MinikaengurukontenMailKontext.LOGIN_INAKTIV, resourceOwner);

		when(mailService.sendMail(emailDaten)).thenReturn(Boolean.TRUE);

		// Act
		handler.handleEvent(eventPayload);

		Mockito.verify(mailService, Mockito.times(1)).sendMail(emailDaten);

	}

	@Test
	void should_handleEventHandleProperly_when_UserCreated() {

		// Act
		ResourceOwner resourceOwner = createTestData();
		UserCreated eventPayload = new UserCreated(
			ResourceOwnerEventPayload.createFromResourceOwner(resourceOwner).withNonce("agsdqgi"));
		DefaultEmailDaten emailDaten = createEmailDaten(MinikaengurukontenMailKontext.USER_CREATED, resourceOwner);

		Map<String, Object> data = new HashMap<>();
		data.put("syncToken", "sdagsd");
		data.put("nonce", "hIODHIQOH");
		ResponsePayload responsePayload = ResponsePayload.messageOnly(MessagePayload.ok());
		responsePayload.setData(data);

		Response response = Response.ok(responsePayload).build();

		Response userCreatedResponse = Response.ok().build();

		when(mailService.sendMail(emailDaten)).thenReturn(Boolean.TRUE);
		when(mkGateway.getSyncToken(any(SyncHandshake.class))).thenReturn(response);
		when(mkGateway.propagateUserCreated(any(CreateUserCommand.class))).thenReturn(userCreatedResponse);

		// Act
		handler.handleEvent(eventPayload);

		verify(mailService, Mockito.times(1)).sendMail(emailDaten);

	}

	@Test
	void should_handleEventHandleProperly_when_userDeleted() {

		// Act
		ResourceOwner resourceOwner = createTestData();
		RegistrationConfirmationExpired eventPayload = new RegistrationConfirmationExpired(resourceOwner);
		DefaultEmailDaten emailDaten = createEmailDaten(MinikaengurukontenMailKontext.CONFIRMATION_EXPIRED, resourceOwner);

		Map<String, Object> data = new HashMap<>();
		data.put("syncToken", "sdagsd");
		data.put("nonce", "hIODHIQOH");
		ResponsePayload responsePayload = ResponsePayload.messageOnly(MessagePayload.ok());
		responsePayload.setData(data);

		when(mkGateway.getSyncToken(any(SyncHandshake.class))).thenReturn(Response.ok(responsePayload).build());
		when(mkGateway.propagateUserDeleted(any(DeleteUserCommand.class))).thenReturn(Response.ok().build());
		when(mailService.sendMail(emailDaten)).thenReturn(Boolean.TRUE);

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
			ResourceOwnerEventPayload.createFromResourceOwner(resourceOwner), kontext, "DEV").createEmailDaten(kontext.name());
	}
}
