// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.mailversand.process;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import de.egladil.web.auth_admin_api.domain.Jobstatus;
import de.egladil.web.auth_admin_api.domain.exceptions.MailversandException;
import de.egladil.web.auth_admin_api.domain.mailversand.api.AuthAdminMailDto;
import de.egladil.web.auth_admin_api.domain.mailversand.api.MailService;
import de.egladil.web.auth_admin_api.domain.mailversand.api.Mailversandgruppe;
import de.egladil.web.auth_admin_api.infrastructure.persistence.dao.MailversandDao;
import de.egladil.web.auth_admin_api.infrastructure.persistence.entities.PersistenteMailversandgruppe;
import de.egladil.web.auth_admin_api.infrastructure.persistence.entities.PersistenterInfomailTextReadOnly;
import de.egladil.web.auth_admin_api.infrastructure.persistence.entities.PersistenterMailversandauftrag;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

/**
 * MailversandProcessorTest
 */
@QuarkusTest
public class MailversandProcessorTest {

	@Inject
	MailversandProcessor processor;

	@InjectMock
	VersandgruppenSource versandgruppenSource;

	@InjectMock
	MailService mailService;

	@InjectMock
	MailversandDao dao;

	@Test
	void should_returnEarly_when_noMailversandauftrag() {

		// Arrange
		when(dao.findOldestNotCompletedMailversandauftrag()).thenReturn(null);

		// Act
		processor.processMailversandauftrag();

		// Assert
		verify(dao).findOldestNotCompletedMailversandauftrag();
		verify(versandgruppenSource, never()).getNextMailversandgruppe(any(String.class));
		verify(dao, never()).updateMailversandauftrag(any(PersistenterMailversandauftrag.class));
		verify(dao, never()).updateMailversandgruppe(any(PersistenteMailversandgruppe.class));
		verify(dao, never()).findInfomailtextReadOnlyByID(any(String.class));
		verify(dao, never()).removeMailversandauftrag(any(PersistenterMailversandauftrag.class));
		verify(dao, never()).findMailversandgruppeByUUID(any(String.class));
		verify(mailService, never()).sendeMail(any(AuthAdminMailDto.class));
	}

	@Test
	void should_markJobFinished_when_noMailversandGruppeAndInProgress() {

		// Arrange
		String uuid = "663d1c4e-46b7-4b41-a3cc-c753b8f7148c";

		PersistenterMailversandauftrag auftrag = new PersistenterMailversandauftrag();
		auftrag.setStatus(Jobstatus.IN_PROGRESS);
		auftrag.setUuid(uuid);
		auftrag.setVersandMitFehlern(true);

		when(dao.findOldestNotCompletedMailversandauftrag()).thenReturn(auftrag);
		when(versandgruppenSource.getNextMailversandgruppe(uuid)).thenReturn(null);

		// Act
		processor.processMailversandauftrag();

		// Assert
		verify(dao).findOldestNotCompletedMailversandauftrag();
		verify(versandgruppenSource).getNextMailversandgruppe(uuid);
		verify(dao).updateMailversandauftrag(any(PersistenterMailversandauftrag.class));
		verify(dao, never()).updateMailversandgruppe(any(PersistenteMailversandgruppe.class));
		verify(dao, never()).findInfomailtextReadOnlyByID(any(String.class));
		verify(dao, never()).removeMailversandauftrag(any(PersistenterMailversandauftrag.class));
		verify(dao, never()).findMailversandgruppeByUUID(any(String.class));
		verify(mailService, never()).sendeMail(any(AuthAdminMailDto.class));
	}

