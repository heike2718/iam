//=====================================================
// Project: authprovider
// (c) Heike Winkelvoß
//=====================================================
package de.egladil.web.authprovider.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * AuthUtilsTest
 */
public class AuthUtilsTest {

	@Nested
	@DisplayName("test roles")
	class RoleTests {

		@Test
		@DisplayName("leerer String liefert 'STANDARD'")
		void check1() {

			// Arrange
			String roles = "";

			// Act
			String result = AuthUtils.normalizeRoles(roles);
			System.out.println("rein: " + roles + ", raus: " + result);

			// Assert
			assertEquals("STANDARD", result);

		}

		@Test
		@DisplayName("null String liefert 'STANDARD'")
		void check2() {

			// Arrange
			String roles = null;

			// Act
			String result = AuthUtils.normalizeRoles(roles);
			System.out.println("rein: " + roles + ", raus: " + result);

			// Assert
			assertEquals("STANDARD", result);

		}

		@Test
		@DisplayName("Standard beliebig geschrieben liefert 'STANDARD'")
		void check3() {

			// Arrange
			String roles = "StanDArd";

			// Act
			String result = AuthUtils.normalizeRoles(roles);
			System.out.println("rein: " + roles + ", raus: " + result);

			// Assert
			assertEquals("STANDARD", result);

		}

		@Test
		@DisplayName("Standard beliebig geschrieben mit anderer Rolle liefert 'ROLLE,STANDARD'")
		void check4() {

			// Arrange
			String roles = "zAdMIN,StanDArd";

			// Act
			String result = AuthUtils.normalizeRoles(roles);
			System.out.println("rein: " + roles + ", raus: " + result);

			// Assert
			assertEquals("STANDARD,ZADMIN", result);

		}

		@Test
		@DisplayName("andere Rolle liefert 'ROLLE,STANDARD'")
		void check5() {

			// Arrange
			String roles = "AdMIN";

			// Act
			String result = AuthUtils.normalizeRoles(roles);
			System.out.println("rein: " + roles + ", raus: " + result);

			// Assert
			assertEquals("ADMIN,STANDARD", result);

		}

		@Test
		@DisplayName("Dubletten werden entfernt, STANDARD wird ergänzt")
		void check6() {

			// Arrange
			String roles = "AdMIN,admin";

			// Act
			String result = AuthUtils.normalizeRoles(roles);
			System.out.println("rein: " + roles + ", raus: " + result);

			// Assert
			assertEquals("ADMIN,STANDARD", result);

		}

		@Test
		@DisplayName("Dubletten werden entfernt")
		void check7() {

			// Arrange
			String roles = "StanDArd,AdMIN,admin,STANDard";

			// Act
			String result = AuthUtils.normalizeRoles(roles);
			System.out.println("rein: " + roles + ", raus: " + result);

			// Assert
			assertEquals("ADMIN,STANDARD", result);

		}
	}

}
