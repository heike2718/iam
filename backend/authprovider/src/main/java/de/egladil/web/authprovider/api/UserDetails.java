package de.egladil.web.authprovider.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User.
 */
public class UserDetails {

    @JsonProperty
    private String vorname;

    @JsonProperty
    private String nachname;

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
}
