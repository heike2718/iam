// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.service.mail;

import de.egladil.web.authprovider.event.BotAttackEventPayload;

/**
 * BotAttackEventMailStrategy
 */
public class BotAttackEventMailStrategy implements CreateDefaultMailDatenStrategy {

	private final String stage;

	private final String mailTo;

	private final BotAttackEventPayload eventPayload;

	public BotAttackEventMailStrategy(final BotAttackEventPayload eventPayload, final String stage, final String mailTo) {

		super();
		this.stage = stage;
		this.mailTo = mailTo;
		this.eventPayload = eventPayload;
	}

	@Override
	public DefaultEmailDaten createEmailDaten(final String messageId) {

		DefaultEmailDaten maildaten = new DefaultEmailDaten();
		maildaten.setEmpfaenger(mailTo);
		maildaten.setBetreff(stage + ": jemand ist in den Honigtopf gefallen");
		maildaten.setText(eventPayload.toString());
		maildaten.setMessageId(messageId);
		return maildaten;
	}

}
