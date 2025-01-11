// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.event;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.authprovider.payload.MessagePayload;
import de.egladil.web.authprovider.payload.ResponsePayload;
import de.egladil.web.authprovider.restclient.MkGatewayRestClient;
import de.egladil.web.authprovider.service.mail.AuthMailService;
import de.egladil.web.authprovider.service.mail.DefaultEmailDaten;
import de.egladil.web.authprovider.service.mail.MinikaengurukontenInfoStrategie;
import de.egladil.web.authprovider.service.mail.MinikaengurukontenInfoStrategie.MinikaengurukontenMailKontext;
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
	@RestClient
	MkGatewayRestClient mkGateway;

	@InjectMock
	AuthMailService mailService;

	@Inject
	AuthproviderEventHandler handler;

	@ConfigProperty(name = "monitoring.mail.to")
	String monitoringMailEmpfaenger;

	@Test
	void should_handleEventHandleProperly_when_LoginvesrsuchInaktiverUser() {

		// Act
		ResourceOwner resourceOwner = createTestData();
		LoginversuchInaktiverUser eventPayload = new LoginversuchInaktiverUser(resourceOwner);
		DefaultEmailDaten emailDaten = createEmailDaten(MinikaengurukontenMailKontext.LOGIN_INAKTIV, resourceOwner);

		doNothing().when(eventRepository).appendEvent(any(StoredEvent.class));

		// Act
		handler.handleEvent(eventPayload);

		// never, weil Infrastructure nicht da ist
		verify(mailService, never()).sendMail(emailDaten);
		verify(eventRepository).appendEvent(any(StoredEvent.class));

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

		doNothing().when(eventRepository).appendEvent(any(StoredEvent.class));
		when(mkGateway.getSyncToken(any(SyncHandshake.class))).thenReturn(response);
		when(mkGateway.propagateUserCreated(any(CreateUserCommand.class))).thenReturn(userCreatedResponse);

		// Act
		handler.handleEvent(eventPayload);

		// never, weil Infrastructure nicht da ist
		verify(mailService, never()).sendMail(emailDaten);
		verify(eventRepository).appendEvent(any(StoredEvent.class));

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

		doNothing().when(eventRepository).appendEvent(any(StoredEvent.class));
		when(mkGateway.getSyncToken(any(SyncHandshake.class))).thenReturn(Response.ok(responsePayload).build());
		when(mkGateway.propagateUserDeleted(any(DeleteUserCommand.class))).thenReturn(Response.ok().build());

		// Act
		handler.handleEvent(eventPayload);

		// never, weil Infrastructure nicht da ist
		verify(mailService, never()).sendMail(emailDaten);
		verify(eventRepository).appendEvent(any(StoredEvent.class));
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

		return new MinikaengurukontenInfoStrategie(ResourceOwnerEventPayload.createFromResourceOwner(resourceOwner), kontext, "DEV",
			monitoringMailEmpfaenger).createEmailDaten(kontext.name());
	}
}
