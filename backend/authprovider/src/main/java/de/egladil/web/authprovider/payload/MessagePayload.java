// =====================================================
// Projekt: commons-validation
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * MessagePayload
 */
@XmlRootElement(name = "gesamtpunktverteilung")
@XmlAccessorType(XmlAccessType.FIELD)
public class MessagePayload {

	@XmlElement
	@JsonProperty
	private String level;

	@XmlElement
	@JsonProperty
	private String message;

	/**
	 * Erzeugt eine Instanz von MessagePayload
	 */
	public MessagePayload() {

	}

	/**
	 * Erzeugt eine Instanz von MessagePayload
	 */
	private MessagePayload(final String level, final String message) {

		super();
		this.level = level;
		this.message = message;
	}

	@Override
	public String toString() {

		return "MessagePayload [level=" + level + ", message=" + message + "]";
	}

	public String getLevel() {

		return level;
	}

	void setLevel(final String level) {

		this.level = level;
	}

	public String getMessage() {

		return message;
	}

	void setMessage(final String message) {

		this.message = message;
	}

	@JsonIgnore
	public boolean isOk() {

		return "INFO".equals(level);
	}

	/**
	 * INFO-MessagePalyod mit Text 'ok'.
	 *
	 * @return
	 */
	public static MessagePayload ok() {

		return info("ok");
	}

	public static MessagePayload info(final String message) {

		return new MessagePayload("INFO", message);
	}

	public static MessagePayload warn(final String message) {

		return new MessagePayload("WARN", message);
	}

	public static MessagePayload error(final String message) {

		return new MessagePayload("ERROR", message);
	}

}
