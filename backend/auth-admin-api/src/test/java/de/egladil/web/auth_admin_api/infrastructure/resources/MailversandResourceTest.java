// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.egladil.web.auth_admin_api.domain.Jobstatus;
import de.egladil.web.auth_admin_api.domain.mailversand.api.MailversandauftragOverview;
import de.egladil.web.auth_admin_api.domain.mailversand.api.MailversandauftragRequestDto;
import de.egladil.web.auth_admin_api.profiles.AuthAdminTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

/**
 * MailversandResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(MailversandResource.class)
@TestProfile(AuthAdminTestProfile.class)
@TestMethodOrder(OrderAnnotation.class)
public class MailversandResourceTest {

	@Test // - wollen die DB micht vollknallen
	@Order(1)
	@TestSecurity(user = "iche", roles = { "AUTH_ADMIN" })
	void testInsert() {

		// Arrange: von 8 IDs sind 5 bestätigt
		MailversandauftragRequestDto requestPayload = new MailversandauftragRequestDto();
		String idInfomailtext = "d67e7bc6-3de7-4348-aa3d-174da70fa177";
		requestPayload.setIdInfomailtext(idInfomailtext);
		requestPayload.setBenutzerUUIDs(Arrays.asList(new String[] {
			"b2e4faab-c89d-4431-b3e9-6e45faded2c4",
			"14a8fd8e-13d9-48bd-9f3f-e86be83ee871",
			"0835e24f-d238-4c65-a1ac-92cc50d17646",
			"a4395ae1-7de8-4a6e-8e8b-6d085025816f",
			"5eaaf0ed-4949-4ccd-a9c3-2db9af3559c2",
			"868d5890-d4d0-45ab-811d-4bfc081b48ec",
			"02a76d60-0aa0-4097-a55e-2ed6d5823088",
			"009fac43-1650-4a45-8ab8-30e646f935b3" }));

		// Act
		MailversandauftragOverview responsePayload = given()
			.contentType(ContentType.JSON)
			.body(requestPayload)
			.post("auftraege")
			.then()
			.statusCode(201)
			.extract()
			.as(MailversandauftragOverview.class);

		// Assert
		assertNotNull(responsePayload.getUuid());
		assertFalse(StringUtils.isBlank(responsePayload.getBetreff()));
		assertEquals(5L, responsePayload.getAnzahlEmpfaenger());
		assertEquals(3, responsePayload.getAnzahlGruppen());
		assertEquals(Jobstatus.WAITING, responsePayload.getStatus());
		assertEquals(idInfomailtext, responsePayload.getIdInfomailtext());

	}

}
