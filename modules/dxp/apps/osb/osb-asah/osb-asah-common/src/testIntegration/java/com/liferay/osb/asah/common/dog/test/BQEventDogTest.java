/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.dog.BQEventDog;
import com.liferay.osb.asah.common.dog.ChannelDog;
import com.liferay.osb.asah.common.entity.BQEvent;
import com.liferay.osb.asah.common.entity.BQEventProperty;
import com.liferay.osb.asah.common.entity.Channel;
import com.liferay.osb.asah.common.model.BQEventPropertyValue;
import com.liferay.osb.asah.common.model.RecentAsset;
import com.liferay.osb.asah.common.model.RecentPage;
import com.liferay.osb.asah.common.model.RecentSite;
import com.liferay.osb.asah.common.model.SearchKeyword;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.annotation.SQLResource;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.tuple.Pair;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;

/**
 * @author Leslie Wong
 */
@Import(JDBCTestConfiguration.class)
public class BQEventDogTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentBlogsLast7Days() {
		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.BLOG,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 10, new String[0], TimeRange.LAST_7_DAYS);

		Assertions.assertEquals(3, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentAsset> expectedRecentAssets = new ArrayList<>();

		RecentAsset recentAsset = new RecentAsset();

		recentAsset.setAssetId("a73ihsy9");
		recentAsset.setAssetTitle("Blog Title 2");
		recentAsset.setContentType(RecentAsset.ContentType.BLOG);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentAsset.setUrl("https://www.beryl.com/delivery");
		recentAsset.setVisits(1L);

		expectedRecentAssets.add(recentAsset);

		recentAsset = new RecentAsset();

		recentAsset.setAssetId("b73ihsy9");
		recentAsset.setAssetTitle("Blog Title 3");
		recentAsset.setContentType(RecentAsset.ContentType.BLOG);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentAsset.setUrl("https://www.beryl.com/confirmation");
		recentAsset.setVisits(1L);

		expectedRecentAssets.add(recentAsset);

		recentAsset = new RecentAsset();

		recentAsset.setAssetId("c73ihsy9");
		recentAsset.setAssetTitle("Blog Title 4");
		recentAsset.setContentType(RecentAsset.ContentType.BLOG);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -4));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -4));
		recentAsset.setUrl("https://www.beryl.com/about");
		recentAsset.setVisits(1L);

		expectedRecentAssets.add(recentAsset);

		Assertions.assertEquals(
			expectedRecentAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_last_24_hours_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentBlogsLast24Hours() {
		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.BLOG,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[0], TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(3, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentAsset> recentAssets = recentAssetPage.getContent();

		Assertions.assertFalse(recentAssets.isEmpty());

		Map<Pair<String, String>, Long> expectedRecentAssetCounts =
			new HashMap<Pair<String, String>, Long>() {
				{
					put(Pair.of("e131fabc", "Blog Title 1"), 10L);
					put(Pair.of("a73ihsy9", "Blog Title 2"), 3L);
					put(Pair.of("b73ihsy9", "Blog Title 3"), 3L);
				}
			};

		for (RecentAsset recentAsset : recentAssets) {
			Pair<String, String> pair = Pair.of(
				recentAsset.getAssetId(), recentAsset.getAssetTitle());

			Assertions.assertEquals(
				expectedRecentAssetCounts.get(pair), recentAsset.getVisits());

			expectedRecentAssetCounts.remove(pair);
		}

		Assertions.assertTrue(expectedRecentAssetCounts.isEmpty());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentBlogsLast28Days() {
		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.BLOG,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[0], TimeRange.LAST_28_DAYS);

		Assertions.assertEquals(4, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentAsset> expectedRecentAssets = new ArrayList<>();

		RecentAsset recentAsset = new RecentAsset();

		recentAsset.setAssetId("a73ihsy9");
		recentAsset.setAssetTitle("Blog Title 2");
		recentAsset.setContentType(RecentAsset.ContentType.BLOG);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -26));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentAsset.setUrl("https://www.beryl.com/delivery");
		recentAsset.setVisits(3L);

		expectedRecentAssets.add(recentAsset);

		recentAsset = new RecentAsset();

		recentAsset.setAssetId("b73ihsy9");
		recentAsset.setAssetTitle("Blog Title 3");
		recentAsset.setContentType(RecentAsset.ContentType.BLOG);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -23));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentAsset.setUrl("https://www.beryl.com/confirmation");
		recentAsset.setVisits(3L);

		expectedRecentAssets.add(recentAsset);

		recentAsset = new RecentAsset();

		recentAsset.setAssetId("c73ihsy9");
		recentAsset.setAssetTitle("Blog Title 4");
		recentAsset.setContentType(RecentAsset.ContentType.BLOG);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentAsset.setUrl("https://www.beryl.com/about");
		recentAsset.setVisits(3L);

		expectedRecentAssets.add(recentAsset);

		recentAsset = new RecentAsset();

		recentAsset.setAssetId("e131fabc");
		recentAsset.setAssetTitle("Blog Title 1");
		recentAsset.setContentType(RecentAsset.ContentType.BLOG);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentAsset.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentAsset.setVisits(3L);

		expectedRecentAssets.add(recentAsset);

		Assertions.assertEquals(
			expectedRecentAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentBlogsLast30Days() {
		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.BLOG,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 10, new String[0], TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(4, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentAsset> expectedRecentAssets = new ArrayList<>();

		RecentAsset recentAsset = new RecentAsset();

		recentAsset.setAssetId("a73ihsy9");
		recentAsset.setAssetTitle("Blog Title 2");
		recentAsset.setContentType(RecentAsset.ContentType.BLOG);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -22));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentAsset.setUrl("https://www.beryl.com/delivery");
		recentAsset.setVisits(3L);

		expectedRecentAssets.add(recentAsset);

		recentAsset = new RecentAsset();

		recentAsset.setAssetId("b73ihsy9");
		recentAsset.setAssetTitle("Blog Title 3");
		recentAsset.setContentType(RecentAsset.ContentType.BLOG);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -27));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentAsset.setUrl("https://www.beryl.com/confirmation");
		recentAsset.setVisits(3L);

		expectedRecentAssets.add(recentAsset);

		recentAsset = new RecentAsset();

		recentAsset.setAssetId("c73ihsy9");
		recentAsset.setAssetTitle("Blog Title 4");
		recentAsset.setContentType(RecentAsset.ContentType.BLOG);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -24));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -4));
		recentAsset.setUrl("https://www.beryl.com/about");
		recentAsset.setVisits(3L);

		expectedRecentAssets.add(recentAsset);

		recentAsset = new RecentAsset();

		recentAsset.setAssetId("e131fabc");
		recentAsset.setAssetTitle("Blog Title 1");
		recentAsset.setContentType(RecentAsset.ContentType.BLOG);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -9));
		recentAsset.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentAsset.setVisits(3L);

		expectedRecentAssets.add(recentAsset);

		Assertions.assertEquals(
			expectedRecentAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentBlogsSortByFirstVisitDate() {
		RecentAsset recentAsset1 = new RecentAsset();

		recentAsset1.setAssetId("a73ihsy9");
		recentAsset1.setAssetTitle("Blog Title 2");
		recentAsset1.setContentType(RecentAsset.ContentType.BLOG);
		recentAsset1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -26));
		recentAsset1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentAsset1.setUrl("https://www.beryl.com/delivery");
		recentAsset1.setVisits(3L);

		RecentAsset recentAsset2 = new RecentAsset();

		recentAsset2.setAssetId("b73ihsy9");
		recentAsset2.setAssetTitle("Blog Title 3");
		recentAsset2.setContentType(RecentAsset.ContentType.BLOG);
		recentAsset2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -30));
		recentAsset2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentAsset2.setUrl("https://www.beryl.com/confirmation");
		recentAsset2.setVisits(4L);

		RecentAsset recentAsset3 = new RecentAsset();

		recentAsset3.setAssetId("c73ihsy9");
		recentAsset3.setAssetTitle("Blog Title 4");
		recentAsset3.setContentType(RecentAsset.ContentType.BLOG);
		recentAsset3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentAsset3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentAsset3.setUrl("https://www.beryl.com/about");
		recentAsset3.setVisits(3L);

		RecentAsset recentAsset4 = new RecentAsset();

		recentAsset4.setAssetId("e131fabc");
		recentAsset4.setAssetTitle("Blog Title 1");
		recentAsset4.setContentType(RecentAsset.ContentType.BLOG);
		recentAsset4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentAsset4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentAsset4.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentAsset4.setVisits(3L);

		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.BLOG,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"firstVisitDate", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentAsset2, recentAsset3, recentAsset1, recentAsset4),
			recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.BLOG,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"firstVisitDate", "desc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentAsset4, recentAsset1, recentAsset3, recentAsset2),
			recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentBlogsSortByLastVisitDate() {
		RecentAsset recentAsset1 = new RecentAsset();

		recentAsset1.setAssetId("a73ihsy9");
		recentAsset1.setAssetTitle("Blog Title 2");
		recentAsset1.setContentType(RecentAsset.ContentType.BLOG);
		recentAsset1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -26));
		recentAsset1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentAsset1.setUrl("https://www.beryl.com/delivery");
		recentAsset1.setVisits(3L);

		RecentAsset recentAsset2 = new RecentAsset();

		recentAsset2.setAssetId("b73ihsy9");
		recentAsset2.setAssetTitle("Blog Title 3");
		recentAsset2.setContentType(RecentAsset.ContentType.BLOG);
		recentAsset2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -30));
		recentAsset2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentAsset2.setUrl("https://www.beryl.com/confirmation");
		recentAsset2.setVisits(4L);

		RecentAsset recentAsset3 = new RecentAsset();

		recentAsset3.setAssetId("c73ihsy9");
		recentAsset3.setAssetTitle("Blog Title 4");
		recentAsset3.setContentType(RecentAsset.ContentType.BLOG);
		recentAsset3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentAsset3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentAsset3.setUrl("https://www.beryl.com/about");
		recentAsset3.setVisits(3L);

		RecentAsset recentAsset4 = new RecentAsset();

		recentAsset4.setAssetId("e131fabc");
		recentAsset4.setAssetTitle("Blog Title 1");
		recentAsset4.setContentType(RecentAsset.ContentType.BLOG);
		recentAsset4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentAsset4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentAsset4.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentAsset4.setVisits(3L);

		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.BLOG,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"lastVisitDate", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentAsset3, recentAsset1, recentAsset2, recentAsset4),
			recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.BLOG,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"lastVisitDate", "desc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentAsset4, recentAsset2, recentAsset1, recentAsset3),
			recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentBlogsSortByVisits() {
		RecentAsset recentAsset1 = new RecentAsset();

		recentAsset1.setAssetId("a73ihsy9");
		recentAsset1.setAssetTitle("Blog Title 2");
		recentAsset1.setContentType(RecentAsset.ContentType.BLOG);
		recentAsset1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -26));
		recentAsset1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentAsset1.setUrl("https://www.beryl.com/delivery");
		recentAsset1.setVisits(3L);

		RecentAsset recentAsset2 = new RecentAsset();

		recentAsset2.setAssetId("b73ihsy9");
		recentAsset2.setAssetTitle("Blog Title 3");
		recentAsset2.setContentType(RecentAsset.ContentType.BLOG);
		recentAsset2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -30));
		recentAsset2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentAsset2.setUrl("https://www.beryl.com/confirmation");
		recentAsset2.setVisits(4L);

		RecentAsset recentAsset3 = new RecentAsset();

		recentAsset3.setAssetId("c73ihsy9");
		recentAsset3.setAssetTitle("Blog Title 4");
		recentAsset3.setContentType(RecentAsset.ContentType.BLOG);
		recentAsset3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentAsset3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentAsset3.setUrl("https://www.beryl.com/about");
		recentAsset3.setVisits(3L);

		RecentAsset recentAsset4 = new RecentAsset();

		recentAsset4.setAssetId("e131fabc");
		recentAsset4.setAssetTitle("Blog Title 1");
		recentAsset4.setContentType(RecentAsset.ContentType.BLOG);
		recentAsset4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentAsset4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentAsset4.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentAsset4.setVisits(3L);

		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.BLOG,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"visits", "asc", "assetTitle", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentAsset4, recentAsset1, recentAsset3, recentAsset2),
			recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.BLOG,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"visits", "desc", "assetTitle", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentAsset2, recentAsset4, recentAsset1, recentAsset3),
			recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentBlogsWithPagination() {
		RecentAsset recentAsset1 = new RecentAsset();

		recentAsset1.setAssetId("a73ihsy9");
		recentAsset1.setAssetTitle("Blog Title 2");
		recentAsset1.setContentType(RecentAsset.ContentType.BLOG);
		recentAsset1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -26));
		recentAsset1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentAsset1.setUrl("https://www.beryl.com/delivery");
		recentAsset1.setVisits(3L);

		RecentAsset recentAsset2 = new RecentAsset();

		recentAsset2.setAssetId("b73ihsy9");
		recentAsset2.setAssetTitle("Blog Title 3");
		recentAsset2.setContentType(RecentAsset.ContentType.BLOG);
		recentAsset2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -30));
		recentAsset2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentAsset2.setUrl("https://www.beryl.com/confirmation");
		recentAsset2.setVisits(4L);

		RecentAsset recentAsset3 = new RecentAsset();

		recentAsset3.setAssetId("c73ihsy9");
		recentAsset3.setAssetTitle("Blog Title 4");
		recentAsset3.setContentType(RecentAsset.ContentType.BLOG);
		recentAsset3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentAsset3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentAsset3.setUrl("https://www.beryl.com/about");
		recentAsset3.setVisits(3L);

		RecentAsset recentAsset4 = new RecentAsset();

		recentAsset4.setAssetId("e131fabc");
		recentAsset4.setAssetTitle("Blog Title 1");
		recentAsset4.setContentType(RecentAsset.ContentType.BLOG);
		recentAsset4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentAsset4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentAsset4.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentAsset4.setVisits(3L);

		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.BLOG,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 3, new String[] {"firstVisitDate", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(recentAsset2, recentAsset3, recentAsset1),
			recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.BLOG,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			1, 3, new String[] {"firstVisitDate", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Collections.singletonList(recentAsset4),
			recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentBlogsWithSuppression() {
		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.BLOG,
			"8bb3cd4319c4cc4df1addc31cb0fae500288133b91228a1cacb4ff2802446220",
			0, 10, new String[0], TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(0L, recentAssetPage.getTotalElements());
		Assertions.assertEquals(0L, recentAssetPage.getTotalPages());

		List<RecentAsset> recentAssets = recentAssetPage.getContent();

		Assertions.assertTrue(recentAssets.isEmpty());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_yesterday_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentBlogsYesterday() {
		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.BLOG,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 10, new String[0], TimeRange.YESTERDAY);

		Assertions.assertEquals(3, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentAsset> recentAssets = recentAssetPage.getContent();

		Assertions.assertFalse(recentAssets.isEmpty());

		Map<Pair<String, String>, Long> expectedRecentAssetCounts =
			new HashMap<Pair<String, String>, Long>() {
				{
					put(Pair.of("e131fabc", "Blog Title 1"), 10L);
					put(Pair.of("a73ihsy9", "Blog Title 2"), 2L);
					put(Pair.of("b73ihsy9", "Blog Title 3"), 2L);
				}
			};

		for (RecentAsset recentAsset : recentAssets) {
			Pair<String, String> pair = Pair.of(
				recentAsset.getAssetId(), recentAsset.getAssetTitle());

			Assertions.assertEquals(
				expectedRecentAssetCounts.get(pair), recentAsset.getVisits());

			expectedRecentAssetCounts.remove(pair);
		}

		Assertions.assertTrue(expectedRecentAssetCounts.isEmpty());
	}

	@BQSQLResource(
		resourcePath = "test_get_recent_bq_event_property_values_bq.sql"
	)
	@SQLResource(resourcePath = "test_get_recent_bq_event_property_values.sql")
	@Test
	public void testGetRecentBQEventPropertyValues() throws Exception {
		Assertions.assertEquals(
			new ArrayList<BQEventPropertyValue>() {
				{
					add(
						new BQEventPropertyValue(
							DateUtil.newDayDate(), "testValue2"));
					add(
						new BQEventPropertyValue(
							DateUtil.addDays(DateUtil.newDayDate(), -1),
							"testValue1"));
				}
			},
			_bqEventDog.getRecentBQEventPropertyValues(98765L, 2));
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentDocumentsLast7Days() {
		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.DOCUMENT,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 10, new String[0], TimeRange.LAST_7_DAYS);

		Assertions.assertEquals(3, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentAsset> expectedRecentAssets = new ArrayList<>();

		RecentAsset recentAsset = new RecentAsset();

		recentAsset.setAssetId("a73ihsy9");
		recentAsset.setAssetTitle("Document Title 2");
		recentAsset.setContentType(RecentAsset.ContentType.DOCUMENT);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentAsset.setUrl("https://www.beryl.com/docs/doc-2");
		recentAsset.setVisits(1L);

		expectedRecentAssets.add(recentAsset);

		recentAsset = new RecentAsset();

		recentAsset.setAssetId("b73ihsy9");
		recentAsset.setAssetTitle("Document Title 3");
		recentAsset.setContentType(RecentAsset.ContentType.DOCUMENT);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentAsset.setUrl("https://www.beryl.com/docs/doc-3");
		recentAsset.setVisits(1L);

		expectedRecentAssets.add(recentAsset);

		recentAsset = new RecentAsset();

		recentAsset.setAssetId("c73ihsy9");
		recentAsset.setAssetTitle("Document Title 4");
		recentAsset.setContentType(RecentAsset.ContentType.DOCUMENT);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -4));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -4));
		recentAsset.setUrl("https://www.beryl.com/docs/doc-4");
		recentAsset.setVisits(1L);

		expectedRecentAssets.add(recentAsset);

		Assertions.assertEquals(
			expectedRecentAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_last_24_hours_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentDocumentsLast24Hours() {
		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.DOCUMENT,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[0], TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(3, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentAsset> recentAssets = recentAssetPage.getContent();

		Assertions.assertFalse(recentAssets.isEmpty());

		Map<Pair<String, String>, Long> expectedRecentAssetCounts =
			new HashMap<Pair<String, String>, Long>() {
				{
					put(Pair.of("e131fabc", "Document Title 1"), 10L);
					put(Pair.of("a73ihsy9", "Document Title 2"), 3L);
					put(Pair.of("b73ihsy9", "Document Title 3"), 3L);
				}
			};

		for (RecentAsset recentAsset : recentAssets) {
			Pair<String, String> pair = Pair.of(
				recentAsset.getAssetId(), recentAsset.getAssetTitle());

			Assertions.assertEquals(
				expectedRecentAssetCounts.get(pair), recentAsset.getVisits());

			expectedRecentAssetCounts.remove(pair);
		}

		Assertions.assertTrue(expectedRecentAssetCounts.isEmpty());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentDocumentsLast28Days() {
		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.DOCUMENT,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[0], TimeRange.LAST_28_DAYS);

		Assertions.assertEquals(4, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentAsset> expectedRecentAssets = new ArrayList<>();

		RecentAsset recentAsset = new RecentAsset();

		recentAsset.setAssetId("a73ihsy9");
		recentAsset.setAssetTitle("Document Title 2");
		recentAsset.setContentType(RecentAsset.ContentType.DOCUMENT);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -26));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentAsset.setUrl("https://www.beryl.com/docs/doc-2");
		recentAsset.setVisits(3L);

		expectedRecentAssets.add(recentAsset);

		recentAsset = new RecentAsset();

		recentAsset.setAssetId("b73ihsy9");
		recentAsset.setAssetTitle("Document Title 3");
		recentAsset.setContentType(RecentAsset.ContentType.DOCUMENT);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -23));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentAsset.setUrl("https://www.beryl.com/docs/doc-3");
		recentAsset.setVisits(3L);

		expectedRecentAssets.add(recentAsset);

		recentAsset = new RecentAsset();

		recentAsset.setAssetId("c73ihsy9");
		recentAsset.setAssetTitle("Document Title 4");
		recentAsset.setContentType(RecentAsset.ContentType.DOCUMENT);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentAsset.setUrl("https://www.beryl.com/docs/doc-4");
		recentAsset.setVisits(3L);

		expectedRecentAssets.add(recentAsset);

		recentAsset = new RecentAsset();

		recentAsset.setAssetId("e131fabc");
		recentAsset.setAssetTitle("Document Title 1");
		recentAsset.setContentType(RecentAsset.ContentType.DOCUMENT);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentAsset.setUrl("https://www.beryl.com/docs/doc-1");
		recentAsset.setVisits(3L);

		expectedRecentAssets.add(recentAsset);

		Assertions.assertEquals(
			expectedRecentAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentDocumentsLast30Days() {
		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.DOCUMENT,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 10, new String[0], TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(4, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentAsset> expectedRecentAssets = new ArrayList<>();

		RecentAsset recentAsset = new RecentAsset();

		recentAsset.setAssetId("a73ihsy9");
		recentAsset.setAssetTitle("Document Title 2");
		recentAsset.setContentType(RecentAsset.ContentType.DOCUMENT);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -22));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentAsset.setUrl("https://www.beryl.com/docs/doc-2");
		recentAsset.setVisits(3L);

		expectedRecentAssets.add(recentAsset);

		recentAsset = new RecentAsset();

		recentAsset.setAssetId("b73ihsy9");
		recentAsset.setAssetTitle("Document Title 3");
		recentAsset.setContentType(RecentAsset.ContentType.DOCUMENT);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -27));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentAsset.setUrl("https://www.beryl.com/docs/doc-3");
		recentAsset.setVisits(3L);

		expectedRecentAssets.add(recentAsset);

		recentAsset = new RecentAsset();

		recentAsset.setAssetId("c73ihsy9");
		recentAsset.setAssetTitle("Document Title 4");
		recentAsset.setContentType(RecentAsset.ContentType.DOCUMENT);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -24));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -4));
		recentAsset.setUrl("https://www.beryl.com/docs/doc-4");
		recentAsset.setVisits(3L);

		expectedRecentAssets.add(recentAsset);

		recentAsset = new RecentAsset();

		recentAsset.setAssetId("e131fabc");
		recentAsset.setAssetTitle("Document Title 1");
		recentAsset.setContentType(RecentAsset.ContentType.DOCUMENT);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -9));
		recentAsset.setUrl("https://www.beryl.com/docs/doc-1");
		recentAsset.setVisits(3L);

		expectedRecentAssets.add(recentAsset);

		Assertions.assertEquals(
			expectedRecentAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentDocumentsSortByFirstVisitDate() {
		RecentAsset recentAsset1 = new RecentAsset();

		recentAsset1.setAssetId("a73ihsy9");
		recentAsset1.setAssetTitle("Document Title 2");
		recentAsset1.setContentType(RecentAsset.ContentType.DOCUMENT);
		recentAsset1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -26));
		recentAsset1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentAsset1.setUrl("https://www.beryl.com/docs/doc-2");
		recentAsset1.setVisits(3L);

		RecentAsset recentAsset2 = new RecentAsset();

		recentAsset2.setAssetId("b73ihsy9");
		recentAsset2.setAssetTitle("Document Title 3");
		recentAsset2.setContentType(RecentAsset.ContentType.DOCUMENT);
		recentAsset2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -30));
		recentAsset2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentAsset2.setUrl("https://www.beryl.com/docs/doc-3");
		recentAsset2.setVisits(4L);

		RecentAsset recentAsset3 = new RecentAsset();

		recentAsset3.setAssetId("c73ihsy9");
		recentAsset3.setAssetTitle("Document Title 4");
		recentAsset3.setContentType(RecentAsset.ContentType.DOCUMENT);
		recentAsset3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentAsset3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentAsset3.setUrl("https://www.beryl.com/docs/doc-4");
		recentAsset3.setVisits(3L);

		RecentAsset recentAsset4 = new RecentAsset();

		recentAsset4.setAssetId("e131fabc");
		recentAsset4.setAssetTitle("Document Title 1");
		recentAsset4.setContentType(RecentAsset.ContentType.DOCUMENT);
		recentAsset4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentAsset4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentAsset4.setUrl("https://www.beryl.com/docs/doc-1");
		recentAsset4.setVisits(3L);

		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.DOCUMENT,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"firstVisitDate", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentAsset2, recentAsset3, recentAsset1, recentAsset4),
			recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.DOCUMENT,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"firstVisitDate", "desc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentAsset4, recentAsset1, recentAsset3, recentAsset2),
			recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentDocumentsSortByLastVisitDate() {
		RecentAsset recentAsset1 = new RecentAsset();

		recentAsset1.setAssetId("a73ihsy9");
		recentAsset1.setAssetTitle("Document Title 2");
		recentAsset1.setContentType(RecentAsset.ContentType.DOCUMENT);
		recentAsset1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -26));
		recentAsset1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentAsset1.setUrl("https://www.beryl.com/docs/doc-2");
		recentAsset1.setVisits(3L);

		RecentAsset recentAsset2 = new RecentAsset();

		recentAsset2.setAssetId("b73ihsy9");
		recentAsset2.setAssetTitle("Document Title 3");
		recentAsset2.setContentType(RecentAsset.ContentType.DOCUMENT);
		recentAsset2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -30));
		recentAsset2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentAsset2.setUrl("https://www.beryl.com/docs/doc-3");
		recentAsset2.setVisits(4L);

		RecentAsset recentAsset3 = new RecentAsset();

		recentAsset3.setAssetId("c73ihsy9");
		recentAsset3.setAssetTitle("Document Title 4");
		recentAsset3.setContentType(RecentAsset.ContentType.DOCUMENT);
		recentAsset3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentAsset3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentAsset3.setUrl("https://www.beryl.com/docs/doc-4");
		recentAsset3.setVisits(3L);

		RecentAsset recentAsset4 = new RecentAsset();

		recentAsset4.setAssetId("e131fabc");
		recentAsset4.setAssetTitle("Document Title 1");
		recentAsset4.setContentType(RecentAsset.ContentType.DOCUMENT);
		recentAsset4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentAsset4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentAsset4.setUrl("https://www.beryl.com/docs/doc-1");
		recentAsset4.setVisits(3L);

		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.DOCUMENT,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"lastVisitDate", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentAsset3, recentAsset1, recentAsset2, recentAsset4),
			recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.DOCUMENT,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"lastVisitDate", "desc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentAsset4, recentAsset2, recentAsset1, recentAsset3),
			recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentDocumentsSortByVisits() {
		RecentAsset recentAsset1 = new RecentAsset();

		recentAsset1.setAssetId("a73ihsy9");
		recentAsset1.setAssetTitle("Document Title 2");
		recentAsset1.setContentType(RecentAsset.ContentType.DOCUMENT);
		recentAsset1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -26));
		recentAsset1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentAsset1.setUrl("https://www.beryl.com/docs/doc-2");
		recentAsset1.setVisits(3L);

		RecentAsset recentAsset2 = new RecentAsset();

		recentAsset2.setAssetId("b73ihsy9");
		recentAsset2.setAssetTitle("Document Title 3");
		recentAsset2.setContentType(RecentAsset.ContentType.DOCUMENT);
		recentAsset2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -30));
		recentAsset2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentAsset2.setUrl("https://www.beryl.com/docs/doc-3");
		recentAsset2.setVisits(4L);

		RecentAsset recentAsset3 = new RecentAsset();

		recentAsset3.setAssetId("c73ihsy9");
		recentAsset3.setAssetTitle("Document Title 4");
		recentAsset3.setContentType(RecentAsset.ContentType.DOCUMENT);
		recentAsset3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentAsset3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentAsset3.setUrl("https://www.beryl.com/docs/doc-4");
		recentAsset3.setVisits(3L);

		RecentAsset recentAsset4 = new RecentAsset();

		recentAsset4.setAssetId("e131fabc");
		recentAsset4.setAssetTitle("Document Title 1");
		recentAsset4.setContentType(RecentAsset.ContentType.DOCUMENT);
		recentAsset4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentAsset4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentAsset4.setUrl("https://www.beryl.com/docs/doc-1");
		recentAsset4.setVisits(3L);

		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.DOCUMENT,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"visits", "asc", "assetTitle", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentAsset4, recentAsset1, recentAsset3, recentAsset2),
			recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.DOCUMENT,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"visits", "desc", "assetTitle", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentAsset2, recentAsset4, recentAsset1, recentAsset3),
			recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentDocumentsWithPagination() {
		RecentAsset recentAsset1 = new RecentAsset();

		recentAsset1.setAssetId("a73ihsy9");
		recentAsset1.setAssetTitle("Document Title 2");
		recentAsset1.setContentType(RecentAsset.ContentType.DOCUMENT);
		recentAsset1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -26));
		recentAsset1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentAsset1.setUrl("https://www.beryl.com/docs/doc-2");
		recentAsset1.setVisits(3L);

		RecentAsset recentAsset2 = new RecentAsset();

		recentAsset2.setAssetId("b73ihsy9");
		recentAsset2.setAssetTitle("Document Title 3");
		recentAsset2.setContentType(RecentAsset.ContentType.DOCUMENT);
		recentAsset2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -30));
		recentAsset2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentAsset2.setUrl("https://www.beryl.com/docs/doc-3");
		recentAsset2.setVisits(4L);

		RecentAsset recentAsset3 = new RecentAsset();

		recentAsset3.setAssetId("c73ihsy9");
		recentAsset3.setAssetTitle("Document Title 4");
		recentAsset3.setContentType(RecentAsset.ContentType.DOCUMENT);
		recentAsset3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentAsset3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentAsset3.setUrl("https://www.beryl.com/docs/doc-4");
		recentAsset3.setVisits(3L);

		RecentAsset recentAsset4 = new RecentAsset();

		recentAsset4.setAssetId("e131fabc");
		recentAsset4.setAssetTitle("Document Title 1");
		recentAsset4.setContentType(RecentAsset.ContentType.DOCUMENT);
		recentAsset4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentAsset4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentAsset4.setUrl("https://www.beryl.com/docs/doc-1");
		recentAsset4.setVisits(3L);

		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.DOCUMENT,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 3, new String[] {"firstVisitDate", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(recentAsset2, recentAsset3, recentAsset1),
			recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.DOCUMENT,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			1, 3, new String[] {"lastVisitDate", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Collections.singletonList(recentAsset4),
			recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentDocumentsWithSuppression() {
		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.DOCUMENT,
			"8bb3cd4319c4cc4df1addc31cb0fae500288133b91228a1cacb4ff2802446220",
			0, 10, new String[0], TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(0L, recentAssetPage.getTotalElements());
		Assertions.assertEquals(0L, recentAssetPage.getTotalPages());

		List<RecentAsset> recentAssets = recentAssetPage.getContent();

		Assertions.assertTrue(recentAssets.isEmpty());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_yesterday_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentDocumentsYesterday() {
		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.DOCUMENT,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 10, new String[0], TimeRange.YESTERDAY);

		Assertions.assertEquals(3, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentAsset> recentAssets = recentAssetPage.getContent();

		Assertions.assertFalse(recentAssets.isEmpty());

		Map<Pair<String, String>, Long> expectedRecentAssetCounts =
			new HashMap<Pair<String, String>, Long>() {
				{
					put(Pair.of("e131fabc", "Document Title 1"), 10L);
					put(Pair.of("a73ihsy9", "Document Title 2"), 2L);
					put(Pair.of("b73ihsy9", "Document Title 3"), 2L);
				}
			};

		for (RecentAsset recentAsset : recentAssets) {
			Pair<String, String> pair = Pair.of(
				recentAsset.getAssetId(), recentAsset.getAssetTitle());

			Assertions.assertEquals(
				expectedRecentAssetCounts.get(pair), recentAsset.getVisits());

			expectedRecentAssetCounts.remove(pair);
		}

		Assertions.assertTrue(expectedRecentAssetCounts.isEmpty());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentFormsLast7Days() {
		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.FORM,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 10, new String[0], TimeRange.LAST_7_DAYS);

		Assertions.assertEquals(3, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentAsset> expectedRecentAssets = new ArrayList<>();

		RecentAsset recentAsset = new RecentAsset();

		recentAsset.setAssetId("a37higg1");
		recentAsset.setAssetTitle("Form Title 2");
		recentAsset.setContentType(RecentAsset.ContentType.FORM);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentAsset.setUrl("https://www.beryl.com/forms/form-2");
		recentAsset.setVisits(1L);

		expectedRecentAssets.add(recentAsset);

		recentAsset = new RecentAsset();

		recentAsset.setAssetId("b42spry4");
		recentAsset.setAssetTitle("Form Title 3");
		recentAsset.setContentType(RecentAsset.ContentType.FORM);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentAsset.setUrl("https://www.beryl.com/forms/form-3");
		recentAsset.setVisits(1L);

		expectedRecentAssets.add(recentAsset);

		recentAsset = new RecentAsset();

		recentAsset.setAssetId("e242gdef");
		recentAsset.setAssetTitle("Form Title 1");
		recentAsset.setContentType(RecentAsset.ContentType.FORM);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -5));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -5));
		recentAsset.setUrl("https://www.beryl.com/forms/form-1");
		recentAsset.setVisits(1L);

		expectedRecentAssets.add(recentAsset);

		Assertions.assertEquals(
			expectedRecentAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_last_24_hours_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentFormsLast24Hours() {
		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.FORM,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[0], TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(3, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentAsset> recentAssets = recentAssetPage.getContent();

		Assertions.assertFalse(recentAssets.isEmpty());

		Map<Pair<String, String>, Long> expectedRecentAssetCounts =
			new HashMap<Pair<String, String>, Long>() {
				{
					put(Pair.of("e242gdef", "Form Title 1"), 10L);
					put(Pair.of("a37higg1", "Form Title 2"), 3L);
					put(Pair.of("b42spry4", "Form Title 3"), 3L);
				}
			};

		for (RecentAsset recentAsset : recentAssets) {
			Pair<String, String> pair = Pair.of(
				recentAsset.getAssetId(), recentAsset.getAssetTitle());

			Assertions.assertEquals(
				expectedRecentAssetCounts.get(pair), recentAsset.getVisits());

			expectedRecentAssetCounts.remove(pair);
		}

		Assertions.assertTrue(expectedRecentAssetCounts.isEmpty());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentFormsLast28Days() {
		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.FORM,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[0], TimeRange.LAST_28_DAYS);

		Assertions.assertEquals(4, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentAsset> expectedRecentAssets = new ArrayList<>();

		RecentAsset recentAsset = new RecentAsset();

		recentAsset.setAssetId("b42spry4");
		recentAsset.setAssetTitle("Form Title 3");
		recentAsset.setContentType(RecentAsset.ContentType.FORM);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -27));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentAsset.setUrl("https://www.beryl.com/forms/form-3");
		recentAsset.setVisits(3L);

		expectedRecentAssets.add(recentAsset);

		recentAsset = new RecentAsset();

		recentAsset.setAssetId("c99ytfl7");
		recentAsset.setAssetTitle("Form Title 4");
		recentAsset.setContentType(RecentAsset.ContentType.FORM);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentAsset.setUrl("https://www.beryl.com/forms/form-4");
		recentAsset.setVisits(3L);

		expectedRecentAssets.add(recentAsset);

		recentAsset = new RecentAsset();

		recentAsset.setAssetId("e242gdef");
		recentAsset.setAssetTitle("Form Title 1");
		recentAsset.setContentType(RecentAsset.ContentType.FORM);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentAsset.setUrl("https://www.beryl.com/forms/form-1");
		recentAsset.setVisits(3L);

		expectedRecentAssets.add(recentAsset);

		recentAsset = new RecentAsset();

		recentAsset.setAssetId("a37higg1");
		recentAsset.setAssetTitle("Form Title 2");
		recentAsset.setContentType(RecentAsset.ContentType.FORM);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -18));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -10));
		recentAsset.setUrl("https://www.beryl.com/forms/form-2");
		recentAsset.setVisits(2L);

		expectedRecentAssets.add(recentAsset);

		Assertions.assertEquals(
			expectedRecentAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentFormsLast30Days() {
		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.FORM,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[0], TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(4, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentAsset> expectedRecentAssets = new ArrayList<>();

		RecentAsset recentAsset = new RecentAsset();

		recentAsset.setAssetId("e242gdef");
		recentAsset.setAssetTitle("Form Title 1");
		recentAsset.setContentType(RecentAsset.ContentType.FORM);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentAsset.setUrl("https://www.beryl.com/forms/form-1");
		recentAsset.setVisits(4L);

		expectedRecentAssets.add(recentAsset);

		recentAsset = new RecentAsset();

		recentAsset.setAssetId("b42spry4");
		recentAsset.setAssetTitle("Form Title 3");
		recentAsset.setContentType(RecentAsset.ContentType.FORM);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -27));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentAsset.setUrl("https://www.beryl.com/forms/form-3");
		recentAsset.setVisits(3L);

		expectedRecentAssets.add(recentAsset);

		recentAsset = new RecentAsset();

		recentAsset.setAssetId("c99ytfl7");
		recentAsset.setAssetTitle("Form Title 4");
		recentAsset.setContentType(RecentAsset.ContentType.FORM);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentAsset.setUrl("https://www.beryl.com/forms/form-4");
		recentAsset.setVisits(3L);

		expectedRecentAssets.add(recentAsset);

		recentAsset = new RecentAsset();

		recentAsset.setAssetId("a37higg1");
		recentAsset.setAssetTitle("Form Title 2");
		recentAsset.setContentType(RecentAsset.ContentType.FORM);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -18));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -10));
		recentAsset.setUrl("https://www.beryl.com/forms/form-2");
		recentAsset.setVisits(2L);

		expectedRecentAssets.add(recentAsset);

		Assertions.assertEquals(
			expectedRecentAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentFormsSortByFirstVisitDate() {
		RecentAsset recentAsset1 = new RecentAsset();

		recentAsset1.setAssetId("e242gdef");
		recentAsset1.setAssetTitle("Form Title 1");
		recentAsset1.setContentType(RecentAsset.ContentType.FORM);
		recentAsset1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentAsset1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentAsset1.setUrl("https://www.beryl.com/forms/form-1");
		recentAsset1.setVisits(4L);

		RecentAsset recentAsset2 = new RecentAsset();

		recentAsset2.setAssetId("b42spry4");
		recentAsset2.setAssetTitle("Form Title 3");
		recentAsset2.setContentType(RecentAsset.ContentType.FORM);
		recentAsset2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -27));
		recentAsset2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentAsset2.setUrl("https://www.beryl.com/forms/form-3");
		recentAsset2.setVisits(3L);

		RecentAsset recentAsset3 = new RecentAsset();

		recentAsset3.setAssetId("c99ytfl7");
		recentAsset3.setAssetTitle("Form Title 4");
		recentAsset3.setContentType(RecentAsset.ContentType.FORM);
		recentAsset3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentAsset3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentAsset3.setUrl("https://www.beryl.com/forms/form-4");
		recentAsset3.setVisits(3L);

		RecentAsset recentAsset4 = new RecentAsset();

		recentAsset4.setAssetId("a37higg1");
		recentAsset4.setAssetTitle("Form Title 2");
		recentAsset4.setContentType(RecentAsset.ContentType.FORM);
		recentAsset4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -18));
		recentAsset4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -10));
		recentAsset4.setUrl("https://www.beryl.com/forms/form-2");
		recentAsset4.setVisits(2L);

		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.FORM,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"firstVisitDate", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentAsset1, recentAsset3, recentAsset2, recentAsset4),
			recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.FORM,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"firstVisitDate", "desc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentAsset4, recentAsset2, recentAsset3, recentAsset1),
			recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentFormsSortByLastVisitDate() {
		RecentAsset recentAsset1 = new RecentAsset();

		recentAsset1.setAssetId("e242gdef");
		recentAsset1.setAssetTitle("Form Title 1");
		recentAsset1.setContentType(RecentAsset.ContentType.FORM);
		recentAsset1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentAsset1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentAsset1.setUrl("https://www.beryl.com/forms/form-1");
		recentAsset1.setVisits(4L);

		RecentAsset recentAsset2 = new RecentAsset();

		recentAsset2.setAssetId("b42spry4");
		recentAsset2.setAssetTitle("Form Title 3");
		recentAsset2.setContentType(RecentAsset.ContentType.FORM);
		recentAsset2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -27));
		recentAsset2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentAsset2.setUrl("https://www.beryl.com/forms/form-3");
		recentAsset2.setVisits(3L);

		RecentAsset recentAsset3 = new RecentAsset();

		recentAsset3.setAssetId("c99ytfl7");
		recentAsset3.setAssetTitle("Form Title 4");
		recentAsset3.setContentType(RecentAsset.ContentType.FORM);
		recentAsset3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentAsset3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentAsset3.setUrl("https://www.beryl.com/forms/form-4");
		recentAsset3.setVisits(3L);

		RecentAsset recentAsset4 = new RecentAsset();

		recentAsset4.setAssetId("a37higg1");
		recentAsset4.setAssetTitle("Form Title 2");
		recentAsset4.setContentType(RecentAsset.ContentType.FORM);
		recentAsset4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -18));
		recentAsset4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -10));
		recentAsset4.setUrl("https://www.beryl.com/forms/form-2");
		recentAsset4.setVisits(2L);

		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.FORM,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"lastVisitDate", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentAsset4, recentAsset3, recentAsset2, recentAsset1),
			recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.FORM,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"lastVisitDate", "desc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentAsset1, recentAsset2, recentAsset3, recentAsset4),
			recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentFormsSortByVisits() {
		RecentAsset recentAsset1 = new RecentAsset();

		recentAsset1.setAssetId("e242gdef");
		recentAsset1.setAssetTitle("Form Title 1");
		recentAsset1.setContentType(RecentAsset.ContentType.FORM);
		recentAsset1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentAsset1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentAsset1.setUrl("https://www.beryl.com/forms/form-1");
		recentAsset1.setVisits(4L);

		RecentAsset recentAsset2 = new RecentAsset();

		recentAsset2.setAssetId("b42spry4");
		recentAsset2.setAssetTitle("Form Title 3");
		recentAsset2.setContentType(RecentAsset.ContentType.FORM);
		recentAsset2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -27));
		recentAsset2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentAsset2.setUrl("https://www.beryl.com/forms/form-3");
		recentAsset2.setVisits(3L);

		RecentAsset recentAsset3 = new RecentAsset();

		recentAsset3.setAssetId("c99ytfl7");
		recentAsset3.setAssetTitle("Form Title 4");
		recentAsset3.setContentType(RecentAsset.ContentType.FORM);
		recentAsset3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentAsset3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentAsset3.setUrl("https://www.beryl.com/forms/form-4");
		recentAsset3.setVisits(3L);

		RecentAsset recentAsset4 = new RecentAsset();

		recentAsset4.setAssetId("a37higg1");
		recentAsset4.setAssetTitle("Form Title 2");
		recentAsset4.setContentType(RecentAsset.ContentType.FORM);
		recentAsset4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -18));
		recentAsset4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -10));
		recentAsset4.setUrl("https://www.beryl.com/forms/form-2");
		recentAsset4.setVisits(2L);

		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.FORM,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"visits", "asc", "assetTitle", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentAsset4, recentAsset2, recentAsset3, recentAsset1),
			recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.FORM,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"visits", "desc", "assetTitle", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentAsset1, recentAsset2, recentAsset3, recentAsset4),
			recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentFormsWithPagination() {
		RecentAsset recentAsset1 = new RecentAsset();

		recentAsset1.setAssetId("e242gdef");
		recentAsset1.setAssetTitle("Form Title 1");
		recentAsset1.setContentType(RecentAsset.ContentType.FORM);
		recentAsset1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentAsset1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentAsset1.setUrl("https://www.beryl.com/forms/form-1");
		recentAsset1.setVisits(4L);

		RecentAsset recentAsset2 = new RecentAsset();

		recentAsset2.setAssetId("b42spry4");
		recentAsset2.setAssetTitle("Form Title 3");
		recentAsset2.setContentType(RecentAsset.ContentType.FORM);
		recentAsset2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -27));
		recentAsset2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentAsset2.setUrl("https://www.beryl.com/forms/form-3");
		recentAsset2.setVisits(3L);

		RecentAsset recentAsset3 = new RecentAsset();

		recentAsset3.setAssetId("c99ytfl7");
		recentAsset3.setAssetTitle("Form Title 4");
		recentAsset3.setContentType(RecentAsset.ContentType.FORM);
		recentAsset3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentAsset3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentAsset3.setUrl("https://www.beryl.com/forms/form-4");
		recentAsset3.setVisits(3L);

		RecentAsset recentAsset4 = new RecentAsset();

		recentAsset4.setAssetId("a37higg1");
		recentAsset4.setAssetTitle("Form Title 2");
		recentAsset4.setContentType(RecentAsset.ContentType.FORM);
		recentAsset4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -18));
		recentAsset4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -10));
		recentAsset4.setUrl("https://www.beryl.com/forms/form-2");
		recentAsset4.setVisits(2L);

		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.FORM,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 3, new String[] {"firstVisitDate", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(recentAsset1, recentAsset3, recentAsset2),
			recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.FORM,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			1, 3, new String[] {"firstVisitDate", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Collections.singletonList(recentAsset4),
			recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentFormsWithSuppression() {
		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.FORM,
			"8bb3cd4319c4cc4df1addc31cb0fae500288133b91228a1cacb4ff2802446220",
			0, 10, new String[0], TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(0L, recentAssetPage.getTotalElements());
		Assertions.assertEquals(0L, recentAssetPage.getTotalPages());

		List<RecentAsset> recentAssets = recentAssetPage.getContent();

		Assertions.assertTrue(recentAssets.isEmpty());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_yesterday_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentFormsYesterday() {
		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.FORM,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 10, new String[0], TimeRange.YESTERDAY);

		Assertions.assertEquals(3, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentAsset> recentAssets = recentAssetPage.getContent();

		Assertions.assertFalse(recentAssets.isEmpty());

		Map<Pair<String, String>, Long> expectedRecentAssetCounts =
			new HashMap<Pair<String, String>, Long>() {
				{
					put(Pair.of("e242gdef", "Form Title 1"), 10L);
					put(Pair.of("a37higg1", "Form Title 2"), 2L);
					put(Pair.of("b42spry4", "Form Title 3"), 2L);
				}
			};

		for (RecentAsset recentAsset : recentAssets) {
			Pair<String, String> pair = Pair.of(
				recentAsset.getAssetId(), recentAsset.getAssetTitle());

			Assertions.assertEquals(
				expectedRecentAssetCounts.get(pair), recentAsset.getVisits());

			expectedRecentAssetCounts.remove(pair);
		}

		Assertions.assertTrue(expectedRecentAssetCounts.isEmpty());
	}

	@Test
	public void testGetRecentGlobalBQEventProperyValues() throws Exception {
		Date date = DateUtil.newDayDate();

		Channel channel = _channelDog.addChannel("Test Channel");

		_bqEventDog.addBQEvent(
			"Page",
			new HashSet<BQEventProperty>() {
				{
					add(
						new BQEventProperty(
							null, "viewDuration", "testValue1"));
					add(
						new BQEventProperty(
							null, "viewDuration", "testValue2"));
				}
			},
			channel.getId(), date, 1L, DateUtil.addDays(date, -3), "pageViewed",
			"analyticsEventId1", "sessionId", "Home", "userId");

		_bqEventDog.addBQEvent(
			"Page",
			new HashSet<BQEventProperty>() {
				{
					add(
						new BQEventProperty(
							null, "viewDuration", "testValue1"));
					add(
						new BQEventProperty(
							null, "viewDuration", "testValue2"));
				}
			},
			channel.getId(), date, 1L, DateUtil.addDays(date, -1), "pageViewed",
			"analyticsEventId2", "sessionId", "Home", "userId");

		_bqEventDog.addBQEvent(
			"Page",
			new HashSet<BQEventProperty>() {
				{
					add(
						new BQEventProperty(
							null, "viewDuration", "testValue1"));
					add(
						new BQEventProperty(
							null, "viewDuration", "testValue2"));
				}
			},
			channel.getId(), date, 1L, DateUtil.addDays(date, -8), "pageViewed",
			"analyticsEventId3", "sessionId", "Test", "userId");

		_bqEventDog.addBQEvent(
			"Page",
			new HashSet<BQEventProperty>() {
				{
					add(
						new BQEventProperty(
							null, "viewDuration", "testValue1"));
					add(
						new BQEventProperty(
							null, "viewDuration", "testValue2"));
				}
			},
			channel.getId(), date, 1L, date, "pageViewed", "analyticsEventId4",
			"sessionId", "Test", "userId");

		Map<String, Date> recentGlobalBQEventProperyValues =
			_bqEventDog.getRecentGlobalBQEventProperyValues("title", 10);

		Assertions.assertEquals(2, recentGlobalBQEventProperyValues.size());

		Set<String> keySet = recentGlobalBQEventProperyValues.keySet();

		Assertions.assertArrayEquals(
			new String[] {"Test", "Home"}, keySet.toArray(new String[0]));

		Collection<Date> values = recentGlobalBQEventProperyValues.values();

		Assertions.assertArrayEquals(
			new Date[] {date, DateUtil.addDays(date, -1)},
			values.toArray(new Date[0]));
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesLast7Days() {
		Page<RecentPage> recentPagePage = _bqEventDog.getRecentPagePage(
			null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 7, 10, new String[0]);

		Assertions.assertEquals(2, recentPagePage.getTotalElements());
		Assertions.assertEquals(1, recentPagePage.getTotalPages());

		List<RecentPage> recentPages = recentPagePage.getContent();

		Assertions.assertFalse(recentPages.isEmpty());

		List<RecentPage> expectedRecentPages = new ArrayList<>();

		RecentPage recentPage = new RecentPage();

		recentPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentPage.setDisplayLanguageId("en-US");
		recentPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentPage.setUrl("https://www.beryl.com/delivery");
		recentPage.setVisits(2L);

		expectedRecentPages.add(recentPage);

		recentPage = new RecentPage();

		recentPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentPage.setDisplayLanguageId("pt-BR");
		recentPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentPage.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentPage.setVisits(1L);

		expectedRecentPages.add(recentPage);

		Assertions.assertEquals(
			expectedRecentPages, recentPagePage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq_last_24_hours.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesLast24Hours() {
		Page<RecentPage> recentPagePage = _bqEventDog.getRecentPagePage(
			null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 0, 10, new String[0]);

		Assertions.assertEquals(4, recentPagePage.getTotalElements());
		Assertions.assertEquals(1, recentPagePage.getTotalPages());

		List<RecentPage> recentPages = recentPagePage.getContent();

		Assertions.assertFalse(recentPages.isEmpty());

		Map<Pair<String, String>, Long> expectedRecentPageCounts =
			new HashMap<Pair<String, String>, Long>() {
				{
					put(
						Pair.of(
							"https://www.beryl.com/products/commercial" +
								"/irrigation/FF-2100",
							"pt-BR"),
						3L);
					put(Pair.of("https://www.beryl.com/delivery", "en-US"), 2L);
					put(
						Pair.of(
							"https://www.beryl.com/products/commercial" +
								"/irrigation/FF-2100",
							"en-US"),
						2L);
					put(Pair.of("https://www.beryl.com/delivery", "pt-BR"), 1L);
				}
			};

		for (RecentPage recentPage : recentPages) {
			Pair<String, String> pair = Pair.of(
				recentPage.getUrl(), recentPage.getDisplayLanguageId());

			Assertions.assertEquals(
				expectedRecentPageCounts.get(pair), recentPage.getVisits());

			expectedRecentPageCounts.remove(pair);
		}

		Assertions.assertTrue(expectedRecentPageCounts.isEmpty());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesLast28Days() {
		Page<RecentPage> recentPagePage = _bqEventDog.getRecentPagePage(
			null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 28, 10, new String[0]);

		Assertions.assertEquals(4, recentPagePage.getTotalElements());
		Assertions.assertEquals(1, recentPagePage.getTotalPages());

		List<RecentPage> expectedRecentPages = new ArrayList<>();

		RecentPage recentPage = new RecentPage();

		recentPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -23));
		recentPage.setDisplayLanguageId("en-US");
		recentPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentPage.setUrl("https://www.beryl.com/delivery");
		recentPage.setVisits(4L);

		expectedRecentPages.add(recentPage);

		recentPage = new RecentPage();

		recentPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentPage.setDisplayLanguageId("pt-BR");
		recentPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentPage.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentPage.setVisits(4L);

		expectedRecentPages.add(recentPage);

		recentPage = new RecentPage();

		recentPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -13));
		recentPage.setDisplayLanguageId("pt-BR");
		recentPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -11));
		recentPage.setUrl("https://www.beryl.com/delivery");
		recentPage.setVisits(2L);

		expectedRecentPages.add(recentPage);

		recentPage = new RecentPage();

		recentPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -18));
		recentPage.setDisplayLanguageId("en-US");
		recentPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -16));
		recentPage.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentPage.setVisits(2L);

		expectedRecentPages.add(recentPage);

		Assertions.assertEquals(
			expectedRecentPages, recentPagePage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesLast30Days() {
		Page<RecentPage> recentPagePage = _bqEventDog.getRecentPagePage(
			null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10, new String[0]);

		Assertions.assertEquals(4, recentPagePage.getTotalElements());
		Assertions.assertEquals(1, recentPagePage.getTotalPages());

		List<RecentPage> expectedRecentPages = new ArrayList<>();

		RecentPage recentPage = new RecentPage();

		recentPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentPage.setDisplayLanguageId("en-US");
		recentPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentPage.setUrl("https://www.beryl.com/delivery");
		recentPage.setVisits(4L);

		expectedRecentPages.add(recentPage);

		recentPage = new RecentPage();

		recentPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -24));
		recentPage.setDisplayLanguageId("pt-BR");
		recentPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentPage.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentPage.setVisits(4L);

		expectedRecentPages.add(recentPage);

		recentPage = new RecentPage();

		recentPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -19));
		recentPage.setDisplayLanguageId("pt-BR");
		recentPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -17));
		recentPage.setUrl("https://www.beryl.com/delivery");
		recentPage.setVisits(2L);

		expectedRecentPages.add(recentPage);

		recentPage = new RecentPage();

		recentPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -14));
		recentPage.setDisplayLanguageId("en-US");
		recentPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -12));
		recentPage.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentPage.setVisits(2L);

		expectedRecentPages.add(recentPage);

		Assertions.assertEquals(
			expectedRecentPages, recentPagePage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesSortByDisplayLanguageId() {
		RecentPage recentPage1 = new RecentPage();

		recentPage1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentPage1.setDisplayLanguageId("en-US");
		recentPage1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentPage1.setUrl("https://www.beryl.com/delivery");
		recentPage1.setVisits(4L);

		RecentPage recentPage2 = new RecentPage();

		recentPage2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -24));
		recentPage2.setDisplayLanguageId("pt-BR");
		recentPage2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentPage2.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentPage2.setVisits(4L);

		RecentPage recentPage3 = new RecentPage();

		recentPage3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -14));
		recentPage3.setDisplayLanguageId("en-US");
		recentPage3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -12));
		recentPage3.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentPage3.setVisits(2L);

		RecentPage recentPage4 = new RecentPage();

		recentPage4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -19));
		recentPage4.setDisplayLanguageId("pt-BR");
		recentPage4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -17));
		recentPage4.setUrl("https://www.beryl.com/delivery");
		recentPage4.setVisits(2L);

		Page<RecentPage> recentPagePage = _bqEventDog.getRecentPagePage(
			null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10, new String[] {"displayLanguageId", "asc", "url", "asc"});

		Assertions.assertEquals(
			Arrays.asList(recentPage1, recentPage3, recentPage4, recentPage2),
			recentPagePage.getContent());

		recentPagePage = _bqEventDog.getRecentPagePage(
			null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10,
			new String[] {"displayLanguageId", "desc", "url", "asc"});

		Assertions.assertEquals(
			Arrays.asList(recentPage4, recentPage2, recentPage1, recentPage3),
			recentPagePage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesSortByFirstVisitDate() {
		RecentPage recentPage1 = new RecentPage();

		recentPage1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentPage1.setDisplayLanguageId("en-US");
		recentPage1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentPage1.setUrl("https://www.beryl.com/delivery");
		recentPage1.setVisits(4L);

		RecentPage recentPage2 = new RecentPage();

		recentPage2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -24));
		recentPage2.setDisplayLanguageId("pt-BR");
		recentPage2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentPage2.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentPage2.setVisits(4L);

		RecentPage recentPage3 = new RecentPage();

		recentPage3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -14));
		recentPage3.setDisplayLanguageId("en-US");
		recentPage3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -12));
		recentPage3.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentPage3.setVisits(2L);

		RecentPage recentPage4 = new RecentPage();

		recentPage4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -19));
		recentPage4.setDisplayLanguageId("pt-BR");
		recentPage4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -17));
		recentPage4.setUrl("https://www.beryl.com/delivery");
		recentPage4.setVisits(2L);

		Page<RecentPage> recentPagePage = _bqEventDog.getRecentPagePage(
			null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10, new String[] {"firstVisitDate", "asc"});

		Assertions.assertEquals(
			Arrays.asList(recentPage1, recentPage2, recentPage4, recentPage3),
			recentPagePage.getContent());

		recentPagePage = _bqEventDog.getRecentPagePage(
			null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10, new String[] {"firstVisitDate", "desc"});

		Assertions.assertEquals(
			Arrays.asList(recentPage3, recentPage4, recentPage2, recentPage1),
			recentPagePage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesSortByLastVisitDate() {
		RecentPage recentPage1 = new RecentPage();

		recentPage1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentPage1.setDisplayLanguageId("en-US");
		recentPage1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentPage1.setUrl("https://www.beryl.com/delivery");
		recentPage1.setVisits(4L);

		RecentPage recentPage2 = new RecentPage();

		recentPage2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -24));
		recentPage2.setDisplayLanguageId("pt-BR");
		recentPage2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentPage2.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentPage2.setVisits(4L);

		RecentPage recentPage3 = new RecentPage();

		recentPage3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -14));
		recentPage3.setDisplayLanguageId("en-US");
		recentPage3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -12));
		recentPage3.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentPage3.setVisits(2L);

		RecentPage recentPage4 = new RecentPage();

		recentPage4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -19));
		recentPage4.setDisplayLanguageId("pt-BR");
		recentPage4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -17));
		recentPage4.setUrl("https://www.beryl.com/delivery");
		recentPage4.setVisits(2L);

		Page<RecentPage> recentPagePage = _bqEventDog.getRecentPagePage(
			null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10, new String[] {"lastVisitDate", "asc"});

		Assertions.assertEquals(
			Arrays.asList(recentPage4, recentPage3, recentPage1, recentPage2),
			recentPagePage.getContent());

		recentPagePage = _bqEventDog.getRecentPagePage(
			null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10, new String[] {"lastVisitDate", "desc"});

		Assertions.assertEquals(
			Arrays.asList(recentPage2, recentPage1, recentPage3, recentPage4),
			recentPagePage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesSortByVisits() {
		RecentPage recentPage1 = new RecentPage();

		recentPage1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentPage1.setDisplayLanguageId("en-US");
		recentPage1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentPage1.setUrl("https://www.beryl.com/delivery");
		recentPage1.setVisits(4L);

		RecentPage recentPage2 = new RecentPage();

		recentPage2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -24));
		recentPage2.setDisplayLanguageId("pt-BR");
		recentPage2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentPage2.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentPage2.setVisits(4L);

		RecentPage recentPage3 = new RecentPage();

		recentPage3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -14));
		recentPage3.setDisplayLanguageId("en-US");
		recentPage3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -12));
		recentPage3.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentPage3.setVisits(2L);

		RecentPage recentPage4 = new RecentPage();

		recentPage4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -19));
		recentPage4.setDisplayLanguageId("pt-BR");
		recentPage4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -17));
		recentPage4.setUrl("https://www.beryl.com/delivery");
		recentPage4.setVisits(2L);

		Page<RecentPage> recentPagePage = _bqEventDog.getRecentPagePage(
			null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10, new String[] {"visits", "asc", "url", "asc"});

		Assertions.assertEquals(
			Arrays.asList(recentPage4, recentPage3, recentPage1, recentPage2),
			recentPagePage.getContent());

		recentPagePage = _bqEventDog.getRecentPagePage(
			null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10, new String[] {"visits", "desc", "url", "asc"});

		Assertions.assertEquals(
			Arrays.asList(recentPage1, recentPage2, recentPage4, recentPage3),
			recentPagePage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesWithDisplayLanguageId() {
		List<RecentPage> expectedRecentPages = new ArrayList<>();

		RecentPage recentPage = new RecentPage();

		recentPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentPage.setDisplayLanguageId("en-US");
		recentPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentPage.setUrl("https://www.beryl.com/delivery");
		recentPage.setVisits(4L);

		expectedRecentPages.add(recentPage);

		recentPage = new RecentPage();

		recentPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -14));
		recentPage.setDisplayLanguageId("en-US");
		recentPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -12));
		recentPage.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentPage.setVisits(2L);

		expectedRecentPages.add(recentPage);

		Page<RecentPage> recentPagePage = _bqEventDog.getRecentPagePage(
			"en-US",
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10, new String[0]);

		Assertions.assertEquals(
			expectedRecentPages, recentPagePage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesWithPagination() {
		RecentPage recentPage1 = new RecentPage();

		recentPage1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentPage1.setDisplayLanguageId("en-US");
		recentPage1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentPage1.setUrl("https://www.beryl.com/delivery");
		recentPage1.setVisits(4L);

		RecentPage recentPage2 = new RecentPage();

		recentPage2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -24));
		recentPage2.setDisplayLanguageId("pt-BR");
		recentPage2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentPage2.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentPage2.setVisits(4L);

		RecentPage recentPage3 = new RecentPage();

		recentPage3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -14));
		recentPage3.setDisplayLanguageId("en-US");
		recentPage3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -12));
		recentPage3.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentPage3.setVisits(2L);

		RecentPage recentPage4 = new RecentPage();

		recentPage4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -19));
		recentPage4.setDisplayLanguageId("pt-BR");
		recentPage4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -17));
		recentPage4.setUrl("https://www.beryl.com/delivery");
		recentPage4.setVisits(2L);

		Page<RecentPage> recentPagePage = _bqEventDog.getRecentPagePage(
			null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 2, new String[] {"visits", "desc", "url", "asc"});

		Assertions.assertEquals(
			Arrays.asList(recentPage1, recentPage2),
			recentPagePage.getContent());

		recentPagePage = _bqEventDog.getRecentPagePage(
			null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			1, 30, 2, new String[] {"visits", "desc", "url", "asc"});

		Assertions.assertEquals(
			Arrays.asList(recentPage4, recentPage3),
			recentPagePage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesWithSuppression() {
		Page<RecentPage> recentPagePage = _bqEventDog.getRecentPagePage(
			null,
			"8bb3cd4319c4cc4df1addc31cb0fae500288133b91228a1cacb4ff2802446220",
			0, 30, 10, new String[] {"counts", "desc", "url", "asc"});

		Assertions.assertEquals(0L, recentPagePage.getTotalElements());
		Assertions.assertEquals(0L, recentPagePage.getTotalPages());

		List<RecentPage> recentPages = recentPagePage.getContent();

		Assertions.assertTrue(recentPages.isEmpty());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_yesterday_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesYesterday() {
		Page<RecentPage> recentPagePage = _bqEventDog.getRecentPagePage(
			null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 1, 10, new String[0]);

		Assertions.assertEquals(4, recentPagePage.getTotalElements());
		Assertions.assertEquals(1, recentPagePage.getTotalPages());

		List<RecentPage> recentPages = recentPagePage.getContent();

		Assertions.assertFalse(recentPages.isEmpty());

		Map<Pair<String, String>, Long> expectedRecentPageCounts =
			new HashMap<Pair<String, String>, Long>() {
				{
					put(
						Pair.of(
							"https://www.beryl.com/products/commercial" +
								"/irrigation/FF-2100",
							"pt-BR"),
						3L);
					put(Pair.of("https://www.beryl.com/delivery", "en-US"), 2L);
					put(
						Pair.of(
							"https://www.beryl.com/products/commercial" +
								"/irrigation/FF-2100",
							"en-US"),
						1L);
					put(Pair.of("https://www.beryl.com/delivery", "pt-BR"), 1L);
				}
			};

		for (RecentPage recentPage : recentPages) {
			Pair<String, String> pair = Pair.of(
				recentPage.getUrl(), recentPage.getDisplayLanguageId());

			Assertions.assertEquals(
				expectedRecentPageCounts.get(pair), recentPage.getVisits());

			expectedRecentPageCounts.remove(pair);
		}

		Assertions.assertTrue(expectedRecentPageCounts.isEmpty());
	}

	@BQSQLResource(resourcePath = "test_bq_identity.sql")
	@Test
	public void testGetRecentSites() throws Exception {
		Channel channel = _channelDog.addChannel("Test Channel");

		_bqEventDog.addBQEvent(
			"Page",
			new HashSet<BQEventProperty>() {
				{
					add(
						new BQEventProperty(
							null, "viewDuration", "testValue1"));
				}
			},
			"Firefox", "http://localhost:8080/search?q=Liferay+DXP",
			channel.getId(), "Diamond Bar", "en_US", "{\"groupId\": \"3213\"}",
			"United States", DateUtil.newDate(), null, "", "",
			DigestUtils.sha256Hex("test@liferay.com"), DateUtil.newDate(),
			"pageViewed", "", "analyticsEventId1", "", "en_US", "", "", "", "",
			"", "", "", "http://localhost:8080/search?q=Liferay%20DXP",
			"123123-sadf-32423-4245", "");

		_bqEventDog.addBQEvent(
			"Page",
			new HashSet<BQEventProperty>() {
				{
					add(
						new BQEventProperty(
							null, "viewDuration", "testValue1"));
				}
			},
			"Firefox", "http://localhost:8080/search?q=Liferay",
			channel.getId(), "Diamond Bar", "en_US", "{\"groupId\": \"3212\"}",
			"United States", DateUtil.newDate(), null, "", "",
			DigestUtils.sha256Hex("test@liferay.com"), DateUtil.newDate(),
			"pageViewed", "", "analyticsEventId2", "", "en_US", "", "", "", "",
			"", "", "", "http://localhost:8080/search?q=Liferay",
			"123123-sadf-32423-4245", "");

		_bqEventDog.addBQEvent(
			"Page",
			new HashSet<BQEventProperty>() {
				{
					add(
						new BQEventProperty(
							null, "viewDuration", "testValue1"));
				}
			},
			"Firefox", "http://localhost:8080/search?q=Liferay+DXP",
			channel.getId(), "Diamond Bar", "en_US", "{\"groupId\": \"3212\"}",
			"United States", DateUtil.newDate(), null, "", "",
			DigestUtils.sha256Hex("test@liferay.com"), DateUtil.newDate(),
			"pageViewed", "", "analyticsEventId3", "", "en_US", "", "", "", "",
			"", "", "", "http://localhost:8080/search?q=Liferay+DXP",
			"123123-sadf-32423-4245", "");

		_bqEventDog.addBQEvent(
			"Page",
			new HashSet<BQEventProperty>() {
				{
					add(
						new BQEventProperty(
							null, "viewDuration", "testValue1"));
				}
			},
			"Firefox", "http://localhost:8080/search?q=Diamond+Bar",
			channel.getId(), "Diamond Bar", "pt_BR", "{\"groupId\": \"3212\"}",
			"United States", DateUtil.newDate(), null, "", "",
			DigestUtils.sha256Hex("test2@liferay.com"), DateUtil.newDate(),
			"pageViewed", "", "analyticsEventId4", "", "en_US", "", "", "", "",
			"", "", "", "http://localhost:8080/search?q=Diamond+Bar",
			"123123-sadf-32423-234afsd", "");

		Page<RecentSite> recentSitePage = _bqEventDog.getRecentSitePage(
			DigestUtils.sha256Hex("test2@liferay.com"), 0, 5,
			new String[] {"visits", "desc"}, TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(1, recentSitePage.getTotalElements());

		List<RecentSite> recentSites = recentSitePage.getContent();

		Assertions.assertEquals(1, recentSites.size());

		RecentSite[] recentSitesArray = recentSites.toArray(new RecentSite[0]);

		RecentSite recentSite = recentSitesArray[0];

		Assertions.assertEquals("3212", recentSite.getGroupId());
		Assertions.assertEquals(1, recentSite.getVisits());

		recentSitePage = _bqEventDog.getRecentSitePage(
			DigestUtils.sha256Hex("test@liferay.com"), 0, 5,
			new String[] {"visits", "desc"}, TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(2, recentSitePage.getTotalElements());

		recentSites = recentSitePage.getContent();

		Assertions.assertEquals(2, recentSites.size());

		recentSitesArray = recentSites.toArray(new RecentSite[0]);

		recentSite = recentSitesArray[0];

		Assertions.assertEquals("3212", recentSite.getGroupId());
		Assertions.assertEquals(2, recentSite.getVisits());

		recentSite = recentSitesArray[1];

		Assertions.assertEquals("3213", recentSite.getGroupId());
		Assertions.assertEquals(1, recentSite.getVisits());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentWebContentLast7Days() {
		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.WEBCONTENT,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 10, new String[0], TimeRange.LAST_7_DAYS);

		Assertions.assertEquals(3, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentAsset> expectedRecentAssets = new ArrayList<>();

		RecentAsset recentAsset = new RecentAsset();

		recentAsset.setAssetId("a73ihsy9");
		recentAsset.setAssetTitle("WebContent Title 2");
		recentAsset.setContentType(RecentAsset.ContentType.WEBCONTENT);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentAsset.setUrl("https://www.beryl.com/journals/journal-2");
		recentAsset.setVisits(1L);

		expectedRecentAssets.add(recentAsset);

		recentAsset = new RecentAsset();

		recentAsset.setAssetId("b73ihsy9");
		recentAsset.setAssetTitle("WebContent Title 3");
		recentAsset.setContentType(RecentAsset.ContentType.WEBCONTENT);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentAsset.setUrl("https://www.beryl.com/journals/journal-3");
		recentAsset.setVisits(1L);

		expectedRecentAssets.add(recentAsset);

		recentAsset = new RecentAsset();

		recentAsset.setAssetId("c73ihsy9");
		recentAsset.setAssetTitle("WebContent Title 4");
		recentAsset.setContentType(RecentAsset.ContentType.WEBCONTENT);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -4));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -4));
		recentAsset.setUrl("https://www.beryl.com/journals/journal-4");
		recentAsset.setVisits(1L);

		expectedRecentAssets.add(recentAsset);

		Assertions.assertEquals(
			expectedRecentAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_last_24_hours_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentWebContentLast24Hours() {
		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.WEBCONTENT,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[0], TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(3, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentAsset> recentAssets = recentAssetPage.getContent();

		Assertions.assertFalse(recentAssets.isEmpty());

		Map<Pair<String, String>, Long> expectedRecentAssetCounts =
			new HashMap<Pair<String, String>, Long>() {
				{
					put(Pair.of("e131fabc", "WebContent Title 1"), 10L);
					put(Pair.of("a73ihsy9", "WebContent Title 2"), 3L);
					put(Pair.of("b73ihsy9", "WebContent Title 3"), 3L);
				}
			};

		for (RecentAsset recentAsset : recentAssets) {
			Pair<String, String> pair = Pair.of(
				recentAsset.getAssetId(), recentAsset.getAssetTitle());

			Assertions.assertEquals(
				expectedRecentAssetCounts.get(pair), recentAsset.getVisits());

			expectedRecentAssetCounts.remove(pair);
		}

		Assertions.assertTrue(expectedRecentAssetCounts.isEmpty());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentWebContentLast28Days() {
		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.WEBCONTENT,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[0], TimeRange.LAST_28_DAYS);

		Assertions.assertEquals(4, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentAsset> expectedRecentAssets = new ArrayList<>();

		RecentAsset recentAsset = new RecentAsset();

		recentAsset.setAssetId("a73ihsy9");
		recentAsset.setAssetTitle("WebContent Title 2");
		recentAsset.setContentType(RecentAsset.ContentType.WEBCONTENT);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -26));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentAsset.setUrl("https://www.beryl.com/journals/journal-2");
		recentAsset.setVisits(3L);

		expectedRecentAssets.add(recentAsset);

		recentAsset = new RecentAsset();

		recentAsset.setAssetId("b73ihsy9");
		recentAsset.setAssetTitle("WebContent Title 3");
		recentAsset.setContentType(RecentAsset.ContentType.WEBCONTENT);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -23));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentAsset.setUrl("https://www.beryl.com/journals/journal-3");
		recentAsset.setVisits(3L);

		expectedRecentAssets.add(recentAsset);

		recentAsset = new RecentAsset();

		recentAsset.setAssetId("c73ihsy9");
		recentAsset.setAssetTitle("WebContent Title 4");
		recentAsset.setContentType(RecentAsset.ContentType.WEBCONTENT);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentAsset.setUrl("https://www.beryl.com/journals/journal-4");
		recentAsset.setVisits(3L);

		expectedRecentAssets.add(recentAsset);

		recentAsset = new RecentAsset();

		recentAsset.setAssetId("e131fabc");
		recentAsset.setAssetTitle("WebContent Title 1");
		recentAsset.setContentType(RecentAsset.ContentType.WEBCONTENT);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentAsset.setUrl("https://www.beryl.com/journals/journal-1");
		recentAsset.setVisits(3L);

		expectedRecentAssets.add(recentAsset);

		Assertions.assertEquals(
			expectedRecentAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentWebContentLast30Days() {
		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.WEBCONTENT,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 10, new String[0], TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(4, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentAsset> expectedRecentAssets = new ArrayList<>();

		RecentAsset recentAsset = new RecentAsset();

		recentAsset.setAssetId("a73ihsy9");
		recentAsset.setAssetTitle("WebContent Title 2");
		recentAsset.setContentType(RecentAsset.ContentType.WEBCONTENT);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -22));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentAsset.setUrl("https://www.beryl.com/journals/journal-2");
		recentAsset.setVisits(3L);

		expectedRecentAssets.add(recentAsset);

		recentAsset = new RecentAsset();

		recentAsset.setAssetId("b73ihsy9");
		recentAsset.setAssetTitle("WebContent Title 3");
		recentAsset.setContentType(RecentAsset.ContentType.WEBCONTENT);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -27));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentAsset.setUrl("https://www.beryl.com/journals/journal-3");
		recentAsset.setVisits(3L);

		expectedRecentAssets.add(recentAsset);

		recentAsset = new RecentAsset();

		recentAsset.setAssetId("c73ihsy9");
		recentAsset.setAssetTitle("WebContent Title 4");
		recentAsset.setContentType(RecentAsset.ContentType.WEBCONTENT);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -24));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -4));
		recentAsset.setUrl("https://www.beryl.com/journals/journal-4");
		recentAsset.setVisits(3L);

		expectedRecentAssets.add(recentAsset);

		recentAsset = new RecentAsset();

		recentAsset.setAssetId("e131fabc");
		recentAsset.setAssetTitle("WebContent Title 1");
		recentAsset.setContentType(RecentAsset.ContentType.WEBCONTENT);
		recentAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -9));
		recentAsset.setUrl("https://www.beryl.com/journals/journal-1");
		recentAsset.setVisits(3L);

		expectedRecentAssets.add(recentAsset);

		Assertions.assertEquals(
			expectedRecentAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentWebContentSortByFirstVisitDate() {
		RecentAsset recentAsset1 = new RecentAsset();

		recentAsset1.setAssetId("a73ihsy9");
		recentAsset1.setAssetTitle("WebContent Title 2");
		recentAsset1.setContentType(RecentAsset.ContentType.WEBCONTENT);
		recentAsset1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -26));
		recentAsset1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentAsset1.setUrl("https://www.beryl.com/journals/journal-2");
		recentAsset1.setVisits(3L);

		RecentAsset recentAsset2 = new RecentAsset();

		recentAsset2.setAssetId("b73ihsy9");
		recentAsset2.setAssetTitle("WebContent Title 3");
		recentAsset2.setContentType(RecentAsset.ContentType.WEBCONTENT);
		recentAsset2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -30));
		recentAsset2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentAsset2.setUrl("https://www.beryl.com/journals/journal-3");
		recentAsset2.setVisits(4L);

		RecentAsset recentAsset3 = new RecentAsset();

		recentAsset3.setAssetId("c73ihsy9");
		recentAsset3.setAssetTitle("WebContent Title 4");
		recentAsset3.setContentType(RecentAsset.ContentType.WEBCONTENT);
		recentAsset3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentAsset3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentAsset3.setUrl("https://www.beryl.com/journals/journal-4");
		recentAsset3.setVisits(3L);

		RecentAsset recentAsset4 = new RecentAsset();

		recentAsset4.setAssetId("e131fabc");
		recentAsset4.setAssetTitle("WebContent Title 1");
		recentAsset4.setContentType(RecentAsset.ContentType.WEBCONTENT);
		recentAsset4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentAsset4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentAsset4.setUrl("https://www.beryl.com/journals/journal-1");
		recentAsset4.setVisits(3L);

		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.WEBCONTENT,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"firstVisitDate", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentAsset2, recentAsset3, recentAsset1, recentAsset4),
			recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.WEBCONTENT,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"firstVisitDate", "desc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentAsset4, recentAsset1, recentAsset3, recentAsset2),
			recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentWebContentSortByLastVisitDate() {
		RecentAsset recentAsset1 = new RecentAsset();

		recentAsset1.setAssetId("a73ihsy9");
		recentAsset1.setAssetTitle("WebContent Title 2");
		recentAsset1.setContentType(RecentAsset.ContentType.WEBCONTENT);
		recentAsset1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -26));
		recentAsset1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentAsset1.setUrl("https://www.beryl.com/journals/journal-2");
		recentAsset1.setVisits(3L);

		RecentAsset recentAsset2 = new RecentAsset();

		recentAsset2.setAssetId("b73ihsy9");
		recentAsset2.setAssetTitle("WebContent Title 3");
		recentAsset2.setContentType(RecentAsset.ContentType.WEBCONTENT);
		recentAsset2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -30));
		recentAsset2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentAsset2.setUrl("https://www.beryl.com/journals/journal-3");
		recentAsset2.setVisits(4L);

		RecentAsset recentAsset3 = new RecentAsset();

		recentAsset3.setAssetId("c73ihsy9");
		recentAsset3.setAssetTitle("WebContent Title 4");
		recentAsset3.setContentType(RecentAsset.ContentType.WEBCONTENT);
		recentAsset3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentAsset3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentAsset3.setUrl("https://www.beryl.com/journals/journal-4");
		recentAsset3.setVisits(3L);

		RecentAsset recentAsset4 = new RecentAsset();

		recentAsset4.setAssetId("e131fabc");
		recentAsset4.setAssetTitle("WebContent Title 1");
		recentAsset4.setContentType(RecentAsset.ContentType.WEBCONTENT);
		recentAsset4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentAsset4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentAsset4.setUrl("https://www.beryl.com/journals/journal-1");
		recentAsset4.setVisits(3L);

		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.WEBCONTENT,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"lastVisitDate", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentAsset3, recentAsset1, recentAsset2, recentAsset4),
			recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.WEBCONTENT,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"lastVisitDate", "desc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentAsset4, recentAsset2, recentAsset1, recentAsset3),
			recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentWebContentSortByVisits() {
		RecentAsset recentAsset1 = new RecentAsset();

		recentAsset1.setAssetId("a73ihsy9");
		recentAsset1.setAssetTitle("WebContent Title 2");
		recentAsset1.setContentType(RecentAsset.ContentType.WEBCONTENT);
		recentAsset1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -26));
		recentAsset1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentAsset1.setUrl("https://www.beryl.com/journals/journal-2");
		recentAsset1.setVisits(3L);

		RecentAsset recentAsset2 = new RecentAsset();

		recentAsset2.setAssetId("b73ihsy9");
		recentAsset2.setAssetTitle("WebContent Title 3");
		recentAsset2.setContentType(RecentAsset.ContentType.WEBCONTENT);
		recentAsset2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -30));
		recentAsset2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentAsset2.setUrl("https://www.beryl.com/journals/journal-3");
		recentAsset2.setVisits(4L);

		RecentAsset recentAsset3 = new RecentAsset();

		recentAsset3.setAssetId("c73ihsy9");
		recentAsset3.setAssetTitle("WebContent Title 4");
		recentAsset3.setContentType(RecentAsset.ContentType.WEBCONTENT);
		recentAsset3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentAsset3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentAsset3.setUrl("https://www.beryl.com/journals/journal-4");
		recentAsset3.setVisits(3L);

		RecentAsset recentAsset4 = new RecentAsset();

		recentAsset4.setAssetId("e131fabc");
		recentAsset4.setAssetTitle("WebContent Title 1");
		recentAsset4.setContentType(RecentAsset.ContentType.WEBCONTENT);
		recentAsset4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentAsset4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentAsset4.setUrl("https://www.beryl.com/journals/journal-1");
		recentAsset4.setVisits(3L);

		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.WEBCONTENT,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"visits", "asc", "assetTitle", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentAsset4, recentAsset1, recentAsset3, recentAsset2),
			recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.WEBCONTENT,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"visits", "desc", "assetTitle", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentAsset2, recentAsset4, recentAsset1, recentAsset3),
			recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentWebContentWithPagination() {
		RecentAsset recentAsset1 = new RecentAsset();

		recentAsset1.setAssetId("a73ihsy9");
		recentAsset1.setAssetTitle("WebContent Title 2");
		recentAsset1.setContentType(RecentAsset.ContentType.WEBCONTENT);
		recentAsset1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -26));
		recentAsset1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentAsset1.setUrl("https://www.beryl.com/journals/journal-2");
		recentAsset1.setVisits(3L);

		RecentAsset recentAsset2 = new RecentAsset();

		recentAsset2.setAssetId("b73ihsy9");
		recentAsset2.setAssetTitle("WebContent Title 3");
		recentAsset2.setContentType(RecentAsset.ContentType.WEBCONTENT);
		recentAsset2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -30));
		recentAsset2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentAsset2.setUrl("https://www.beryl.com/journals/journal-3");
		recentAsset2.setVisits(4L);

		RecentAsset recentAsset3 = new RecentAsset();

		recentAsset3.setAssetId("c73ihsy9");
		recentAsset3.setAssetTitle("WebContent Title 4");
		recentAsset3.setContentType(RecentAsset.ContentType.WEBCONTENT);
		recentAsset3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentAsset3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentAsset3.setUrl("https://www.beryl.com/journals/journal-4");
		recentAsset3.setVisits(3L);

		RecentAsset recentAsset4 = new RecentAsset();

		recentAsset4.setAssetId("e131fabc");
		recentAsset4.setAssetTitle("WebContent Title 1");
		recentAsset4.setContentType(RecentAsset.ContentType.WEBCONTENT);
		recentAsset4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentAsset4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentAsset4.setUrl("https://www.beryl.com/journals/journal-1");
		recentAsset4.setVisits(3L);

		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.WEBCONTENT,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 3, new String[] {"firstVisitDate", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(recentAsset2, recentAsset3, recentAsset1),
			recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.WEBCONTENT,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			1, 3, new String[] {"firstVisitDate", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Collections.singletonList(recentAsset4),
			recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentWebContentWithSuppression() {
		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.WEBCONTENT,
			"8bb3cd4319c4cc4df1addc31cb0fae500288133b91228a1cacb4ff2802446220",
			0, 10, new String[0], TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(0L, recentAssetPage.getTotalElements());
		Assertions.assertEquals(0L, recentAssetPage.getTotalPages());

		List<RecentAsset> recentAssets = recentAssetPage.getContent();

		Assertions.assertTrue(recentAssets.isEmpty());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_yesterday_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentWebContentYesterday() {
		Page<RecentAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			RecentAsset.ContentType.WEBCONTENT,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 10, new String[0], TimeRange.YESTERDAY);

		Assertions.assertEquals(3, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentAsset> recentAssets = recentAssetPage.getContent();

		Assertions.assertFalse(recentAssets.isEmpty());

		Map<Pair<String, String>, Long> expectedRecentAssetCounts =
			new HashMap<Pair<String, String>, Long>() {
				{
					put(Pair.of("e131fabc", "WebContent Title 1"), 10L);
					put(Pair.of("a73ihsy9", "WebContent Title 2"), 2L);
					put(Pair.of("b73ihsy9", "WebContent Title 3"), 2L);
				}
			};

		for (RecentAsset recentAsset : recentAssets) {
			Pair<String, String> pair = Pair.of(
				recentAsset.getAssetId(), recentAsset.getAssetTitle());

			Assertions.assertEquals(
				expectedRecentAssetCounts.get(pair), recentAsset.getVisits());

			expectedRecentAssetCounts.remove(pair);
		}

		Assertions.assertTrue(expectedRecentAssetCounts.isEmpty());
	}

	@Test
	public void testGetSearchKeywords1() throws Exception {
		Channel channel = _channelDog.addChannel("Test Channel");

		_bqEventDog.addBQEvent(
			"Page",
			new HashSet<BQEventProperty>() {
				{
					add(
						new BQEventProperty(
							null, "viewDuration", "testValue1"));
				}
			},
			"Firefox", "http://localhost:8080/search?q=Liferay+DXP",
			channel.getId(), "Diamond Bar", "en_US", "{\"groupId\": \"3212\"}",
			"United States", DateUtil.newDate(), null, "", "", null,
			DateUtil.newDate(), "pageViewed", "", "analyticsEventId1", "",
			"en_US", "", "", "", "", "", "", "",
			"http://localhost:8080/search?q=Liferay%20DXP", "userId", "");

		_bqEventDog.addBQEvent(
			"Page",
			new HashSet<BQEventProperty>() {
				{
					add(
						new BQEventProperty(
							null, "viewDuration", "testValue1"));
				}
			},
			"Firefox", "http://localhost:8080/search?q=Liferay",
			channel.getId(), "Diamond Bar", "en_US", "{\"groupId\": \"3212\"}",
			"United States", DateUtil.newDate(), null, "", "", null,
			DateUtil.newDate(), "pageViewed", "", "analyticsEventId2", "",
			"en_US", "", "", "", "", "", "", "",
			"http://localhost:8080/search?q=Liferay", "userId", "");

		_bqEventDog.addBQEvent(
			"Page",
			new HashSet<BQEventProperty>() {
				{
					add(
						new BQEventProperty(
							null, "viewDuration", "testValue1"));
				}
			},
			"Firefox", "http://localhost:8080/search?q=Liferay+DXP",
			channel.getId(), "Diamond Bar", "en_US", "{\"groupId\": \"3212\"}",
			"United States", DateUtil.newDate(), null, "", "", null,
			DateUtil.newDate(), "pageViewed", "", "analyticsEventId3", "",
			"en_US", "", "", "", "", "", "", "",
			"http://localhost:8080/search?q=Liferay+DXP", "userId", "");

		_bqEventDog.addBQEvent(
			"Page",
			new HashSet<BQEventProperty>() {
				{
					add(
						new BQEventProperty(
							null, "viewDuration", "testValue1"));
				}
			},
			"Firefox", "http://localhost:8080/search?q=Diamond+Bar",
			channel.getId(), "Diamond Bar", "pt_BR", "{\"groupId\": \"3212\"}",
			"United States", DateUtil.newDate(), null, "", "", null,
			DateUtil.newDate(), "pageViewed", "", "analyticsEventId4", "",
			"en_US", "", "", "", "", "", "", "",
			"http://localhost:8080/search?q=Diamond+Bar", "userId", "");

		Page<SearchKeyword> searchKeywordPage =
			_bqEventDog.getSearchKeywordPage(
				null, null, null, 1, 0, 2, new String[] {"counts", "desc"},
				null);

		Assertions.assertEquals(3, searchKeywordPage.getTotalElements());

		List<SearchKeyword> searchKeywords = searchKeywordPage.getContent();

		Assertions.assertEquals(2, searchKeywords.size());

		SearchKeyword[] searchKeywordsArray = searchKeywords.toArray(
			new SearchKeyword[0]);

		SearchKeyword searchKeyword = searchKeywordsArray[0];

		Assertions.assertEquals(2, searchKeyword.getCounts());
		Assertions.assertEquals("en_US", searchKeyword.getDisplayLanguageId());
		Assertions.assertEquals("3212", searchKeyword.getGroupId());
		Assertions.assertEquals("liferay dxp", searchKeyword.getKeywords());

		searchKeywordPage = _bqEventDog.getSearchKeywordPage(
			null, null, null, 0, 0, 1,
			new String[] {"lastmodifieddate", "desc"}, null);

		Assertions.assertEquals(3, searchKeywordPage.getTotalElements());

		searchKeywords = searchKeywordPage.getContent();

		Assertions.assertEquals(1, searchKeywords.size());

		searchKeywordsArray = searchKeywords.toArray(new SearchKeyword[0]);

		searchKeyword = searchKeywordsArray[0];

		Assertions.assertEquals(1, searchKeyword.getCounts());
		Assertions.assertEquals("pt_BR", searchKeyword.getDisplayLanguageId());
		Assertions.assertEquals("3212", searchKeyword.getGroupId());
		Assertions.assertEquals("diamond bar", searchKeyword.getKeywords());

		searchKeywordPage = _bqEventDog.getSearchKeywordPage(
			null, null, null, 3, 0, 1, new String[] {"counts", "desc"}, null);

		Assertions.assertEquals(0, searchKeywordPage.getTotalElements());
	}

	@BQSQLResource(resourcePath = "test_bq_identity.sql")
	@Test
	public void testGetSearchKeywords2() throws Exception {
		Channel channel = _channelDog.addChannel("Test Channel");

		_bqEventDog.addBQEvent(
			"Page",
			new HashSet<BQEventProperty>() {
				{
					add(
						new BQEventProperty(
							null, "viewDuration", "testValue1"));
				}
			},
			"Firefox", "http://localhost:8080/search?q=Liferay+DXP",
			channel.getId(), "Diamond Bar", "en_US", "{\"groupId\": \"3212\"}",
			"United States", DateUtil.newDate(), null, "", "",
			DigestUtils.sha256Hex("test@liferay.com"), DateUtil.newDate(),
			"pageViewed", "", "analyticsEventId1", "", "en_US", "", "", "", "",
			"", "", "", "http://localhost:8080/search?q=Liferay%20DXP",
			"123123-sadf-32423-4245", "");

		_bqEventDog.addBQEvent(
			"Page",
			new HashSet<BQEventProperty>() {
				{
					add(
						new BQEventProperty(
							null, "viewDuration", "testValue1"));
				}
			},
			"Firefox", "http://localhost:8080/search?q=Liferay",
			channel.getId(), "Diamond Bar", "en_US", "{\"groupId\": \"3212\"}",
			"United States", DateUtil.newDate(), null, "", "",
			DigestUtils.sha256Hex("test@liferay.com"), DateUtil.newDate(),
			"pageViewed", "", "analyticsEventId2", "", "en_US", "", "", "", "",
			"", "", "", "http://localhost:8080/search?q=Liferay",
			"123123-sadf-32423-4245", "");

		_bqEventDog.addBQEvent(
			"Page",
			new HashSet<BQEventProperty>() {
				{
					add(
						new BQEventProperty(
							null, "viewDuration", "testValue1"));
				}
			},
			"Firefox", "http://localhost:8080/search?q=Liferay+DXP",
			channel.getId(), "Diamond Bar", "en_US", "{\"groupId\": \"3212\"}",
			"United States", DateUtil.newDate(), null, "", "",
			DigestUtils.sha256Hex("test@liferay.com"), DateUtil.newDate(),
			"pageViewed", "", "analyticsEventId3", "", "en_US", "", "", "", "",
			"", "", "", "http://localhost:8080/search?q=Liferay+DXP",
			"123123-sadf-32423-4245", "");

		_bqEventDog.addBQEvent(
			"Page",
			new HashSet<BQEventProperty>() {
				{
					add(
						new BQEventProperty(
							null, "viewDuration", "testValue1"));
				}
			},
			"Firefox", "http://localhost:8080/search?q=Diamond+Bar",
			channel.getId(), "Diamond Bar", "pt_BR", "{\"groupId\": \"3212\"}",
			"United States", DateUtil.newDate(), null, "", "",
			DigestUtils.sha256Hex("test2@liferay.com"), DateUtil.newDate(),
			"pageViewed", "", "analyticsEventId4", "", "en_US", "", "", "", "",
			"", "", "", "http://localhost:8080/search?q=Diamond+Bar", "userId",
			"");

		Page<SearchKeyword> searchKeywordPage =
			_bqEventDog.getSearchKeywordPage(
				null, null, DigestUtils.sha256Hex("test@liferay.com"), 1, 0, 2,
				new String[] {"counts", "desc"}, TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(2, searchKeywordPage.getTotalElements());

		List<SearchKeyword> searchKeywords = searchKeywordPage.getContent();

		Assertions.assertEquals(2, searchKeywords.size());

		SearchKeyword[] searchKeywordsArray = searchKeywords.toArray(
			new SearchKeyword[0]);

		SearchKeyword searchKeyword = searchKeywordsArray[0];

		Assertions.assertEquals(2, searchKeyword.getCounts());
		Assertions.assertEquals("en_US", searchKeyword.getDisplayLanguageId());
		Assertions.assertEquals("3212", searchKeyword.getGroupId());
		Assertions.assertEquals("liferay dxp", searchKeyword.getKeywords());

		searchKeyword = searchKeywordsArray[1];

		Assertions.assertEquals(1, searchKeyword.getCounts());
		Assertions.assertEquals("en_US", searchKeyword.getDisplayLanguageId());
		Assertions.assertEquals("3212", searchKeyword.getGroupId());
		Assertions.assertEquals("liferay", searchKeyword.getKeywords());
	}

	@BQSQLResource(resourcePath = "test_bq_identity.sql")
	@Test
	public void testGetSearchKeywords3() throws Exception {
		Channel channel = _channelDog.addChannel("Test Channel");

		_bqEventDog.addBQEvent(
			"Page",
			new HashSet<BQEventProperty>() {
				{
					add(
						new BQEventProperty(
							null, "viewDuration", "testValue1"));
				}
			},
			"Firefox", "http://localhost:8080/search?q=Liferay+DXP",
			channel.getId(), "Diamond Bar", "en_US", "{\"groupId\": \"3212\"}",
			"United States", DateUtil.newDate(), null, "", "",
			DigestUtils.sha256Hex("test@liferay.com"), DateUtil.newDate(),
			"pageViewed", "", "analyticsEventId1", "", "en_US", "", "", "", "",
			"", "", "", "http://localhost:8080/search?q=Liferay%20DXP",
			"123123-sadf-32423-4245", "");

		_bqEventDog.addBQEvent(
			"Page",
			new HashSet<BQEventProperty>() {
				{
					add(
						new BQEventProperty(
							null, "viewDuration", "testValue1"));
				}
			},
			"Firefox", "http://localhost:8080/search?q=Liferay",
			channel.getId(), "Diamond Bar", "en_US", "{\"groupId\": \"3212\"}",
			"United States", DateUtil.newDate(), null, "", "",
			DigestUtils.sha256Hex("test@liferay.com"), DateUtil.newDate(),
			"pageViewed", "", "analyticsEventId2", "", "en_US", "", "", "", "",
			"", "", "", "http://localhost:8080/search?q=Liferay",
			"123123-sadf-32423-4245", "");

		_bqEventDog.addBQEvent(
			"Page",
			new HashSet<BQEventProperty>() {
				{
					add(
						new BQEventProperty(
							null, "viewDuration", "testValue1"));
				}
			},
			"Firefox", "http://localhost:8080/search?q=Liferay+DXP",
			channel.getId(), "Diamond Bar", "en_US", "{\"groupId\": \"3212\"}",
			"United States", DateUtil.newDate(), null, "", "",
			DigestUtils.sha256Hex("test@liferay.com"), DateUtil.newDate(),
			"pageViewed", "", "analyticsEventId3", "", "en_US", "", "", "", "",
			"", "", "", "http://localhost:8080/search?q=Liferay+DXP",
			"123123-sadf-32423-4245", "");

		_bqEventDog.addBQEvent(
			"Page",
			new HashSet<BQEventProperty>() {
				{
					add(
						new BQEventProperty(
							null, "viewDuration", "testValue1"));
				}
			},
			"Firefox", "http://localhost:8080/search?q=Diamond+Bar",
			channel.getId(), "Diamond Bar", "pt_BR", "{\"groupId\": \"3212\"}",
			"United States", DateUtil.newDate(), null, "", "",
			DigestUtils.sha256Hex("test@liferay.com"), DateUtil.newDate(),
			"pageViewed", "", "analyticsEventId4", "", "en_US", "", "", "", "",
			"", "", "", "http://localhost:8080/search?q=Diamond+Bar",
			"123123-sadf-32423-4245", "");

		Page<SearchKeyword> searchKeywordPage =
			_bqEventDog.getSearchKeywordPage(
				"pt_BR", null, DigestUtils.sha256Hex("test@liferay.com"), 1, 0,
				2, new String[] {"counts", "desc"}, TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(1, searchKeywordPage.getTotalElements());

		List<SearchKeyword> searchKeywords = searchKeywordPage.getContent();

		Assertions.assertEquals(1, searchKeywords.size());

		SearchKeyword[] searchKeywordsArray = searchKeywords.toArray(
			new SearchKeyword[0]);

		SearchKeyword searchKeyword = searchKeywordsArray[0];

		Assertions.assertEquals(1, searchKeyword.getCounts());
		Assertions.assertEquals("pt_BR", searchKeyword.getDisplayLanguageId());
		Assertions.assertEquals("3212", searchKeyword.getGroupId());
		Assertions.assertEquals("diamond bar", searchKeyword.getKeywords());

		searchKeywordPage = _bqEventDog.getSearchKeywordPage(
			"en_US", null, DigestUtils.sha256Hex("test@liferay.com"), 1, 0, 2,
			new String[] {"counts", "desc"}, TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(2, searchKeywordPage.getTotalElements());

		searchKeywords = searchKeywordPage.getContent();

		Assertions.assertEquals(2, searchKeywords.size());

		searchKeywordsArray = searchKeywords.toArray(new SearchKeyword[0]);

		searchKeyword = searchKeywordsArray[0];

		Assertions.assertEquals(2, searchKeyword.getCounts());
		Assertions.assertEquals("en_US", searchKeyword.getDisplayLanguageId());
		Assertions.assertEquals("3212", searchKeyword.getGroupId());
		Assertions.assertEquals("liferay dxp", searchKeyword.getKeywords());

		searchKeyword = searchKeywordsArray[1];

		Assertions.assertEquals(1, searchKeyword.getCounts());
		Assertions.assertEquals("en_US", searchKeyword.getDisplayLanguageId());
		Assertions.assertEquals("3212", searchKeyword.getGroupId());
		Assertions.assertEquals("liferay", searchKeyword.getKeywords());
	}

	@BQSQLResource(resourcePath = "test_bq_identity.sql")
	@Test
	public void testGetSearchKeywords4() throws Exception {
		Channel channel = _channelDog.addChannel("Test Channel");

		_bqEventDog.addBQEvent(
			"Page",
			new HashSet<BQEventProperty>() {
				{
					add(
						new BQEventProperty(
							null, "viewDuration", "testValue1"));
				}
			},
			"Firefox", "http://localhost:8080/search?q=Liferay+DXP",
			channel.getId(), "Diamond Bar", "en_US", "{\"groupId\": \"3212\"}",
			"United States", DateUtil.newDate(), null, "", "",
			DigestUtils.sha256Hex("test@liferay.com"), DateUtil.newDate(),
			"pageViewed", "", "analyticsEventId1", "", "en_US", "", "", "", "",
			"", "", "", "http://localhost:8080/search?q=Liferay%20DXP",
			"123123-sadf-32423-4245", "");

		_bqEventDog.addBQEvent(
			"Page",
			new HashSet<BQEventProperty>() {
				{
					add(
						new BQEventProperty(
							null, "viewDuration", "testValue1"));
				}
			},
			"Firefox", "http://localhost:8080/search?q=Liferay",
			channel.getId(), "Diamond Bar", "en_US", "{\"groupId\": \"3212\"}",
			"United States", DateUtil.newDate(), null, "", "",
			DigestUtils.sha256Hex("test@liferay.com"), DateUtil.newDate(),
			"pageViewed", "", "analyticsEventId2", "", "en_US", "", "", "", "",
			"", "", "", "http://localhost:8080/search?q=Liferay",
			"123123-sadf-32423-4245", "");

		_bqEventDog.addBQEvent(
			"Page",
			new HashSet<BQEventProperty>() {
				{
					add(
						new BQEventProperty(
							null, "viewDuration", "testValue1"));
				}
			},
			"Firefox", "http://localhost:8080/search?q=Liferay+DXP",
			channel.getId(), "Diamond Bar", "en_US", "{\"groupId\": \"3212\"}",
			"United States", DateUtil.newDate(), null, "", "",
			DigestUtils.sha256Hex("test@liferay.com"), DateUtil.newDate(),
			"pageViewed", "", "analyticsEventId3", "", "en_US", "", "", "", "",
			"", "", "", "http://localhost:8080/search?q=Liferay+DXP",
			"123123-sadf-32423-4245", "");

		_bqEventDog.addBQEvent(
			"Page",
			new HashSet<BQEventProperty>() {
				{
					add(
						new BQEventProperty(
							null, "viewDuration", "testValue1"));
				}
			},
			"Firefox", "http://localhost:8080/search?q=Diamond+Bar",
			channel.getId(), "Diamond Bar", "pt_BR", "{\"groupId\": \"3213\"}",
			"United States", DateUtil.newDate(), null, "", "",
			DigestUtils.sha256Hex("test@liferay.com"), DateUtil.newDate(),
			"pageViewed", "", "analyticsEventId4", "", "en_US", "", "", "", "",
			"", "", "", "http://localhost:8080/search?q=Diamond+Bar",
			"123123-sadf-32423-4245", "");

		Page<SearchKeyword> searchKeywordPage =
			_bqEventDog.getSearchKeywordPage(
				null, "3213", DigestUtils.sha256Hex("test@liferay.com"), 1, 0,
				2, new String[] {"counts", "desc"}, TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(1, searchKeywordPage.getTotalElements());

		List<SearchKeyword> searchKeywords = searchKeywordPage.getContent();

		Assertions.assertEquals(1, searchKeywords.size());

		SearchKeyword[] searchKeywordsArray = searchKeywords.toArray(
			new SearchKeyword[0]);

		SearchKeyword searchKeyword = searchKeywordsArray[0];

		Assertions.assertEquals(1, searchKeyword.getCounts());
		Assertions.assertEquals("pt_BR", searchKeyword.getDisplayLanguageId());
		Assertions.assertEquals("3213", searchKeyword.getGroupId());
		Assertions.assertEquals("diamond bar", searchKeyword.getKeywords());

		searchKeywordPage = _bqEventDog.getSearchKeywordPage(
			null, "3212", DigestUtils.sha256Hex("test@liferay.com"), 1, 0, 2,
			new String[] {"counts", "desc"}, TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(2, searchKeywordPage.getTotalElements());

		searchKeywords = searchKeywordPage.getContent();

		Assertions.assertEquals(2, searchKeywords.size());

		searchKeywordsArray = searchKeywords.toArray(new SearchKeyword[0]);

		searchKeyword = searchKeywordsArray[0];

		Assertions.assertEquals(2, searchKeyword.getCounts());
		Assertions.assertEquals("en_US", searchKeyword.getDisplayLanguageId());
		Assertions.assertEquals("3212", searchKeyword.getGroupId());
		Assertions.assertEquals("liferay dxp", searchKeyword.getKeywords());

		searchKeyword = searchKeywordsArray[1];

		Assertions.assertEquals(1, searchKeyword.getCounts());
		Assertions.assertEquals("en_US", searchKeyword.getDisplayLanguageId());
		Assertions.assertEquals("3212", searchKeyword.getGroupId());
		Assertions.assertEquals("liferay", searchKeyword.getKeywords());
	}

	@Test
	public void testSearchEventOrderByDesc() throws Exception {
		Date date = DateUtil.newDayDate();

		Channel channel = _channelDog.addChannel("Test Channel");

		for (int i = 2; i <= 7; i++) {
			_bqEventDog.addBQEvent(
				"Page",
				new HashSet<BQEventProperty>() {
					{
						add(
							new BQEventProperty(
								null, "viewDuration", "testValue1"));
						add(
							new BQEventProperty(
								null, "viewDuration", "testValue2"));
					}
				},
				channel.getId(), DateUtil.addDays(date, -i), 1L,
				DateUtil.addDays(date, -i), "pageViewed",
				"analyticsEventId" + i, "sessionId", "userId");
		}

		List<BQEvent> bqEvents = _bqEventDog.searchBQEvents(
			channel.getId(), null, null, 0, 50, TimeRange.LAST_7_DAYS);

		BQEvent bqEvent = bqEvents.get(bqEvents.size() - 1);

		Date eventDate = bqEvent.getEventDate();

		Date lastEventDate = DateUtil.addDays(date, -7);

		Assertions.assertEquals(lastEventDate, eventDate);
	}

	@Test
	public void testSearchEvents() throws Exception {
		Date date = DateUtil.newDayDate();

		Channel channel = _channelDog.addChannel("Test Channel");

		_bqEventDog.addBQEvent(
			"Page",
			new HashSet<BQEventProperty>() {
				{
					add(
						new BQEventProperty(
							null, "viewDuration", "testValue1"));
					add(
						new BQEventProperty(
							null, "viewDuration", "testValue2"));
				}
			},
			channel.getId(), date, 1L, date, "pageViewed", "analyticsEventId1",
			"sessionId", "userId");

		_bqEventDog.addBQEvent(
			"Page",
			new HashSet<BQEventProperty>() {
				{
					add(
						new BQEventProperty(
							null, "viewDuration", "testValue1"));
					add(
						new BQEventProperty(
							null, "viewDuration", "testValue2"));
				}
			},
			channel.getId(), date, 1L, date, "pageViewed", "analyticsEventId2",
			"sessionId", "userId");

		List<BQEvent> bqEvents = _bqEventDog.searchBQEvents(
			channel.getId(), null, null, 0, 50, TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(2, bqEvents.size(), bqEvents.toString());

		bqEvents.forEach(
			bqEvent -> {
				try {
					List<BQEventProperty> bqEventProperties =
						_objectMapper.readValue(
							bqEvent.getEventProperties(),
							new TypeReference<List<BQEventProperty>>() {
							});

					Assertions.assertEquals(
						2, bqEventProperties.size(),
						bqEventProperties.toString());
				}
				catch (JsonProcessingException jsonProcessingException) {
					Assertions.fail("Could not read event properties");
				}
			});
	}

	@Autowired
	private BQEventDog _bqEventDog;

	@Autowired
	private ChannelDog _channelDog;

	@Autowired
	private ObjectMapper _objectMapper;

}