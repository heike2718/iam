// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.payload.profile;

import jakarta.validation.constraints.NotNull;

import de.egladil.web.auth_validations.annotations.UuidString;
import de.egladil.web.auth_validations.dto.OAuthClientCredentials;

/**
 * ChangeProfileDataPayload
 */
public class ChangeProfileDataPayload {

    @NotNull
    private OAuthClientCredentials clientCredentials;

    @NotNull
    private ProfileDataPayload profileData;

    @NotNull
    @UuidString
    private String uuid;

    public OAuthClientCredentials getClientCredentials() {

        return clientCredentials;
    }

    public ProfileDataPayload getProfileData() {

        return profileData;
    }

    public String getUuid() {

        return uuid;
    }

    public void clean() {

        if (clientCredentials != null) {

            clientCredentials.clean();
        }
    }

}
