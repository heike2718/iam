// =====================================================
// Project: profil-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_api.domain.event;

import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.web.profil_api.domain.benutzer.BenutzerDto;

/**
 * UserChanged
 */
public class UserChanged implements ProfilEvent {

	@JsonIgnore
	private final LocalDateTime occuredOn;

	@JsonProperty
	private String uuid;

	@JsonProperty
	private String vorname;

	@JsonProperty
	private String nachname;

	@JsonProperty
	private String email;

	public UserChanged() {

		this.occuredOn = LocalDateTime.now();
	}

	public UserChanged(final String uuid, final BenutzerDto user) {

		this();
		this.uuid = uuid;
		this.email = user.getEmail().trim();
		this.vorname = user.getVorname();
		this.nachname = user.getNachname();

	}

	@Override
	public LocalDateTime occuredOn() {

		return this.occuredOn;
	}

	@Override
	public String typeName() {

		return TYPE_USER_CHANGED;
	}

	@Override
	public boolean propagateToListeners() {

		return true;
	}

	public String uuid() {

		return uuid;
	}

	public String vorname() {

		return vorname != null ? vorname.trim() : null;
	}

	public String nachname() {

		return nachname != null ? nachname.trim() : null;
	}

	public String email() {

		return email;
	}

	@Override
	public String toString() {

		return "UserChanged [uuid=" + StringUtils.abbreviate(uuid, 11) + ", vorname=" + vorname + ", nachname=" + nachname
			+ ", email=" + StringUtils.abbreviate(email, 8) + "]";
	}

}
