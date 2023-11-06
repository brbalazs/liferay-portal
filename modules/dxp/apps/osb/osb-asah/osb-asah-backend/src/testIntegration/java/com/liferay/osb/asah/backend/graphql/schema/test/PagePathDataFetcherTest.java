/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dog.PagePathDog;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.dto.PagePathNodeDTO;
import com.liferay.osb.asah.backend.graphql.schema.PagePathDataFetcher;
import com.liferay.osb.asah.backend.model.AdjacentPageViewsMetric;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import graphql.schema.DataFetchingEnvironment;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @author Marcellus Tavares
 */
public class PagePathDataFetcherTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@Test
	public void testGet() {
		List<AdjacentPageViewsMetric> adjacentPageViewsMetrics =
			new ArrayList<>();

		adjacentPageViewsMetrics.add(
			new AdjacentPageViewsMetric(
				"direct", Boolean.TRUE, "direct", BigDecimal.valueOf(5)));
		adjacentPageViewsMetrics.add(
			new AdjacentPageViewsMetric(
				"others", Boolean.TRUE, "others", BigDecimal.valueOf(7)));
		adjacentPageViewsMetrics.add(
			new AdjacentPageViewsMetric(
				"others", Boolean.FALSE, "others", BigDecimal.valueOf(90)));
		adjacentPageViewsMetrics.add(
			new AdjacentPageViewsMetric(
				"url-1", Boolean.TRUE, "url 1", BigDecimal.valueOf(10)));
		adjacentPageViewsMetrics.add(
			new AdjacentPageViewsMetric(
				"url-2", Boolean.TRUE, "url 2", BigDecimal.valueOf(100)));
		adjacentPageViewsMetrics.add(
			new AdjacentPageViewsMetric(
				"url-3", Boolean.TRUE, "url 3", BigDecimal.valueOf(1000)));
		adjacentPageViewsMetrics.add(
			new AdjacentPageViewsMetric(
				"url-4", Boolean.FALSE, "url 4", BigDecimal.valueOf(100)));
		adjacentPageViewsMetrics.add(
			new AdjacentPageViewsMetric(
				"url-5", Boolean.FALSE, "url 5", BigDecimal.valueOf(300)));
		adjacentPageViewsMetrics.add(
			new AdjacentPageViewsMetric(
				"url-6", Boolean.FALSE, "url 6", BigDecimal.valueOf(200)));

		Mockito.when(
			_pagePathDog.getAdjacentPagesViewsMetric(
				ArgumentMatchers.any(), ArgumentMatchers.any(),
				ArgumentMatchers.any(), ArgumentMatchers.any(),
				ArgumentMatchers.any())
		).thenReturn(
			adjacentPageViewsMetrics
		);

		PagePathNodeDTO pagePathNodeDTO = _pagePathDataFetcher.get(
			_dataFetchingEnvironment,
			new SearchQueryContext() {
				{
					setCanonicalUrl("http://www.liferay.com");
					setChannelId(1L);
					setTimeRange(TimeRange.LAST_30_DAYS);
					setTitle("Liferay");
				}
			});

		Assertions.assertEquals(
			"http://www.liferay.com", pagePathNodeDTO.getCanonicalUrl());
		Assertions.assertEquals("Liferay", pagePathNodeDTO.getTitle());
		Assertions.assertEquals(1122, pagePathNodeDTO.getViews());

		_assertPagePathNodeDTOs(
			pagePathNodeDTO.getPreviousPagePathNodeDTOs(),
			Arrays.asList(
				new PagePathNodeDTO("url-3", null, null, "url 3", 1000L),
				new PagePathNodeDTO("url-2", null, null, "url 2", 100L),
				new PagePathNodeDTO("url-1", null, null, "url 1", 10L),
				new PagePathNodeDTO("direct", null, null, "direct", 5L),
				new PagePathNodeDTO("others", null, null, "others", 7L)));

		_assertPagePathNodeDTOs(
			pagePathNodeDTO.getFollowingPagePathNodeDTOs(),
			Arrays.asList(
				new PagePathNodeDTO("url-5", null, null, "url 5", 300L),
				new PagePathNodeDTO("url-6", null, null, "url 6", 200L),
				new PagePathNodeDTO("url-4", null, null, "url 4", 100L),
				new PagePathNodeDTO("others", null, null, "others", 90L),
				new PagePathNodeDTO(
					"drop-offs", null, null, "drop-offs", 432L)));
	}

	private void _assertPagePathNodeDTOs(
		List<PagePathNodeDTO> actualPagePathNodeDTOs,
		List<PagePathNodeDTO> expectedPagePathNodeDTOs) {

		if ((expectedPagePathNodeDTOs == null) &&
			(actualPagePathNodeDTOs == null)) {

			return;
		}

		if ((expectedPagePathNodeDTOs == null) ||
			(actualPagePathNodeDTOs == null)) {

			Assertions.fail();

			return;
		}

		Assertions.assertEquals(
			expectedPagePathNodeDTOs.size(), actualPagePathNodeDTOs.size());

		for (int i = 0; i < expectedPagePathNodeDTOs.size(); i++) {
			PagePathNodeDTO actualPagePathNodeDTO = actualPagePathNodeDTOs.get(
				i);
			PagePathNodeDTO expectedPagePathNodeDTO =
				expectedPagePathNodeDTOs.get(i);

			Assertions.assertEquals(
				expectedPagePathNodeDTO.getCanonicalUrl(),
				actualPagePathNodeDTO.getCanonicalUrl());
			Assertions.assertEquals(
				expectedPagePathNodeDTO.getTitle(),
				actualPagePathNodeDTO.getTitle());
			Assertions.assertEquals(
				expectedPagePathNodeDTO.getViews(),
				actualPagePathNodeDTO.getViews());

			_assertPagePathNodeDTOs(
				actualPagePathNodeDTO.getFollowingPagePathNodeDTOs(),
				expectedPagePathNodeDTO.getFollowingPagePathNodeDTOs());
			_assertPagePathNodeDTOs(
				actualPagePathNodeDTO.getPreviousPagePathNodeDTOs(),
				expectedPagePathNodeDTO.getPreviousPagePathNodeDTOs());
		}
	}

	@Mock
	private DataFetchingEnvironment _dataFetchingEnvironment;

	@Autowired
	private PagePathDataFetcher _pagePathDataFetcher;

	@MockBean
	private PagePathDog _pagePathDog;

}