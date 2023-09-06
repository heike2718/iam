// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Salt
 */
@Entity()
@Table(name = "SLZ")
public class Salt implements AuthProviderEntity {

	/* serialVersionUID */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	@JsonIgnore
	private Long id;

	@Column(name = "ALGORITHM", length = 10)
	@NotBlank
	private String algorithmName;

	@Column(name = "ROUNDS")
	private int iterations;

	@Column(name = "WERT", length = 1000)
	private String wert;

	@Version
	@Column(name = "VERSION")
	@JsonIgnore
	private int version;

	@Override
	public Long getId() {

		return this.id;
	}

	public String getAlgorithmName() {

		return algorithmName;
	}

	public void setAlgorithmName(final String algorithmName) {

		this.algorithmName = algorithmName;
	}

	public int getIterations() {

		return iterations;
	}

	public void setIterations(final int iterations) {

		this.iterations = iterations;
	}

	public String getWert() {

		return wert;
	}

	public void setWert(final String wert) {

		this.wert = wert;
	}

	public int getVersion() {

		return version;
	}

	public void setVersion(final int version) {

		this.version = version;
	}

	public void setId(final Long id) {

		this.id = id;
	}

}
