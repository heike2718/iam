// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.service;

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.egladil.web.authprovider.dao.ResourceOwnerDao;
import de.egladil.web.authprovider.domain.ResourceOwner;

/**
 * ImportRunnerTest
 */
public class ImportRunnerTest {

	@Test
	void listFilesInImportDir() {

		String pathImportDir = System.getProperty("user.home") + "/bv-migration/import";

		final ImportRunner importRunner = new ImportRunner(Mockito.mock(ResourceOwnerDao.class), pathImportDir);

		List<Path> paths = importRunner.getUserPaths();

		List<ResourceOwner> alle = paths.stream().map(p -> importRunner.readUser(p)).collect(Collectors.toList());

		assertEquals(3, alle.size());

	}

}
