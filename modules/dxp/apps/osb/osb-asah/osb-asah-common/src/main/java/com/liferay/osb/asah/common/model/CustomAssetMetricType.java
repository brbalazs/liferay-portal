/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Marcellus Tavares
 */
public enum CustomAssetMetricType implements MetricType {

	ABANDONMENTS(
		"abandonments", "abandonmentsMetric", TrendClassification.Order.DESC),
	CLICKS("clicks", "clicksMetric", TrendClassification.Order.ASC),
	COMPLETION_TIME(
		"submissionsTime", "completionTimeMetric",
		TrendClassification.Order.DESC),
	DOWNLOADS("downloads", "downloadsMetric", TrendClassification.Order.ASC),
	READING_TIME(
		"readTime", "readingTimeMetric", TrendClassification.Order.ASC),
	SESSIONS("sessions", "sessionsMetric", TrendClassification.Order.ASC),
	SUBMISSIONS(
		"submissions", "submissionsMetric", TrendClassification.Order.ASC),
	VIEWS("views", "viewsMetric", TrendClassification.Order.ASC);

	public static CustomAssetMetricType of(String name) {
		return Optional.ofNullable(
			_customAssetMetricTypes.get(name)
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

	private CustomAssetMetricType(
		String aggregationName, String fieldName, String name,
		TrendClassification.Order order) {

		_aggregationName = aggregationName;
		_fieldName = fieldName;
		_name = name;
		_order = order;
	}

	private CustomAssetMetricType(
		String fieldName, String name, TrendClassification.Order order) {

		_fieldName = fieldName;
		_name = name;
		_order = order;

		_aggregationName = fieldName;
	}

	private static final Map<String, CustomAssetMetricType>
		_customAssetMetricTypes = new HashMap<>();

	static {
		Stream.of(
			values()
		).forEach(
			metricType -> _customAssetMetricTypes.put(
				metricType.getName(), metricType)
		);
	}

	private final String _aggregationName;
	private final String _fieldName;
	private final String _name;
	private final TrendClassification.Order _order;

}