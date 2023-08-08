/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import com.liferay.osb.asah.common.model.MetricType;
import com.liferay.osb.asah.common.model.TrendClassification;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Inácio Nery
 */
public enum FormMetricType implements MetricType {

	ABANDONMENTS(
		"abandonmentsMetric", "abandonments", TrendClassification.Order.DESC),
	COMPLETION_TIME(
		"completionTimeMetric", "submissionsTime",
		TrendClassification.Order.DESC),
	SUBMISSIONS(
		"submissionsMetric", "submissions", TrendClassification.Order.ASC),
	VIEWS("viewsMetric", "views", TrendClassification.Order.ASC);

	public static FormMetricType of(String name) {
		return Optional.ofNullable(
			_formMetricTypes.get(name)
		).orElseThrow(
			IllegalArgumentException::new
		);
	}

	@Override
	public String getAggregationName() {
		return _aggregationName;
	}

	@Override
	public String getFieldName() {
		return _fieldName;
	}

	@Override
	public String getName() {
		return _name;
	}

	@Override
	public TrendClassification.Order getTrendClassificationOrder() {
		return _order;
	}

	private FormMetricType(
		String name, String fieldName, TrendClassification.Order order) {

		_name = name;
		_fieldName = fieldName;
		_order = order;

		_aggregationName = fieldName;
	}

	private static final Map<String, FormMetricType> _formMetricTypes =
		new HashMap<>();

	static {
		Stream.of(
			values()
		).forEach(
			metricType -> _formMetricTypes.put(metricType.getName(), metricType)
		);
	}

	private final String _aggregationName;
	private final String _fieldName;
	private final String _name;
	private final TrendClassification.Order _order;

}