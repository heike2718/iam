//=====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
//=====================================================
package de.egladil.web.bv_admin.infrastructure.persistence.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.egladil.web.bv_admin.infrastructure.persistence.entities.PersistenterUserReadOnly;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

/**
 *
 */
@QuarkusTest
public class MailversandDaoTest {

	@Inject
	MailversandDao mailversandDao;

	@Test
	void should_selectForVersandgruppe_work() {

		// arrange
		List<String> uuidList = Arrays.asList(new String[] { "804a9bac-05de-4fc6-b2ee-a31a6f69d2ce",
			"82d6451e-4bb1-49f5-93d2-db79ee3592b8", "b142b4d1-abef-4d29-8317-00ba7b995c69" });

		// act
		List<PersistenterUserReadOnly> result = mailversandDao.findActivatedAndNotBannedUsersByUUIDs(uuidList);

		// assert
		assertEquals(1, result.size());

		PersistenterUserReadOnly user = result.get(0);
		assertEquals("804a9bac-05de-4fc6-b2ee-a31a6f69d2ce", user.getUuid());
	}
}
