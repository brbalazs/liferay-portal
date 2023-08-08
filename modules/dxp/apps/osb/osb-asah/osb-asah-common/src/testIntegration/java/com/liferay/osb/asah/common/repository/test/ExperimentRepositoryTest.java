/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.test;

import com.liferay.osb.asah.common.entity.Channel;
import com.liferay.osb.asah.common.entity.Experiment;
import com.liferay.osb.asah.common.entity.ExperimentMetric;
import com.liferay.osb.asah.common.entity.ExperimentVariant;
import com.liferay.osb.asah.common.entity.ExperimentVariantMetric;
import com.liferay.osb.asah.common.model.ExperimentStatus;
import com.liferay.osb.asah.common.model.ExperimentType;
import com.liferay.osb.asah.common.model.Goal;
import com.liferay.osb.asah.common.model.GoalMetric;
import com.liferay.osb.asah.common.repository.ChannelRepository;
import com.liferay.osb.asah.common.repository.ExperimentRepository;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;

import java.math.BigDecimal;

import java.time.LocalDate;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Marcos Martins
 */
@Import(JDBCTestConfiguration.class)
public class ExperimentRepositoryTest
	extends BaseRepositoryTestCase<Experiment, Long> {

	@BeforeEach
	public void setUp() {
		Experiment experiment = new Experiment();

		Channel channel1 = new Channel("Channel Test 1");

		channel1.setId(1L);
		channel1.setIsNew(Boolean.TRUE);

		_channelRepository.save(channel1);

		experiment.setChannelId(channel1.getId());

		experiment.setCreateDate(new Date());
		experiment.setConfidenceLevel(95.0);
		experiment.setDataSourceId(2L);
		experiment.setDXPExperienceId("DEFAULT");
		experiment.setDXPExperienceName("Default");
		experiment.setDXPGroupId("63439959");
		experiment.setDXPLayoutId("3c473639-f09e-4c7c-bc51-dc15b23f0bd5");
		experiment.setDXPSegmentId("DEFAULT");
		experiment.setDXPSegmentName("Anyone");
		experiment.setExperimentStatus(ExperimentStatus.RUNNING);
		experiment.setExperimentType(ExperimentType.AB);
		experiment.setGoal(new Goal(GoalMetric.BOUNCE_RATE, ""));
		experiment.setModifiedDate(new Date());
		experiment.setName("Experiment Repository Testing");
		experiment.setPageURL("https://www.beryl.com/products/vehicles");
		experiment.setStartedDate(new Date());

		ExperimentVariant experimentVariant1 = new ExperimentVariant();

		experimentVariant1.setDXPVariantId("DEFAULT");
		experimentVariant1.setChanges(0);
		experimentVariant1.setControl(false);
		experimentVariant1.setDXPVariantName("Control");
		experimentVariant1.setTrafficSplit(34.0);

		experiment.addExperimentVariant(experimentVariant1);

		ExperimentVariant experimentVariant2 = new ExperimentVariant();

		experimentVariant2.setDXPVariantId("63593047");
		experimentVariant2.setChanges(0);
		experimentVariant2.setControl(false);
		experimentVariant2.setDXPVariantName("Organic");
		experimentVariant2.setTrafficSplit(33.0);

		experiment.addExperimentVariant(experimentVariant2);

		ExperimentVariant experimentVariant3 = new ExperimentVariant();

		experimentVariant3.setDXPVariantId("74875919");
		experimentVariant3.setChanges(0);
		experimentVariant3.setControl(false);
		experimentVariant3.setDXPVariantName("Exclusive");
		experimentVariant3.setTrafficSplit(34.0);

		experiment.addExperimentVariant(experimentVariant3);

		ExperimentMetric experimentMetric = new ExperimentMetric();

		experimentMetric.setConfidenceLevel(0.95);
		experimentMetric.setElapsedDays(1L);

		LocalDate localDate = LocalDate.now();

		LocalDate processedLocalDate = localDate.minusDays(2);

		experimentMetric.setProcessedLocalDateTime(
			processedLocalDate.atStartOfDay());

		ExperimentVariantMetric experimentVariantMetric1 =
			new ExperimentVariantMetric(true, "1");

		experimentVariantMetric1.setConfidenceIntervals(
			new BigDecimal[] {
				BigDecimal.valueOf(0.5), BigDecimal.valueOf(2.5)
			});
		experimentVariantMetric1.setImprovement(0.3);
		experimentVariantMetric1.setMedian(0.5);
		experimentVariantMetric1.setProbabilityToWin(0.3);

		experimentMetric.addExperimentVariantMetric(experimentVariantMetric1);

		ExperimentVariantMetric experimentVariantMetric2 =
			new ExperimentVariantMetric(false, "2");

		experimentVariantMetric2.setConfidenceIntervals(
			new BigDecimal[] {
				BigDecimal.valueOf(0.6), BigDecimal.valueOf(3.2)
			});
		experimentVariantMetric2.setImprovement(0.4);
		experimentVariantMetric2.setMedian(0.5);
		experimentVariantMetric2.setProbabilityToWin(0.3);

		experimentMetric.addExperimentVariantMetric(experimentVariantMetric2);

		experiment.addExperimentMetric(experimentMetric);

		setUpRepository(experiment);
	}

	@Test
	public void testFindByExperimentStatus() {
		List<Experiment> experiments =
			_experimentRepository.findByExperimentStatus(
				ExperimentStatus.RUNNING);

		Assertions.assertEquals(1, experiments.size(), experiments.toString());
	}

	@Test
	public void testSearchExperimentsByChannelIdAndKeywords() {
		List<Experiment> experiments =
			_experimentRepository.searchExperimentsByChannelIdAndKeywords(
				1L, null, PageRequest.of(0, 10, Sort.by(Sort.Order.asc("id"))));

		Assertions.assertEquals(1, experiments.size(), experiments.toString());
	}

	@Override
	protected PagingAndSortingRepository<Experiment, Long>
		getPagingAndSortingRepository() {

		return _experimentRepository;
	}

	@Autowired
	private ChannelRepository _channelRepository;

	@Autowired
	private ExperimentRepository _experimentRepository;

}