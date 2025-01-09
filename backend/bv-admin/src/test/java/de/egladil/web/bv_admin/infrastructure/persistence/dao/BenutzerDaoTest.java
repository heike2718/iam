// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.infrastructure.persistence.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import de.egladil.web.bv_admin.infrastructure.persistence.dao.BenutzerDao;
import de.egladil.web.bv_admin.infrastructure.persistence.entities.PersistenterUser;
import de.egladil.web.bv_admin.infrastructure.persistence.entities.PersistenterUserReadOnly;
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
		String uuid = "5d89c2e1-5d35-4e1b-b5a5-c56defd8ba43";

		// Act
		PersistenterUserReadOnly user = benutzerDao.findUserReadonlyByUUID(uuid);

		// Assert
		assertNotNull(user);
		assertEquals(758L, user.saltId);
		assertEquals("Bilbo", user.vorname);

	}

	@Test
	void should_findUserByUUID_returnTheUser() {

		// Arrange
		String uuid = "5d89c2e1-5d35-4e1b-b5a5-c56defd8ba43";

		// Act
		PersistenterUser user = benutzerDao.findUserByUUID(uuid);

		// Assert
		assertNotNull(user);
		assertEquals("Bilbo", user.getVorname());

	}

	@Test
	void should_findUserReadonlyByUUID_returnNull_when_admin() {

		// Arrange
		String uuid = "b865fc75-1bcf-40c7-96c3-33744826e49f";

		// Act
		PersistenterUserReadOnly user = benutzerDao.findUserReadonlyByUUID(uuid);

		// Assert
		assertNull(user);
	}

	@Test
	void should_findUserByUUID_returnTheUser_even_if_admin() {

		// Arrange
		String uuid = "20721575-8c45-4201-a025-7a9fece1f2aa";

		// Act
		PersistenterUser user = benutzerDao.findUserByUUID(uuid);

		// Assert
		assertNotNull(user);
		assertEquals("Checki", user.getVorname());

	}
}
