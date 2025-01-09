// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.infrastructure.error;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import de.egladil.web.bv_admin.domain.validation.ValidationErrorResponseDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * ConstraintViolationMapper
 */
@Provider
public class ConstraintViolationMapper implements ExceptionMapper<ConstraintViolationException> {

	@Override
	public Response toResponse(final ConstraintViolationException exception) {

		Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();

		final List<ValidationErrorResponseDto> errors = new ArrayList<>();

		constraintViolations.forEach(cv -> {

			String fieldName = getFieldName(cv);
			String message = cv.getMessage();
			errors.add(new ValidationErrorResponseDto(fieldName, message));
		});

		return Response.status(400).entity(groupByFields(errors)).build();
	}

	String getFieldName(final ConstraintViolation<?> constraintViolation) {

		String propertyPath = constraintViolation.getPropertyPath().toString();
		String[] tokens = StringUtils.split(propertyPath, ".");
		return tokens[tokens.length - 1];
	}

	List<ValidationErrorResponseDto> groupByFields(final List<ValidationErrorResponseDto> errors) {

		List<ValidationErrorResponseDto> result = new ArrayList<>();

		Map<String, List<String>> fieldNamesMessagesMap = new HashMap<>();

		for (ValidationErrorResponseDto error : errors) {

			String fieldName = error.getFieldName();

			List<String> messages = fieldNamesMessagesMap.get(fieldName);

			if (messages == null) {

				messages = new ArrayList<>();
			}
			messages.add(error.getMessage());

			fieldNamesMessagesMap.put(fieldName, messages);

		}

		for (String fieldName : fieldNamesMessagesMap.keySet()) {

			String message = StringUtils.join(fieldNamesMessagesMap.get(fieldName), ", ");
			result.add(new ValidationErrorResponseDto(fieldName, message));
		}

		return result;

	}

}
