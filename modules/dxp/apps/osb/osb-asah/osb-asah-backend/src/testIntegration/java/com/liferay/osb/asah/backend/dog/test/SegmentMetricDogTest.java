/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dog.SegmentMetricDog;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.AssetType;
import com.liferay.osb.asah.backend.model.BlogMetricType;
import com.liferay.osb.asah.backend.model.FormMetricType;
import com.liferay.osb.asah.backend.model.JournalMetricType;
import com.liferay.osb.asah.backend.model.Metric;
import com.liferay.osb.asah.common.model.ResultBag;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.repository.CrudBQBlogRepository;
import com.liferay.osb.asah.test.util.repository.CrudBQFormRepository;
import com.liferay.osb.asah.test.util.repository.CrudBQJournalRepository;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Lino Alves
 * @author André Miranda
 */
public class SegmentMetricDogTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQBlogRepository.class,
		resourcePath = "osbasahcerebroinfo/segment_blogs_info.json"
	)
	@Test
	public void testBlogViewsSegmentMetrics() {
		ResultBag<Metric> resultBag =
			_segmentMetricDog.getSegmentMetricResultBag(
				BlogMetricType.VIEWS, _createSearchQuery("1", AssetType.BLOG));

		List<Metric> segmentMetrics = resultBag.getResults();

		Assertions.assertEquals(
			2, segmentMetrics.size(), segmentMetrics.toString());

		Assertions.assertEquals(2, resultBag.getTotal());

		DogTestUtil.assertMetric(1, segmentMetrics, "CEO");
		DogTestUtil.assertMetric(2, segmentMetrics, "Developer");
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQFormRepository.class,
		resourcePath = "osbasahcerebroinfo/segment_forms_info.json"
	)
	@Test
	public void testFormViewsSegmentMetrics() {
		ResultBag<Metric> resultBag =
			_segmentMetricDog.getSegmentMetricResultBag(
				FormMetricType.VIEWS, _createSearchQuery("1", AssetType.FORM));

		List<Metric> segmentMetrics = resultBag.getResults();

		Assertions.assertEquals(
			3, segmentMetrics.size(), segmentMetrics.toString());

		Assertions.assertEquals(3, resultBag.getTotal());

		DogTestUtil.assertMetric(1, segmentMetrics, "Developer");
		DogTestUtil.assertMetric(2, segmentMetrics, "Manager");
		DogTestUtil.assertMetric(1, segmentMetrics, "Marketeer");
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQJournalRepository.class,
		resourcePath = "osbasahcereroinfo/segment_journal_info.json"
	)
	@Test
	public void testJournalViewsSegmentMetrics() {
		ResultBag<Metric> resultBag =
			_segmentMetricDog.getSegmentMetricResultBag(
				JournalMetricType.VIEWS,
				_createSearchQuery("1", AssetType.JOURNAL));

		List<Metric> segmentMetrics = resultBag.getResults();

		Assertions.assertEquals(
			15, segmentMetrics.size(), segmentMetrics.toString());

		Assertions.assertEquals(19, resultBag.getTotal());

		DogTestUtil.assertMetric(13, segmentMetrics, "S");
		DogTestUtil.assertMetric(13, segmentMetrics, "Q");
		DogTestUtil.assertMetric(13, segmentMetrics, "R");
		DogTestUtil.assertMetric(12, segmentMetrics, "P");
		DogTestUtil.assertMetric(11, segmentMetrics, "O");
		DogTestUtil.assertMetric(10, segmentMetrics, "L");
		DogTestUtil.assertMetric(10, segmentMetrics, "M");
		DogTestUtil.assertMetric(9, segmentMetrics, "K");
		DogTestUtil.assertMetric(8, segmentMetrics, "A");
		DogTestUtil.assertMetric(8, segmentMetrics, "J");
		DogTestUtil.assertMetric(7, segmentMetrics, "H");
		DogTestUtil.assertMetric(7, segmentMetrics, "I");
		DogTestUtil.assertMetric(6, segmentMetrics, "G");
		DogTestUtil.assertMetric(5, segmentMetrics, "others");
	}

	private SearchQueryContext _createSearchQuery(
		String assetId, AssetType assetType) {

		return new SearchQueryContext(assetId, assetType) {
			{
				setTimeRange(TimeRange.LAST_7_DAYS);
			}
		};
	}

	@Autowired
	private SegmentMetricDog _segmentMetricDog;

}