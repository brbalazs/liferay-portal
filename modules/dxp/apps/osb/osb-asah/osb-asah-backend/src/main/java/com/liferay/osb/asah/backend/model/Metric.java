/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import com.liferay.osb.asah.common.model.MetricType;
import com.liferay.osb.asah.common.model.TrendClassification;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Inácio Nery
 */
public class Metric {

	public static Metric of(
		MetricType metricType, Double value, String valueKey) {

		Metric metric = new Metric(metricType);

		metric.setValue(value);
		metric.setValueKey(valueKey);

		return metric;
	}

	public Metric(MetricType metricType) {
		_metricType = metricType;
	}

	public void addMetric(Metric metric) {
		if (_metrics == null) {
			_metrics = new ArrayList<>();
		}

		_metrics.add(metric);
	}

	public void addPreviousValue(Double previousValue) {
		if (previousValue != null) {
			_previousValue += previousValue;
		}
	}

	public void addValue(Double value) {
		if (value != null) {
			_value += value;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Metric)) {
			return false;
		}

		Metric metric = (Metric)obj;

		if (equalsMetric(metric)) {
			Class<?> clazz = obj.getClass();

			if (!clazz.isInstance(this) &&
				Metric.class.isAssignableFrom(clazz)) {

				return obj.equals(this);
			}

			return true;
		}

		return false;
	}

	public List<Metric> getMetrics() {
		return _metrics;
	}

	public MetricType getMetricType() {
		return _metricType;
	}

	public Double getPreviousValue() {
		return _previousValue;
	}

	public String getPreviousValueKey() {
		return _previousValueKey;
	}

	public Trend getTrend() {
		return new Trend(getTrendClassification(), getPercentage());
	}

	public Double getValue() {
		return _value;
	}

	public String getValueKey() {
		return _valueKey;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_metrics, _metricType, _previousValue, _previousValueKey, _value,
			_valueKey);
	}

	public void setMetrics(List<Metric> metrics) {
		_metrics = metrics;
	}

	public void setPreviousValue(Double previousValue) {
		if (previousValue != null) {
			_previousValue = previousValue;
		}
	}

	public void setPreviousValueKey(String previousValueKey) {
		_previousValueKey = previousValueKey;
	}

	public void setValue(Double value) {
		if (value != null) {
			_value = value;
		}
	}

	public void setValueKey(String valueKey) {
		_valueKey = valueKey;
	}

	protected int compare() {
		BigDecimal percentage = getPercentage();

		if (percentage == null) {
			return 0;
		}

		percentage = percentage.setScale(2, BigDecimal.ROUND_HALF_UP);

		return percentage.compareTo(BigDecimal.ZERO);
	}

	protected boolean equalsMetric(Metric metric) {
		if (Objects.equals(_metrics, metric._metrics) &&
			Objects.equals(_metricType, metric._metricType) &&
			Objects.equals(_previousValue, metric._previousValue) &&
			Objects.equals(_previousValueKey, metric._previousValueKey) &&
			Objects.equals(_value, metric._value) &&
			Objects.equals(_valueKey, metric._valueKey)) {

			return true;
		}

		return false;
	}

	protected BigDecimal getPercentage() {
		if (_previousValue == null) {
			return null;
		}

		BigDecimal previousValueBigDecimal = BigDecimal.valueOf(_previousValue);

		previousValueBigDecimal = previousValueBigDecimal.setScale(
			2, BigDecimal.ROUND_HALF_UP);

		if (previousValueBigDecimal.compareTo(_BIG_DECIMAL_0) == 0) {
			return null;
		}

		BigDecimal valueBigDecimal = BigDecimal.valueOf(_value);

		valueBigDecimal = valueBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);

		BigDecimal resultBigDecimal = valueBigDecimal.multiply(
			_BIG_DECIMAL_100);

		resultBigDecimal = resultBigDecimal.divide(
			previousValueBigDecimal, 1, BigDecimal.ROUND_HALF_UP);

		return resultBigDecimal.subtract(_BIG_DECIMAL_100);
	}

	protected TrendClassification getTrendClassification() {
		int trend = compare();

		return TrendClassification.classify(
			trend, _metricType.getTrendClassificationOrder());
	}

	private static final BigDecimal _BIG_DECIMAL_0 = BigDecimal.valueOf(0.0);

	private static final BigDecimal _BIG_DECIMAL_100 = BigDecimal.valueOf(100);

	private List<Metric> _metrics;
	private final MetricType _metricType;
	private Double _previousValue;
	private String _previousValueKey;
	private Double _value = 0D;
	private String _valueKey;

}