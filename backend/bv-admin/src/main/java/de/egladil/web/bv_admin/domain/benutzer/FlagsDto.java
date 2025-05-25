//=====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
//=====================================================
package de.egladil.web.bv_admin.domain.benutzer;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO mit Werten für alle togglebaren Flags")
public class FlagsDto {

	@NotNull
	private Boolean aktiviert;

	@NotNull
	private Boolean bannedForMail;

	@NotNull
	private Boolean darfNichtGeloeschtWerden;

}
