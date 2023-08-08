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
 * @author Matthew Kong
 */
public enum IndividualMetricType implements MetricType {

	ANONYMOUS_INDIVIDUALS(
		"identity.createDate", "anonymousIndividualsMetric",
		TrendClassification.Order.ASC),
	KNOWN_INDIVIDUALS(
		"individual.createDate", "knownIndividualsMetric",
		TrendClassification.Order.ASC),
	TOTAL_INDIVIDUALS(
		"identity.createDate", "totalIndividualsMetric",
		TrendClassification.Order.ASC);

	public static IndividualMetricType of(String name) {
		return Optional.ofNullable(
			_individualMetricTypes.get(name)
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

	private IndividualMetricType(
		String fieldName, String name, TrendClassification.Order order) {

		_fieldName = fieldName;
		_name = name;
		_order = order;

		_aggregationName = fieldName;
	}

	private static final Map<String, IndividualMetricType>
		_individualMetricTypes = new HashMap<>();

	static {
		Stream.of(
			values()
		).forEach(
			metricType -> _individualMetricTypes.put(
				metricType.getName(), metricType)
		);
	}

	private final String _aggregationName;
	private final String _fieldName;
	private final String _name;
	private final TrendClassification.Order _order;

}