	@Test
	void should_markJobFinished_when_noMailversandGruppeAndWaiting() {

		// Arrange
		String uuid = "663d1c4e-46b7-4b41-a3cc-c753b8f7148c";

		PersistenterMailversandauftrag auftrag = new PersistenterMailversandauftrag();
		auftrag.setStatus(Jobstatus.WAITING);
		auftrag.setUuid(uuid);

		when(dao.findOldestNotCompletedMailversandauftrag()).thenReturn(auftrag);
		when(versandgruppenSource.getNextMailversandgruppe(uuid)).thenReturn(null);

		// Act
		processor.processMailversandauftrag();

		// Assert
		verify(dao).findOldestNotCompletedMailversandauftrag();
		verify(versandgruppenSource).getNextMailversandgruppe(uuid);
		verify(dao).updateMailversandauftrag(any(PersistenterMailversandauftrag.class));
		verify(dao, never()).updateMailversandgruppe(any(PersistenteMailversandgruppe.class));
		verify(dao, never()).findInfomailtextReadOnlyByID(any(String.class));
		verify(dao, never()).removeMailversandauftrag(any(PersistenterMailversandauftrag.class));
		verify(dao, never()).findMailversandgruppeByUUID(any(String.class));
		verify(mailService, never()).sendeMail(any(AuthAdminMailDto.class));
	}

	@Test
	void should_notMarkJobFinished_when_noMailversandGruppeAndCompleted() {

		// Arrange
		String uuid = "663d1c4e-46b7-4b41-a3cc-c753b8f7148c";

		PersistenterMailversandauftrag auftrag = new PersistenterMailversandauftrag();
		auftrag.setStatus(Jobstatus.COMPLETED);
		auftrag.setUuid(uuid);

		when(dao.findOldestNotCompletedMailversandauftrag()).thenReturn(auftrag);
		when(versandgruppenSource.getNextMailversandgruppe(uuid)).thenReturn(null);

		// Act
		processor.processMailversandauftrag();

		// Assert
		verify(dao).findOldestNotCompletedMailversandauftrag();
		verify(versandgruppenSource).getNextMailversandgruppe(uuid);
		verify(dao, never()).updateMailversandauftrag(any(PersistenterMailversandauftrag.class));
		verify(dao, never()).updateMailversandgruppe(any(PersistenteMailversandgruppe.class));
		verify(dao, never()).findInfomailtextReadOnlyByID(any(String.class));
		verify(dao, never()).removeMailversandauftrag(any(PersistenterMailversandauftrag.class));
		verify(dao, never()).findMailversandgruppeByUUID(any(String.class));
		verify(mailService, never()).sendeMail(any(AuthAdminMailDto.class));
	}

	@Test
	void should_removeVersandauftrag_when_noMailvorlage() {

		// Arrange
		String uuid = "663d1c4e-46b7-4b41-a3cc-c753b8f7148c";
		String idInfomailtext = "78573dc4-06d7-43f1-9b85-ae79f36c92b7";

		Mailversandgruppe gruppe = new Mailversandgruppe();
		gruppe.setSortnr(2);
		gruppe.setStatus(Jobstatus.WAITING);

		PersistenterMailversandauftrag auftrag = new PersistenterMailversandauftrag();
		auftrag.setStatus(Jobstatus.IN_PROGRESS);
		auftrag.setUuid(uuid);
		auftrag.setIdInfomailtext(idInfomailtext);

		when(dao.findOldestNotCompletedMailversandauftrag()).thenReturn(auftrag);
		when(versandgruppenSource.getNextMailversandgruppe(uuid)).thenReturn(gruppe);
		when(dao.findInfomailtextReadOnlyByID(idInfomailtext)).thenReturn(null);
		doNothing().when(dao).removeMailversandauftrag(any(PersistenterMailversandauftrag.class));

		// Act
		processor.processMailversandauftrag();

		// Assert
		verify(dao).findOldestNotCompletedMailversandauftrag();
		verify(versandgruppenSource).getNextMailversandgruppe(uuid);
		verify(dao, never()).updateMailversandauftrag(any(PersistenterMailversandauftrag.class));
		verify(dao, never()).updateMailversandgruppe(any(PersistenteMailversandgruppe.class));
		verify(dao).findInfomailtextReadOnlyByID(any(String.class));
		verify(dao).removeMailversandauftrag(any(PersistenterMailversandauftrag.class));
		verify(dao, never()).findMailversandgruppeByUUID(any(String.class));
		verify(mailService, never()).sendeMail(any(AuthAdminMailDto.class));
	}

