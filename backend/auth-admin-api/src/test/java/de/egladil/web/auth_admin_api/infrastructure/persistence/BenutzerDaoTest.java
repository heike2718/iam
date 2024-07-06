// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import de.egladil.web.auth_admin_api.infrastructure.persistence.dao.BenutzerDao;
import de.egladil.web.auth_admin_api.infrastructure.persistence.entities.PersistenterUser;
import de.egladil.web.auth_admin_api.infrastructure.persistence.entities.PersistenterUserReadOnly;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

/**
 * BenutzerDaoTest
 */
@QuarkusTest
public class BenutzerDaoTest {

	@Inject
	BenutzerDao benutzerDao;

	@Test
	void should_findUserReadonlyByUUID_returnTheUser() {

		// Arrange
		String uuid = "20721575-8c45-4201-a025-7a9fece1f2aa";

		// Act
		PersistenterUserReadOnly user = benutzerDao.findUserReadonlyByUUID(uuid);

		// Assert
		assertNotNull(user);
		assertEquals(515L, user.saltId);
		assertEquals("Checki", user.vorname);

	}

	@Test
	void should_findUserByUUID_returnTheUser() {

		// Arrange
		String uuid = "20721575-8c45-4201-a025-7a9fece1f2aa";

		// Act
		PersistenterUser user = benutzerDao.findUserByUUID(uuid);

		// Assert
		assertNotNull(user);
		assertEquals("Checki", user.getVorname());

	}
}
