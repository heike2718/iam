// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import de.egladil.web.bv_admin.domain.SortDirection;
import de.egladil.web.bv_admin.domain.benutzer.BenutzerSearchResult;
import de.egladil.web.bv_admin.domain.benutzer.BenutzerSuchparameter;
import de.egladil.web.bv_admin.domain.benutzer.BenutzerTrefferlisteItem;
import de.egladil.web.bv_admin.domain.benutzer.UsersSortColumn;
import de.egladil.web.bv_admin.domain.validation.ValidationErrorResponseDto;
import de.egladil.web.bv_admin.profiles.AuthAdminTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

/**
 * BenutzerResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(BenutzerResource.class)
@TestProfile(AuthAdminTestProfile.class)
public class BenutzerResourceTest {

	@Test
	@TestSecurity(user = "iche", roles = { "AUTH_ADMIN" })
	void should_validateInput() {

		BenutzerSuchparameter dto = new BenutzerSuchparameter();
		dto.setAenderungsdatum("abscefg:hijklmn:stuv");
		dto.setEmail(
			"Rainer Rainer Rainer Rainer Rainer Rainer Rainer Rainer Rainer Rainer Rainer Γεια σας Rainer Rainerja Rainer Rainer Rainer Rainer Rainer Rainer Rainer Rainer Rainer Rainer Rainer Γεια σας Rainer Rainerja Rainer Rainer Rainer Rainer Rainer Rainer Rainer Rai");

		dto.setNachname("Rainer Rainer Rainer Rainer Rainer Rainer Rainer Rainer Rainer Rainer Rainer Γεια σας Rainer Rainerja");
		dto.setRolle(
			"STANDARD und ADMIN STANDARD und ADMIN STANDARD und ADMIN STANDARD und ADMIN STANDARD und ADMIN STANDARD und ADMIN STANDARD und ADMIN STANDARD und ADMIN");
		dto.setUuid("732b2ed8xy-732b2ed8-732b2ed8-732b2ed823");
		dto.setVorname("Rainer Rainer Rainer Rainer Rainer Rainer Rainer Rainer Rainer Rainer Rainer Γεια σας Rainer Rainerja");

		ValidationErrorResponseDto[] responsePayload = given().contentType(ContentType.JSON).body(dto).post("").then()
			.statusCode(400).extract().as(ValidationErrorResponseDto[].class);

		assertEquals(6, responsePayload.length);

	}

	@Test
	@TestSecurity(user = "iche", roles = { "AUTH_ADMIN" })
	void should_returnTheResultList() {

		BenutzerSuchparameter dto = new BenutzerSuchparameter();
		dto.setUuid("7");
		dto.setSortByLabelname(UsersSortColumn.EMAIL.getLabel());
		dto.setPageIndex(2);
		dto.setPageSize(11);

		BenutzerSearchResult responsePayload = given().contentType(ContentType.JSON).body(dto).post("").then().statusCode(200)
			.extract().as(BenutzerSearchResult.class);

		assertTrue(responsePayload.getAnzahlGesamt() > 11);
		List<BenutzerTrefferlisteItem> items = responsePayload.getItems();
		assertEquals(11, items.size());

		long anzahlMit7 = items.stream().filter(i -> i.getUuid().contains("7")).count();
		assertEquals(11, anzahlMit7);

	}

	@Test
	@TestSecurity(user = "iche", roles = { "AUTH_ADMIN" })
	void testRequestFromFrontend() {

		BenutzerSuchparameter dto = new BenutzerSuchparameter();
		dto.setUuid("");
		dto.setAenderungsdatum("");
		dto.setEmail("");
		dto.setNachname("test");
		dto.setRolle("");
		dto.setSortDirection(SortDirection.desc);
		dto.setVorname("");
		dto.setSortByLabelname(UsersSortColumn.DATE_MODIFIED_STRING.getLabel());

		BenutzerSearchResult responsePayload = given().contentType(ContentType.JSON).body(dto).post("").then().statusCode(200)
			.extract().as(BenutzerSearchResult.class);

		assertTrue(responsePayload.getAnzahlGesamt() > 25);
		List<BenutzerTrefferlisteItem> items = responsePayload.getItems();
		assertEquals(25, items.size());

		long anzahlMitTest = items.stream().filter(i -> i.getNachname().toLowerCase().contains("test")).count();
		assertEquals(25, anzahlMitTest);

	}

	@Test
	@TestSecurity(user = "iche", roles = { "AUTH_ADMIN" })
	void should_returnSortByDatumGeaendert_page3() {

		BenutzerSuchparameter dto = new BenutzerSuchparameter();
		dto.setSortByLabelname(UsersSortColumn.DATE_MODIFIED_STRING.getLabel());
		dto.setPageIndex(2);
		dto.setPageSize(25);

		BenutzerSearchResult responsePayload = given().contentType(ContentType.JSON).body(dto).post("").then().statusCode(200)
			.extract().as(BenutzerSearchResult.class);

		assertEquals(232, responsePayload.getAnzahlGesamt());
		List<BenutzerTrefferlisteItem> items = responsePayload.getItems();
		assertEquals(25, items.size());

		{

			BenutzerTrefferlisteItem item = items.get(0);
			assertEquals("e5807325-e14d-4f74-8b52-e6b289e48d51", item.getUuid());
			assertEquals("2020-04-19 15:58:59", item.getAenderungsdatum());
		}

		{

			BenutzerTrefferlisteItem item = items.get(24);
			assertEquals("057ec7aa-65c2-4bcb-8a9e-8d3689cdb8ea", item.getUuid());
			assertEquals("2020-08-09 15:13:57", item.getAenderungsdatum());
		}

	}

	@Test
	@TestSecurity(user = "iche", roles = { "AUTH_ADMIN" })
	void should_NotExcludeAdminsFromResultList_anyMore() {

		BenutzerSuchparameter dto = new BenutzerSuchparameter();
		dto.setVorname("checki");
		dto.setPageIndex(1);
		dto.setPageSize(50);

		BenutzerSearchResult responsePayload = given().contentType(ContentType.JSON).body(dto).post("").then().statusCode(200)
			.extract().as(BenutzerSearchResult.class);

		assertEquals(1, responsePayload.getAnzahlGesamt());
		List<BenutzerTrefferlisteItem> items = responsePayload.getItems();
		assertEquals(1, items.size());

	}
}
