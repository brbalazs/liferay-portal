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
 * @author Marcellus Tavares
 */
public enum DocumentLibraryMetricType implements MetricType {

	COMMENTS("comments", "commentsMetric", TrendClassification.Order.ASC),
	DOWNLOADS("downloads", "downloadsMetric", TrendClassification.Order.ASC),
	PREVIEWS("previews", "previewsMetric", TrendClassification.Order.ASC),
	RATINGS("ratingsScore", "ratingsMetric", TrendClassification.Order.ASC);

	public static DocumentLibraryMetricType of(String name) {
		return Optional.ofNullable(
			_documentLibraryMetricTypes.get(name)
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

	private DocumentLibraryMetricType(
		String fieldName, String name, TrendClassification.Order order) {

		_fieldName = fieldName;
		_name = name;
		_order = order;

		_aggregationName = fieldName;
	}

	private static final Map<String, DocumentLibraryMetricType>
		_documentLibraryMetricTypes = new HashMap<>();

	static {
		Stream.of(
			values()
		).forEach(
			metricType -> _documentLibraryMetricTypes.put(
				metricType.getName(), metricType)
		);
	}

	private final String _aggregationName;
	private final String _fieldName;
	private final String _name;
	private final TrendClassification.Order _order;

}