	@Test
	void should_startAndSendMail_when_AuftragWaiting() {

		// Arrange
		String uuid = "663d1c4e-46b7-4b41-a3cc-c753b8f7148c";
		String idInfomailtext = "78573dc4-06d7-43f1-9b85-ae79f36c92b7";
		String uuidMailgruppe = "729fb865-ce0c-4c26-a1f1-742aad8d3072";

		Mailversandgruppe gruppe = new Mailversandgruppe();
		gruppe.setSortnr(2);
		gruppe.setStatus(Jobstatus.WAITING);
		gruppe.setUuid(uuidMailgruppe);
		gruppe.setEmpfaengerUUIDs(
			Arrays.asList(new String[] { "14a8fd8e-13d9-48bd-9f3f-e86be83ee871", "868d5890-d4d0-45ab-811d-4bfc081b48ec" }));
		gruppe.setEmpfaengerEmails(Arrays.asList(new String[] { "14a8fd8e-13d9@48bd.de", "868d5890@d4d0-45ab.de" }));

		PersistenteMailversandgruppe persistenteMailversandgruppe = new PersistenteMailversandgruppe();
		persistenteMailversandgruppe.setIdVersandauftrag(uuid);
		persistenteMailversandgruppe.setStatus(Jobstatus.WAITING);

		PersistenterMailversandauftrag auftrag = new PersistenterMailversandauftrag();
		auftrag.setStatus(Jobstatus.WAITING);
		auftrag.setUuid(uuid);
		auftrag.setIdInfomailtext(idInfomailtext);

		PersistenterInfomailTextReadOnly infomailtext = new PersistenterInfomailTextReadOnly();
		infomailtext.betreff = "Ja hallo auch";
		infomailtext.mailtext = "Mein Name ist Hase";
		infomailtext.uuid = idInfomailtext;

		when(dao.findOldestNotCompletedMailversandauftrag()).thenReturn(auftrag);
		when(versandgruppenSource.getNextMailversandgruppe(uuid)).thenReturn(gruppe);
		when(dao.findInfomailtextReadOnlyByID(idInfomailtext)).thenReturn(infomailtext);
		when(dao.findMailversandgruppeByUUID(uuidMailgruppe)).thenReturn(persistenteMailversandgruppe);
		when(dao.updateMailversandauftrag(any(PersistenterMailversandauftrag.class))).thenReturn(auftrag);
		doNothing().when(mailService).sendeMail(any(AuthAdminMailDto.class));

		// Act
		processor.processMailversandauftrag();

		// Assert
		verify(dao).findOldestNotCompletedMailversandauftrag();
		verify(versandgruppenSource).getNextMailversandgruppe(uuid);
		verify(dao, times(2)).updateMailversandauftrag(any(PersistenterMailversandauftrag.class));
		verify(dao).updateMailversandgruppe(any(PersistenteMailversandgruppe.class));
		verify(dao).findInfomailtextReadOnlyByID(any(String.class));
		verify(dao, never()).removeMailversandauftrag(any(PersistenterMailversandauftrag.class));
		verify(dao).findMailversandgruppeByUUID(any(String.class));
		verify(mailService).sendeMail(any(AuthAdminMailDto.class));
	}

