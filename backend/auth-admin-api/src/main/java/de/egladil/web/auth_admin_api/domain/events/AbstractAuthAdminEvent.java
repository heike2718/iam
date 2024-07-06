// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.events;

import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * AbstractAuthAdminEvent
 */
public abstract class AbstractAuthAdminEvent implements AuthAdminEvent {

	private final LocalDateTime occuredOn;

	private AuthAdminEventPayload eventPayload;

	public AbstractAuthAdminEvent() {

		this.occuredOn = LocalDateTime.now();
	}

	public AbstractAuthAdminEvent(final AuthAdminEventPayload eventPayload) {

		this();
		this.eventPayload = eventPayload;
	}

	@Override
	public String serializePayload() {

		return new AuthproviderEventPayloadSerializer().apply(eventPayload);
	}

	@Override
	public Object payload() {

		return eventPayload;
	}

	@Override
	public LocalDateTime occuredOn() {

		return this.occuredOn;
	}

	@Override
	public void writeToConsoleQuietly() {

		try {

			String body = new ObjectMapper().writeValueAsString(eventPayload);
			System.out.println(eventType().getLabel() + ": " + body);
		} catch (JsonProcessingException e) {

			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}

	protected void setEventPayload(final AuthAdminEventPayload resourceOwner) {

		this.eventPayload = resourceOwner;
	}
}
