/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dog.IndividualHistogramDog;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.AssetType;
import com.liferay.osb.asah.backend.model.HistogramMetric;
import com.liferay.osb.asah.backend.model.HistogramMetricBag;
import com.liferay.osb.asah.common.model.IndividualMetricType;
import com.liferay.osb.asah.common.model.MetricType;
import com.liferay.osb.asah.common.repository.BQIndividualRepository;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Matthew Kong
 */
@Disabled
public class IndividualHistogramDogTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@Disabled
	@RepositoryResource(
		repositoryClass = BQIndividualRepository.class,
		resourcePath = "osbasahfaroinfo/individuals_histogram_info.json"
	)
	@Test
	public void testActiveIndividualHistogramMetrics() {
		List<HistogramMetric> histogramMetrics = _getHistogramMetrics(
			true, IndividualMetricType.TOTAL_INDIVIDUALS);

		Assertions.assertEquals(
			30, histogramMetrics.size(), histogramMetrics.toString());

		double[] expectedValues = {
			0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
			1, 1, 1, 1, 1, 2, 2
		};

		Assertions.assertArrayEquals(
			expectedValues, _getActualValues(histogramMetrics), 0);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = BQIndividualRepository.class,
		resourcePath = "osbasahfaroinfo/individuals_histogram_info.json"
	)
	@Test
	public void testAnonymousIndividualHistogramMetrics() {
		List<HistogramMetric> histogramMetrics = _getHistogramMetrics(
			null, IndividualMetricType.ANONYMOUS_INDIVIDUALS);

		Assertions.assertEquals(
			30, histogramMetrics.size(), histogramMetrics.toString());

		double[] expectedValues = {
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1,
			1, 1, 1, 1, 1, 1, 1
		};

		Assertions.assertArrayEquals(
			expectedValues, _getActualValues(histogramMetrics), 0);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = BQIndividualRepository.class,
		resourcePath = "osbasahfaroinfo/individuals_histogram_info.json"
	)
	@Test
	public void testInactiveIndividualHistogramMetrics() {
		List<HistogramMetric> histogramMetrics = _getHistogramMetrics(
			false, IndividualMetricType.TOTAL_INDIVIDUALS);

		Assertions.assertEquals(
			30, histogramMetrics.size(), histogramMetrics.toString());

		double[] expectedValues = {
			1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4,
			4, 4, 4, 4, 4, 5, 5
		};

		Assertions.assertArrayEquals(
			expectedValues, _getActualValues(histogramMetrics), 0);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = BQIndividualRepository.class,
		resourcePath = "osbasahfaroinfo/individuals_histogram_info.json"
	)
	@Test
	public void testKnownIndividualHistogramMetrics() {
		List<HistogramMetric> histogramMetrics = _getHistogramMetrics(
			null, IndividualMetricType.KNOWN_INDIVIDUALS);

		Assertions.assertEquals(
			30, histogramMetrics.size(), histogramMetrics.toString());

		double[] expectedValues = {
			1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2,
			2, 2, 2, 3, 3, 4, 4
		};

		Assertions.assertArrayEquals(
			expectedValues, _getActualValues(histogramMetrics), 0);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = BQIndividualRepository.class,
		resourcePath = "osbasahfaroinfo/individuals_histogram_info.json"
	)
	@Test
	public void testTotalIndividualHistogramMetrics() {
		List<HistogramMetric> histogramMetrics = _getHistogramMetrics(
			null, IndividualMetricType.TOTAL_INDIVIDUALS);

		Assertions.assertEquals(
			30, histogramMetrics.size(), histogramMetrics.toString());

		double[] expectedValues = {
			1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4,
			4, 4, 4, 4, 4, 5, 5
		};

		Assertions.assertArrayEquals(
			expectedValues, _getActualValues(histogramMetrics), 0);
	}

	private double[] _getActualValues(List<HistogramMetric> histogramMetrics) {
		double[] actualValues = new double[histogramMetrics.size()];

		for (int i = 0; i < histogramMetrics.size(); i++) {
			HistogramMetric histogramMetric = histogramMetrics.get(i);

			actualValues[i] = histogramMetric.getValue();
		}

		return actualValues;
	}

	private List<HistogramMetric> _getHistogramMetrics(
		Boolean active, MetricType metricType) {

		HistogramMetricBag histogramMetricBag =
			_individualHistogramDog.getHistogramMetricBag(
				metricType,
				new SearchQueryContext(AssetType.INDIVIDUAL_METRIC) {
					{
						setActive(active);
						setInterval("D");
						setRangeKey(30);
					}
				});

		return histogramMetricBag.getMetrics();
	}

	@Autowired
	private IndividualHistogramDog _individualHistogramDog;

}