	@Test
	void should_notStartButFinish_when_AuftragInProgressAndAlleVersendet() {

		// Arrange
		String uuid = "663d1c4e-46b7-4b41-a3cc-c753b8f7148c";
		String idInfomailtext = "78573dc4-06d7-43f1-9b85-ae79f36c92b7";
		String uuidMailgruppe = "729fb865-ce0c-4c26-a1f1-742aad8d3072";

		Mailversandgruppe gruppe = new Mailversandgruppe();
		gruppe.setSortnr(2);
		gruppe.setStatus(Jobstatus.WAITING);
		gruppe.setUuid(uuidMailgruppe);
		gruppe.setEmpfaengerUUIDs(
			Arrays.asList(new String[] { "14a8fd8e-13d9-48bd-9f3f-e86be83ee871", "868d5890-d4d0-45ab-811d-4bfc081b48ec" }));
		gruppe.setEmpfaengerEmails(Arrays.asList(new String[] { "14a8fd8e-13d9@48bd.de", "868d5890@d4d0-45ab.de" }));

		PersistenteMailversandgruppe persistenteMailversandgruppe = new PersistenteMailversandgruppe();
		persistenteMailversandgruppe.setIdVersandauftrag(uuid);
		persistenteMailversandgruppe.setStatus(Jobstatus.WAITING);

		PersistenterMailversandauftrag auftrag = new PersistenterMailversandauftrag();
		auftrag.setStatus(Jobstatus.IN_PROGRESS);
		auftrag.setUuid(uuid);
		auftrag.setIdInfomailtext(idInfomailtext);
		auftrag.setAnzahlEmpfaenger(gruppe.getEmpfaengerEmails().size());

		PersistenterInfomailTextReadOnly infomailtext = new PersistenterInfomailTextReadOnly();
		infomailtext.betreff = "Ja hallo auch";
		infomailtext.mailtext = "Mein Name ist Hase";
		infomailtext.uuid = idInfomailtext;

		when(dao.findOldestNotCompletedMailversandauftrag()).thenReturn(auftrag);
		when(versandgruppenSource.getNextMailversandgruppe(uuid)).thenReturn(gruppe);
		when(dao.findInfomailtextReadOnlyByID(idInfomailtext)).thenReturn(infomailtext);
		when(dao.findMailversandgruppeByUUID(uuidMailgruppe)).thenReturn(persistenteMailversandgruppe);
		when(dao.updateMailversandauftrag(any(PersistenterMailversandauftrag.class))).thenReturn(auftrag);
		doNothing().when(mailService).sendeMail(any(AuthAdminMailDto.class));

		// Act
		processor.processMailversandauftrag();

		// Assert
		verify(dao).findOldestNotCompletedMailversandauftrag();
		verify(versandgruppenSource).getNextMailversandgruppe(uuid);
		verify(dao, times(2)).updateMailversandauftrag(any(PersistenterMailversandauftrag.class));
		verify(dao).updateMailversandgruppe(any(PersistenteMailversandgruppe.class));
		verify(dao).findInfomailtextReadOnlyByID(any(String.class));
		verify(dao, never()).removeMailversandauftrag(any(PersistenterMailversandauftrag.class));
		verify(dao).findMailversandgruppeByUUID(any(String.class));
		verify(mailService).sendeMail(any(AuthAdminMailDto.class));
	}

