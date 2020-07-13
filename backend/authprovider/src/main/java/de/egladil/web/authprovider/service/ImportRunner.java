// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.web.authprovider.dao.ResourceOwnerDao;
import de.egladil.web.authprovider.domain.ResourceOwner;

/**
 * ImportRunner
 */
@RequestScoped
public class ImportRunner {

	private static final Logger LOG = LoggerFactory.getLogger(ImportRunner.class);

	private Path importDir;

	private final ObjectMapper objectMapper;

	@ConfigProperty(name = "pathImportDir")
	String pathImportDir;

	@Inject
	ResourceOwnerDao resourceOwnerDao;

	/**
	 *
	 */
	public ImportRunner() {

		objectMapper = new ObjectMapper();
	}

	/**
	 * @param resourceOwnerDao
	 */
	public ImportRunner(final ResourceOwnerDao resourceOwnerDao, final String pathImportDir) {

		this();

		this.pathImportDir = pathImportDir;
		this.resourceOwnerDao = resourceOwnerDao;

	}

	public void startImport() {

		if (importDir == null) {

			importDir = Path.of(URI.create("file://" + pathImportDir));
		}

		List<Path> files = getUserPaths();
		files.stream().map(p -> readUser(p)).forEach(ro -> importResourceOwner(ro));
	}

	List<Path> getUserPaths() {

		if (importDir == null) {

			importDir = Path.of(URI.create("file://" + pathImportDir));
		}

		final List<Path> result = new ArrayList<>();

		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(importDir)) {

			directoryStream.forEach(p -> {

				if (p.toString().endsWith(".json")) {

					result.add(Path.of(p.toString()));
				}
			});

		} catch (IOException e) {

			LOG.error("Benutzer konnten nicht importiert werden: {}", e.getMessage());

		}

		return result;
	}

	ResourceOwner readUser(final Path path) {

		try (InputStream in = Files.newInputStream(path)) {

			return objectMapper.readValue(in, ResourceOwner.class);

		} catch (IOException e) {

			LOG.error("Datei {} konnte nicht importiert werden (wird übersprungen): {}", path, e.getMessage());
			return null;

		}

	}

	@Transactional(value = TxType.REQUIRED)
	void importResourceOwner(final ResourceOwner resourceOwner) {

		try {

			if (canImport(resourceOwner)) {

				ResourceOwner persisted = resourceOwnerDao.save(resourceOwner);
				LOG.info("{} importiert", persisted);
			}
		} catch (Exception e) {

			LOG.error("ResourceOwner {} konnte nicht importiert werden (wird übersprungen): {}", resourceOwner, e.getMessage());
		}

	}

	boolean canImport(final ResourceOwner resourceOwner) {

		Optional<ResourceOwner> optRO = resourceOwnerDao.findByUUID(resourceOwner.getUuid());

		if (optRO.isPresent()) {

			LOG.info("USERS enthält bereits einen Eintrag mit UUID='{}'", resourceOwner.getUuid());
			return false;
		}

		optRO = resourceOwnerDao.findByLoginName(resourceOwner.getEmail());

		if (optRO.isPresent()) {

			LOG.info("USERS enthält bereits einen Eintrag mit email='{}'", resourceOwner.getEmail());
			return false;
		}

		optRO = resourceOwnerDao.findByLoginName(resourceOwner.getLoginName());

		if (optRO.isPresent()) {

			LOG.info("USERS enthält bereits einen Eintrag mit loginName='{}'", resourceOwner.getLoginName());
			return false;
		}

		return true;
	}

}
