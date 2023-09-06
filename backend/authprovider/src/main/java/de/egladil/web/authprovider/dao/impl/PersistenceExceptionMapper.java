// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.dao.impl;

import org.hibernate.exception.ConstraintViolationException;

import de.egladil.web.authprovider.error.AuthPersistenceException;
import de.egladil.web.authprovider.error.ConcurrentUpdateException;
import de.egladil.web.authprovider.error.DuplicateEntityException;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.RollbackException;

/**
 * PersistenceExceptionMapper
 */
public class PersistenceExceptionMapper {

	/**
	 * Wandelt die PersistenceException in eine geeignete um.
	 *
	 * @param  exception
	 *                   PersistenceException
	 * @return           RuntimeException
	 */
	public static RuntimeException mapException(final Exception exception) {

		String exceptionClassName = exception.getClass().getName();
		Throwable cause = exception.getCause();

		if (RollbackException.class.getName().equals(exceptionClassName)) {

			if (cause instanceof OptimisticLockException) {

				return new ConcurrentUpdateException(exception.getMessage());
			}

			if (cause instanceof PersistenceException) {

				return handlePersistenceException((PersistenceException) cause);
			}
		}

		if (exception instanceof ConstraintViolationException) {

			ConstraintViolationException cve = (ConstraintViolationException) cause;
			String constraintName = cve.getConstraintName().toLowerCase();

			if (constraintName.startsWith("uk_")) {

				throw new DuplicateEntityException(getDuplicateEntityMessage(constraintName));
			}

		}

		if (!(exception instanceof PersistenceException)) {

			return new AuthPersistenceException(exception.getMessage(), exception);
		}

		return handlePersistenceException((PersistenceException) exception);
	}

	private static RuntimeException handlePersistenceException(final PersistenceException exception) {

		Throwable cause = exception.getCause();

		if (cause instanceof OptimisticLockException) {

			return new ConcurrentUpdateException(exception.getMessage());
		}

		if (cause instanceof ConstraintViolationException) {

			ConstraintViolationException cve = (ConstraintViolationException) cause;
			String constraintName = cve.getConstraintName().toLowerCase();

			if (constraintName.startsWith("uk_")) {

				return new DuplicateEntityException(getDuplicateEntityMessage(constraintName));
			}

		}

		return new AuthPersistenceException(exception.getMessage(), exception);

	}

	private static String getDuplicateEntityMessage(final String constraintName) {

		if ("uk_accesstokens_1".equals(constraintName)) {

			return "ClientAccessToken mit gleichem Eintrag in ACCESS_TOKEN existiert schon.";
		}

		if ("uk_accesstokens_2".equals(constraintName)) {

			return "ClientAccessToken mit gleichem Eintrag in REFRESH_TOKEN existiert schon.";
		}

		if ("uk_activationcodes_1".equals(constraintName)) {

			return "ActivationCode mit gleichem Eintrag in CONFIRM_CODE existiert schon.";
		}

		if ("uk_clients_1".equals(constraintName)) {

			return "Client mit gleicher ClientID existiert schon.";
		}

		if ("uk_clients_2".equals(constraintName)) {

			return "Client mit gleichem Namen existiert schon.";
		}

		if ("uk_temppwds_1".equals(constraintName)) {

			return "TempPassword mit gleichem Eintrag in TOKEN_ID existiert schon.";
		}

		if ("uk_users_1".equals(constraintName)) {

			return "Es gibt bereits ein Benutzerkonto mit diesem Loginnamen.";
		}

		if ("uk_users_2".equals(constraintName)) {

			return "Es gibt bereits ein Benutzerkonto mit dieser Mailadresse.";
		}

		if ("uk_users_3".equals(constraintName)) {

			return "ResourceOwner mit dieser UUID existiert schon.";
		}

		return "Duplicate entry for key '" + constraintName + "'";
	}

}
