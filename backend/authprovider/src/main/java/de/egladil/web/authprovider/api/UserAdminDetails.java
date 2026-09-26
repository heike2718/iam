package de.egladil.web.authprovider.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UserAdminDetails
 */
public class UserAdminDetails {

    @JsonProperty
    private String vorname;

    @JsonProperty
    private String nachname;

    @JsonProperty
    private String email;

    @JsonProperty
    private boolean bannedForMails;

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isBannedForMails() {
        return bannedForMails;
    }

    public void setBannedForMails(boolean bannedForMails) {
        this.bannedForMails = bannedForMails;
    }

}
