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
public enum PageMetricType implements MetricType {

	AVG_TIME_ON_PAGE(
		"avgTimeOnPage", "timeOnPage", "avgTimeOnPageMetric",
		TrendClassification.Order.ASC),
	BOUNCE("bounce", "bounceMetric", TrendClassification.Order.DESC),
	BOUNCE_RATE("bounce", "bounceRateMetric", TrendClassification.Order.DESC),
	CTA_CLICKS("ctaClicks", "ctaClicksMetric", TrendClassification.Order.ASC),
	DIRECT_ACCESS(
		"directAccess", "directAccessMetric", TrendClassification.Order.ASC),
	ENTRANCES("entrances", "entrancesMetric", TrendClassification.Order.ASC),
	EXIT_RATE("exits", "exitRateMetric", TrendClassification.Order.DESC),
	INDIRECT_ACCESS(
		"indirectAccess", "indirectAccessMetric",
		TrendClassification.Order.ASC),
	READS("reads", "readsMetric", TrendClassification.Order.ASC),
	SESSIONS(
		"sessions", "sessionId", "sessionsMetric", false,
		TrendClassification.Order.ASC),
	TIME_ON_PAGE(
		"timeOnPage", "timeOnPageMetric", TrendClassification.Order.ASC),
	VIEWS("views", "viewsMetric", TrendClassification.Order.ASC),
	VISITORS("visitors", "visitorsMetric", TrendClassification.Order.ASC);

	public static PageMetricType of(String name) {
		return Optional.ofNullable(
			_pageMetricTypes.get(name)
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

	@Override
	public boolean isFieldNumeric() {
		return _numericFieldType;
	}

	private PageMetricType(
		String aggregationName, String fieldName, String name,
		boolean numericFieldType, TrendClassification.Order order) {

		_aggregationName = aggregationName;
		_fieldName = fieldName;
		_name = name;
		_numericFieldType = numericFieldType;
		_order = order;
	}

	private PageMetricType(
		String aggregationName, String fieldName, String name,
		TrendClassification.Order order) {

		_aggregationName = aggregationName;
		_fieldName = fieldName;
		_name = name;
		_order = order;
	}

	private PageMetricType(
		String fieldName, String name, TrendClassification.Order order) {

		_fieldName = fieldName;
		_name = name;
		_order = order;

		_aggregationName = fieldName;
	}

	private static final Map<String, PageMetricType> _pageMetricTypes =
		new HashMap<>();

	static {
		Stream.of(
			values()
		).forEach(
			metricType -> _pageMetricTypes.put(metricType.getName(), metricType)
		);
	}

	private final String _aggregationName;
	private final String _fieldName;
	private final String _name;
	private boolean _numericFieldType = true;
	private final TrendClassification.Order _order;

}