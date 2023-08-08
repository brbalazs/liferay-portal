/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dog.ExperimentDog;
import com.liferay.osb.asah.backend.dog.experiment.ExperimentMetricDog;
import com.liferay.osb.asah.backend.model.HistogramMetric;
import com.liferay.osb.asah.common.entity.Experiment;
import com.liferay.osb.asah.common.entity.ExperimentMetric;
import com.liferay.osb.asah.common.entity.ExperimentVariantMetric;
import com.liferay.osb.asah.common.model.ExperimentStatus;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.repository.ChannelRepository;
import com.liferay.osb.asah.common.repository.ExperimentRepository;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahException;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahBQSQLTestExecutionListener;
import com.liferay.osb.asah.test.util.spring.OSBAsahRepositoryTestExecutionListener;
import com.liferay.osb.asah.test.util.spring.OSBAsahSQLTestExecutionListener;

import java.math.BigDecimal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.ToDoubleFunction;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.data.domain.Page;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * @author André Miranda
 */
@TestExecutionListeners(
	mergeMode = TestExecutionListeners.MergeMode.REPLACE_DEFAULTS,
	value = {
		DependencyInjectionTestExecutionListener.class,
		MockitoTestExecutionListener.class,
		OSBAsahBQSQLTestExecutionListener.class,
		OSBAsahRepositoryTestExecutionListener.class,
		OSBAsahSQLTestExecutionListener.class
	}
)
public class ExperimentDogTest implements OSBAsahBackendSpringTestContext {

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@RepositoryResource(
		repositoryClass = ExperimentRepository.class,
		resourcePath = "osbasahfaroinfo/experiments.json"
	)
	@Test
	public void testDeleteExperiment() {
		Experiment experiment = _experimentDog.fetchExperiment(2L);

		Assertions.assertNotNull(experiment);

		Assertions.assertEquals(
			ExperimentStatus.DRAFT, experiment.getExperimentStatus());

		_experimentDog.deleteExperiment(2L);

		Assertions.assertNull(_experimentDog.fetchExperiment(2L));
	}

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@RepositoryResource(
		repositoryClass = ExperimentRepository.class,
		resourcePath = "osbasahfaroinfo/experiments.json"
	)
	@Test
	public void testDeleteExperimentNotAllowed() {
		Experiment experiment = _experimentDog.fetchExperiment(1L);

		Assertions.assertNotNull(experiment);

		Assertions.assertEquals(
			ExperimentStatus.RUNNING, experiment.getExperimentStatus());

		Assertions.assertThrows(
			OSBAsahException.class, () -> _experimentDog.deleteExperiment(1L));
	}

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@RepositoryResource(
		repositoryClass = ExperimentRepository.class,
		resourcePath = "osbasahfaroinfo/experiments.json"
	)
	@Test
	public void testExperimentNotFound() {
		Experiment experiment = _experimentDog.fetchExperiment(0L);

		Assertions.assertNull(experiment);
	}

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@RepositoryResource(
		repositoryClass = ExperimentRepository.class,
		resourcePath = "osbasahfaroinfo/experiments.json"
	)
	@Test
	public void testExperimentNotFoundWhileEstimatingDaysDuration() {
		Assertions.assertThrows(
			OSBAsahException.class,
			() -> _experimentDog.getExperimentEstimatedDaysDuration(
				95, null, 0L));
	}

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@RepositoryResource(
		repositoryClass = ExperimentRepository.class,
		resourcePath = "osbasahfaroinfo/experiment_metrics.json"
	)
	@Test
	public void testGetEmptyExperimentMetricsRunningExperiment() {
		ExperimentMetric experimentMetric = _experimentDog.getExperimentMetric(
			1L);

		Assertions.assertEquals(2, experimentMetric.getElapsedDays(), 0.0);
		Assertions.assertNull(experimentMetric.getEstimatedDaysLeft());

		List<ExperimentVariantMetric> experimentVariantMetrics =
			new ArrayList<>(experimentMetric.getExperimentVariantMetrics());

		Assertions.assertEquals(
			2, experimentVariantMetrics.size(),
			experimentVariantMetrics.toString());

		_assertExperimentVariantMetric(
			experimentVariantMetrics.get(0),
			new BigDecimal[] {BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.0)},
			0, 0, 0, "1");
		_assertExperimentVariantMetric(
			experimentVariantMetrics.get(1),
			new BigDecimal[] {BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.0)},
			0, 0, 0, "2");
	}

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@RepositoryResource(
		repositoryClass = ExperimentRepository.class,
		resourcePath = "osbasahfaroinfo/experiments.json"
	)
	@Test
	public void testGetExperiment() {
		Experiment experiment = _experimentDog.getExperiment(2L);

		Assertions.assertEquals("Regular test", experiment.getName());
	}

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@RepositoryResource(
		repositoryClass = ExperimentRepository.class,
		resourcePath = "osbasahfaroinfo/experiment_metrics.json"
	)
	@Test
	public void testGetExperimentMetricsDraftExperiment() {
		Assertions.assertThrows(
			OSBAsahException.class,
			() -> _experimentDog.getExperimentMetric(2L));
	}

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@RepositoryResource(
		repositoryClass = ExperimentRepository.class,
		resourcePath = "osbasahfaroinfo/experiment_metrics.json"
	)
	@Test
	public void testGetExperimentMetricsRunningExperiment() {
		ExperimentMetric experimentMetric = _experimentDog.getExperimentMetric(
			3L);

		Assertions.assertEquals(2, experimentMetric.getElapsedDays(), 0.0);
		Assertions.assertEquals(12, experimentMetric.getEstimatedDaysLeft());

		List<ExperimentVariantMetric> experimentVariantMetrics =
			new ArrayList<>(experimentMetric.getExperimentVariantMetrics());

		Assertions.assertEquals(
			2, experimentVariantMetrics.size(),
			experimentVariantMetrics.toString());

		_assertExperimentVariantMetric(
			experimentVariantMetrics.get(0),
			new BigDecimal[] {BigDecimal.valueOf(0.6), BigDecimal.valueOf(2.6)},
			0.4, 0.5, 0.25, "1");
		_assertExperimentVariantMetric(
			experimentVariantMetrics.get(1),
			new BigDecimal[] {BigDecimal.valueOf(0.7), BigDecimal.valueOf(3.5)},
			0.6, 0.6, 0.5, "2");
	}

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@RepositoryResource(
		repositoryClass = ExperimentRepository.class,
		resourcePath = "osbasahfaroinfo/experiments.json"
	)
	@Test
	public void testGetExperimentsAll() {
		Page<Experiment> experimentPage = _experimentDog.getExperimentPage(
			1L, null, 0, 10, Sort.asc("name"));

		List<Experiment> experiments = experimentPage.getContent();

		Assertions.assertNotNull(experiments);
		Assertions.assertEquals(3, experiments.size(), experiments.toString());

		Assertions.assertEquals(3, experiments.size(), experiments.toString());
	}

	@BQSQLResource(resourcePath = "bq_experiment_pages.sql")
	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@RepositoryResource(
		repositoryClass = ExperimentRepository.class,
		resourcePath = "osbasahfaroinfo/experiments.json"
	)
	@Test
	public void testGetExperimentSessionHistogramMetrics() {
		List<HistogramMetric> experimentSessionHistogramMetrics =
			_experimentDog.getExperimentSessionHistogramMetrics(1L, null);

		Assertions.assertEquals(
			3, experimentSessionHistogramMetrics.size(),
			experimentSessionHistogramMetrics.toString());

		LocalDateTime nowLocalDateTime = LocalDateTime.of(
			LocalDate.now(ZoneOffset.UTC), LocalTime.MIDNIGHT);

		_assertSessionHistogramMetric(
			String.valueOf(nowLocalDateTime.minusDays(2)), 4,
			experimentSessionHistogramMetrics.get(0));
		_assertSessionHistogramMetric(
			String.valueOf(nowLocalDateTime.minusDays(1)), 3,
			experimentSessionHistogramMetrics.get(1));
		_assertSessionHistogramMetric(
			String.valueOf(nowLocalDateTime), 2,
			experimentSessionHistogramMetrics.get(2));
	}

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@RepositoryResource(
		repositoryClass = ExperimentRepository.class,
		resourcePath = "osbasahfaroinfo/experiments.json"
	)
	@Test
	public void testGetExperimentsPaginated() {
		Page<Experiment> experimentPage = _experimentDog.getExperimentPage(
			1L, null, 1, 1, Sort.asc("name"));

		List<Experiment> experiments = experimentPage.getContent();

		Assertions.assertNotNull(experiments);
		Assertions.assertEquals(1, experiments.size(), experiments.toString());

		Assertions.assertEquals(1, experiments.size(), experiments.toString());

		Experiment experiment = experiments.get(0);

		Assertions.assertEquals("Crazy test", experiment.getName());
	}

	@BQSQLResource(resourcePath = "bq_experiment_pages.sql")
	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@RepositoryResource(
		repositoryClass = ExperimentRepository.class,
		resourcePath = "osbasahfaroinfo/experiments.json"
	)
	@Test
	public void testGetExperimentVariant1SessionHistogramMetrics() {
		List<HistogramMetric> experimentSessionHistogramMetrics =
			_experimentDog.getExperimentSessionHistogramMetrics(1L, "1");

		Assertions.assertEquals(
			3, experimentSessionHistogramMetrics.size(),
			experimentSessionHistogramMetrics.toString());

		LocalDateTime nowLocalDateTime = LocalDateTime.of(
			LocalDate.now(ZoneOffset.UTC), LocalTime.MIDNIGHT);

		_assertSessionHistogramMetric(
			String.valueOf(nowLocalDateTime.minusDays(2)), 3,
			experimentSessionHistogramMetrics.get(0));
		_assertSessionHistogramMetric(
			String.valueOf(nowLocalDateTime.minusDays(1)), 1,
			experimentSessionHistogramMetrics.get(1));
		_assertSessionHistogramMetric(
			String.valueOf(nowLocalDateTime), 1,
			experimentSessionHistogramMetrics.get(2));
	}

	@BQSQLResource(resourcePath = "bq_experiment_pages.sql")
	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@RepositoryResource(
		repositoryClass = ExperimentRepository.class,
		resourcePath = "osbasahfaroinfo/experiments.json"
	)
	@Test
	public void testGetExperimentVariant2SessionHistogramMetrics() {
		List<HistogramMetric> experimentSessionHistogramMetrics =
			_experimentDog.getExperimentSessionHistogramMetrics(1L, "2");

		Assertions.assertEquals(
			3, experimentSessionHistogramMetrics.size(),
			experimentSessionHistogramMetrics.toString());

		LocalDateTime nowLocalDateTime = LocalDateTime.of(
			LocalDate.now(ZoneOffset.UTC), LocalTime.MIDNIGHT);

		_assertSessionHistogramMetric(
			String.valueOf(nowLocalDateTime.minusDays(2)), 1,
			experimentSessionHistogramMetrics.get(0));
		_assertSessionHistogramMetric(
			String.valueOf(nowLocalDateTime.minusDays(1)), 2,
			experimentSessionHistogramMetrics.get(1));
		_assertSessionHistogramMetric(
			String.valueOf(nowLocalDateTime), 1,
			experimentSessionHistogramMetrics.get(2));
	}

	@BQSQLResource(resourcePath = "bq_experiment_pages.sql")
	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@RepositoryResource(
		repositoryClass = ExperimentRepository.class,
		resourcePath = "osbasahfaroinfo/experiments.json"
	)
	@Test
	public void testGetTotalSessions() {
		Long totalSessions = _experimentDog.getExperimentSessions(1L);

		Assertions.assertNotNull(totalSessions);
		Assertions.assertEquals(9L, totalSessions);
	}

	@BQSQLResource(resourcePath = "bq_experiment_pages.sql")
	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@RepositoryResource(
		repositoryClass = ExperimentRepository.class,
		resourcePath = "osbasahfaroinfo/experiments.json"
	)
	@Test
	public void testGetVariantUniqueVisitors() {
		Long variantUniqueVisitors1 = _experimentDog.getVariantUniqueVisitors(
			1L, "1");

		Assertions.assertNotNull(variantUniqueVisitors1);
		Assertions.assertEquals(3L, variantUniqueVisitors1);

		Long variantUniqueVisitors2 = _experimentDog.getVariantUniqueVisitors(
			1L, "2");

		Assertions.assertNotNull(variantUniqueVisitors2);
		Assertions.assertEquals(3L, variantUniqueVisitors2);
	}

	private void _assertExperimentVariantMetric(
		ExperimentVariantMetric actualExperimentVariantMetric,
		BigDecimal[] expectedConfidenceIntervals, double expectetedImprovement,
		double expectedMedian, double expectedProbabilityToWin,
		String expectedDXPVariantId) {

		Assertions.assertArrayEquals(
			_mapToDoubles(expectedConfidenceIntervals),
			_mapToDoubles(
				actualExperimentVariantMetric.getConfidenceIntervals()),
			.1);

		Assertions.assertEquals(
			expectedMedian,
			Optional.ofNullable(
				actualExperimentVariantMetric.getMedian()
			).orElse(
				0.0
			),
			.1);
		Assertions.assertEquals(
			expectedProbabilityToWin,
			Optional.ofNullable(
				actualExperimentVariantMetric.getProbabilityToWin()
			).orElse(
				0.0
			),
			.1);
		Assertions.assertEquals(
			expectedDXPVariantId,
			actualExperimentVariantMetric.getDXPVariantId());
		Assertions.assertEquals(
			expectetedImprovement,
			Optional.ofNullable(
				actualExperimentVariantMetric.getImprovement()
			).orElse(
				0.0
			),
			.1);
	}

	private void _assertSessionHistogramMetric(
		String expectedHistogramMetricKey, double expectedHistogramMetricValue,
		HistogramMetric actualHistogramMetric) {

		Assertions.assertEquals(
			expectedHistogramMetricKey, actualHistogramMetric.getKey());
		Assertions.assertEquals(
			expectedHistogramMetricValue, actualHistogramMetric.getValue(), .1);
	}

	private double[] _mapToDoubles(BigDecimal[] bigDecimals) {
		ToDoubleFunction<BigDecimal> toDoubleFunction = BigDecimal::doubleValue;

		return Stream.of(
			bigDecimals
		).mapToDouble(
			toDoubleFunction
		).toArray();
	}

	@Autowired
	private ExperimentDog _experimentDog;

	@MockBean
	private ExperimentMetricDog _experimentMetricDog;

}