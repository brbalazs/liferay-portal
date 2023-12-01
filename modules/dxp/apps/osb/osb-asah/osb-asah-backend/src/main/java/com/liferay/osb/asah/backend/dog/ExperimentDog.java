/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog;

import com.liferay.osb.asah.backend.dog.experiment.ExperimentMetricDog;
import com.liferay.osb.asah.backend.model.ExperimentSettings;
import com.liferay.osb.asah.backend.model.HistogramMetric;
import com.liferay.osb.asah.backend.model.HistogramMetricBag;
import com.liferay.osb.asah.backend.repository.PageAssetMetricRepository;
import com.liferay.osb.asah.common.date.dog.TimeZoneDog;
import com.liferay.osb.asah.common.dog.DataSourceDog;
import com.liferay.osb.asah.common.entity.Experiment;
import com.liferay.osb.asah.common.entity.ExperimentMetric;
import com.liferay.osb.asah.common.entity.ExperimentVariant;
import com.liferay.osb.asah.common.entity.ExperimentVariantMetric;
import com.liferay.osb.asah.common.model.DXPVariantSettings;
import com.liferay.osb.asah.common.model.ExperimentStatus;
import com.liferay.osb.asah.common.model.Goal;
import com.liferay.osb.asah.common.model.GoalMetric;
import com.liferay.osb.asah.common.model.Interval;
import com.liferay.osb.asah.common.model.PageMetricType;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.repository.ExperimentRepository;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahException;
import com.liferay.osb.asah.common.util.TimeOrderedUuidGenerator;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author André Miranda
 * @author Marcellus Tavares
 */
@Component
public class ExperimentDog {

	public Experiment addExperiment(Experiment experiment) {
		experiment.setChannelId(_getChannelId(experiment));
		experiment.setId(_timeOrderedUuidGenerator.generateIdAsLong());
		experiment.setIsNew(Boolean.TRUE);

		if (experiment.getModifiedDate() == null) {
			experiment.setModifiedDate(experiment.getCreateDate());
		}

		return _experimentRepository.save(experiment);
	}

	public ExperimentMetric calculateExperimentMetric(Long experimentId) {
		return _experimentMetricDog.calculateExperimentMetric(
			getExperiment(experimentId));
	}

	public boolean deleteExperiment(Long experimentId) {
		Experiment experiment = getExperiment(experimentId);

		ExperimentStatus experimentStatus = experiment.getExperimentStatus();

		if (!experimentStatus.isDeleteAllowed()) {
			throw new OSBAsahException(
				HttpStatus.BAD_REQUEST,
				String.format(
					"Unable to delete an experiment in the %s status",
					experimentStatus));
		}

		try {
			_experimentRepository.delete(experiment);
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			return false;
		}

		return true;
	}

	public Experiment fetchExperiment(Long experimentId) {
		Optional<Experiment> experimentOptional =
			_experimentRepository.findById(experimentId);

		return experimentOptional.orElse(null);
	}

	public Experiment getExperiment(Long experimentId) {
		Experiment experiment = fetchExperiment(experimentId);

		if (experiment == null) {
			throw new OSBAsahException(
				HttpStatus.BAD_REQUEST,
				"There is no experiment with ID " + experimentId);
		}

		return experiment;
	}

	public Long getExperimentEstimatedDaysDuration(
		double confidenceLevel, List<DXPVariantSettings> dxpVariantsSettings,
		Long experimentId) {

		if ((confidenceLevel < 80) || (confidenceLevel > 99)) {
			throw new OSBAsahException(
				HttpStatus.BAD_REQUEST,
				"Confidence level value must be between 80 and 99");
		}

		Experiment experiment = getExperiment(experimentId);

		experiment.setConfidenceLevel(confidenceLevel);

		return _experimentMetricDog.estimateDaysDuration(
			dxpVariantsSettings, experiment);
	}

	public ExperimentMetric getExperimentMetric(Long experimentId) {
		List<ExperimentMetric> experimentMetrics = getExperimentMetrics(
			experimentId);

		if (experimentMetrics.isEmpty()) {
			return _createEmptyExperimentMetric(experimentId);
		}

		return experimentMetrics.get(experimentMetrics.size() - 1);
	}

	public List<ExperimentMetric> getExperimentMetrics(Long experimentId) {
		Experiment experiment = getExperiment(experimentId);

		if (experiment.getExperimentStatus() == ExperimentStatus.DRAFT) {
			throw new OSBAsahException(
				HttpStatus.BAD_REQUEST,
				"Unable to retrieve experiment metrics while experiment is " +
					"draft");
		}

		List<ExperimentMetric> experimentMetrics = new ArrayList<>(
			experiment.getExperimentMetrics());

		experimentMetrics.sort(
			Comparator.comparing(ExperimentMetric::getElapsedDays));

		return experimentMetrics;
	}

