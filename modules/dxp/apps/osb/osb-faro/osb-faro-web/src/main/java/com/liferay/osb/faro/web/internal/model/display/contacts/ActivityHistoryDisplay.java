/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.web.internal.model.display.contacts;

import com.liferay.osb.faro.engine.client.model.ActivityAggregation;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author Matthew Kong
 */
@SuppressWarnings({"FieldCanBeLocal", "UnusedDeclaration"})
public class ActivityHistoryDisplay {

	public ActivityHistoryDisplay() {
	}

	public ActivityHistoryDisplay(
		List<ActivityAggregation> activityAggregations,
		List<ActivityAggregation> previousActivityAggregations) {

		_activityAggregations = activityAggregations;

		Stream<ActivityAggregation> activityAggregationStream =
			_activityAggregations.stream();

		_count = activityAggregationStream.mapToLong(
			ActivityAggregation::getTotalElements
		).sum();

		Stream<ActivityAggregation> previousActivityAggregationsStream =
			previousActivityAggregations.stream();

		_previousCount = previousActivityAggregationsStream.mapToLong(
			ActivityAggregation::getTotalElements
		).sum();

		_change = (double)(_count - _previousCount) / _previousCount;
	}

	private List<ActivityAggregation> _activityAggregations;
	private double _change;
	private long _count;
	private long _previousCount;

}