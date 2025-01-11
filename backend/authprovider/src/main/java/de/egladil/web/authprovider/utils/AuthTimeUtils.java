// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

import de.egladil.web.authprovider.domain.TimeInterval;

/**
 * AuthTimeUtils
 */
public final class AuthTimeUtils {

	public static final String DEFAULT_DATE_TIME_FORMAT = "dd.MM.yyyy kk:mm:ss";

	public static final String DEFAULT_DATE_MINUTES_FORMAT = "dd.MM.yyyy kk:mm";

	public static final String DEFAULT_DATE_FORMAT = "dd.MM.yyyy";

	/**
	 * Erzeugt eine Instanz von AuthTimeUtils
	 */
	private AuthTimeUtils() {

	}

	/**
	 * Wandelt milliseconds in LocalDateTime in der system timezone um.
	 *
	 * @param milliseconds
	 * @return LocalDateTime
	 */
	public static LocalDateTime transformFromMilliseconds(final long milliseconds) {

		return LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault());
	}

	/**
	 * Wandelt date in LocalDateTime in der system timezone um.
	 *
	 * @param date
	 * @return LocalDateTime
	 */
	public static LocalDateTime transformFromDate(final Date date) {

		if (date == null) {

			throw new IllegalArgumentException("date null");
		}

		return transformFromMilliseconds(date.getTime());
	}

	/**
	 * Wandelt ein LocalDateTime in ein Date um.
	 *
	 * @param ldt
	 * @return
	 */
	public static Date transformFromLocalDateTime(final LocalDateTime ldt) {

		if (ldt == null) {

			throw new IllegalArgumentException("ldt null");
		}

		return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * Wandelt date in LocalDateTime in der system timezone um.
	 *
	 * @param date Date darf null sein.
	 * @return LocalDate null bei null
	 */
	public static LocalDate transformToLocalDateFromDate(final Date date) {

		return date == null ? null : LocalDate.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());

	}

	/**
	 * Wandelt ein LocalDateTime in ein Date um.
	 *
	 * @param localDate darf null sein
	 * @return Date oder null
	 */
	public static Date transformFromLocalDate(final LocalDate localDate) {

		return localDate == null ? null : Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * Erzeugt ein abgeschlossenes Zeitintervall.
	 *
	 * @param startTime Date
	 * @param amount int Anzahl Zeiteinheiten muss >= 0 sein, 0 ist zulässig.
	 * @param chronoUnit ChronoUnit
	 * @return TimeInterval
	 */
	public static TimeInterval getInterval(final LocalDateTime startTime, final int amount, final ChronoUnit chronoUnit) {

		if (startTime == null) {

			throw new IllegalArgumentException("startTime null");
		}

		if (chronoUnit == null) {

			throw new IllegalArgumentException("chronoUnit null");
		}

		if (amount < 0) {

			throw new IllegalArgumentException("amount must be >=0");
		}
		Date startsAt = transformFromLocalDateTime(startTime);

		if (amount == 0) {

			return new TimeInterval(startsAt, startsAt);
		}
		LocalDateTime endTime = startTime.plus(amount, chronoUnit);
		Date endsAt = transformFromLocalDateTime(endTime);

		return new TimeInterval(startsAt, endsAt);
	}

	/**
	 * Jetzt in der system timezone.
	 *
	 * @return LocalDateTime
	 */
	public static LocalDateTime now() {

		LocalDateTime ldt = LocalDateTime.now(ZoneId.systemDefault());

		return ldt;

	}

	/**
	 * Formatiert ein LocalDate als dd.MM.yyyy.
	 *
	 * @param localDate
	 * @return
	 */
	public static String format(final LocalDate localDate) {

		return localDate == null ? null : localDate.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
	}

	/**
	 * Formatiert ein LocalDateTime als dd.MM.yyyy kk:mm:ss.
	 *
	 * @param localDateTime LocalDateTime darf null sein
	 * @return String oder null
	 */
	public static String format(final LocalDateTime localDateTime) {

		return localDateTime == null ? null : localDateTime.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));
	}

	/**
	 * Formatiert ein LocalDateTime als dd.MM.yyyy kk:mm.
	 *
	 * @param localDateTime LocalDateTime darf null sein
	 * @return String oder null
	 */
	public static String formatToMinutes(final LocalDateTime localDateTime) {

		return localDateTime == null ? null : localDateTime.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_MINUTES_FORMAT));
	}

	/**
	 * Wandelt einen String mit dem Format 'dd.MM.yyyy' in ein LocalDate um.
	 *
	 * @param dateString darf nicht null sein. Muss gültiges Datumsformat haben.
	 * @return LocalDate
	 */
	public static LocalDate parseToLocalDate(final String dateString) throws IllegalArgumentException {

		if (dateString == null) {

			throw new IllegalArgumentException("dateString darf nicht null sein");
		}

		try {

			TemporalAccessor ta = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT).parse(dateString);
			return LocalDate.from(ta);
		} catch (DateTimeParseException e) {

			throw new IllegalArgumentException(dateString + " ist kein gültiges Datum");
		}

	}

	/**
	 * Wandelt einen String mit dem Format 'dd.MM.yyyy hh:mm:ss' in ein LocalDate um.
	 *
	 * @param dateString darf nicht null sein. Muss gültiges Datumsformat haben.
	 * @return LocalDate
	 */
	public static LocalDateTime parseToLocalDateTime(final String dateString) throws IllegalArgumentException {

		if (dateString == null) {

			throw new IllegalArgumentException("dateString darf nicht null sein");
		}

		try {

			TemporalAccessor ta = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT).parse(dateString);
			return LocalDateTime.from(ta);
		} catch (DateTimeParseException e) {

			throw new IllegalArgumentException(dateString + " ist kein gültiges Datum");
		}

	}
}
