/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.experiment;

import com.liferay.osb.asah.common.entity.Experiment;
import com.liferay.osb.asah.common.model.Goal;
import com.liferay.osb.asah.common.model.GoalMetric;
import com.liferay.osb.asah.common.model.PageMetricType;

import org.apache.commons.math3.util.FastMath;

/**
 * @author Marcellus Tavares
 */
public class ExperimentUtil {

	public static double calculateAlpha(
		double confidenceLevel, int variantsSize) {

		confidenceLevel /= 100;

		double alpha = (1.0 - confidenceLevel) / 2.0;

		if (variantsSize == 2) {
			return alpha;
		}

		// Apply Šidák correction if there are 3 or more variants.
		// https://en.wikipedia.org/wiki/%C5%A0id%C3%A1k_correction

		return 1.0 - FastMath.pow(1.0 - alpha, 1.0 / variantsSize);
	}

	public static PageMetricType getPageMetricType(Experiment experiment) {
		Goal goal = experiment.getGoal();

		GoalMetric goalMetric = goal.getGoalMetric();

		return goalMetric.getPageMetricType();
	}

}