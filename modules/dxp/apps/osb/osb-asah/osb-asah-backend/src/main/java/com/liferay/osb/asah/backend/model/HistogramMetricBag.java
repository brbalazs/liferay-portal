/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Leslie Wong
 */
public class HistogramMetricBag {

	public HistogramMetricBag() {
	}

	public HistogramMetricBag(
		boolean asymmetricComparison, List<HistogramMetric> metrics,
		long total) {

		_asymmetricComparison = asymmetricComparison;
		_metrics = metrics;
		_total = total;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof HistogramMetricBag)) {
			return false;
		}

		HistogramMetricBag histogramMetricBag = (HistogramMetricBag)obj;

		if (Objects.equals(
				_asymmetricComparison,
				histogramMetricBag._asymmetricComparison) &&
			Objects.equals(_metrics, histogramMetricBag._metrics) &&
			Objects.equals(_total, histogramMetricBag._total)) {

			return true;
		}

		return false;
	}

	public List<HistogramMetric> getMetrics() {
		return _metrics;
	}

	public long getTotal() {
		return _total;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_asymmetricComparison, _metrics, _total);
	}

	public boolean isAsymmetricComparison() {
		return _asymmetricComparison;
	}

	public void setAsymmetricComparison(boolean asymmetricComparison) {
		_asymmetricComparison = asymmetricComparison;
	}

	public void setHistogramMetrics(List<HistogramMetric> histogramMetrics) {
		_metrics = histogramMetrics;
	}

	public void setTotal(long total) {
		_total = total;
	}

	private boolean _asymmetricComparison;
	private List<HistogramMetric> _metrics = new ArrayList<>();
	private long _total;

}