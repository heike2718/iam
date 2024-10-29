// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.service.temppwd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.egladil.web.auth_validations.dto.ZweiPassworte;
import de.egladil.web.authprovider.dao.TempPasswordDao;
import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.authprovider.domain.TempPassword;
import de.egladil.web.authprovider.payload.ChangeTempPasswordPayload;
import de.egladil.web.authprovider.payload.ResponsePayload;
import de.egladil.web.authprovider.service.AuthMailService;
import de.egladil.web.authprovider.service.ChangeLoginSecretsDelegate;
import io.quarkus.test.junit.QuarkusTest;

/**
 * ChangeTempPasswordServiceTest
 */
@QuarkusTest
public class ChangeTempPasswordServiceTest {

	private static final String VALID_TOKENID = "bsdhv-sbjkc";

	private static final String VALID_EMAIL = "eclipse@egladil.de";

	private static final String VALID_TEMP_PWD = "gsaigig";

	private TempPasswordDao tempPasswordDao;

	private AuthMailService authMailservice;

	private ChangeLoginSecretsDelegate loginSecretsDelegate;

	private ChangeTempPasswordService service;

	private ChangeTempPasswordPayload payload;

	@BeforeEach
	public void setUp() {

		tempPasswordDao = Mockito.mock(TempPasswordDao.class);
		authMailservice = Mockito.mock(AuthMailService.class);
		loginSecretsDelegate = Mockito.mock(ChangeLoginSecretsDelegate.class);

		service = new ChangeTempPasswordService(tempPasswordDao, authMailservice, loginSecretsDelegate);

		payload = createValidPayload();
	}

	private ChangeTempPasswordPayload createValidPayload() {

		ChangeTempPasswordPayload result = new ChangeTempPasswordPayload();
		result.setEmail(VALID_EMAIL);
		result.setZweiPassworte(new ZweiPassworte("start567", "start567"));
		result.setTempPassword(VALID_TEMP_PWD);
		result.setTokenId(VALID_TOKENID);

		return result;
	}

	@Test
	void changeTempPasswordPayloadNull() {

		try {

			service.changeTempPassword(null);
			fail("keine IllegalArgumentException");
		} catch (IllegalArgumentException e) {

			assertEquals("payload null", e.getMessage());
		}

	}

	@Test
	void changeTempPasswordTokenIdUnbekannt() {

		// Arrange
		String tokenId = "lhasha-shiaoho";
		Mockito.when(tempPasswordDao.findByTokenId(tokenId)).thenReturn(Optional.empty());

		// Act
		ResponsePayload responsePayload = service.changeTempPassword(payload);

		// Assert
		assertEquals("ERROR", responsePayload.getMessage().getLevel());
		assertEquals("Das Einmalpasswort ist bereits abgelaufen. Bitte fordern Sie ein neues an.",
			responsePayload.getMessage().getMessage());

		assertNull(payload.getZweiPassworte().getPasswort());
		assertNull(payload.getZweiPassworte().getPasswortWdh());
		assertNull(payload.getTempPassword());

	}

	@Test
	void changeTempPasswordTokenAbgelaufen() throws ParseException {

		// Arrange
		TempPassword tempPassword = new TempPassword();
		tempPassword.setExpiresAt(new SimpleDateFormat("dd.MM.yyyy kk:mm:ss").parse("03.07.2019 12:08:15"));
		tempPassword.setTokenId(VALID_TOKENID);

		Mockito.when(tempPasswordDao.findByTokenId(VALID_TOKENID)).thenReturn(Optional.of(tempPassword));
		Mockito.when(tempPasswordDao.delete(tempPassword)).thenReturn(Boolean.TRUE);

		// Act
		ResponsePayload responsePayload = service.changeTempPassword(payload);

		// Assert
		assertEquals("ERROR", responsePayload.getMessage().getLevel());
		assertEquals("Das Einmalpasswort ist bereits abgelaufen. Bitte fordern Sie ein neues an.",
			responsePayload.getMessage().getMessage());

		assertNull(payload.getZweiPassworte().getPasswort());
		assertNull(payload.getZweiPassworte().getPasswortWdh());
		assertNull(payload.getTempPassword());

	}

