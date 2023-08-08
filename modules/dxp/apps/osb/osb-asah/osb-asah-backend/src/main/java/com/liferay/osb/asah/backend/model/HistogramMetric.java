/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import java.util.Objects;

/**
 * @author Inácio Nery
 */
public class HistogramMetric extends Metric {

	public HistogramMetric(String key, Metric metric) {
		super(metric.getMetricType());

		_key = key;

		setPreviousValue(metric.getPreviousValue());
		setPreviousValueKey(metric.getPreviousValueKey());
		setValue(metric.getValue());
		setValueKey(metric.getValueKey());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!super.equals(obj) || !(obj instanceof HistogramMetric)) {
			return false;
		}

		HistogramMetric histogramMetric = (HistogramMetric)obj;

		if (super.equalsMetric(histogramMetric) &&
			Objects.equals(_key, histogramMetric._key)) {

			return true;
		}

		return false;
	}

	public String getKey() {
		return _key;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ Objects.hash(_key);
	}

	private final String _key;

}