// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.payload;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.web.commons_validation.ValidationUtils;
import de.egladil.web.commons_validation.payload.ResponsePayload;

/**
 * EmailPayloadTest
 */
public class OrderTempPasswordPayloadTest {

	private Validator validator;

	@BeforeEach
	void setUp() {

		final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	@Test
	void serialize() throws JsonProcessingException {

		// Arrange
		OrderTempPasswordPayload payload = new OrderTempPasswordPayload();
		payload.setEmail("zeze@egladil.de");
		payload
			.setClientCredentials(
				ClientCredentials.createWithState("aJDGUQQhuHQUIWHDIQ", "localhost:4200", "guenni"));

		// Act + Assert
		System.out.println(new ObjectMapper().writeValueAsString(payload));
	}

	@Test
	void should_throwValidationError_when_kleberEmptyString() throws Exception {

		// Arrange
		OrderTempPasswordPayload payload = new OrderTempPasswordPayload();
		payload.setEmail("zeze.egladil.de");
		payload
			.setClientCredentials(
				ClientCredentials.createWithState("aJDGUQQhuHQUIWHDIQ", "localhost:4200", "guenni"));
		payload.setKleber(" ");

		// Act
		final Set<ConstraintViolation<OrderTempPasswordPayload>> errors = validator.validate(payload);

		// Assert
		assertEquals(2, errors.size());
		ResponsePayload rp = new ValidationUtils().toConstraintViolationMessage(errors, OrderTempPasswordPayload.class);
		System.out.println(new ObjectMapper().writeValueAsString(rp));
	}

}
