// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.mailversand.api;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * SingleUuidDto
 */
@Schema(description = "Response-Objekt für das Löschen eines Benutzers")
public class SingleUuidDto {

	@JsonProperty
	@Schema(description = "UUID des Mailversandauftrags", example = "732b2ed8-b9b7-4800-8685-38fd09d330cf")
	@Pattern(regexp = "^[abcdef\\d\\-]*$")
	@Size(max = 36)
	private String uuid;

	SingleUuidDto() {

		super();

	}

	public SingleUuidDto(@Pattern(regexp = "^[abcdef\\d\\-]*$") @Size(max = 36) final String uuid) {

		super();
		this.uuid = uuid;
	}

	public String getUuid() {

		return uuid;
	}

}
