/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.faro.web.internal.model.display.contacts;

import com.liferay.osb.faro.engine.client.model.EngagementAggregation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Matthew Kong
 */
@SuppressWarnings({"FieldCanBeLocal", "UnusedDeclaration"})
public class EngagementHistoryDisplay {

	public EngagementHistoryDisplay() {
	}

	public EngagementHistoryDisplay(
		List<EngagementAggregation> engagementAggregations,
		EngagementAggregation previousEngagementAggregation) {

		_engagementAggregations = filter(engagementAggregations);

		Double scoreAvg = null;

		for (int i = _engagementAggregations.size() - 1; i > 0; i--) {
			EngagementAggregation engagementAggregation =
				_engagementAggregations.get(i);

			if (engagementAggregation.getScoreAvg() != null) {
				scoreAvg = engagementAggregation.getScoreAvg();

				break;
			}
		}

		if (scoreAvg == null) {
			scoreAvg = 0.0;
		}

		_previousScoreAvg = previousEngagementAggregation.getScoreAvg();

		_change = (scoreAvg - _previousScoreAvg) / _previousScoreAvg;
	}

	protected List<EngagementAggregation> filter(
		List<EngagementAggregation> engagementAggregations) {

		int size = engagementAggregations.size();

		if (size <= _MAX_CALCULATION_DELAY) {
			return engagementAggregations;
		}

		for (int i = 0; i < _MAX_CALCULATION_DELAY; i++) {
			EngagementAggregation engagementAggregation =
				engagementAggregations.get(size - (i + 1));

			if (engagementAggregation.getTotalElements() == 0) {
				if (i == 0) {
					engagementAggregations.remove(engagementAggregation);
				}
				else {
					engagementAggregation.setScoreAvg(null);
				}
			}
		}

		return engagementAggregations;
	}

	private static final int _MAX_CALCULATION_DELAY = 2;

	private double _change;
	private List<EngagementAggregation> _engagementAggregations =
		new ArrayList<>();
	private Double _previousScoreAvg;

}