// =====================================================
// Projekt: auth-validations
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.auth_validations;

import java.text.Collator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolation;

import org.apache.commons.lang3.StringUtils;

import de.egladil.web.auth_validations.exceptions.InvalidInputException;

/**
 * ValidationHelper.
 */
public class ValidationHelper {

    /**
     * @param loggableObject
     * @param errors
     * @throws IllegalArgumentException
     * @throws InvalidInputException
     */
    public <T> void toInvalidInputException(final Set<ConstraintViolation<T>> errors) throws IllegalArgumentException {

        if (!errors.isEmpty()) {

            // Dubletten filtern und sortieren, damit in Tests vorhersagbare Reihenfolge
            // entsteht
            List<String> messages = errors
                    .stream()
                    .map(v -> v.getMessage())
                    .collect(Collectors.toSet())
                    .stream()
                    .collect(Collectors.toList());

            messages.sort(Collator.getInstance(Locale.GERMAN));

            String message = StringUtils.join(messages, ",");

            throw new InvalidInputException(message);
        }
    }
}