	@Test
	void should_notStartAndNotFinish_when_AuftragInProgressAndAnzahlVersendetKleinerAlsAnzahlEmpfaenger() {

		// Arrange
		String uuid = "663d1c4e-46b7-4b41-a3cc-c753b8f7148c";
		String idInfomailtext = "78573dc4-06d7-43f1-9b85-ae79f36c92b7";
		String uuidMailgruppe = "729fb865-ce0c-4c26-a1f1-742aad8d3072";

		Mailversandgruppe gruppe = new Mailversandgruppe();
		gruppe.setSortnr(2);
		gruppe.setStatus(Jobstatus.WAITING);
		gruppe.setUuid(uuidMailgruppe);
		gruppe.setEmpfaengerUUIDs(
			Arrays.asList(new String[] { "14a8fd8e-13d9-48bd-9f3f-e86be83ee871", "868d5890-d4d0-45ab-811d-4bfc081b48ec" }));
		gruppe.setEmpfaengerEmails(Arrays.asList(new String[] { "14a8fd8e-13d9@48bd.de", "868d5890@d4d0-45ab.de" }));

		PersistenteMailversandgruppe persistenteMailversandgruppe = new PersistenteMailversandgruppe();
		persistenteMailversandgruppe.setIdVersandauftrag(uuid);
		persistenteMailversandgruppe.setStatus(Jobstatus.WAITING);

		PersistenterMailversandauftrag auftrag = new PersistenterMailversandauftrag();
		auftrag.setStatus(Jobstatus.IN_PROGRESS);
		auftrag.setUuid(uuid);
		auftrag.setIdInfomailtext(idInfomailtext);
		auftrag.setAnzahlEmpfaenger(gruppe.getEmpfaengerEmails().size() + 1);

		PersistenterInfomailTextReadOnly infomailtext = new PersistenterInfomailTextReadOnly();
		infomailtext.betreff = "Ja hallo auch";
		infomailtext.mailtext = "Mein Name ist Hase";
		infomailtext.uuid = idInfomailtext;

		when(dao.findOldestNotCompletedMailversandauftrag()).thenReturn(auftrag);
		when(versandgruppenSource.getNextMailversandgruppe(uuid)).thenReturn(gruppe);
		when(dao.findInfomailtextReadOnlyByID(idInfomailtext)).thenReturn(infomailtext);
		when(dao.findMailversandgruppeByUUID(uuidMailgruppe)).thenReturn(persistenteMailversandgruppe);
		when(dao.updateMailversandauftrag(any(PersistenterMailversandauftrag.class))).thenReturn(auftrag);
		doNothing().when(mailService).sendeMail(any(AuthAdminMailDto.class));

		// Act
		processor.processMailversandauftrag();

		// Assert
		verify(dao).findOldestNotCompletedMailversandauftrag();
		verify(versandgruppenSource).getNextMailversandgruppe(uuid);
		verify(dao).updateMailversandauftrag(any(PersistenterMailversandauftrag.class));
		verify(dao).updateMailversandgruppe(any(PersistenteMailversandgruppe.class));
		verify(dao).findInfomailtextReadOnlyByID(any(String.class));
		verify(dao, never()).removeMailversandauftrag(any(PersistenterMailversandauftrag.class));
		verify(dao).findMailversandgruppeByUUID(any(String.class));
		verify(mailService).sendeMail(any(AuthAdminMailDto.class));
	}

	@Test
	void should_notUpdateAuftragUndGruppe_when_UnexpectedExceptionOnUpdate() {

		// Arrange
		String uuid = "663d1c4e-46b7-4b41-a3cc-c753b8f7148c";
		String idInfomailtext = "78573dc4-06d7-43f1-9b85-ae79f36c92b7";
		String uuidMailgruppe = "729fb865-ce0c-4c26-a1f1-742aad8d3072";

		Mailversandgruppe gruppe = new Mailversandgruppe();
		gruppe.setSortnr(2);
		gruppe.setStatus(Jobstatus.WAITING);
		gruppe.setUuid(uuidMailgruppe);
		gruppe.setEmpfaengerUUIDs(
			Arrays.asList(new String[] { "14a8fd8e-13d9-48bd-9f3f-e86be83ee871", "868d5890-d4d0-45ab-811d-4bfc081b48ec" }));
		gruppe.setEmpfaengerEmails(Arrays.asList(new String[] { "14a8fd8e-13d9@48bd.de", "868d5890@d4d0-45ab.de" }));

		PersistenteMailversandgruppe persistenteMailversandgruppe = new PersistenteMailversandgruppe();
		persistenteMailversandgruppe.setIdVersandauftrag(uuid);
		persistenteMailversandgruppe.setStatus(Jobstatus.WAITING);

		PersistenterMailversandauftrag auftrag = new PersistenterMailversandauftrag();
		auftrag.setStatus(Jobstatus.IN_PROGRESS);
		auftrag.setUuid(uuid);
		auftrag.setIdInfomailtext(idInfomailtext);
		auftrag.setAnzahlEmpfaenger(gruppe.getEmpfaengerEmails().size() + 1);

		PersistenterInfomailTextReadOnly infomailtext = new PersistenterInfomailTextReadOnly();
		infomailtext.betreff = "Ja hallo auch";
		infomailtext.mailtext = "Mein Name ist Hase";
		infomailtext.uuid = idInfomailtext;

		when(dao.findOldestNotCompletedMailversandauftrag()).thenReturn(auftrag);
		when(versandgruppenSource.getNextMailversandgruppe(uuid)).thenReturn(gruppe);
		when(dao.findInfomailtextReadOnlyByID(idInfomailtext)).thenReturn(infomailtext);
		when(dao.findMailversandgruppeByUUID(uuidMailgruppe)).thenReturn(persistenteMailversandgruppe);
		when(dao.updateMailversandauftrag(any(PersistenterMailversandauftrag.class))).thenReturn(auftrag);
		doNothing().when(mailService).sendeMail(any(AuthAdminMailDto.class));
		doThrow(new RuntimeException("hat mal nicht geklappt")).when(dao)
			.updateMailversandgruppe(any(PersistenteMailversandgruppe.class));

		// Act
		processor.processMailversandauftrag();

		// Assert
		verify(dao).findOldestNotCompletedMailversandauftrag();
		verify(versandgruppenSource).getNextMailversandgruppe(uuid);
		verify(dao).updateMailversandauftrag(any(PersistenterMailversandauftrag.class));
		verify(dao).updateMailversandgruppe(any(PersistenteMailversandgruppe.class));
		verify(dao).findInfomailtextReadOnlyByID(any(String.class));
		verify(dao, never()).removeMailversandauftrag(any(PersistenterMailversandauftrag.class));
		verify(dao).findMailversandgruppeByUUID(any(String.class));
		verify(mailService).sendeMail(any(AuthAdminMailDto.class));
	}

