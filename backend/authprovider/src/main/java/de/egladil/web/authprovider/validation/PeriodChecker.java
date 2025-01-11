// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.validation;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PeriodChecker
 */
public class PeriodChecker {

	private static final Logger LOG = LoggerFactory.getLogger(PeriodChecker.class);

	/**
	 * Vergleicht die Länge des Zeitintervalls zwischen startDate und endDate mit der erwarteten Zeitspanne.
	 *
	 * @param startDate Date
	 * @param endDate Date
	 * @param expectedPeriodMillis long Anzahl Millisekunden, die zwischen startDate und endDate liegen sollen.
	 * @return boolean true, wenn kürzer oder gleich lang wie expectedLengthPeriod, false sonst.
	 */
	public boolean isPeriodLessEqualExpectedPeriod(final Date startDate, final Date endDate, final long expectedPeriodMillis) {

		long diff = endDate.getTime() - startDate.getTime();

		LOG.debug("Interval: {}, Ende: {}, Start: {}, Differenz: {}", expectedPeriodMillis, endDate, startDate, diff);

		return diff <= expectedPeriodMillis;
	}
}
