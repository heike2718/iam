//=====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
//=====================================================

package de.egladil.web.authprovider.crypto;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

/**
 * PemFile
 */
public class PemFile {

	private PemObject pemObject;

	/**
	 *
	 * Erzeugt eine Instanz von PemFile
	 *
	 * @param filename
	 */
	public PemFile(final String filename) throws FileNotFoundException, IOException {
		try (PemReader pemReader = new PemReader(new InputStreamReader(new FileInputStream(filename)))) {
			this.pemObject = pemReader.readPemObject();
		}
	}

	public PemObject getPemObject() {
		return pemObject;
	}
}
