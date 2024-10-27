// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_api.domain.event;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UserDeleted
 */
public class UserDeleted implements ProfilEvent {

	@JsonIgnore
	private final LocalDateTime occuredOn;

	@JsonProperty
	private String uuid;

	public UserDeleted() {

		this.occuredOn = LocalDateTime.now();
	}

	public UserDeleted(final String uuid) {

		this();
		this.uuid = uuid;
	}

	@Override
	public LocalDateTime occuredOn() {

		return null;
	}

	@Override
	public boolean propagateToListeners() {

		return true;
	}

	@Override
	public String typeName() {

		return TYPE_USER_DELETED;
	}

	public String uuid() {

		return uuid;
	}

}