	@Test
	void should_updateAnzahl_when_Mailversandexception() {

		// Arrange
		String uuid = "663d1c4e-46b7-4b41-a3cc-c753b8f7148c";
		String idInfomailtext = "78573dc4-06d7-43f1-9b85-ae79f36c92b7";
		String uuidMailgruppe = "729fb865-ce0c-4c26-a1f1-742aad8d3072";

		Mailversandgruppe gruppe = new Mailversandgruppe();
		gruppe.setSortnr(2);
		gruppe.setStatus(Jobstatus.WAITING);
		gruppe.setUuid(uuidMailgruppe);
		gruppe.setEmpfaengerUUIDs(
			Arrays.asList(new String[] { "14a8fd8e-13d9-48bd-9f3f-e86be83ee871", "868d5890-d4d0-45ab-811d-4bfc081b48ec" }));
		gruppe.setEmpfaengerEmails(Arrays.asList(new String[] { "14a8fd8e-13d9@48bd.de", "868d5890@d4d0-45ab.de" }));

		PersistenteMailversandgruppe persistenteMailversandgruppe = new PersistenteMailversandgruppe();
		persistenteMailversandgruppe.setIdVersandauftrag(uuid);
		persistenteMailversandgruppe.setStatus(Jobstatus.WAITING);

		PersistenterMailversandauftrag auftrag = new PersistenterMailversandauftrag();
		auftrag.setStatus(Jobstatus.IN_PROGRESS);
		auftrag.setUuid(uuid);
		auftrag.setIdInfomailtext(idInfomailtext);
		auftrag.setAnzahlEmpfaenger(gruppe.getEmpfaengerEmails().size() + 1);

		PersistenterInfomailTextReadOnly infomailtext = new PersistenterInfomailTextReadOnly();
		infomailtext.betreff = "Ja hallo auch";
		infomailtext.mailtext = "Mein Name ist Hase";
		infomailtext.uuid = idInfomailtext;

		when(dao.findOldestNotCompletedMailversandauftrag()).thenReturn(auftrag);
		when(versandgruppenSource.getNextMailversandgruppe(uuid)).thenReturn(gruppe);
		when(dao.findInfomailtextReadOnlyByID(idInfomailtext)).thenReturn(infomailtext);
		when(dao.findMailversandgruppeByUUID(uuidMailgruppe)).thenReturn(persistenteMailversandgruppe);
		when(dao.updateMailversandauftrag(any(PersistenterMailversandauftrag.class))).thenReturn(auftrag);

		doThrow(new MailversandException("hat mal nicht geklappt")).when(mailService).sendeMail(any(AuthAdminMailDto.class));

		// Act
		processor.processMailversandauftrag();

		// Assert
		verify(dao).findOldestNotCompletedMailversandauftrag();
		verify(versandgruppenSource).getNextMailversandgruppe(uuid);
		verify(dao).updateMailversandauftrag(any(PersistenterMailversandauftrag.class));
		verify(dao).updateMailversandgruppe(any(PersistenteMailversandgruppe.class));
		verify(dao).findInfomailtextReadOnlyByID(any(String.class));
		verify(dao, never()).removeMailversandauftrag(any(PersistenterMailversandauftrag.class));
		verify(dao).findMailversandgruppeByUUID(any(String.class));
		verify(mailService).sendeMail(any(AuthAdminMailDto.class));
	}

