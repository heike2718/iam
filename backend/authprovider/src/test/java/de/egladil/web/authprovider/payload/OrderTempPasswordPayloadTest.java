// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.payload;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * EmailPayloadTest
 */
@QuarkusTest
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

		// Act + Assert
		System.out.println(new ObjectMapper().writeValueAsString(payload));
	}

	@Test
	void should_throwValidationError_when_kleberEmptyString() throws Exception {

		// Arrange
		OrderTempPasswordPayload payload = new OrderTempPasswordPayload();
		payload.setEmail("zeze.egladil.de");
		payload.setKleber(" ");

		// Act
		final Set<ConstraintViolation<OrderTempPasswordPayload>> errors = validator.validate(payload);

		// Assert
		assertEquals(1, errors.size());
	}

}
