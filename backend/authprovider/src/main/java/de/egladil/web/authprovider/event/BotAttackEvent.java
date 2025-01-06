// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.event;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * BotAttackEvent
 */
public class BotAttackEvent implements AuthproviderEvent {

	private static final Logger LOGGER = LoggerFactory.getLogger(BotAttackEvent.class);

	private BotAttackEventPayload payload;

	BotAttackEvent() {

	}

	public BotAttackEvent(final BotAttackEventPayload payload) {

		this.payload = payload;
	}

	@Override
	public boolean writeToEventStore() {

		return true;
	}

	@Override
	public boolean writeToServerLog() {

		return true;
	}

	@Override
	public boolean propagateToListeners() {

		return false;
	}

	@Override
	public AuthproviderEventType eventType() {

		return AuthproviderEventType.BOT_ATTACK;
	}

	@Override
	public Object payload() {

		return this.payload;
	}

	@Override
	public String serializePayload() {

		try {

			return new ObjectMapper().writeValueAsString(payload);

		} catch (JsonProcessingException e) {

			LOGGER.error("konnte event nicht serialisieren: " + e.getMessage(), e);

			return null;
		}
	}

	@Override
	public LocalDateTime occuredOn() {

		return LocalDateTime.now();
	}

	@Override
	public void writeToConsoleQuietly() {

		try {

			String body = new ObjectMapper().writeValueAsString(payload);
			LOGGER.error(body);
		} catch (JsonProcessingException e) {

			e.printStackTrace();
			LOGGER.error(e.getMessage(), e);
		}
	}

}