	public Page<Experiment> getExperimentPage(
		Long channelId, @Nullable String keywords, int start, int size,
		Sort sort) {

		PageRequest pageRequest = PageRequest.of(start / size, size, sort);

		return PageableExecutionUtils.getPage(
			_experimentRepository.searchExperimentsByChannelIdAndKeywords(
				channelId, keywords, pageRequest),
			pageRequest,
			() -> _experimentRepository.countExperimentsByChannelIdAndKeywords(
				channelId, keywords));
	}

	public List<HistogramMetric> getExperimentSessionHistogramMetrics(
		Long experimentId, @Nullable String variantId) {

		Experiment experiment = fetchExperiment(experimentId);

		if (experiment == null) {
			return Collections.emptyList();
		}

		TimeRange timeRange = _getTimeRange(
			experiment.getStartedDateLocalDateTime());

		List<HistogramMetric> histogramMetrics =
			_pageAssetMetricRepository.getExperimentHistogramMetrics(
				experimentId, PageMetricType.SESSIONS, timeRange, variantId);

		HistogramMetricBag histogramMetricBag =
			_histogramDog.getHistogramMetricBag(
				histogramMetrics, false, Interval.DAY, PageMetricType.SESSIONS,
				timeRange);

		return histogramMetricBag.getMetrics();
	}

	public Long getExperimentSessions(Long experimentId) {
		Experiment experiment = fetchExperiment(experimentId);

		if (experiment == null) {
			return 0L;
		}

		return _pageAssetMetricRepository.getUniqueSessionsCount(
			experimentId,
			_getTimeRange(experiment.getStartedDateLocalDateTime()));
	}

	public Long getVariantUniqueVisitors(Long experimentId, String variantId) {
		Experiment experiment = fetchExperiment(experimentId);

		if (experiment == null) {
			return 0L;
		}

		return _pageAssetMetricRepository.getVariantUniqueVisitors(
			experimentId,
			_getTimeRange(experiment.getStartedDateLocalDateTime()), variantId);
	}

	public Experiment patchExperiment(
		Experiment experiment, ExperimentSettings experimentSettings) {

		Experiment existingExperiment = getExperiment(
			Optional.ofNullable(
				experiment.getId()
			).orElse(
				0L
			));

		if (experiment.getDescription() != null) {
			existingExperiment.setDescription(experiment.getDescription());
		}

		if (experiment.getDXPExperienceName() != null) {
			existingExperiment.setDXPExperienceName(
				experiment.getDXPExperienceName());
		}

		if (experiment.getDXPSegmentName() != null) {
			existingExperiment.setDXPSegmentName(
				experiment.getDXPSegmentName());
		}

		_setExperimentSettings(existingExperiment, experimentSettings);

		if (experiment.getExperimentStatus() != null) {
			_setExperimentStatus(
				existingExperiment, experiment.getExperimentStatus());
			_setPublishedDXPVariantId(
				existingExperiment, experiment.getPublishedDXPVariantId());
		}

		if ((experiment.getExperimentStatus() == ExperimentStatus.RUNNING) &&
			(existingExperiment.getExperimentStatus() ==
				ExperimentStatus.DRAFT)) {

			existingExperiment.setExperimentType(
				experiment.getExperimentType());
		}

		if (experiment.getName() != null) {
			existingExperiment.setName(experiment.getName());
		}

		if (experiment.getGoal() != null) {
			existingExperiment.setGoal(experiment.getGoal());
		}

		if (experiment.getPageRelativePath() != null) {
			existingExperiment.setPageRelativePath(
				experiment.getPageRelativePath());
		}

		if (experiment.getPageTitle() != null) {
			existingExperiment.setPageTitle(experiment.getPageTitle());
		}

		if (experiment.getPageURL() != null) {
			existingExperiment.setPageURL(experiment.getPageURL());
		}

		if (experiment.getPublishable() != null) {
			existingExperiment.setPublishable(experiment.getPublishable());
		}

		return updateExperiment(existingExperiment);
	}

	public Experiment updateExperiment(Experiment experiment) {
		experiment.setModifiedDate(new Date());

		return _experimentRepository.save(experiment);
	}

	private ExperimentMetric _createEmptyExperimentMetric(Long experimentId) {
		ExperimentMetric experimentMetric = new ExperimentMetric();

		Experiment experiment = getExperiment(experimentId);

		for (ExperimentVariant experimentVariant :
				experiment.getExperimentVariants()) {

			experimentMetric.addExperimentVariantMetric(
				new ExperimentVariantMetric(
					experimentVariant.isControl(),
					experimentVariant.getDXPVariantId()));
		}

		experimentMetric.setElapsedDays(_getExperimentElapsedDays(experiment));
		experimentMetric.setEstimatedDaysLeft(null);

		return experimentMetric;
	}

	private Long _getChannelId(Experiment experiment) {
		if (experiment.getChannelId() != null) {
			return experiment.getChannelId();
		}

		Long channelId = _dataSourceDog.fetchDefaultChannelId(
			experiment.getDataSourceId());

		if (channelId != null) {
			return channelId;
		}

		throw new OSBAsahException(
			HttpStatus.BAD_REQUEST,
			"Unable to create an experiment without channel ID");
	}

