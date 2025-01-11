// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.egladil.web.authprovider.dao.PacemakerDao;
import de.egladil.web.authprovider.entities.Pacemaker;
import de.egladil.web.authprovider.payload.ResponsePayload;
import io.quarkus.test.junit.QuarkusTest;

/**
 * HeartbeatServiceTest
 */
@QuarkusTest
public class HeartbeatServiceTest {

	private PacemakerDao dao;

	private HeartbeatService service;

	private Pacemaker pacemaker;

	@BeforeEach
	public void setUp() {

		dao = Mockito.mock(PacemakerDao.class);
		service = new HeartbeatService(dao);
		pacemaker = new Pacemaker();
		pacemaker.setId(1l);
		pacemaker.setMonitorId("authprovider-database");
		pacemaker.setWert("wert-1558961580334");
	}

	@Test
	void updateSuccess() {

		// Arrange
		Mockito.when(dao.findByMonitorId("authprovider-database")).thenReturn(pacemaker);
		Mockito.when(dao.save(pacemaker)).thenReturn(pacemaker);

		// Act
		ResponsePayload responsePayload = service.update();

		// Assert
		assertEquals("INFO", responsePayload.getMessage().getLevel());
		assertEquals("authprovider-database lebt", responsePayload.getMessage().getMessage());
		assertNull(responsePayload.getData());

	}

	@Test
	void updateExceptionOnFind() {

		// Arrange
		Mockito.when(dao.findByMonitorId("authprovider-database")).thenThrow(new RuntimeException("testmessage"));

		// Act
		ResponsePayload responsePayload = service.update();

		// Assert
		assertEquals("ERROR", responsePayload.getMessage().getLevel());
		assertEquals("Fehler beim Speichern des pacemakers authprovider-database: testmessage",
			responsePayload.getMessage().getMessage());
		assertNull(responsePayload.getData());

	}

	@Test
	void updateExceptionOnSave() {

		// Arrange
		Mockito.when(dao.findByMonitorId("authprovider-database")).thenReturn(pacemaker);
		Mockito.when(dao.save(pacemaker)).thenThrow(new RuntimeException("testmessage"));

		// Act
		ResponsePayload responsePayload = service.update();

		// Assert
		assertEquals("ERROR", responsePayload.getMessage().getLevel());
		assertEquals("Fehler beim Speichern des pacemakers authprovider-database: testmessage",
			responsePayload.getMessage().getMessage());
		assertNull(responsePayload.getData());

	}

}
