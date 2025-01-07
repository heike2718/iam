// =====================================================
// Project: profil-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_api.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import de.egladil.web.auth_validations.dto.ZweiPassworte;
import de.egladil.web.profil_api.domain.auth.dto.MessagePayload;
import de.egladil.web.profil_api.domain.passwort.PasswortPayload;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

/**
 * PasswortResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(PasswortResource.class)
public class PasswortResourceTest {

	@Test
	@TestSecurity(authorizationEnabled = false)
	void should_return400_when_payloadInvalid() {

		// Arrange
		PasswortPayload bean = new PasswortPayload();
		ZweiPassworte invalidZweiPassworte = new ZweiPassworte();
		invalidZweiPassworte.setPasswort("Qw<ert>z!");
		bean.setZweiPassworte(invalidZweiPassworte);
		bean.setPasswort("start123");

		// Act
		MessagePayload responsePayload = given()
			.contentType(ContentType.JSON)
			.body(bean)
			.put("")
			.then()
			.statusCode(400)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(MessagePayload.class);

		// Assert
		assertEquals("ERROR", responsePayload.getLevel());
		assertEquals(
			"Das (neue) Passwort ist nicht regelkonform. Das wiederholte Passwort ist erforderlich. Die (neuen) Passwörter stimmen nicht überein. Die Passwörter stimmen nicht überein",
			responsePayload.getMessage());

	}

}
