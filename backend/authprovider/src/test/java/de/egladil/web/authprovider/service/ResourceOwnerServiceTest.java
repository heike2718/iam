// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.egladil.web.authprovider.crypto.impl.AuthCryptoServiceImpl;
import de.egladil.web.authprovider.dao.ResourceOwnerDao;
import de.egladil.web.authprovider.domain.ResourceOwner;
import de.egladil.web.authprovider.error.DuplicateEntityException;
import de.egladil.web.authprovider.payload.DuplicateAttributeType;
import io.quarkus.test.junit.QuarkusTest;

/**
 * ResourceOwnerServiceTest
 */
@QuarkusTest
public class ResourceOwnerServiceTest {

	private ResourceOwnerDao resourceOwnerDao;

	private ResourceOwnerService service;

	@BeforeEach
	public void setUp() {

		this.resourceOwnerDao = Mockito.mock(ResourceOwnerDao.class);
		this.service = new ResourceOwnerService(resourceOwnerDao, new AuthCryptoServiceImpl());
	}

	@Nested
	@DisplayName("test check for registration")
	class RegistrationTests {

		@Test
		@DisplayName("loginName unbekannt, Email unbekannt")
		void check1() {

			// Arrange
			String email = "unbekannteMail@gmx.de";
			String loginName = "unbekannterLoginName";
			Mockito.when(resourceOwnerDao.findByEmail(email)).thenReturn(Optional.empty());
			Mockito.when(resourceOwnerDao.findByLoginName(loginName)).thenReturn(Optional.empty());

			// Act
			Optional<ResourceOwner> opt = service.checkExiststAndIsConsistent(loginName, email);

			// Assert
			assertFalse(opt.isPresent());
		}

		@Test
		@DisplayName("Email bekannt, Loginname unbekannt, User aktiviert und user.loginName == user.email")
		void check2() {

			// Arrange
			String email = "bekannteMail@gmx.de";
			String loginName = "unbekannterLoginName";
			String uuid = "bjkags";

			ResourceOwner resourceOwner = ResourceOwner.createAktiviert(uuid, email, email);
			Mockito.when(resourceOwnerDao.findByEmail(email)).thenReturn(Optional.of(resourceOwner));
			Mockito.when(resourceOwnerDao.findByLoginName(loginName)).thenReturn(Optional.empty());

			// Act
			Optional<ResourceOwner> opt = service.checkExiststAndIsConsistent(loginName, email);

			// Assert
			assertTrue(opt.isPresent());
			assertEquals(resourceOwner, opt.get());

		}

		@Test
		@DisplayName("Email bekannt, LoginName unbekannt, User deaktiviert und user.loginName == user.email")
		void check3() {

			// Arrange
			String email = "bekannteMail@gmx.de";
			String loginName = "unbekannterLoginName";
			String uuid = "bjkags";

			ResourceOwner resourceOwner = ResourceOwner.createDeaktiviert(uuid, email, email);
			Mockito.when(resourceOwnerDao.findByEmail(email)).thenReturn(Optional.of(resourceOwner));
			Mockito.when(resourceOwnerDao.findByLoginName(loginName)).thenReturn(Optional.empty());

			// Act
			Optional<ResourceOwner> opt = service.checkExiststAndIsConsistent(loginName, email);

			// Assert
			assertTrue(opt.isPresent());
			assertEquals(resourceOwner, opt.get());
		}

		@Test
		@DisplayName("Email bekannt, Loginname unbekannt, User aktiviert und user.loginName !== user.email")
		void check4() {

			// Arrange
			String email = "bekannteMail@gmx.de";
			String loginName = "unbekannterLoginName";
			String uuid = "bjkags";

			ResourceOwner resourceOwner = ResourceOwner.createAktiviert(uuid, "andererLoginname", email);
			Mockito.when(resourceOwnerDao.findByEmail(email)).thenReturn(Optional.of(resourceOwner));
			Mockito.when(resourceOwnerDao.findByLoginName(loginName)).thenReturn(Optional.empty());

			// Act
			try {

				service.checkExiststAndIsConsistent(loginName, email);
				fail("keine DuplicateEntityException");
			} catch (DuplicateEntityException e) {

				assertEquals("Diese Mailadresse gibt es schon.", e.getMessage());
			}
		}

