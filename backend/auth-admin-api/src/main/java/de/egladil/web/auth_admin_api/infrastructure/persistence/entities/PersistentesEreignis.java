// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.infrastructure.persistence.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

/**
 * PersistentesEreignis
 */
@Entity
@Table(name = "EVENTS")
public class PersistentesEreignis {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "TIME_OCCURED")
	private LocalDateTime occuredOn;

	@Column(name = "NAME")
	private String name;

	@Column(name = "BODY")
	private String body;

	@Version
	@Column(name = "VERSION")
	private int version;

	public static PersistentesEreignis createEvent(final LocalDateTime occuredOn, final String name, final String body) {

		PersistentesEreignis result = new PersistentesEreignis();
		result.body = body;
		result.name = name;
		result.occuredOn = occuredOn;

		return result;

	}

	/**
	 *
	 */
	PersistentesEreignis() {

	}

	public Long getId() {

		return id;
	}

	public LocalDateTime getOccuredOn() {

		return occuredOn;
	}

	public String getName() {

		return name;
	}

	public String getBody() {

		return body;
	}
}
