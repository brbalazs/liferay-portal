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
public class BounceRateExperimentMetricCalculatorTest
	extends BaseExperimentMetricCalculatorTestCase {

	@Test
	public void test2Variants() {
		Experiment experiment = new Experiment();

		experiment.setConfidenceLevel(95D);
		experiment.setGoal(new Goal(GoalMetric.BOUNCE_RATE, ""));

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
							new ExperimentDataPoint<>(3025, 2235D),
							new ExperimentDataPoint<>(2895, 2132D))),
					createVariant(
						false, "Variant", 50,
						Arrays.asList(
							new ExperimentDataPoint<>(3200, 1789D),
							new ExperimentDataPoint<>(3523, 3002D)))));

		ExperimentMetric experimentMetric =
			experimentMetricCalculator.calculateExperimentMetric(experiment);

		assertExperimentMetric(experimentMetric, 95, 2, 12);

		List<ExperimentVariantMetric> experimentVariantMetrics =
			new ArrayList<>(experimentMetric.getExperimentVariantMetrics());

		experimentVariantMetrics.sort(
			Comparator.comparing(ExperimentVariantMetric::getDXPVariantId));

		assertExperimentVariantMetric(
			71, 72, "Control", experimentVariantMetrics.get(0), 0, 72, 0);
		assertExperimentVariantMetric(
			69, 69, "Variant", experimentVariantMetrics.get(1), 3, 69, 99);
	}

	@Test
	public void test3Variants() {
		Experiment experiment = new Experiment();

		experiment.setConfidenceLevel(95D);
		experiment.setGoal(new Goal(GoalMetric.BOUNCE_RATE, ""));

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
							new ExperimentDataPoint<>(3025, 2235D),
							new ExperimentDataPoint<>(2895, 2132D))),
					createVariant(
						false, "Variant A", 25,
						Arrays.asList(
							new ExperimentDataPoint<>(3200, 1789D),
							new ExperimentDataPoint<>(3523, 3002D))),
					createVariant(
						false, "Variant B", 25,
						Arrays.asList(
							new ExperimentDataPoint<>(2065, 1245D),
							new ExperimentDataPoint<>(1645, 1135D)))));

		ExperimentMetric experimentMetric =
			experimentMetricCalculator.calculateExperimentMetric(experiment);

		assertExperimentMetric(experimentMetric, 95, 2, 12);

		List<ExperimentVariantMetric> experimentVariantMetrics =
			new ArrayList<>(experimentMetric.getExperimentVariantMetrics());

		experimentVariantMetrics.sort(
			Comparator.comparing(ExperimentVariantMetric::getDXPVariantId));

		assertExperimentVariantMetric(
			71, 72, "Control", experimentVariantMetrics.get(0), 0, 71, 0);
		assertExperimentVariantMetric(
			69, 69, "Variant A", experimentVariantMetrics.get(1), 2, 69, 1);
		assertExperimentVariantMetric(
			63, 64, "Variant B", experimentVariantMetrics.get(2), 11, 63, 99);
	}

	@Disabled
	@Test
	public void testNoData() {
		Experiment experiment = new Experiment();

		experiment.setConfidenceLevel(95D);
		experiment.setGoal(new Goal(GoalMetric.BOUNCE_RATE, ""));

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