		@Test
		@DisplayName("Email bekannt, Loginname unbekannt, User deaktiviert und user.loginName !== user.email")
		void check5() {

			// Arrange
			String email = "bekannteMail@gmx.de";
			String loginName = "unbekannterLoginName";
			String uuid = "bjkags";

			ResourceOwner resourceOwner = ResourceOwner.createDeaktiviert(uuid, "andererLoginname", email);
			Mockito.when(resourceOwnerDao.findByEmail(email)).thenReturn(Optional.of(resourceOwner));
			Mockito.when(resourceOwnerDao.findByLoginName(loginName)).thenReturn(Optional.empty());

			// Act
			try {

				service.checkExiststAndIsConsistent(loginName, email);
				fail("keine DuplicateEntityException");
			} catch (DuplicateEntityException e) {

				assertEquals("Diese Mailadresse gibt es schon.", e.getMessage());
			}
		}

		@Test
		@DisplayName("Email unbekannt, Loginname bekannt")
		void check6() {

			// Arrange
			String email = "unbekannteMail@gmx.de";
			String loginName = "bekannterLoginName";
			String uuid = "jskahck";

			ResourceOwner resourceOwner = ResourceOwner.createAktiviert(uuid, loginName, "andereMail@egladil.de");

			Mockito.when(resourceOwnerDao.findByEmail(email)).thenReturn(Optional.empty());
			Mockito.when(resourceOwnerDao.findByLoginName(loginName)).thenReturn(Optional.of(resourceOwner));

			// Act
			try {

				service.checkExiststAndIsConsistent(loginName, email);
				fail("keine DuplicateEntityException");
			} catch (DuplicateEntityException e) {

				assertEquals("Diesen Loginnamen gibt es schon.", e.getMessage());
			}
		}

		@Test
		@DisplayName("Email bekannt, Loginname bekannt, userMitEmail == userMitLoginname, user aktiviert")
		void check7() {

			// Arrange
			String email = "bekannteEmail@gmx.de";
			String loginName = "bekannterLoginName";
			String uuid = "jskahck";

			ResourceOwner resourceOwner = ResourceOwner.createAktiviert(uuid, loginName, email);

			Mockito.when(resourceOwnerDao.findByEmail(email)).thenReturn(Optional.of(resourceOwner));
			Mockito.when(resourceOwnerDao.findByLoginName(loginName)).thenReturn(Optional.of(resourceOwner));

			// Act
			Optional<ResourceOwner> opt = service.checkExiststAndIsConsistent(loginName, email);

			// Assert
			assertTrue(opt.isPresent());
			assertEquals(resourceOwner, opt.get());
		}

		@Test
		@DisplayName("Email bekannt, Loginname bekannt, userMitEmail == userMitLoginname, user deaktiviert")
		void check8() {

			// Arrange
			String email = "bekannteEmail@gmx.de";
			String loginName = "bekannterLoginName";
			String uuid = "jskahck";

			ResourceOwner resourceOwner = ResourceOwner.createDeaktiviert(uuid, loginName, email);

			Mockito.when(resourceOwnerDao.findByEmail(email)).thenReturn(Optional.of(resourceOwner));
			Mockito.when(resourceOwnerDao.findByLoginName(loginName)).thenReturn(Optional.of(resourceOwner));

			// Act
			Optional<ResourceOwner> opt = service.checkExiststAndIsConsistent(loginName, email);

			// Assert
			assertTrue(opt.isPresent());
			assertEquals(resourceOwner, opt.get());
		}

		@Test
		@DisplayName("Email bekannt, Loginname bekannt, userMitEmail !== userMitLoginname")
		void check9() {

			// Arrange
			String email = "bekannteEmail@gmx.de";
			String loginName = "bekannterLoginName";
			String uuidMitEmail = "jskahck";
			String uuidMitLoginname = "hasfha";

			ResourceOwner resourceOwnerMitEmail = ResourceOwner.createAktiviert(uuidMitEmail, loginName, "andereMail@egladil.de");
			ResourceOwner resourceOwnerMitLoginname = ResourceOwner.createAktiviert(uuidMitLoginname, "andererLoginName", email);

			Mockito.when(resourceOwnerDao.findByEmail(email)).thenReturn(Optional.of(resourceOwnerMitEmail));
			Mockito.when(resourceOwnerDao.findByLoginName(loginName)).thenReturn(Optional.of(resourceOwnerMitLoginname));

			// Act
			try {

				service.checkExiststAndIsConsistent(loginName, email);
				fail("keine DuplicateEntityException");
			} catch (DuplicateEntityException e) {

				assertEquals("Diesen Loginnamen und diese Mailadresse gibt es schon.", e.getMessage());
			}
		}

