/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import com.liferay.osb.asah.common.model.MetricType;
import com.liferay.osb.asah.common.model.TrendClassification;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Alejo Ceballos
 * @author Marcos Martins
 */
public enum EventMetricType implements MetricType {

	TOTAL_EVENTS("totalEventsMetric"), TOTAL_SESSIONS("totalSessionsMetric");

	public static EventMetricType of(String name) {
		return _eventMetricTypes.get(name);
	}

	@Override
	public String getAggregationName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getFieldName() {
		return _name;
	}

	@Override
	public String getName() {
		return _name;
	}

	@Override
	public TrendClassification.Order getTrendClassificationOrder() {
		return TrendClassification.Order.ASC;
	}

	private EventMetricType(String name) {
		_name = name;
	}

	private static final Map<String, EventMetricType> _eventMetricTypes =
		new HashMap<>();

	static {
		Stream.of(
			values()
		).forEach(
			metricType -> _eventMetricTypes.put(
				metricType.getName(), metricType)
		);
	}

	private final String _name;

}