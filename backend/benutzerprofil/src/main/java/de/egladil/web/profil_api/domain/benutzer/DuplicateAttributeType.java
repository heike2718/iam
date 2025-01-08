// =====================================================
// Project: benutzerprofil
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_api.domain.benutzer;

/**
 * DuplicateAttributeType
 */
public enum DuplicateAttributeType {

	EMAIL_AND_LOGINNAME(912, "duplicate.emailAndLoginName"),
	EMAIL(910, "duplicate.email"),
	LOGINNAME(911, "duplicate.loginName");

	private final int detailedStatuscode;

	private final String applicationMessagesKey;

	private DuplicateAttributeType(final int detailedStatuscode, final String applicationMessagesKey) {

		this.detailedStatuscode = detailedStatuscode;
		this.applicationMessagesKey = applicationMessagesKey;
	}

	public int getDetailedStatuscode() {

		return detailedStatuscode;
	}

	public String getApplicationMessagesKey() {

		return applicationMessagesKey;
	}

}