		@Test
		@DisplayName("Email bekannt, Loginname bekannt, userMitEmail !== userMitLoginname")
		void check10() {

			// Arrange if (!optROEmail.isPresent() && optROLoginname.isPresent()) {
			String email = "unbekannte@gmx.de";
			String loginName = "bekannterLoginName";
			String uuidMitLoginname = "hasfha";

			ResourceOwner resourceOwnerMitLoginname = ResourceOwner.createAktiviert(uuidMitLoginname, "andererLoginName", email);

			Mockito.when(resourceOwnerDao.findByEmail(email)).thenReturn(Optional.empty());
			Mockito.when(resourceOwnerDao.findByLoginName(loginName)).thenReturn(Optional.of(resourceOwnerMitLoginname));

			// Act
			try {

				service.checkExiststAndIsConsistent(loginName, email);
				fail("keine DuplicateEntityException");
			} catch (DuplicateEntityException e) {

				assertEquals("Diesen Loginnamen gibt es schon.", e.getMessage());
			}
		}
	}

	/////////////

	@Nested
	@DisplayName("test check for change profile data")
	class ChangeDataTests {

		private static final String UUID1 = "hjlas";

		private static final String UUID2 = "lshuiuo";

		private static final String EMAIL_UNBEKANNT = "email3@web.de";

		private static final String LOGINNAME_UNBEKANNT = "loginname3";

		private final ResourceOwner person;

		private final ResourceOwner anderePerson;

		/**
		 *
		 */
		public ChangeDataTests() {

			person = new ResourceOwner();
			person.setUuid(UUID1);
			person.setAktiviert(true);
			person.setEmail("email1@web.de");
			person.setLoginName("loginname1");

			anderePerson = new ResourceOwner();
			anderePerson.setUuid(UUID2);
			anderePerson.setAktiviert(true);
			anderePerson.setEmail("email2@web.de");
			anderePerson.setLoginName("loginname2");
		}

		@Test
		void should_returnNull_when_emailAndLoginNameUnbekant() {

			// Arrange
			Mockito.when(resourceOwnerDao.findOtherUsersWithSameEmail(EMAIL_UNBEKANNT, UUID1))
				.thenReturn(Collections.emptyList());

			Mockito.when(resourceOwnerDao.findOtherUsersWithSameLoginName(LOGINNAME_UNBEKANNT, UUID1))
				.thenReturn(Collections.emptyList());

			// Act
			DuplicateAttributeType result = service.changeLoginNameAndEmailAllowed(LOGINNAME_UNBEKANNT, EMAIL_UNBEKANNT, UUID1);

			// Assert
			assertNull(result);
		}

		@Test
		void should_returnExpectedMessageKey_when_duplicateEmail() {

			// Arrange
			String loginName = "loginName";
			String email = "email@provider.com";

			Mockito.when(resourceOwnerDao.findOtherUsersWithSameEmail(email, UUID1))
				.thenReturn(Collections.singletonList(anderePerson));

			Mockito.when(resourceOwnerDao.findOtherUsersWithSameLoginName(loginName, UUID1))
				.thenReturn(Collections.emptyList());

			// Act
			DuplicateAttributeType result = service.changeLoginNameAndEmailAllowed(loginName, email, UUID1);

			// Assert
			assertEquals("BenutzerprofilResource.data.duplicate.email", result.getApplicationMessagesKey());
		}

		@Test
		void should_returnExpectedMessageKey_when_duplicateLoginName() {

			// Arrange
			String loginName = "loginName";
			String email = "email@provider.com";

			Mockito.when(resourceOwnerDao.findOtherUsersWithSameEmail(email, UUID1))
				.thenReturn(Collections.emptyList());

			Mockito.when(resourceOwnerDao.findOtherUsersWithSameLoginName(loginName, UUID1))
				.thenReturn(Collections.singletonList(anderePerson));

			// Act
			DuplicateAttributeType result = service.changeLoginNameAndEmailAllowed(loginName, email, UUID1);

			// Assert
			assertEquals("BenutzerprofilResource.data.duplicate.loginName", result.getApplicationMessagesKey());
		}

		@Test
		@DisplayName("Mail behalten, Loginname ändern zu bekanntem anderen, Mail != Loginname, andere Person aktiviert")
		void should_returnExpectedMessageKey_when_duplicateEmailAndLoginName() {

			String loginName = "loginName";
			String email = "email@provider.com";

			// Arrange
			Mockito.when(resourceOwnerDao.findOtherUsersWithSameEmail(email, UUID1))
				.thenReturn(Collections.singletonList(anderePerson));

			Mockito.when(resourceOwnerDao.findOtherUsersWithSameLoginName(loginName, UUID1))
				.thenReturn(Collections.singletonList(anderePerson));

			// Act
			DuplicateAttributeType result = service.changeLoginNameAndEmailAllowed(loginName, email, UUID1);

			// Assert
			assertEquals("BenutzerprofilResource.data.duplicate.emailAndLoginName", result.getApplicationMessagesKey());
		}
	}
}