	private long _getExperimentElapsedDays(Experiment experiment) {
		Date experimentStartedDate = experiment.getStartedDate();

		return ChronoUnit.DAYS.between(
			experimentStartedDate.toInstant(), Instant.now());
	}

	private TimeRange _getTimeRange(LocalDateTime startedDateLocalDateTime) {
		ZoneId zoneId = _timeZoneDog.getZoneId();

		ZonedDateTime startZonedDateTime = ZonedDateTime.now(zoneId);

		if (startedDateLocalDateTime != null) {
			ZonedDateTime startedDateZonedDateTime =
				startedDateLocalDateTime.atZone(ZoneOffset.UTC);

			startZonedDateTime = startedDateZonedDateTime.withZoneSameInstant(
				zoneId);
		}

		return TimeRange.of(
			LocalDate.now(zoneId), startZonedDateTime.toLocalDate());
	}

	private void _setExperimentSettings(
		Experiment experiment, ExperimentSettings experimentSettings) {

		if (experimentSettings == null) {
			return;
		}

		Double confidenceLevel = experimentSettings.getConfidenceLevel();

		if ((confidenceLevel < 80) || (confidenceLevel > 99)) {
			throw new OSBAsahException(
				HttpStatus.BAD_REQUEST,
				"Confidence level value must be between 80 and 99");
		}

		experiment.setConfidenceLevel(confidenceLevel);

		Map<String, DXPVariantSettings> dxpVariantsSettingsMap =
			experimentSettings.getDXPVariantsSettingsMap();

		double dxpVariantsTrafficSum = 0;

		for (ExperimentVariant experimentVariant :
				experiment.getExperimentVariants()) {

			DXPVariantSettings dxpVariantSettings = dxpVariantsSettingsMap.get(
				experimentVariant.getDXPVariantId());

			experimentVariant.setTrafficSplit(
				dxpVariantSettings.getTrafficSplit());

			dxpVariantsTrafficSum += dxpVariantSettings.getTrafficSplit();
		}

		if (dxpVariantsTrafficSum != 100) {
			throw new OSBAsahException(
				HttpStatus.BAD_REQUEST,
				"The sum of all variant traffic splits must be 100");
		}
	}

	private void _setExperimentStatus(
		Experiment experiment, ExperimentStatus experimentStatus) {

		if ((experimentStatus == ExperimentStatus.FINISHED_NO_WINNER) ||
			(experimentStatus == ExperimentStatus.FINISHED_WINNER)) {

			throw new OSBAsahException(
				HttpStatus.BAD_REQUEST,
				String.format(
					"This API is not allowed to set the status %s",
					experimentStatus));
		}

		if (!ExperimentStatus.isValidTransition(
				experiment.getExperimentStatus(), experimentStatus)) {

			throw new OSBAsahException(
				HttpStatus.BAD_REQUEST,
				String.format(
					"Invalid status transition: from %s to %s",
					experiment.getExperimentStatus(), experimentStatus));
		}

		experiment.setExperimentStatus(experimentStatus);

		if (experimentStatus == ExperimentStatus.RUNNING) {
			experiment.setStartedDate(new Date());

			Goal goal = experiment.getGoal();

			if ((goal.getGoalMetric() == GoalMetric.CLICK_RATE) &&
				StringUtils.isBlank(goal.getTarget())) {

				throw new OSBAsahException(
					HttpStatus.BAD_REQUEST,
					String.format(
						"Transition to %s requires target element",
						experimentStatus));
			}
		}
		else if (experimentStatus == ExperimentStatus.TERMINATED) {
			experiment.setFinishedDate(new Date());
		}
	}

	private void _setPublishedDXPVariantId(
		Experiment experiment, String publishedDXPVariantId) {

		ExperimentStatus experimentStatus = experiment.getExperimentStatus();

		if (experimentStatus == ExperimentStatus.COMPLETED) {
			if (publishedDXPVariantId == null) {
				throw new OSBAsahException(
					HttpStatus.BAD_REQUEST,
					String.format(
						"Transition to %s requires published DXP variant ID",
						experimentStatus));
			}

			experiment.setPublishedDXPVariantId(publishedDXPVariantId);
		}

		if ((experimentStatus == ExperimentStatus.TERMINATED) &&
			(publishedDXPVariantId != null)) {

			experiment.setPublishedDXPVariantId(publishedDXPVariantId);
		}
	}

	private static final Log _log = LogFactory.getLog(ExperimentDog.class);

	@Autowired
	private DataSourceDog _dataSourceDog;

	@Autowired
	private ExperimentMetricDog _experimentMetricDog;

	@Autowired
	private ExperimentRepository _experimentRepository;

	@Autowired
	private HistogramDog _histogramDog;

	@Autowired
	private PageAssetMetricRepository _pageAssetMetricRepository;

	private final TimeOrderedUuidGenerator _timeOrderedUuidGenerator =
		new TimeOrderedUuidGenerator();

	@Autowired
	private TimeZoneDog _timeZoneDog;

}