	@Test
	void should_notSenddMailUndMarkGruppeFinished_when_mailempfaenderLeer() {

		// Arrange
		String uuid = "663d1c4e-46b7-4b41-a3cc-c753b8f7148c";
		String idInfomailtext = "78573dc4-06d7-43f1-9b85-ae79f36c92b7";
		String uuidMailgruppe = "729fb865-ce0c-4c26-a1f1-742aad8d3072";

		Mailversandgruppe gruppe = new Mailversandgruppe();
		gruppe.setSortnr(2);
		gruppe.setStatus(Jobstatus.WAITING);
		gruppe.setUuid(uuidMailgruppe);
		gruppe.setEmpfaengerUUIDs(
			Arrays.asList(new String[] { "14a8fd8e-13d9-48bd-9f3f-e86be83ee871", "868d5890-d4d0-45ab-811d-4bfc081b48ec" }));

		PersistenteMailversandgruppe persistenteMailversandgruppe = new PersistenteMailversandgruppe();
		persistenteMailversandgruppe.setIdVersandauftrag(uuid);
		persistenteMailversandgruppe.setStatus(Jobstatus.WAITING);

		PersistenterMailversandauftrag auftrag = new PersistenterMailversandauftrag();
		auftrag.setStatus(Jobstatus.WAITING);
		auftrag.setUuid(uuid);
		auftrag.setIdInfomailtext(idInfomailtext);
		auftrag.setAnzahlEmpfaenger(10);

		PersistenterInfomailTextReadOnly infomailtext = new PersistenterInfomailTextReadOnly();
		infomailtext.betreff = "Ja hallo auch";
		infomailtext.mailtext = "Mein Name ist Hase";
		infomailtext.uuid = idInfomailtext;

		when(dao.findOldestNotCompletedMailversandauftrag()).thenReturn(auftrag);
		when(versandgruppenSource.getNextMailversandgruppe(uuid)).thenReturn(gruppe);
		when(dao.findInfomailtextReadOnlyByID(idInfomailtext)).thenReturn(infomailtext);
		when(dao.findMailversandgruppeByUUID(uuidMailgruppe)).thenReturn(persistenteMailversandgruppe);
		when(dao.updateMailversandauftrag(any(PersistenterMailversandauftrag.class))).thenReturn(auftrag);
		doNothing().when(mailService).sendeMail(any(AuthAdminMailDto.class));

		// Act
		processor.processMailversandauftrag();

		// Assert
		verify(dao).findOldestNotCompletedMailversandauftrag();
		verify(versandgruppenSource).getNextMailversandgruppe(uuid);
		verify(dao, times(2)).updateMailversandauftrag(any(PersistenterMailversandauftrag.class));
		verify(dao).updateMailversandgruppe(any(PersistenteMailversandgruppe.class));
		verify(dao).findInfomailtextReadOnlyByID(any(String.class));
		verify(dao, never()).removeMailversandauftrag(any(PersistenterMailversandauftrag.class));
		verify(dao).findMailversandgruppeByUUID(any(String.class));
		verify(mailService, never()).sendeMail(any(AuthAdminMailDto.class));
	}

}
