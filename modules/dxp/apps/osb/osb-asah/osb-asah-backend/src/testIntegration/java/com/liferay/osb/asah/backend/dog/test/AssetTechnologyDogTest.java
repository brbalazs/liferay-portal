/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dog.MetricDog;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.AssetType;
import com.liferay.osb.asah.backend.model.JournalMetricType;
import com.liferay.osb.asah.backend.model.Metric;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.repository.CrudBQJournalRepository;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Lino Alves
 */
@Disabled
public class AssetTechnologyDogTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@RepositoryResource(
		repositoryClass = CrudBQJournalRepository.class,
		resourcePath = "osbasahcereroinfo/technology_journal_info.json"
	)
	@Test
	public void testBrowserMetrics() {
		List<Metric> browserMetrics = _metricDog.getBrowserMetrics(
			JournalMetricType.VIEWS,
			new SearchQueryContext("1", AssetType.JOURNAL) {
				{
					setTimeRange(TimeRange.LAST_7_DAYS);
				}
			});

		Assertions.assertEquals(
			3, browserMetrics.size(), browserMetrics.toString());
	}

	@RepositoryResource(
		repositoryClass = CrudBQJournalRepository.class,
		resourcePath = "osbasahcereroinfo/technology_journal_info.json"
	)
	@Test
	public void testDeviceMetrics() {
		List<Metric> deviceMetrics = _metricDog.getDeviceMetrics(
			JournalMetricType.VIEWS,
			new SearchQueryContext("1", AssetType.JOURNAL) {
				{
					setTimeRange(TimeRange.LAST_7_DAYS);
				}
			});

		Assertions.assertEquals(
			2, deviceMetrics.size(), deviceMetrics.toString());

		DogTestUtil.assertMetric(5, deviceMetrics, "Desktop");
		DogTestUtil.assertMetric(2, deviceMetrics, "Desktop", "Linux");
		DogTestUtil.assertMetric(2, deviceMetrics, "Desktop", "MacOS");
		DogTestUtil.assertMetric(1, deviceMetrics, "Desktop", "Windows");
		DogTestUtil.assertMetric(2, deviceMetrics, "Mobile");
		DogTestUtil.assertMetric(2, deviceMetrics, "Mobile", "Android");
		DogTestUtil.assertMetric(0, deviceMetrics, "Mobile", "Windows");
	}

	@Autowired
	private MetricDog _metricDog;

}