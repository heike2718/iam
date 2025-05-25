// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.egladil.web.bv_admin.profiles.AuthAdminTestProfile;
import io.quarkus.mailer.MockMailbox;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.vertx.ext.mail.MailMessage;
import jakarta.inject.Inject;

/**
 * MailingResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(MailingResource.class)
@TestProfile(AuthAdminTestProfile.class)
public class MailingResourceTest {

	@ConfigProperty(name = "emails.standardempfaenger")
	private String standardempfaenger;

	@Inject
	MockMailbox mailbox;

	@BeforeEach
	void init() {

		mailbox.clear();
	}

	@Test
	void testWithoutRoles() {

		given().when().queryParam("to", standardempfaenger).get("/testmail").then().statusCode(401);
	}

	@Test
	@TestSecurity(user = "iche", roles = { "STANDARD", "ADMIN" })
	void testRolesAllowed() {

		given().when().queryParam("to", standardempfaenger).get("/testmail").then().statusCode(403);
	}

	@Test
	@TestSecurity(user = "iche", roles = { "AUTH_ADMIN" })
	void testTextMail() {

		given().when().queryParam("to", standardempfaenger).get("/testmail").then().statusCode(202);

		// Assert
		List<String> expectedEmpfaenger = Arrays.asList(StringUtils.split(standardempfaenger, ","));

		{

			List<MailMessage> mailMessagesSentTo = mailbox.getMailMessagesSentTo(expectedEmpfaenger.get(0));
			assertEquals(2, mailMessagesSentTo.size());
		}

		{

			List<MailMessage> mailMessagesSentTo = mailbox.getMailMessagesSentTo(expectedEmpfaenger.get(1));
			assertEquals(1, mailMessagesSentTo.size());
		}
	}
}
