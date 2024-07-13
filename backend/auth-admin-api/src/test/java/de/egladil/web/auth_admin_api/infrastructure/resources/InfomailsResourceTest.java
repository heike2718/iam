// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.egladil.web.auth_admin_api.domain.infomails.InfomailRequestDto;
import de.egladil.web.auth_admin_api.domain.infomails.InfomailResponseDto;
import de.egladil.web.auth_admin_api.domain.infomails.UpdateInfomailResponseDto;
import de.egladil.web.auth_admin_api.profiles.AuthAdminTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

/**
 * InfomailsResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(InfomailsResource.class)
@TestProfile(AuthAdminTestProfile.class)
@TestMethodOrder(OrderAnnotation.class)
public class InfomailsResourceTest {

	// @Test - wollen die DB micht vollknallen
	@Order(1)
	@TestSecurity(user = "iche", roles = { "AUTH_ADMIN" })
	void testInsert() {

		// Arrange
		InfomailRequestDto requestPayload = new InfomailRequestDto();
		requestPayload.setBetreff("Testmailtext Nummer 1");
		requestPayload
			.setMailtext("Dies ist der Text einer ersten Testmail, damit man in der GUI auch eine Treffermenge angezeigt bekommt");

		// Act
		InfomailResponseDto responsePayload = given()
			.contentType(ContentType.JSON)
			.body(requestPayload)
			.post("")
			.then()
			.statusCode(201)
			.extract()
			.as(InfomailResponseDto.class);

		// Assert
		assertNotNull(responsePayload.getUuid());
		assertEquals(requestPayload.getBetreff(), responsePayload.getMailtext());
		assertEquals(requestPayload.getMailtext(), responsePayload.getMailtext());
		assertEquals(0, responsePayload.getUuidsMailversandauftraege().size());

	}

	@Test
	@Order(2)
	@TestSecurity(user = "iche", roles = { "AUTH_ADMIN" })
	void testUpdate() {

		// Arrange
		String uuid = "81188afe-abfa-4870-ae27-98259fdf36aa";
		InfomailRequestDto requestPayload = new InfomailRequestDto();
		requestPayload.setBetreff("Testmail Nummer 1 - bitte damit keine Versandaufträge erzeugen!");
		requestPayload
			.setMailtext(
				"Der Text wird jetzt geändert. Versandaufträge sollten damit nicht erzeugt werden, da sonst der size()-Test fehlschlägt. Mit freundlichen Grüßen.");

		// Act
		UpdateInfomailResponseDto responsePayload = given()
			.contentType(ContentType.JSON)
			.body(requestPayload)
			.put("/" + uuid)
			.then()
			.statusCode(200)
			.extract()
			.as(UpdateInfomailResponseDto.class);

		// Assert
		assertEquals(uuid, responsePayload.getUuid());
		assertNotNull(responsePayload.getInfomail());
		InfomailResponseDto infomailResponseDto = responsePayload.getInfomail();

		assertEquals(uuid, infomailResponseDto.getUuid());
		assertEquals(requestPayload.getBetreff(), infomailResponseDto.getBetreff());
		assertEquals(requestPayload.getMailtext(), infomailResponseDto.getMailtext());
		assertEquals(0, infomailResponseDto.getUuidsMailversandauftraege().size());
	}

	@Test
	@Order(3)
	@TestSecurity(user = "iche", roles = { "AUTH_ADMIN" })
	void testLoadInfomailTexte() {

		// Arrange
		final String uuid = "81188afe-abfa-4870-ae27-98259fdf36aa";

		InfomailResponseDto[] responsePayload = given()
			.get("")
			.then()
			.statusCode(200)
			.extract()
			.as(InfomailResponseDto[].class);

		// Assert
		assertTrue(responsePayload.length >= 2);

		Optional<InfomailResponseDto> opt = Arrays.stream(responsePayload).filter(i -> uuid.equals(i.getUuid())).findFirst();
		assertTrue(opt.isPresent());

	}

}
