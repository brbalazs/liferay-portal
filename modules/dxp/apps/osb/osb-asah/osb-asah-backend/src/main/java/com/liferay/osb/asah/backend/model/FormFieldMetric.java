/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import com.liferay.osb.asah.backend.model.util.MetricUtil;

import java.util.Objects;
import java.util.Set;

/**
 * @author Marcellus Tavares
 */
public class FormFieldMetric {

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof FormFieldMetric)) {
			return false;
		}

		FormFieldMetric formFieldMetric = (FormFieldMetric)obj;

		if (Objects.equals(
				_fieldAbandonmentsMetric,
				formFieldMetric._fieldAbandonmentsMetric) &&
			Objects.equals(
				_fieldInteractionDurationMetric,
				formFieldMetric._fieldInteractionDurationMetric) &&
			Objects.equals(
				_fieldInteractionsMetric,
				formFieldMetric._fieldInteractionsMetric) &&
			Objects.equals(_fieldName, formFieldMetric._fieldName) &&
			Objects.equals(
				_fieldRefilledMetric, formFieldMetric._fieldRefilledMetric)) {

			return true;
		}

		return false;
	}

	public Set<Metric> getAvailableMetrics() {
		return MetricUtil.getAvailableMetrics(this);
	}

	public Metric getFieldAbandonmentsMetric() {
		return _fieldAbandonmentsMetric;
	}

	public Metric getFieldInteractionDurationMetric() {
		return _fieldInteractionDurationMetric;
	}

	public Metric getFieldInteractionsMetric() {
		return _fieldInteractionsMetric;
	}

	public String getFieldName() {
		return _fieldName;
	}

	public Metric getFieldRefilledMetric() {
		return _fieldRefilledMetric;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_fieldAbandonmentsMetric, _fieldInteractionDurationMetric,
			_fieldInteractionsMetric, _fieldName, _fieldRefilledMetric);
	}

	public void setFieldAbandonmentsMetric(Metric fieldAbandonmentsMetric) {
		_fieldAbandonmentsMetric = fieldAbandonmentsMetric;
	}

	public void setFieldInteractionDurationMetric(
		Metric fieldInteractionDurationMetric) {

		_fieldInteractionDurationMetric = fieldInteractionDurationMetric;
	}

	public void setFieldInteractionsMetric(Metric fieldInteractionsMetric) {
		_fieldInteractionsMetric = fieldInteractionsMetric;
	}

	public void setFieldName(String fieldName) {
		_fieldName = fieldName;
	}

	public void setFieldRefilledMetric(Metric fieldRefilledMetric) {
		_fieldRefilledMetric = fieldRefilledMetric;
	}

	private Metric _fieldAbandonmentsMetric;
	private Metric _fieldInteractionDurationMetric;
	private Metric _fieldInteractionsMetric;
	private String _fieldName;
	private Metric _fieldRefilledMetric;

}