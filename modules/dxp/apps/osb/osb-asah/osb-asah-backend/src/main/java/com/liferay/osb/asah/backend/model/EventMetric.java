/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import java.util.Objects;

/**
 * @author Alejo Ceballos
 * @author Marcos Martins
 */
public class EventMetric {

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof EventMetric)) {
			return false;
		}

		EventMetric eventMetric = (EventMetric)obj;

		if (Objects.equals(
				_totalEventsMetric, eventMetric._totalEventsMetric) &&
			Objects.equals(
				_totalSessionsMetric, eventMetric._totalSessionsMetric)) {

			return true;
		}

		return false;
	}

	public Metric getTotalEventsMetric() {
		return _totalEventsMetric;
	}

	public Metric getTotalSessionsMetric() {
		return _totalSessionsMetric;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_totalEventsMetric, _totalSessionsMetric);
	}

	public void setTotalEventsMetric(Metric totalEventsMetric) {
		_totalEventsMetric = totalEventsMetric;
	}

	public void setTotalSessionsMetric(Metric totalSessionsMetric) {
		_totalSessionsMetric = totalSessionsMetric;
	}

	private Metric _totalEventsMetric = new Metric(
		EventMetricType.TOTAL_EVENTS);
	private Metric _totalSessionsMetric = new Metric(
		EventMetricType.TOTAL_SESSIONS);

}