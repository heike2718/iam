// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.profiles;

import java.util.HashMap;
import java.util.Map;

import io.quarkus.test.junit.QuarkusTestProfile;

/**
 * BVAdminTestProfile
 */
public class BVAdminTestProfile implements QuarkusTestProfile {

	@Override
	public Map<String, String> getConfigOverrides() {

		Map<String, String> configOverrides = new HashMap<>();
		configOverrides.put("quarkus.http.test.timeout", "600S");
		configOverrides.put("quarkus.http.test.timeout", "600S");
		configOverrides.put("quarkus.mailer.mock", "true");
		configOverrides.put("emails.standardempfaenger", "empfaenger-1@gmx.de,empfaenger-2@gmx.de");

		System.out.println("config overridden:");

		return configOverrides;
	}

}
