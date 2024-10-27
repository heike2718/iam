// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_api.domain.event;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UserLoggedIn
 */
public class UserLoggedIn implements ProfilEvent {

	@JsonIgnore
	private final LocalDateTime occuredOn;

	@JsonProperty
	private String uuid;

	UserLoggedIn() {

		this.occuredOn = LocalDateTime.now();

	}

	public UserLoggedIn(final String uuid) {

		this();
		this.uuid = uuid;
	}

	@Override
	public LocalDateTime occuredOn() {

		return this.occuredOn;
	}

	@Override
	public String typeName() {

		return TYPE_USER_LOGGED_IN;
	}

	@Override
	public boolean propagateToListeners() {

		return false;
	}

}