	@Test
	void changeTempPasswordTokenAbgelaufenExceptionBeimLoeschen() throws ParseException {

		// Arrange
		TempPassword tempPassword = new TempPassword();
		tempPassword.setExpiresAt(new SimpleDateFormat("dd.MM.yyyy kk:mm:ss").parse("03.07.2019 12:08:15"));
		tempPassword.setTokenId(VALID_TOKENID);

		Mockito.when(tempPasswordDao.findByTokenId(VALID_TOKENID)).thenReturn(Optional.of(tempPassword));
		Mockito.when(tempPasswordDao.delete(tempPassword)).thenThrow(new RuntimeException("Exception vom Dao"));

		// Act
		ResponsePayload responsePayload = service.changeTempPassword(payload);

		// Assert
		assertEquals("ERROR", responsePayload.getMessage().getLevel());
		assertEquals("Das Einmalpasswort ist bereits abgelaufen. Bitte fordern Sie ein neues an.",
			responsePayload.getMessage().getMessage());

		assertNull(payload.getZweiPassworte().getPasswort());
		assertNull(payload.getZweiPassworte().getPasswortWdh());
		assertNull(payload.getTempPassword());

	}

	@Test
	void changeTempPasswordTempPasswordNichtKorrekt() throws ParseException {

		// Arrange
		TempPassword tempPassword = new TempPassword();
		tempPassword.setExpiresAt(new SimpleDateFormat("dd.MM.yyyy kk:mm:ss").parse("03.07.2100 12:08:15"));
		tempPassword.setTokenId(VALID_TOKENID);
		tempPassword.setPassword("kdaias");

		Mockito.when(tempPasswordDao.findByTokenId(VALID_TOKENID)).thenReturn(Optional.of(tempPassword));

		// Act
		ResponsePayload responsePayload = service.changeTempPassword(payload);

		// Assert
		assertFalse(responsePayload.isOk());
		assertEquals("ERROR", responsePayload.getMessage().getLevel());
		assertEquals("Bitte überprüfen Sie die eingegebene Mailadresse und das Einmalpasswort.",
			responsePayload.getMessage().getMessage());

		assertNull(payload.getZweiPassworte().getPasswort());
		assertNull(payload.getZweiPassworte().getPasswortWdh());
		assertNull(payload.getTempPassword());
	}

	@Test
	void changeTempPasswordTempPasswordMailadresseStimmtNicht() throws ParseException {

		// Arrange
		TempPassword tempPassword = new TempPassword();
		tempPassword.setExpiresAt(new SimpleDateFormat("dd.MM.yyyy kk:mm:ss").parse("03.07.2100 12:08:15"));
		tempPassword.setTokenId(VALID_TOKENID);
		tempPassword.setPassword(VALID_TEMP_PWD);

		ResourceOwner resourceOwner = new ResourceOwner();
		resourceOwner.setAktiviert(true);
		resourceOwner.setUuid("43f6243");
		resourceOwner.setEmail("unbekannte-mailadress@gmx.de");

		tempPassword.setResourceOwner(resourceOwner);

		Mockito.when(tempPasswordDao.findByTokenId(VALID_TOKENID)).thenReturn(Optional.of(tempPassword));

		// Act
		ResponsePayload responsePayload = service.changeTempPassword(payload);

		// Assert
		assertFalse(responsePayload.isOk());
		assertEquals("ERROR", responsePayload.getMessage().getLevel());
		assertEquals("Bitte überprüfen Sie die eingegebene Mailadresse und das Einmalpasswort.",
			responsePayload.getMessage().getMessage());

		assertNull(payload.getZweiPassworte().getPasswort());
		assertNull(payload.getZweiPassworte().getPasswortWdh());
		assertNull(payload.getTempPassword());
	}

	@Test
	void changeTempPasswordTempPasswordKontoNichtAktiviert() throws ParseException {

		// Arrange
		TempPassword tempPassword = new TempPassword();
		tempPassword.setExpiresAt(new SimpleDateFormat("dd.MM.yyyy kk:mm:ss").parse("03.07.2100 12:08:15"));
		tempPassword.setTokenId(VALID_TOKENID);
		tempPassword.setPassword(VALID_TEMP_PWD);

		ResourceOwner resourceOwner = new ResourceOwner();
		resourceOwner.setAktiviert(false);
		resourceOwner.setUuid("43f6243");
		resourceOwner.setEmail(VALID_EMAIL);

		tempPassword.setResourceOwner(resourceOwner);

		Mockito.when(tempPasswordDao.findByTokenId(VALID_TOKENID)).thenReturn(Optional.of(tempPassword));

		// Act
		ResponsePayload responsePayload = service.changeTempPassword(payload);

		// Assert
		assertEquals("ERROR", responsePayload.getMessage().getLevel());
		assertEquals(
			"Das Benutzerkonto ist nicht aktiviert. Falls Sie die Mail mit dem Aktivierungslink nicht mehr haben, senden Sie eine Mail an 'info@egladil.de'.",
			responsePayload.getMessage().getMessage());

		assertNull(payload.getZweiPassworte().getPasswort());
		assertNull(payload.getZweiPassworte().getPasswortWdh());
		assertNull(payload.getTempPassword());
	}

}
