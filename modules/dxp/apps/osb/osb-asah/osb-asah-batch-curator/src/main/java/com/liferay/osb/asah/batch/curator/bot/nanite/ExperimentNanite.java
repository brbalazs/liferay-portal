/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.batch.curator.bot.nanite;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.common.entity.Experiment;
import com.liferay.osb.asah.common.entity.ExperimentMetric;
import com.liferay.osb.asah.common.entity.ExperimentVariantMetric;
import com.liferay.osb.asah.common.http.ExperimentHttp;
import com.liferay.osb.asah.common.model.ExperimentStatus;
import com.liferay.osb.asah.common.model.Goal;
import com.liferay.osb.asah.common.model.GoalMetric;
import com.liferay.osb.asah.common.repository.ExperimentRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.lang3.Range;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author André Miranda
 */
@Component
public class ExperimentNanite extends BaseNanite {

	@Override
	public boolean isLogRunEnabled() {
		return true;
	}

	@Override
	public void run(JSONObject contextJSONObject) throws Exception {
		for (Experiment experiment :
				_experimentRepository.findByExperimentStatus(
					ExperimentStatus.RUNNING)) {

			try {
				_updateExperiment(experiment);
			}
			catch (Exception exception) {
				_log.error(exception, exception);
			}
		}
	}

	@Override
	protected Log getLog() {
		return _log;
	}

	private ExperimentVariantMetric _findControlExperimentVariantMetric(
		Set<ExperimentVariantMetric> experimentVariantMetrics) {

		Stream<ExperimentVariantMetric> stream =
			experimentVariantMetrics.stream();

		Optional<ExperimentVariantMetric>
			controlExperimentVariantMetricsOptional = stream.filter(
				ExperimentVariantMetric::isControl
			).findFirst();

		return controlExperimentVariantMetricsOptional.orElseThrow(
			IllegalStateException::new);
	}

	private ExperimentVariantMetric _getWinnerExperimentVariantMetric(
		Set<ExperimentVariantMetric> experimentVariantMetrics,
		GoalMetric goalMetric) {

		ExperimentVariantMetric controlExperimentVariantMetric =
			_findControlExperimentVariantMetric(experimentVariantMetrics);

		Range<Double> controlConfidenceIntervalRange =
			controlExperimentVariantMetric.getConfidenceIntervalRange();

		List<ExperimentVariantMetric> improvedExperimentVariantMetrics =
			new ArrayList<>();
		List<ExperimentVariantMetric> tiedExperimentVariantMetrics =
			new ArrayList<>();

		for (ExperimentVariantMetric experimentVariantMetric :
				experimentVariantMetrics) {

			if (experimentVariantMetric.isControl()) {
				continue;
			}

			Range<Double> variantConfidenceIntervalRange =
				experimentVariantMetric.getConfidenceIntervalRange();

			if (_hasExperimentVariantMetricImproved(
					controlConfidenceIntervalRange, goalMetric,
					variantConfidenceIntervalRange)) {

				improvedExperimentVariantMetrics.add(experimentVariantMetric);
			}
			else if (variantConfidenceIntervalRange.isOverlappedBy(
						controlConfidenceIntervalRange)) {

				tiedExperimentVariantMetrics.add(experimentVariantMetric);
			}
		}

		if (!improvedExperimentVariantMetrics.isEmpty()) {
			if (Objects.equals(goalMetric, GoalMetric.BOUNCE_RATE)) {
				return Collections.min(
					improvedExperimentVariantMetrics,
					Comparator.comparing(ExperimentVariantMetric::getMedian));
			}

			return Collections.max(
				improvedExperimentVariantMetrics,
				Comparator.comparing(ExperimentVariantMetric::getMedian));
		}

		if (tiedExperimentVariantMetrics.isEmpty()) {
			return controlExperimentVariantMetric;
		}

		return null;
	}

	private boolean _hasExperimentVariantMetricImproved(
		Range<Double> controlConfidenceIntervalRange, GoalMetric goalMetric,
		Range<Double> variantConfidenceIntervalRange) {

		if (Objects.equals(goalMetric, GoalMetric.BOUNCE_RATE)) {
			return variantConfidenceIntervalRange.isBeforeRange(
				controlConfidenceIntervalRange);
		}

		return variantConfidenceIntervalRange.isAfterRange(
			controlConfidenceIntervalRange);
	}

	private void _putOrReplaceMetric(
		Set<ExperimentMetric> experimentMetrics,
		ExperimentMetric newExperimentMetric) {

		LocalDateTime newProcessedLocalDateTime =
			newExperimentMetric.getProcessedLocalDateTime();

		LocalDate newProcessedLocalDate =
			newProcessedLocalDateTime.toLocalDate();

		experimentMetrics.removeIf(
			experimentMetric -> {
				LocalDateTime processedLocalDateTime =
					experimentMetric.getProcessedLocalDateTime();

				LocalDate processedLocalDate =
					processedLocalDateTime.toLocalDate();

				if (processedLocalDate.isEqual(newProcessedLocalDate)) {
					return true;
				}

				return false;
			});

		experimentMetrics.add(newExperimentMetric);
	}

	private void _updateExperiment(Experiment experiment) {
		Set<ExperimentMetric> experimentMetrics =
			experiment.getExperimentMetrics();

		String experimentId = String.valueOf(experiment.getId());

		ExperimentMetric experimentMetric = _objectMapper.convertValue(
			_experimentHttp.getExperimentMetricsJSONObject(experimentId),
			ExperimentMetric.class);

		_putOrReplaceMetric(experimentMetrics, experimentMetric);

		Goal goal = experiment.getGoal();

		ExperimentVariantMetric experimentVariantMetric =
			_getWinnerExperimentVariantMetric(
				experimentMetric.getExperimentVariantMetrics(),
				goal.getGoalMetric());

		if (experimentVariantMetric == null) {
			experiment.setWinnerDXPVariantId(null);
		}
		else {
			experiment.setWinnerDXPVariantId(
				experimentVariantMetric.getDXPVariantId());
		}

		Long estimatedDaysLeft = experimentMetric.getEstimatedDaysLeft();

		if ((estimatedDaysLeft != null) && (estimatedDaysLeft <= 0)) {
			experiment.setFinishedDate(new Date());

			ExperimentStatus experimentStatus =
				ExperimentStatus.FINISHED_WINNER;

			if (experimentVariantMetric == null) {
				experimentStatus = ExperimentStatus.FINISHED_NO_WINNER;
			}

			experiment.setExperimentStatus(experimentStatus);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Updated metrics for experiment " + experimentId);
		}

		_experimentRepository.save(experiment);
	}

	private static final Log _log = LogFactory.getLog(ExperimentNanite.class);

	@Autowired
	private ExperimentHttp _experimentHttp;

	@Autowired
	private ExperimentRepository _experimentRepository;

	@Autowired
	private ObjectMapper _objectMapper;

}