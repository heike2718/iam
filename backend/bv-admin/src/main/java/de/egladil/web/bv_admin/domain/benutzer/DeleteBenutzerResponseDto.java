// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.benutzer;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DeleteBenutzerResponseDto
 */
@Schema(description = "Response-Objekt für das Löschen eines Benutzers")
public class DeleteBenutzerResponseDto {

	@JsonProperty
	@Schema(description = "UUID des Users", example = "732b2ed8-b9b7-4800-8685-38fd09d330cf")
	@Pattern(regexp = "^[abcdef\\d\\-]*$")
	@Size(max = 36)
	private String uuid;

	DeleteBenutzerResponseDto() {

		super();

	}

	public DeleteBenutzerResponseDto(@Pattern(regexp = "^[abcdef\\d\\-]*$") @Size(max = 36) final String uuid) {

		super();
		this.uuid = uuid;
	}

	public String getUuid() {

		return uuid;
	}
}
