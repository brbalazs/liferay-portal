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
 * @author Rachael Koestartyo
 */
public enum SiteMetricType implements MetricType {

	ANONYMOUS_VISITORS(
		"anonymousVisitors", "anonymousVisitorsMetric",
		TrendClassification.Order.ASC),
	BOUNCE_RATE("bounce", "bounceRateMetric", TrendClassification.Order.DESC),
	KNOWN_VISITORS(
		"knownVisitors", "knownVisitorsMetric", TrendClassification.Order.ASC),
	SESSION_DURATION(
		"avgTimeOnPage", "timeOnPage", "sessionDurationMetric",
		TrendClassification.Order.ASC),
	SESSIONS("sessionId", "sessionsMetric", TrendClassification.Order.ASC),
	SESSIONS_PER_VISITOR(
		"sessionsPerVisitor", "sessionsPerVisitorMetric",
		TrendClassification.Order.ASC),
	VIEWS("views", "viewsMetric", TrendClassification.Order.ASC),
	VISITORS("visitors", "visitorsMetric", TrendClassification.Order.ASC);

	public static SiteMetricType of(String name) {
		return Optional.ofNullable(
			_siteMetricTypes.get(name)
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

	private SiteMetricType(
		String aggregationName, String fieldName, String name,
		TrendClassification.Order order) {

		_aggregationName = aggregationName;
		_fieldName = fieldName;
		_name = name;
		_order = order;
	}

	private SiteMetricType(
		String fieldName, String name, TrendClassification.Order order) {

		_fieldName = fieldName;
		_name = name;
		_order = order;

		_aggregationName = fieldName;
	}

	private static final Map<String, SiteMetricType> _siteMetricTypes =
		new HashMap<>();

	static {
		Stream.of(
			values()
		).forEach(
			metricType -> _siteMetricTypes.put(metricType.getName(), metricType)
		);
	}

	private final String _aggregationName;
	private final String _fieldName;
	private final String _name;
	private final TrendClassification.Order _order;

}