// =====================================================
// Project: profil-server
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.profil_server.event;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.web.commons_net.time.CommonTimeUtils;

/**
 * UserLoggedOut
 */
public class UserLoggedOut implements ProfilEvent {

	@JsonIgnore
	private final LocalDateTime occuredOn;

	@JsonProperty
	private String uuid;

	UserLoggedOut() {

		this.occuredOn = CommonTimeUtils.now();

	}

	public UserLoggedOut(final String uuid) {

		this();
		this.uuid = uuid;
	}

	@Override
	public LocalDateTime occuredOn() {

		return this.occuredOn;
	}

	@Override
	public String typeName() {

		return TYPE_USER_LOGGED_OUT;
	}

	public String uuid() {

		return uuid;
	}

	@Override
	public boolean propagateToListeners() {

		return false;
	}

}
