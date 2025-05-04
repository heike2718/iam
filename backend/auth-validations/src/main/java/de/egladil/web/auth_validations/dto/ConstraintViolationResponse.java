// =====================================================
// Project: auth-validations
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_validations.dto;

import java.util.List;

/**
 * ConstraintViolationResponse
 */
public class ConstraintViolationResponse {

	private String title;

	private int status;

	private List<CustomConstraintViolation> violations;

	public String getTitle() {

		return title;
	}

	public void setTitle(final String title) {

		this.title = title;
	}

	public int getStatus() {

		return status;
	}

	public void setStatus(final int status) {

		this.status = status;
	}

	public List<CustomConstraintViolation> getViolations() {

		return violations;
	}

	public void setViolations(final List<CustomConstraintViolation> violations) {

		this.violations = violations;
	}

}
