// =====================================================
// Projekt: authprovider
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.authprovider.domain;

import java.util.Date;

/**
 * TimeInterval abgeschlossenes Zeitintervall.
 */
public class TimeInterval {

	private final Date startTime;

	private final Date endTime;

	/**
	 * Erzeugt eine Instanz von TimeInterval
	 */
	public TimeInterval(final Date startTime, final Date endTime) {

		super();
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public Date getStartTime() {

		return startTime;
	}

	public Date getEndTime() {

		return endTime;
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append("TimeInterval [startTime=");
		builder.append(startTime);
		builder.append(", endTime=");
		builder.append(endTime);
		builder.append("]");
		return builder.toString();
	}

}
