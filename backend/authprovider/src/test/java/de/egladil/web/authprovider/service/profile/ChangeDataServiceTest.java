//=====================================================
// Project: authprovider
// (c) Heike Winkelvoß
//=====================================================
package de.egladil.web.authprovider.service.profile;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import de.egladil.web.authprovider.entities.ResourceOwner;

/**
 *
 */
public class ChangeDataServiceTest {

	final ChangeDataService service = new ChangeDataService();

	@Test
	void should_getResetBanFlag_returnFalse_when_notBannedForMails() {

		// arrange
		ResourceOwner ro = new ResourceOwner();
		ro.setEmail("bla@the-provider.to");
		ro.setBannedForMails(false);

		String email = "blubb@provider.com";

		// act
		boolean result = service.getResetBanFlag(ro, email);

		// assert
		assertFalse(result);

	}

	@Test
	void should_getResetBanFlag_returnFalse_when_mailsAreEqual() {

		// arrange
		ResourceOwner ro = new ResourceOwner();
		ro.setEmail("blubb@provider.com");
		ro.setBannedForMails(true);

		String email = " blubb@provider.com ";

		// act
		boolean result = service.getResetBanFlag(ro, email);

		// assert
		assertFalse(result);

	}

	@Test
	void should_getResetBanFlag_returnFalse_when_sameDomain() {

		// arrange
		ResourceOwner ro = new ResourceOwner();
		ro.setEmail("bla@provider.com");
		ro.setBannedForMails(true);

		String email = " blubb@provider.com ";

		// act
		boolean result = service.getResetBanFlag(ro, email);

		// assert
		assertFalse(result);

	}

	@Test
	void should_getResetBanFlag_returnFalse_when_domainUnklar() {

		// arrange
		ResourceOwner ro = new ResourceOwner();
		ro.setEmail("bla@provider.com");
		ro.setBannedForMails(true);

		String email = " blubb-@bla@provider.com ";

		// act
		boolean result = service.getResetBanFlag(ro, email);

		// assert
		assertFalse(result);

	}

	@Test
	void should_getResetBanFlag_returnTrue_when_differentDomains() {

		// arrange
		ResourceOwner ro = new ResourceOwner();
		ro.setUuid(UUID.randomUUID().toString());
		ro.setEmail("bla@the-provider.com");
		ro.setBannedForMails(true);

		String email = " blubb@provider.com ";

		// act
		boolean result = service.getResetBanFlag(ro, email);

		// assert
		assertTrue(result);

	}

}
