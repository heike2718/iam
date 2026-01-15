// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import de.egladil.web.auth_validations.annotations.UuidString;

/**
 * UuidPayload
 */
public class UuidPayload {

    @UuidString
    @NotNull
    @Size(max = 42)
    private String uuid;

    public UuidPayload() {

    }

    public UuidPayload(final String uuid) {

        super();
        this.uuid = uuid;
    }

    public String getUuid() {

        return uuid;
    }

    public void setUuid(final String uuid) {

        this.uuid = uuid;
    }

}
