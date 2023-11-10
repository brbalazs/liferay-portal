/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dog.PageReferrerDog;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.common.model.Interval;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Gabriel Ibson
 */
public class PageReferrerDogTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BQSQLResource(resourcePath = "page_referrers_events.sql")
	@Test
	public void testAcquisitionChannels() {
		Map<String, Double> acquisitionChannels =
			_pageReferrerDog.getAcquisitionChannels(
				new SearchQueryContext() {
					{
						setCanonicalUrl("http://liferay.com");
						setChannelId("1");
						setDataSourceId("1");
						setInterval(Interval.DAY.getKey());
						setRangeKey(7);
					}
				});

		Assertions.assertEquals(3, acquisitionChannels.get("direct"), 0);

		acquisitionChannels = _pageReferrerDog.getAcquisitionChannels(
			new SearchQueryContext() {
				{
					setCanonicalUrl("http://liferay.com");
					setDataSourceId("1");
					setInterval(Interval.DAY.getKey());
					setRangeKey(7);
				}
			});

		Assertions.assertEquals(3, acquisitionChannels.get("direct"), 0);
	}

	@BQSQLResource(resourcePath = "page_referrers_events.sql")
	@Test
	public void testPageReferrerHosts() {
		Map<String, Double> pageReferrers = _pageReferrerDog.getPageReferrers(
			"referrerHost",
			new SearchQueryContext() {
				{
					setCanonicalUrl("http://liferay.com");
					setChannelId("1");
					setDataSourceId("1");
					setInterval(Interval.DAY.getKey());
					setRangeKey(7);
				}
			},
			10);

		Assertions.assertEquals(
			1, pageReferrers.size(), pageReferrers.toString());

		pageReferrers = _pageReferrerDog.getPageReferrers(
			"referrerHost",
			new SearchQueryContext() {
				{
					setCanonicalUrl("http://liferay.com");
					setDataSourceId("1");
					setInterval(Interval.DAY.getKey());
					setRangeKey(7);
				}
			},
			10);

		Assertions.assertEquals(
			1, pageReferrers.size(), pageReferrers.toString());
	}

	@BQSQLResource(resourcePath = "page_referrers_events.sql")
	@Test
	public void testPageReferrers() {
		Map<String, Double> pageReferrers = _pageReferrerDog.getPageReferrers(
			"referrerCanonicalUrl",
			new SearchQueryContext() {
				{
					setCanonicalUrl("http://liferay.com");
					setChannelId("1");
					setDataSourceId("1");
					setInterval(Interval.DAY.getKey());
					setRangeKey(7);
				}
			},
			10);

		Assertions.assertEquals(
			1, pageReferrers.size(), pageReferrers.toString());

		pageReferrers = _pageReferrerDog.getPageReferrers(
			"referrerCanonicalUrl",
			new SearchQueryContext() {
				{
					setCanonicalUrl("http://liferay.com");
					setDataSourceId("1");
					setInterval(Interval.DAY.getKey());
					setRangeKey(7);
				}
			},
			10);

		Assertions.assertEquals(
			1, pageReferrers.size(), pageReferrers.toString());
	}

	@BQSQLResource(resourcePath = "page_referrers_events.sql")
	@Test
	public void testSocialPageReferrers() {
		Map<String, Double> socialReferrers =
			_pageReferrerDog.getSocialPageReferrers(
				new SearchQueryContext() {
					{
						setCanonicalUrl("http://liferay.com");
						setChannelId("1");
						setDataSourceId("1");
						setInterval(Interval.DAY.getKey());
						setRangeKey(7);
					}
				});

		Assertions.assertEquals(
			1, socialReferrers.size(), socialReferrers.toString());

		socialReferrers = _pageReferrerDog.getSocialPageReferrers(
			new SearchQueryContext() {
				{
					setCanonicalUrl("http://liferay.com");
					setDataSourceId("1");
					setInterval(Interval.DAY.getKey());
					setRangeKey(7);
				}
			});

		Assertions.assertEquals(
			1, socialReferrers.size(), socialReferrers.toString());
	}

	@Autowired
	private PageReferrerDog _pageReferrerDog;

}