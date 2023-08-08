/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.experiment;

import com.liferay.osb.asah.common.entity.Experiment;
import com.liferay.osb.asah.common.entity.ExperimentMetric;
import com.liferay.osb.asah.common.entity.ExperimentVariantMetric;
import com.liferay.osb.asah.common.model.Goal;
import com.liferay.osb.asah.common.model.GoalMetric;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author André Miranda
 */
public class ClickThroughRateExperimentMetricCalculatorTest
	extends BaseExperimentMetricCalculatorTestCase {

	@Test
	public void test2Variants() {
		Experiment experiment = new Experiment();

		experiment.setConfidenceLevel(95D);
		experiment.setGoal(new Goal(GoalMetric.CLICK_RATE, ""));

		LocalDateTime nowLocalDateTime = LocalDateTime.now();

		LocalDateTime startedLocalDateTime = nowLocalDateTime.minusDays(2);

		experiment.setStartedDate(
			Date.from(startedLocalDateTime.toInstant(ZoneOffset.UTC)));

		ExperimentMetricCalculator experimentMetricCalculator =
			getDichotomousDataExperimentMetricCalculator(
				2,
				Arrays.asList(
					createVariant(
						true, "Control", 50,
						Arrays.asList(
							new ExperimentDataPoint<>(1025, 235D),
							new ExperimentDataPoint<>(895, 132D))),
					createVariant(
						false, "Variant", 50,
						Arrays.asList(
							new ExperimentDataPoint<>(1002, 289D),
							new ExperimentDataPoint<>(1523, 165D)))));

		ExperimentMetric experimentMetric =
			experimentMetricCalculator.calculateExperimentMetric(experiment);

		assertExperimentMetric(experimentMetric, 95, 2, 12);

		List<ExperimentVariantMetric> experimentVariantMetrics =
			new ArrayList<>(experimentMetric.getExperimentVariantMetrics());

		experimentVariantMetrics.sort(
			Comparator.comparing(ExperimentVariantMetric::getDXPVariantId));

		assertExperimentVariantMetric(
			17.70, 20.63, "Control", experimentVariantMetrics.get(0), 0, 19.14,
			83);
		assertExperimentVariantMetric(
			16.76, 19.28, "Variant", experimentVariantMetrics.get(1), -5, 18.00,
			17);
	}

	@Test
	public void test3Variants() {
		Experiment experiment = new Experiment();

		experiment.setConfidenceLevel(95D);
		experiment.setGoal(new Goal(GoalMetric.CLICK_RATE, ""));

		LocalDateTime nowLocalDateTime = LocalDateTime.now();

		LocalDateTime startedLocalDateTime = nowLocalDateTime.minusDays(2);

		experiment.setStartedDate(
			Date.from(startedLocalDateTime.toInstant(ZoneOffset.UTC)));

		ExperimentMetricCalculator experimentMetricCalculator =
			getDichotomousDataExperimentMetricCalculator(
				2,
				Arrays.asList(
					createVariant(
						true, "Control", 50,
						Arrays.asList(
							new ExperimentDataPoint<>(1025, 235D),
							new ExperimentDataPoint<>(895, 132D))),
					createVariant(
						false, "Variant A", 25,
						Arrays.asList(
							new ExperimentDataPoint<>(1002, 289D),
							new ExperimentDataPoint<>(1523, 165D))),
					createVariant(
						false, "Variant B", 25,
						Arrays.asList(
							new ExperimentDataPoint<>(1065, 245D),
							new ExperimentDataPoint<>(1645, 135D)))));

		ExperimentMetric experimentMetric =
			experimentMetricCalculator.calculateExperimentMetric(experiment);

		assertExperimentMetric(experimentMetric, 95, 2, 13);

		List<ExperimentVariantMetric> experimentVariantMetrics =
			new ArrayList<>(experimentMetric.getExperimentVariantMetrics());

		experimentVariantMetrics.sort(
			Comparator.comparing(ExperimentVariantMetric::getDXPVariantId));

		assertExperimentVariantMetric(
			17.13, 20.04, "Control", experimentVariantMetrics.get(0), 0, 18.55,
			77);
		assertExperimentVariantMetric(
			16.47, 18.97, "Variant A", experimentVariantMetrics.get(1), -3,
			17.70, 23);
		assertExperimentVariantMetric(
			13.63, 15.87, "Variant B", experimentVariantMetrics.get(2), -20,
			14.73, 0);
	}

	@Disabled
	@Test
	public void testNoData() {
		Experiment experiment = new Experiment();

		experiment.setConfidenceLevel(.95);
		experiment.setGoal(new Goal(GoalMetric.CLICK_RATE, ""));

		LocalDateTime nowLocalDateTime = LocalDateTime.now();

		LocalDateTime startedLocalDateTime = nowLocalDateTime.minusDays(1);

		experiment.setStartedDate(
			Date.from(startedLocalDateTime.toInstant(ZoneOffset.UTC)));

		ExperimentMetricCalculator experimentMetricCalculator =
			getDichotomousDataExperimentMetricCalculator(
				2,
				Arrays.asList(
					createVariant(
						true, "Control", 50,
						Collections.singletonList(
							new ExperimentDataPoint<>(0, 0D))),
					createVariant(
						false, "Variant", 50,
						Collections.singletonList(
							new ExperimentDataPoint<>(0, 0D)))));

		ExperimentMetric experimentMetric =
			experimentMetricCalculator.calculateExperimentMetric(experiment);

		assertExperimentMetric(experimentMetric, 95, 1, 13);

		List<ExperimentVariantMetric> experimentVariantMetrics =
			new ArrayList<>(experimentMetric.getExperimentVariantMetrics());

		experimentVariantMetrics.sort(
			Comparator.comparing(ExperimentVariantMetric::getDXPVariantId));

		assertExperimentVariantMetric(
			0, 0, "Control", experimentVariantMetrics.get(0), 0, 0, 50);
		assertExperimentVariantMetric(
			0, 0, "Variant", experimentVariantMetrics.get(1), 0, 0, 50);
	}

}