// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import de.egladil.web.auth_admin_api.infrastructure.persistence.dao.SaltDao;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

/**
 * SaltDaoTest
 */
@QuarkusTest
public class SaltDaoTest {

	@Inject
	SaltDao saltDao;

	@Test
	void should_findById() {

		// Arrange
		Long id = 515L;

		// Act + Assert
		assertNotNull(saltDao.findSaltByID(id));
	}
}
