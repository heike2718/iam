// =====================================================
// Projekt: authenticationprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.config;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * PasswordConfig ist der password-config-Part in auth-provider-config.yml.
 */
@ApplicationScoped
public class PasswordConfig {

	@ConfigProperty(name = "crypto.pepper")
	String pepper;

	@ConfigProperty(name = "crypto.algorithm")
	String cryptoAlgorithm;

	@ConfigProperty(name = "crypto.iterations")
	int iterations;

	@Override
	public String toString() {

		return "PasswordConfig [pepper=" + pepper + ", cryptoAlgorithm=" + cryptoAlgorithm + ", iterations=" + iterations
			+ ", randomAlgorithm=" + randomAlgorithm + ", tempPwdLength=" + tempPwdLength + ", tempPwdCharPool=" + tempPwdCharPool
			+ ", tempPwdUrl=" + tempPwdUrl + "]";
	}

	@ConfigProperty(name = "crypto.random-algorithm")
	String randomAlgorithm;

	@ConfigProperty(name = "temp-pwd.length")
	int tempPwdLength;

	@ConfigProperty(name = "temp-pwd.char-pool")
	String tempPwdCharPool;

	@ConfigProperty(name = "temp-pwd.url")
	String tempPwdUrl;

	public String getPepper() {

		return pepper;
	}

	public String getCryptoAlgorithm() {

		return cryptoAlgorithm;
	}

	public int getIterations() {

		return iterations;
	}

	public String getRandomAlgorithm() {

		return randomAlgorithm;
	}

	public void setPepper(final String pepper) {

		this.pepper = pepper;
	}

	public void setCryptoAlgorithm(final String cryptoAlgorithm) {

		this.cryptoAlgorithm = cryptoAlgorithm;
	}

	public void setIterations(final int iterations) {

		this.iterations = iterations;
	}

	public void setRandomAlgorithm(final String randomAlgorithm) {

		this.randomAlgorithm = randomAlgorithm;
	}

	public int getTempPwdLength() {

		return tempPwdLength;
	}

	public void setTempPwdLength(final int tempPwdLength) {

		this.tempPwdLength = tempPwdLength;
	}

	public String getTempPwdCharPool() {

		return tempPwdCharPool;
	}

	public void setTempPwdCharPool(final String tempPwdCharPool) {

		this.tempPwdCharPool = tempPwdCharPool;
	}

	public String getTempPwdUrl() {

		return tempPwdUrl;
	}

	public void setTempPwdUrl(final String tempPwdUrl) {

		this.tempPwdUrl = tempPwdUrl;
	}

}
