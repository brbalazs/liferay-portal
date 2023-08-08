/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.batch.curator.bot.nanite.test;

import com.liferay.osb.asah.batch.curator.bot.nanite.ExperimentNanite;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.Channel;
import com.liferay.osb.asah.common.entity.Experiment;
import com.liferay.osb.asah.common.entity.ExperimentMetric;
import com.liferay.osb.asah.common.http.ExperimentHttp;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.model.ExperimentStatus;
import com.liferay.osb.asah.common.model.GoalMetric;
import com.liferay.osb.asah.common.repository.ChannelRepository;
import com.liferay.osb.asah.common.repository.ExperimentRepository;
import com.liferay.osb.asah.test.util.faro.FaroInfoTestUtil;
import com.liferay.osb.asah.test.util.spring.OSBAsahRepositoryTestExecutionListener;
import com.liferay.osb.asah.test.util.spring.OSBAsahSQLTestExecutionListener;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import org.json.JSONObject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestExecutionListeners;

/**
 * @author André Miranda
 */
@TestExecutionListeners(
	mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS,
	value = {
		OSBAsahRepositoryTestExecutionListener.class,
		OSBAsahSQLTestExecutionListener.class
	}
)
public class ExperimentNaniteTest extends BaseNaniteTestCase {

	@BeforeEach
	public void setUp() {
		Channel channel = new Channel();

		channel.setId(1234567890L);
		channel.setIsNew(Boolean.TRUE);

		_channelRepository.save(channel);

		Experiment experiment = FaroInfoTestUtil.buildExperiment(
			ExperimentStatus.RUNNING, GoalMetric.CLICK_RATE, 1L);

		experiment.setChannelId(1234567890L);
		experiment.setIsNew(Boolean.TRUE);

		_experimentRepository.save(experiment);
	}

	@AfterEach
	public void tearDown() {
		_experimentRepository.deleteByChannelIdIn(
			Collections.singleton(1234567890L));

		_channelRepository.deleteById(1234567890L);
	}

	@Test
	public void testFinishedExperimentNoWinner() throws Exception {
		_testFinishedExperiment(
			ExperimentStatus.FINISHED_NO_WINNER,
			JSONUtil.put(
				"estimatedDaysLeft", 0
			).put(
				"processedDate", DateUtil.newDateString()
			).put(
				"variantMetrics",
				JSONUtil.putAll(
					_createExperimentVariantMetricJSONObject(
						new double[] {30, 45}, true, "1"),
					_createExperimentVariantMetricJSONObject(
						new double[] {20, 32}, false, "2"))
			),
			null);
	}

	@Test
	public void testFinishedExperimentWinner() throws Exception {
		_testFinishedExperiment(
			ExperimentStatus.FINISHED_WINNER,
			JSONUtil.put(
				"estimatedDaysLeft", 0
			).put(
				"processedDate", DateUtil.newDateString()
			).put(
				"variantMetrics",
				JSONUtil.putAll(
					_createExperimentVariantMetricJSONObject(
						new double[] {30, 45}, true, "10"),
					_createExperimentVariantMetricJSONObject(
						new double[] {15, 28}, false, "20"))
			),
			"10");
	}

	@Test
	public void testFinishedExperimentWinnerBounceRate() throws Exception {
		Experiment experiment = FaroInfoTestUtil.buildExperiment(
			ExperimentStatus.RUNNING, GoalMetric.BOUNCE_RATE, 1L);

		experiment.setChannelId(1234567890L);

		_experimentRepository.save(experiment);

		_testFinishedExperiment(
			ExperimentStatus.FINISHED_WINNER,
			JSONUtil.put(
				"estimatedDaysLeft", 0
			).put(
				"processedDate", DateUtil.newDateString()
			).put(
				"variantMetrics",
				JSONUtil.putAll(
					_createExperimentVariantMetricJSONObject(
						new double[] {30, 45}, true, "10"),
					_createExperimentVariantMetricJSONObject(
						new double[] {15, 28}, false, "20"))
			),
			"20");
	}

	@Test
	public void testNotFinishedExperiment() throws Exception {
		Mockito.when(
			_experimentHttp.getExperimentMetricsJSONObject(
				ArgumentMatchers.anyString())
		).thenReturn(
			JSONUtil.put("estimatedDaysLeft", 1)
		);

		_experimentNanite.run(null);

		Optional<Experiment> experimentOptional =
			_experimentRepository.findById(1L);

		Experiment experiment = experimentOptional.get();

		Assertions.assertEquals(
			ExperimentStatus.RUNNING, experiment.getExperimentStatus());
		Assertions.assertNull(experiment.getFinishedDate());
	}

	private JSONObject _createExperimentVariantMetricJSONObject(
		double[] confidenceInterval, boolean control, String dxpVariantId) {

		return JSONUtil.put(
			"confidenceInterval", confidenceInterval
		).put(
			"control", control
		).put(
			"dxpVariantId", dxpVariantId
		);
	}

	private void _testFinishedExperiment(
			ExperimentStatus expectedStatus, JSONObject responseBody,
			String winnerDXPVariantId)
		throws Exception {

		Mockito.when(
			_experimentHttp.getExperimentMetricsJSONObject(
				ArgumentMatchers.anyString())
		).thenReturn(
			responseBody
		);

		_experimentNanite.run(null);

		Optional<Experiment> experimentOptional =
			_experimentRepository.findById(1L);

		Experiment experiment = experimentOptional.get();

		Set<ExperimentMetric> experimentMetrics =
			experiment.getExperimentMetrics();

		Assertions.assertNotEquals(experimentMetrics.size(), 0);

		Assertions.assertEquals(
			expectedStatus, experiment.getExperimentStatus());
		Assertions.assertNotNull(experiment.getFinishedDate());
		Assertions.assertEquals(
			winnerDXPVariantId, experiment.getWinnerDXPVariantId());
	}

	@Autowired
	private ChannelRepository _channelRepository;

	@MockBean
	private ExperimentHttp _experimentHttp;

	@Autowired
	private ExperimentNanite _experimentNanite;

	@Autowired
	private ExperimentRepository _experimentRepository;

}