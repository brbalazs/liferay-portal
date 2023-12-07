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
import com.liferay.osb.asah.common.model.RecentVisitAsset;
import com.liferay.osb.asah.common.model.RecentVisitPage;
import com.liferay.osb.asah.common.model.RecentVisitSite;
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
	public void testGetRecentAssetMultipleWebContentTypes() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Arrays.asList(
				RecentVisitAsset.ContentType.BLOG,
				RecentVisitAsset.ContentType.FORM),
			null, null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 10, new String[] {"visits", "desc"}, TimeRange.LAST_7_DAYS);

		Assertions.assertEquals(6, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> expectedRecentVisitAssets = new ArrayList<>();

		RecentVisitAsset recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("a73ihsy9");
		recentVisitAsset.setAssetTitle("Blog Title 2");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitAsset.setUrl("https://www.beryl.com/delivery");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("b73ihsy9");
		recentVisitAsset.setAssetTitle("Blog Title 3");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitAsset.setUrl("https://www.beryl.com/confirmation");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("c73ihsy9");
		recentVisitAsset.setAssetTitle("Blog Title 4");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -4));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -4));
		recentVisitAsset.setUrl("https://www.beryl.com/about");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("a37higg1");
		recentVisitAsset.setAssetTitle("Form Title 2");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitAsset.setUrl("https://www.beryl.com/forms/form-2");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("b42spry4");
		recentVisitAsset.setAssetTitle("Form Title 3");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentVisitAsset.setUrl("https://www.beryl.com/forms/form-3");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("e242gdef");
		recentVisitAsset.setAssetTitle("Form Title 1");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -5));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -5));
		recentVisitAsset.setUrl("https://www.beryl.com/forms/form-1");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		Assertions.assertEquals(
			expectedRecentVisitAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentAssetNoContentTypes() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.emptyList(), null, null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 10, new String[] {"contentType", "asc"}, TimeRange.LAST_7_DAYS);

		Assertions.assertEquals(12, recentAssetPage.getTotalElements());
		Assertions.assertEquals(2, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> expectedRecentVisitAssets = new ArrayList<>();

		RecentVisitAsset recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("a73ihsy9");
		recentVisitAsset.setAssetTitle("Blog Title 2");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitAsset.setUrl("https://www.beryl.com/delivery");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("b73ihsy9");
		recentVisitAsset.setAssetTitle("Blog Title 3");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitAsset.setUrl("https://www.beryl.com/confirmation");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("c73ihsy9");
		recentVisitAsset.setAssetTitle("Blog Title 4");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -4));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -4));
		recentVisitAsset.setUrl("https://www.beryl.com/about");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("a73ihsy9");
		recentVisitAsset.setAssetTitle("Document Title 2");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitAsset.setUrl("https://www.beryl.com/docs/doc-2");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("b73ihsy9");
		recentVisitAsset.setAssetTitle("Document Title 3");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitAsset.setUrl("https://www.beryl.com/docs/doc-3");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("c73ihsy9");
		recentVisitAsset.setAssetTitle("Document Title 4");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -4));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -4));
		recentVisitAsset.setUrl("https://www.beryl.com/docs/doc-4");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("a37higg1");
		recentVisitAsset.setAssetTitle("Form Title 2");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitAsset.setUrl("https://www.beryl.com/forms/form-2");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("b42spry4");
		recentVisitAsset.setAssetTitle("Form Title 3");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentVisitAsset.setUrl("https://www.beryl.com/forms/form-3");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("e242gdef");
		recentVisitAsset.setAssetTitle("Form Title 1");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -5));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -5));
		recentVisitAsset.setUrl("https://www.beryl.com/forms/form-1");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("a73ihsy9");
		recentVisitAsset.setAssetTitle("WebContent Title 2");
		recentVisitAsset.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitAsset.setUrl("https://www.beryl.com/journals/journal-2");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		Assertions.assertEquals(
			expectedRecentVisitAssets, recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.emptyList(), null, null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			1, 10, new String[] {"contentType", "asc"}, TimeRange.LAST_7_DAYS);

		expectedRecentVisitAssets = new ArrayList<>();

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("b73ihsy9");
		recentVisitAsset.setAssetTitle("WebContent Title 3");
		recentVisitAsset.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitAsset.setUrl("https://www.beryl.com/journals/journal-3");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("c73ihsy9");
		recentVisitAsset.setAssetTitle("WebContent Title 4");
		recentVisitAsset.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -4));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -4));
		recentVisitAsset.setUrl("https://www.beryl.com/journals/journal-4");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		Assertions.assertEquals(
			expectedRecentVisitAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentBlogsLast7Days() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.BLOG), null,
			null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 10, new String[0], TimeRange.LAST_7_DAYS);

		Assertions.assertEquals(3, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> expectedRecentVisitAssets = new ArrayList<>();

		RecentVisitAsset recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("a73ihsy9");
		recentVisitAsset.setAssetTitle("Blog Title 2");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitAsset.setUrl("https://www.beryl.com/delivery");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("b73ihsy9");
		recentVisitAsset.setAssetTitle("Blog Title 3");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitAsset.setUrl("https://www.beryl.com/confirmation");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("c73ihsy9");
		recentVisitAsset.setAssetTitle("Blog Title 4");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -4));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -4));
		recentVisitAsset.setUrl("https://www.beryl.com/about");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		Assertions.assertEquals(
			expectedRecentVisitAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_last_24_hours_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentBlogsLast24Hours() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.BLOG), null,
			null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[0], TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(3, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> recentVisitAssets = recentAssetPage.getContent();

		Assertions.assertFalse(recentVisitAssets.isEmpty());

		Map<Pair<String, String>, Long> expectedRecentAssetCounts =
			new HashMap<Pair<String, String>, Long>() {
				{
					put(Pair.of("e131fabc", "Blog Title 1"), 10L);
					put(Pair.of("a73ihsy9", "Blog Title 2"), 3L);
					put(Pair.of("b73ihsy9", "Blog Title 3"), 3L);
				}
			};

		for (RecentVisitAsset recentVisitAsset : recentVisitAssets) {
			Pair<String, String> pair = Pair.of(
				recentVisitAsset.getAssetId(),
				recentVisitAsset.getAssetTitle());

			Assertions.assertEquals(
				expectedRecentAssetCounts.get(pair),
				recentVisitAsset.getVisits());

			expectedRecentAssetCounts.remove(pair);
		}

		Assertions.assertTrue(expectedRecentAssetCounts.isEmpty());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentBlogsLast28Days() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.BLOG), null,
			null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[0], TimeRange.LAST_28_DAYS);

		Assertions.assertEquals(4, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> expectedRecentVisitAssets = new ArrayList<>();

		RecentVisitAsset recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("a73ihsy9");
		recentVisitAsset.setAssetTitle("Blog Title 2");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -26));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentVisitAsset.setUrl("https://www.beryl.com/delivery");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("b73ihsy9");
		recentVisitAsset.setAssetTitle("Blog Title 3");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -23));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentVisitAsset.setUrl("https://www.beryl.com/confirmation");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("c73ihsy9");
		recentVisitAsset.setAssetTitle("Blog Title 4");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentVisitAsset.setUrl("https://www.beryl.com/about");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("e131fabc");
		recentVisitAsset.setAssetTitle("Blog Title 1");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentVisitAsset.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		Assertions.assertEquals(
			expectedRecentVisitAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentBlogsLast30Days() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.BLOG), null,
			null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 10, new String[0], TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(4, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> expectedRecentVisitAssets = new ArrayList<>();

		RecentVisitAsset recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("a73ihsy9");
		recentVisitAsset.setAssetTitle("Blog Title 2");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -22));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitAsset.setUrl("https://www.beryl.com/delivery");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("b73ihsy9");
		recentVisitAsset.setAssetTitle("Blog Title 3");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -27));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitAsset.setUrl("https://www.beryl.com/confirmation");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("c73ihsy9");
		recentVisitAsset.setAssetTitle("Blog Title 4");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -24));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -4));
		recentVisitAsset.setUrl("https://www.beryl.com/about");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("e131fabc");
		recentVisitAsset.setAssetTitle("Blog Title 1");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -9));
		recentVisitAsset.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		Assertions.assertEquals(
			expectedRecentVisitAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(
		resourcePath = "test_get_recent_assets_multiple_datasource_ids_bq.sql"
	)
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentBlogsMultipleDataSourceIds() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.BLOG), null,
			null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 10, new String[0], TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(6, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> expectedRecentVisitAssets = new ArrayList<>();

		RecentVisitAsset recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("a73ihsy9");
		recentVisitAsset.setAssetTitle("Blog Title 2");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -22));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitAsset.setUrl("https://www.beryl.com/delivery");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("c73ihsy9");
		recentVisitAsset.setAssetTitle("Blog Title 4");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -24));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -4));
		recentVisitAsset.setUrl("https://www.beryl.com/about");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("b73ihsy9");
		recentVisitAsset.setAssetTitle("Blog Title 3");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -27));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -19));
		recentVisitAsset.setUrl("https://www.beryl.com/confirmation");
		recentVisitAsset.setVisits(2L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("e131fabc");
		recentVisitAsset.setAssetTitle("Blog Title 1");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -17));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -9));
		recentVisitAsset.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentVisitAsset.setVisits(2L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("b73ihsy9");
		recentVisitAsset.setAssetTitle("Blog Title 3");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitAsset.setUrl("https://www.beryl.com/confirmation");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("e131fabc");
		recentVisitAsset.setAssetTitle("Blog Title 1");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentVisitAsset.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		Assertions.assertEquals(
			expectedRecentVisitAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentBlogsSortByFirstVisitDate() {
		RecentVisitAsset recentVisitAsset1 = new RecentVisitAsset();

		recentVisitAsset1.setAssetId("a73ihsy9");
		recentVisitAsset1.setAssetTitle("Blog Title 2");
		recentVisitAsset1.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset1.setDataSourceId(84756L);
		recentVisitAsset1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -26));
		recentVisitAsset1.setGroupId("67890");
		recentVisitAsset1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentVisitAsset1.setUrl("https://www.beryl.com/delivery");
		recentVisitAsset1.setVisits(3L);

		RecentVisitAsset recentVisitAsset2 = new RecentVisitAsset();

		recentVisitAsset2.setAssetId("b73ihsy9");
		recentVisitAsset2.setAssetTitle("Blog Title 3");
		recentVisitAsset2.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset2.setDataSourceId(10293L);
		recentVisitAsset2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -30));
		recentVisitAsset2.setGroupId("12345");
		recentVisitAsset2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentVisitAsset2.setUrl("https://www.beryl.com/confirmation");
		recentVisitAsset2.setVisits(4L);

		RecentVisitAsset recentVisitAsset3 = new RecentVisitAsset();

		recentVisitAsset3.setAssetId("c73ihsy9");
		recentVisitAsset3.setAssetTitle("Blog Title 4");
		recentVisitAsset3.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset3.setDataSourceId(84756L);
		recentVisitAsset3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentVisitAsset3.setGroupId("67890");
		recentVisitAsset3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentVisitAsset3.setUrl("https://www.beryl.com/about");
		recentVisitAsset3.setVisits(3L);

		RecentVisitAsset recentVisitAsset4 = new RecentVisitAsset();

		recentVisitAsset4.setAssetId("e131fabc");
		recentVisitAsset4.setAssetTitle("Blog Title 1");
		recentVisitAsset4.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset4.setDataSourceId(10293L);
		recentVisitAsset4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentVisitAsset4.setGroupId("12345");
		recentVisitAsset4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentVisitAsset4.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentVisitAsset4.setVisits(3L);

		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.BLOG), null,
			null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"firstVisitDate", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitAsset2, recentVisitAsset3, recentVisitAsset1,
				recentVisitAsset4),
			recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.BLOG), null,
			null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"firstVisitDate", "desc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitAsset4, recentVisitAsset1, recentVisitAsset3,
				recentVisitAsset2),
			recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentBlogsSortByLastVisitDate() {
		RecentVisitAsset recentVisitAsset1 = new RecentVisitAsset();

		recentVisitAsset1.setAssetId("a73ihsy9");
		recentVisitAsset1.setAssetTitle("Blog Title 2");
		recentVisitAsset1.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset1.setDataSourceId(84756L);
		recentVisitAsset1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -26));
		recentVisitAsset1.setGroupId("67890");
		recentVisitAsset1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentVisitAsset1.setUrl("https://www.beryl.com/delivery");
		recentVisitAsset1.setVisits(3L);

		RecentVisitAsset recentVisitAsset2 = new RecentVisitAsset();

		recentVisitAsset2.setAssetId("b73ihsy9");
		recentVisitAsset2.setAssetTitle("Blog Title 3");
		recentVisitAsset2.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset2.setDataSourceId(10293L);
		recentVisitAsset2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -30));
		recentVisitAsset2.setGroupId("12345");
		recentVisitAsset2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentVisitAsset2.setUrl("https://www.beryl.com/confirmation");
		recentVisitAsset2.setVisits(4L);

		RecentVisitAsset recentVisitAsset3 = new RecentVisitAsset();

		recentVisitAsset3.setAssetId("c73ihsy9");
		recentVisitAsset3.setAssetTitle("Blog Title 4");
		recentVisitAsset3.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset3.setDataSourceId(84756L);
		recentVisitAsset3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentVisitAsset3.setGroupId("67890");
		recentVisitAsset3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentVisitAsset3.setUrl("https://www.beryl.com/about");
		recentVisitAsset3.setVisits(3L);

		RecentVisitAsset recentVisitAsset4 = new RecentVisitAsset();

		recentVisitAsset4.setAssetId("e131fabc");
		recentVisitAsset4.setAssetTitle("Blog Title 1");
		recentVisitAsset4.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset4.setDataSourceId(10293L);
		recentVisitAsset4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentVisitAsset4.setGroupId("12345");
		recentVisitAsset4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentVisitAsset4.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentVisitAsset4.setVisits(3L);

		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.BLOG), null,
			null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"lastVisitDate", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitAsset3, recentVisitAsset1, recentVisitAsset2,
				recentVisitAsset4),
			recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.BLOG), null,
			null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"lastVisitDate", "desc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitAsset4, recentVisitAsset2, recentVisitAsset1,
				recentVisitAsset3),
			recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentBlogsSortByVisits() {
		RecentVisitAsset recentVisitAsset1 = new RecentVisitAsset();

		recentVisitAsset1.setAssetId("a73ihsy9");
		recentVisitAsset1.setAssetTitle("Blog Title 2");
		recentVisitAsset1.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset1.setDataSourceId(84756L);
		recentVisitAsset1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -26));
		recentVisitAsset1.setGroupId("67890");
		recentVisitAsset1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentVisitAsset1.setUrl("https://www.beryl.com/delivery");
		recentVisitAsset1.setVisits(3L);

		RecentVisitAsset recentVisitAsset2 = new RecentVisitAsset();

		recentVisitAsset2.setAssetId("b73ihsy9");
		recentVisitAsset2.setAssetTitle("Blog Title 3");
		recentVisitAsset2.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset2.setDataSourceId(10293L);
		recentVisitAsset2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -30));
		recentVisitAsset2.setGroupId("12345");
		recentVisitAsset2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentVisitAsset2.setUrl("https://www.beryl.com/confirmation");
		recentVisitAsset2.setVisits(4L);

		RecentVisitAsset recentVisitAsset3 = new RecentVisitAsset();

		recentVisitAsset3.setAssetId("c73ihsy9");
		recentVisitAsset3.setAssetTitle("Blog Title 4");
		recentVisitAsset3.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset3.setDataSourceId(84756L);
		recentVisitAsset3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentVisitAsset3.setGroupId("67890");
		recentVisitAsset3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentVisitAsset3.setUrl("https://www.beryl.com/about");
		recentVisitAsset3.setVisits(3L);

		RecentVisitAsset recentVisitAsset4 = new RecentVisitAsset();

		recentVisitAsset4.setAssetId("e131fabc");
		recentVisitAsset4.setAssetTitle("Blog Title 1");
		recentVisitAsset4.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset4.setDataSourceId(10293L);
		recentVisitAsset4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentVisitAsset4.setGroupId("12345");
		recentVisitAsset4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentVisitAsset4.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentVisitAsset4.setVisits(3L);

		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.BLOG), null,
			null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"visits", "asc", "assetTitle", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitAsset4, recentVisitAsset1, recentVisitAsset3,
				recentVisitAsset2),
			recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.BLOG), null,
			null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"visits", "desc", "assetTitle", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitAsset2, recentVisitAsset4, recentVisitAsset1,
				recentVisitAsset3),
			recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentBlogsWithDataSourceId() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.BLOG),
			84756L, null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 10, new String[0], TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(2, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> expectedRecentVisitAssets = new ArrayList<>();

		RecentVisitAsset recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("a73ihsy9");
		recentVisitAsset.setAssetTitle("Blog Title 2");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -22));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitAsset.setUrl("https://www.beryl.com/delivery");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("c73ihsy9");
		recentVisitAsset.setAssetTitle("Blog Title 4");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -24));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -4));
		recentVisitAsset.setUrl("https://www.beryl.com/about");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		Assertions.assertEquals(
			expectedRecentVisitAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentBlogsWithGroupId() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.BLOG), null,
			"12345",
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[0], TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(2, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> expectedRecentVisitAssets = new ArrayList<>();

		RecentVisitAsset recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("b73ihsy9");
		recentVisitAsset.setAssetTitle("Blog Title 3");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -30));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentVisitAsset.setUrl("https://www.beryl.com/confirmation");
		recentVisitAsset.setVisits(4L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("e131fabc");
		recentVisitAsset.setAssetTitle("Blog Title 1");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentVisitAsset.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		Assertions.assertEquals(
			expectedRecentVisitAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentBlogsWithPagination() {
		RecentVisitAsset recentVisitAsset1 = new RecentVisitAsset();

		recentVisitAsset1.setAssetId("a73ihsy9");
		recentVisitAsset1.setAssetTitle("Blog Title 2");
		recentVisitAsset1.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset1.setDataSourceId(84756L);
		recentVisitAsset1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -26));
		recentVisitAsset1.setGroupId("67890");
		recentVisitAsset1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentVisitAsset1.setUrl("https://www.beryl.com/delivery");
		recentVisitAsset1.setVisits(3L);

		RecentVisitAsset recentVisitAsset2 = new RecentVisitAsset();

		recentVisitAsset2.setAssetId("b73ihsy9");
		recentVisitAsset2.setAssetTitle("Blog Title 3");
		recentVisitAsset2.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset2.setDataSourceId(10293L);
		recentVisitAsset2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -30));
		recentVisitAsset2.setGroupId("12345");
		recentVisitAsset2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentVisitAsset2.setUrl("https://www.beryl.com/confirmation");
		recentVisitAsset2.setVisits(4L);

		RecentVisitAsset recentVisitAsset3 = new RecentVisitAsset();

		recentVisitAsset3.setAssetId("c73ihsy9");
		recentVisitAsset3.setAssetTitle("Blog Title 4");
		recentVisitAsset3.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset3.setDataSourceId(84756L);
		recentVisitAsset3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentVisitAsset3.setGroupId("67890");
		recentVisitAsset3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentVisitAsset3.setUrl("https://www.beryl.com/about");
		recentVisitAsset3.setVisits(3L);

		RecentVisitAsset recentVisitAsset4 = new RecentVisitAsset();

		recentVisitAsset4.setAssetId("e131fabc");
		recentVisitAsset4.setAssetTitle("Blog Title 1");
		recentVisitAsset4.setContentType(RecentVisitAsset.ContentType.BLOG);
		recentVisitAsset4.setDataSourceId(10293L);
		recentVisitAsset4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentVisitAsset4.setGroupId("12345");
		recentVisitAsset4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentVisitAsset4.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentVisitAsset4.setVisits(3L);

		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.BLOG), null,
			null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 3, new String[] {"firstVisitDate", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitAsset2, recentVisitAsset3, recentVisitAsset1),
			recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.BLOG), null,
			null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			1, 3, new String[] {"firstVisitDate", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Collections.singletonList(recentVisitAsset4),
			recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentBlogsWithSuppression() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.BLOG), null,
			null,
			"8bb3cd4319c4cc4df1addc31cb0fae500288133b91228a1cacb4ff2802446220",
			0, 10, new String[0], TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(0L, recentAssetPage.getTotalElements());
		Assertions.assertEquals(0L, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> recentVisitAssets = recentAssetPage.getContent();

		Assertions.assertTrue(recentVisitAssets.isEmpty());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_yesterday_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentBlogsYesterday() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.BLOG), null,
			null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 10, new String[0], TimeRange.YESTERDAY);

		Assertions.assertEquals(3, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> recentVisitAssets = recentAssetPage.getContent();

		Assertions.assertFalse(recentVisitAssets.isEmpty());

		Map<Pair<String, String>, Long> expectedRecentAssetCounts =
			new HashMap<Pair<String, String>, Long>() {
				{
					put(Pair.of("e131fabc", "Blog Title 1"), 10L);
					put(Pair.of("a73ihsy9", "Blog Title 2"), 2L);
					put(Pair.of("b73ihsy9", "Blog Title 3"), 2L);
				}
			};

		for (RecentVisitAsset recentVisitAsset : recentVisitAssets) {
			Pair<String, String> pair = Pair.of(
				recentVisitAsset.getAssetId(),
				recentVisitAsset.getAssetTitle());

			Assertions.assertEquals(
				expectedRecentAssetCounts.get(pair),
				recentVisitAsset.getVisits());

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

	@BQSQLResource(
		resourcePath = "test_get_recent_assets_multiple_datasource_ids_bq.sql"
	)
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentDocumentMultipleDataSourceIds() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.DOCUMENT),
			null, null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 10, new String[0], TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(6, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> expectedRecentVisitAssets = new ArrayList<>();

		RecentVisitAsset recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("b73ihsy9");
		recentVisitAsset.setAssetTitle("Document Title 3");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -27));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitAsset.setUrl("https://www.beryl.com/docs/doc-3");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("c73ihsy9");
		recentVisitAsset.setAssetTitle("Document Title 4");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -24));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -4));
		recentVisitAsset.setUrl("https://www.beryl.com/docs/doc-4");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("a73ihsy9");
		recentVisitAsset.setAssetTitle("Document Title 2");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -22));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -14));
		recentVisitAsset.setUrl("https://www.beryl.com/docs/doc-2");
		recentVisitAsset.setVisits(2L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("e131fabc");
		recentVisitAsset.setAssetTitle("Document Title 1");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -17));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -9));
		recentVisitAsset.setUrl("https://www.beryl.com/docs/doc-1");
		recentVisitAsset.setVisits(2L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("a73ihsy9");
		recentVisitAsset.setAssetTitle("Document Title 2");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitAsset.setUrl("https://www.beryl.com/docs/doc-2");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("e131fabc");
		recentVisitAsset.setAssetTitle("Document Title 1");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentVisitAsset.setUrl("https://www.beryl.com/docs/doc-1");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		Assertions.assertEquals(
			expectedRecentVisitAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentDocumentsLast7Days() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.DOCUMENT),
			null, null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 10, new String[0], TimeRange.LAST_7_DAYS);

		Assertions.assertEquals(3, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> expectedRecentVisitAssets = new ArrayList<>();

		RecentVisitAsset recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("a73ihsy9");
		recentVisitAsset.setAssetTitle("Document Title 2");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitAsset.setUrl("https://www.beryl.com/docs/doc-2");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("b73ihsy9");
		recentVisitAsset.setAssetTitle("Document Title 3");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitAsset.setUrl("https://www.beryl.com/docs/doc-3");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("c73ihsy9");
		recentVisitAsset.setAssetTitle("Document Title 4");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -4));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -4));
		recentVisitAsset.setUrl("https://www.beryl.com/docs/doc-4");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		Assertions.assertEquals(
			expectedRecentVisitAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_last_24_hours_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentDocumentsLast24Hours() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.DOCUMENT),
			null, null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[0], TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(3, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> recentVisitAssets = recentAssetPage.getContent();

		Assertions.assertFalse(recentVisitAssets.isEmpty());

		Map<Pair<String, String>, Long> expectedRecentAssetCounts =
			new HashMap<Pair<String, String>, Long>() {
				{
					put(Pair.of("e131fabc", "Document Title 1"), 10L);
					put(Pair.of("a73ihsy9", "Document Title 2"), 3L);
					put(Pair.of("b73ihsy9", "Document Title 3"), 3L);
				}
			};

		for (RecentVisitAsset recentVisitAsset : recentVisitAssets) {
			Pair<String, String> pair = Pair.of(
				recentVisitAsset.getAssetId(),
				recentVisitAsset.getAssetTitle());

			Assertions.assertEquals(
				expectedRecentAssetCounts.get(pair),
				recentVisitAsset.getVisits());

			expectedRecentAssetCounts.remove(pair);
		}

		Assertions.assertTrue(expectedRecentAssetCounts.isEmpty());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentDocumentsLast28Days() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.DOCUMENT),
			null, null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[0], TimeRange.LAST_28_DAYS);

		Assertions.assertEquals(4, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> expectedRecentVisitAssets = new ArrayList<>();

		RecentVisitAsset recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("a73ihsy9");
		recentVisitAsset.setAssetTitle("Document Title 2");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -26));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentVisitAsset.setUrl("https://www.beryl.com/docs/doc-2");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("b73ihsy9");
		recentVisitAsset.setAssetTitle("Document Title 3");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -23));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentVisitAsset.setUrl("https://www.beryl.com/docs/doc-3");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("c73ihsy9");
		recentVisitAsset.setAssetTitle("Document Title 4");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentVisitAsset.setUrl("https://www.beryl.com/docs/doc-4");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("e131fabc");
		recentVisitAsset.setAssetTitle("Document Title 1");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentVisitAsset.setUrl("https://www.beryl.com/docs/doc-1");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		Assertions.assertEquals(
			expectedRecentVisitAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentDocumentsLast30Days() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.DOCUMENT),
			null, null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 10, new String[0], TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(4, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> expectedRecentVisitAssets = new ArrayList<>();

		RecentVisitAsset recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("a73ihsy9");
		recentVisitAsset.setAssetTitle("Document Title 2");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -22));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitAsset.setUrl("https://www.beryl.com/docs/doc-2");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("b73ihsy9");
		recentVisitAsset.setAssetTitle("Document Title 3");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -27));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitAsset.setUrl("https://www.beryl.com/docs/doc-3");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("c73ihsy9");
		recentVisitAsset.setAssetTitle("Document Title 4");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -24));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -4));
		recentVisitAsset.setUrl("https://www.beryl.com/docs/doc-4");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("e131fabc");
		recentVisitAsset.setAssetTitle("Document Title 1");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -9));
		recentVisitAsset.setUrl("https://www.beryl.com/docs/doc-1");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		Assertions.assertEquals(
			expectedRecentVisitAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentDocumentsSortByFirstVisitDate() {
		RecentVisitAsset recentVisitAsset1 = new RecentVisitAsset();

		recentVisitAsset1.setAssetId("a73ihsy9");
		recentVisitAsset1.setAssetTitle("Document Title 2");
		recentVisitAsset1.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset1.setDataSourceId(10293L);
		recentVisitAsset1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -26));
		recentVisitAsset1.setGroupId("12345");
		recentVisitAsset1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentVisitAsset1.setUrl("https://www.beryl.com/docs/doc-2");
		recentVisitAsset1.setVisits(3L);

		RecentVisitAsset recentVisitAsset2 = new RecentVisitAsset();

		recentVisitAsset2.setAssetId("b73ihsy9");
		recentVisitAsset2.setAssetTitle("Document Title 3");
		recentVisitAsset2.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset2.setDataSourceId(84756L);
		recentVisitAsset2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -30));
		recentVisitAsset2.setGroupId("67890");
		recentVisitAsset2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentVisitAsset2.setUrl("https://www.beryl.com/docs/doc-3");
		recentVisitAsset2.setVisits(4L);

		RecentVisitAsset recentVisitAsset3 = new RecentVisitAsset();

		recentVisitAsset3.setAssetId("c73ihsy9");
		recentVisitAsset3.setAssetTitle("Document Title 4");
		recentVisitAsset3.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset3.setDataSourceId(84756L);
		recentVisitAsset3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentVisitAsset3.setGroupId("67890");
		recentVisitAsset3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentVisitAsset3.setUrl("https://www.beryl.com/docs/doc-4");
		recentVisitAsset3.setVisits(3L);

		RecentVisitAsset recentVisitAsset4 = new RecentVisitAsset();

		recentVisitAsset4.setAssetId("e131fabc");
		recentVisitAsset4.setAssetTitle("Document Title 1");
		recentVisitAsset4.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset4.setDataSourceId(10293L);
		recentVisitAsset4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentVisitAsset4.setGroupId("12345");
		recentVisitAsset4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentVisitAsset4.setUrl("https://www.beryl.com/docs/doc-1");
		recentVisitAsset4.setVisits(3L);

		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.DOCUMENT),
			null, null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"firstVisitDate", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitAsset2, recentVisitAsset3, recentVisitAsset1,
				recentVisitAsset4),
			recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.DOCUMENT),
			null, null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"firstVisitDate", "desc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitAsset4, recentVisitAsset1, recentVisitAsset3,
				recentVisitAsset2),
			recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentDocumentsSortByLastVisitDate() {
		RecentVisitAsset recentVisitAsset1 = new RecentVisitAsset();

		recentVisitAsset1.setAssetId("a73ihsy9");
		recentVisitAsset1.setAssetTitle("Document Title 2");
		recentVisitAsset1.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset1.setDataSourceId(10293L);
		recentVisitAsset1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -26));
		recentVisitAsset1.setGroupId("12345");
		recentVisitAsset1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentVisitAsset1.setUrl("https://www.beryl.com/docs/doc-2");
		recentVisitAsset1.setVisits(3L);

		RecentVisitAsset recentVisitAsset2 = new RecentVisitAsset();

		recentVisitAsset2.setAssetId("b73ihsy9");
		recentVisitAsset2.setAssetTitle("Document Title 3");
		recentVisitAsset2.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset2.setDataSourceId(84756L);
		recentVisitAsset2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -30));
		recentVisitAsset2.setGroupId("67890");
		recentVisitAsset2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentVisitAsset2.setUrl("https://www.beryl.com/docs/doc-3");
		recentVisitAsset2.setVisits(4L);

		RecentVisitAsset recentVisitAsset3 = new RecentVisitAsset();

		recentVisitAsset3.setAssetId("c73ihsy9");
		recentVisitAsset3.setAssetTitle("Document Title 4");
		recentVisitAsset3.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset3.setDataSourceId(84756L);
		recentVisitAsset3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentVisitAsset3.setGroupId("67890");
		recentVisitAsset3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentVisitAsset3.setUrl("https://www.beryl.com/docs/doc-4");
		recentVisitAsset3.setVisits(3L);

		RecentVisitAsset recentVisitAsset4 = new RecentVisitAsset();

		recentVisitAsset4.setAssetId("e131fabc");
		recentVisitAsset4.setAssetTitle("Document Title 1");
		recentVisitAsset4.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset4.setDataSourceId(10293L);
		recentVisitAsset4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentVisitAsset4.setGroupId("12345");
		recentVisitAsset4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentVisitAsset4.setUrl("https://www.beryl.com/docs/doc-1");
		recentVisitAsset4.setVisits(3L);

		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.DOCUMENT),
			null, null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"lastVisitDate", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitAsset3, recentVisitAsset1, recentVisitAsset2,
				recentVisitAsset4),
			recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.DOCUMENT),
			null, null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"lastVisitDate", "desc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitAsset4, recentVisitAsset2, recentVisitAsset1,
				recentVisitAsset3),
			recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentDocumentsSortByVisits() {
		RecentVisitAsset recentVisitAsset1 = new RecentVisitAsset();

		recentVisitAsset1.setAssetId("a73ihsy9");
		recentVisitAsset1.setAssetTitle("Document Title 2");
		recentVisitAsset1.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset1.setDataSourceId(10293L);
		recentVisitAsset1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -26));
		recentVisitAsset1.setGroupId("12345");
		recentVisitAsset1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentVisitAsset1.setUrl("https://www.beryl.com/docs/doc-2");
		recentVisitAsset1.setVisits(3L);

		RecentVisitAsset recentVisitAsset2 = new RecentVisitAsset();

		recentVisitAsset2.setAssetId("b73ihsy9");
		recentVisitAsset2.setAssetTitle("Document Title 3");
		recentVisitAsset2.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset2.setDataSourceId(84756L);
		recentVisitAsset2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -30));
		recentVisitAsset2.setGroupId("67890");
		recentVisitAsset2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentVisitAsset2.setUrl("https://www.beryl.com/docs/doc-3");
		recentVisitAsset2.setVisits(4L);

		RecentVisitAsset recentVisitAsset3 = new RecentVisitAsset();

		recentVisitAsset3.setAssetId("c73ihsy9");
		recentVisitAsset3.setAssetTitle("Document Title 4");
		recentVisitAsset3.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset3.setDataSourceId(84756L);
		recentVisitAsset3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentVisitAsset3.setGroupId("67890");
		recentVisitAsset3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentVisitAsset3.setUrl("https://www.beryl.com/docs/doc-4");
		recentVisitAsset3.setVisits(3L);

		RecentVisitAsset recentVisitAsset4 = new RecentVisitAsset();

		recentVisitAsset4.setAssetId("e131fabc");
		recentVisitAsset4.setAssetTitle("Document Title 1");
		recentVisitAsset4.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset4.setDataSourceId(10293L);
		recentVisitAsset4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentVisitAsset4.setGroupId("12345");
		recentVisitAsset4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentVisitAsset4.setUrl("https://www.beryl.com/docs/doc-1");
		recentVisitAsset4.setVisits(3L);

		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.DOCUMENT),
			null, null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"visits", "asc", "assetTitle", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitAsset4, recentVisitAsset1, recentVisitAsset3,
				recentVisitAsset2),
			recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.DOCUMENT),
			null, null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"visits", "desc", "assetTitle", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitAsset2, recentVisitAsset4, recentVisitAsset1,
				recentVisitAsset3),
			recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentDocumentsWithGroupId() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.DOCUMENT),
			null, "12345",
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 10, new String[0], TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(2, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> expectedRecentVisitAssets = new ArrayList<>();

		RecentVisitAsset recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("a73ihsy9");
		recentVisitAsset.setAssetTitle("Document Title 2");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -22));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitAsset.setUrl("https://www.beryl.com/docs/doc-2");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("e131fabc");
		recentVisitAsset.setAssetTitle("Document Title 1");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -9));
		recentVisitAsset.setUrl("https://www.beryl.com/docs/doc-1");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		Assertions.assertEquals(
			expectedRecentVisitAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentDocumentsWithPagination() {
		RecentVisitAsset recentVisitAsset1 = new RecentVisitAsset();

		recentVisitAsset1.setAssetId("a73ihsy9");
		recentVisitAsset1.setAssetTitle("Document Title 2");
		recentVisitAsset1.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset1.setDataSourceId(10293L);
		recentVisitAsset1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -26));
		recentVisitAsset1.setGroupId("12345");
		recentVisitAsset1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentVisitAsset1.setUrl("https://www.beryl.com/docs/doc-2");
		recentVisitAsset1.setVisits(3L);

		RecentVisitAsset recentVisitAsset2 = new RecentVisitAsset();

		recentVisitAsset2.setAssetId("b73ihsy9");
		recentVisitAsset2.setAssetTitle("Document Title 3");
		recentVisitAsset2.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset2.setDataSourceId(84756L);
		recentVisitAsset2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -30));
		recentVisitAsset2.setGroupId("67890");
		recentVisitAsset2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentVisitAsset2.setUrl("https://www.beryl.com/docs/doc-3");
		recentVisitAsset2.setVisits(4L);

		RecentVisitAsset recentVisitAsset3 = new RecentVisitAsset();

		recentVisitAsset3.setAssetId("c73ihsy9");
		recentVisitAsset3.setAssetTitle("Document Title 4");
		recentVisitAsset3.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset3.setDataSourceId(84756L);
		recentVisitAsset3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentVisitAsset3.setGroupId("67890");
		recentVisitAsset3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentVisitAsset3.setUrl("https://www.beryl.com/docs/doc-4");
		recentVisitAsset3.setVisits(3L);

		RecentVisitAsset recentVisitAsset4 = new RecentVisitAsset();

		recentVisitAsset4.setAssetId("e131fabc");
		recentVisitAsset4.setAssetTitle("Document Title 1");
		recentVisitAsset4.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset4.setDataSourceId(10293L);
		recentVisitAsset4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentVisitAsset4.setGroupId("12345");
		recentVisitAsset4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentVisitAsset4.setUrl("https://www.beryl.com/docs/doc-1");
		recentVisitAsset4.setVisits(3L);

		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.DOCUMENT),
			null, null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 3, new String[] {"firstVisitDate", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitAsset2, recentVisitAsset3, recentVisitAsset1),
			recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.DOCUMENT),
			null, null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			1, 3, new String[] {"lastVisitDate", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Collections.singletonList(recentVisitAsset4),
			recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentDocumentsWithSuppression() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.DOCUMENT),
			null, null,
			"8bb3cd4319c4cc4df1addc31cb0fae500288133b91228a1cacb4ff2802446220",
			0, 10, new String[0], TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(0L, recentAssetPage.getTotalElements());
		Assertions.assertEquals(0L, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> recentVisitAssets = recentAssetPage.getContent();

		Assertions.assertTrue(recentVisitAssets.isEmpty());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_yesterday_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentDocumentsYesterday() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.DOCUMENT),
			null, null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 10, new String[0], TimeRange.YESTERDAY);

		Assertions.assertEquals(3, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> recentVisitAssets = recentAssetPage.getContent();

		Assertions.assertFalse(recentVisitAssets.isEmpty());

		Map<Pair<String, String>, Long> expectedRecentAssetCounts =
			new HashMap<Pair<String, String>, Long>() {
				{
					put(Pair.of("e131fabc", "Document Title 1"), 10L);
					put(Pair.of("a73ihsy9", "Document Title 2"), 2L);
					put(Pair.of("b73ihsy9", "Document Title 3"), 2L);
				}
			};

		for (RecentVisitAsset recentVisitAsset : recentVisitAssets) {
			Pair<String, String> pair = Pair.of(
				recentVisitAsset.getAssetId(),
				recentVisitAsset.getAssetTitle());

			Assertions.assertEquals(
				expectedRecentAssetCounts.get(pair),
				recentVisitAsset.getVisits());

			expectedRecentAssetCounts.remove(pair);
		}

		Assertions.assertTrue(expectedRecentAssetCounts.isEmpty());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentDocumentWithDataSourceId() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.DOCUMENT),
			10293L, null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 10, new String[0], TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(2, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> expectedRecentVisitAssets = new ArrayList<>();

		RecentVisitAsset recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("a73ihsy9");
		recentVisitAsset.setAssetTitle("Document Title 2");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -22));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitAsset.setUrl("https://www.beryl.com/docs/doc-2");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("e131fabc");
		recentVisitAsset.setAssetTitle("Document Title 1");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.DOCUMENT);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -9));
		recentVisitAsset.setUrl("https://www.beryl.com/docs/doc-1");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		Assertions.assertEquals(
			expectedRecentVisitAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentFormsLast7Days() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.FORM), null,
			null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 10, new String[0], TimeRange.LAST_7_DAYS);

		Assertions.assertEquals(3, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> expectedRecentVisitAssets = new ArrayList<>();

		RecentVisitAsset recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("a37higg1");
		recentVisitAsset.setAssetTitle("Form Title 2");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitAsset.setUrl("https://www.beryl.com/forms/form-2");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("b42spry4");
		recentVisitAsset.setAssetTitle("Form Title 3");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentVisitAsset.setUrl("https://www.beryl.com/forms/form-3");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("e242gdef");
		recentVisitAsset.setAssetTitle("Form Title 1");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -5));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -5));
		recentVisitAsset.setUrl("https://www.beryl.com/forms/form-1");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		Assertions.assertEquals(
			expectedRecentVisitAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_last_24_hours_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentFormsLast24Hours() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.FORM), null,
			null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[0], TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(3, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> recentVisitAssets = recentAssetPage.getContent();

		Assertions.assertFalse(recentVisitAssets.isEmpty());

		Map<Pair<String, String>, Long> expectedRecentAssetCounts =
			new HashMap<Pair<String, String>, Long>() {
				{
					put(Pair.of("e242gdef", "Form Title 1"), 10L);
					put(Pair.of("a37higg1", "Form Title 2"), 3L);
					put(Pair.of("b42spry4", "Form Title 3"), 3L);
				}
			};

		for (RecentVisitAsset recentVisitAsset : recentVisitAssets) {
			Pair<String, String> pair = Pair.of(
				recentVisitAsset.getAssetId(),
				recentVisitAsset.getAssetTitle());

			Assertions.assertEquals(
				expectedRecentAssetCounts.get(pair),
				recentVisitAsset.getVisits());

			expectedRecentAssetCounts.remove(pair);
		}

		Assertions.assertTrue(expectedRecentAssetCounts.isEmpty());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentFormsLast28Days() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.FORM), null,
			null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[0], TimeRange.LAST_28_DAYS);

		Assertions.assertEquals(4, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> expectedRecentVisitAssets = new ArrayList<>();

		RecentVisitAsset recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("b42spry4");
		recentVisitAsset.setAssetTitle("Form Title 3");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -27));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitAsset.setUrl("https://www.beryl.com/forms/form-3");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("c99ytfl7");
		recentVisitAsset.setAssetTitle("Form Title 4");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentVisitAsset.setUrl("https://www.beryl.com/forms/form-4");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("e242gdef");
		recentVisitAsset.setAssetTitle("Form Title 1");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentVisitAsset.setUrl("https://www.beryl.com/forms/form-1");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("a37higg1");
		recentVisitAsset.setAssetTitle("Form Title 2");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -18));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -10));
		recentVisitAsset.setUrl("https://www.beryl.com/forms/form-2");
		recentVisitAsset.setVisits(2L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		Assertions.assertEquals(
			expectedRecentVisitAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentFormsLast30Days() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.FORM), null,
			null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[0], TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(4, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> expectedRecentVisitAssets = new ArrayList<>();

		RecentVisitAsset recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("e242gdef");
		recentVisitAsset.setAssetTitle("Form Title 1");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentVisitAsset.setUrl("https://www.beryl.com/forms/form-1");
		recentVisitAsset.setVisits(4L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("b42spry4");
		recentVisitAsset.setAssetTitle("Form Title 3");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -27));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitAsset.setUrl("https://www.beryl.com/forms/form-3");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("c99ytfl7");
		recentVisitAsset.setAssetTitle("Form Title 4");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentVisitAsset.setUrl("https://www.beryl.com/forms/form-4");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("a37higg1");
		recentVisitAsset.setAssetTitle("Form Title 2");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -18));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -10));
		recentVisitAsset.setUrl("https://www.beryl.com/forms/form-2");
		recentVisitAsset.setVisits(2L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		Assertions.assertEquals(
			expectedRecentVisitAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(
		resourcePath = "test_get_recent_assets_multiple_datasource_ids_bq.sql"
	)
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentFormsMultipleDataSourceIds() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.FORM), null,
			null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[0], TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(6, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> expectedRecentVisitAssets = new ArrayList<>();

		RecentVisitAsset recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("b42spry4");
		recentVisitAsset.setAssetTitle("Form Title 3");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -27));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitAsset.setUrl("https://www.beryl.com/forms/form-3");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("a37higg1");
		recentVisitAsset.setAssetTitle("Form Title 2");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -18));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -10));
		recentVisitAsset.setUrl("https://www.beryl.com/forms/form-2");
		recentVisitAsset.setVisits(2L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("c99ytfl7");
		recentVisitAsset.setAssetTitle("Form Title 4");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -16));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentVisitAsset.setUrl("https://www.beryl.com/forms/form-4");
		recentVisitAsset.setVisits(2L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("e242gdef");
		recentVisitAsset.setAssetTitle("Form Title 1");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -9));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentVisitAsset.setUrl("https://www.beryl.com/forms/form-1");
		recentVisitAsset.setVisits(2L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("e242gdef");
		recentVisitAsset.setAssetTitle("Form Title 1");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentVisitAsset.setUrl("https://www.beryl.com/forms/form-1");
		recentVisitAsset.setVisits(2L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("c99ytfl7");
		recentVisitAsset.setAssetTitle("Form Title 4");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentVisitAsset.setUrl("https://www.beryl.com/forms/form-4");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		Assertions.assertEquals(
			expectedRecentVisitAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentFormsSortByFirstVisitDate() {
		RecentVisitAsset recentVisitAsset1 = new RecentVisitAsset();

		recentVisitAsset1.setAssetId("e242gdef");
		recentVisitAsset1.setAssetTitle("Form Title 1");
		recentVisitAsset1.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset1.setDataSourceId(10293L);
		recentVisitAsset1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentVisitAsset1.setGroupId("12345");
		recentVisitAsset1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentVisitAsset1.setUrl("https://www.beryl.com/forms/form-1");
		recentVisitAsset1.setVisits(4L);

		RecentVisitAsset recentVisitAsset2 = new RecentVisitAsset();

		recentVisitAsset2.setAssetId("b42spry4");
		recentVisitAsset2.setAssetTitle("Form Title 3");
		recentVisitAsset2.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset2.setDataSourceId(84756L);
		recentVisitAsset2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -27));
		recentVisitAsset2.setGroupId("67890");
		recentVisitAsset2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitAsset2.setUrl("https://www.beryl.com/forms/form-3");
		recentVisitAsset2.setVisits(3L);

		RecentVisitAsset recentVisitAsset3 = new RecentVisitAsset();

		recentVisitAsset3.setAssetId("c99ytfl7");
		recentVisitAsset3.setAssetTitle("Form Title 4");
		recentVisitAsset3.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset3.setDataSourceId(10293L);
		recentVisitAsset3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentVisitAsset3.setGroupId("12345");
		recentVisitAsset3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentVisitAsset3.setUrl("https://www.beryl.com/forms/form-4");
		recentVisitAsset3.setVisits(3L);

		RecentVisitAsset recentVisitAsset4 = new RecentVisitAsset();

		recentVisitAsset4.setAssetId("a37higg1");
		recentVisitAsset4.setAssetTitle("Form Title 2");
		recentVisitAsset4.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset4.setDataSourceId(84756L);
		recentVisitAsset4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -18));
		recentVisitAsset4.setGroupId("67890");
		recentVisitAsset4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -10));
		recentVisitAsset4.setUrl("https://www.beryl.com/forms/form-2");
		recentVisitAsset4.setVisits(2L);

		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.FORM), null,
			null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"firstVisitDate", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitAsset1, recentVisitAsset3, recentVisitAsset2,
				recentVisitAsset4),
			recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.FORM), null,
			null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"firstVisitDate", "desc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitAsset4, recentVisitAsset2, recentVisitAsset3,
				recentVisitAsset1),
			recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentFormsSortByLastVisitDate() {
		RecentVisitAsset recentVisitAsset1 = new RecentVisitAsset();

		recentVisitAsset1.setAssetId("e242gdef");
		recentVisitAsset1.setAssetTitle("Form Title 1");
		recentVisitAsset1.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset1.setDataSourceId(10293L);
		recentVisitAsset1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentVisitAsset1.setGroupId("12345");
		recentVisitAsset1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentVisitAsset1.setUrl("https://www.beryl.com/forms/form-1");
		recentVisitAsset1.setVisits(4L);

		RecentVisitAsset recentVisitAsset2 = new RecentVisitAsset();

		recentVisitAsset2.setAssetId("b42spry4");
		recentVisitAsset2.setAssetTitle("Form Title 3");
		recentVisitAsset2.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset2.setDataSourceId(84756L);
		recentVisitAsset2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -27));
		recentVisitAsset2.setGroupId("67890");
		recentVisitAsset2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitAsset2.setUrl("https://www.beryl.com/forms/form-3");
		recentVisitAsset2.setVisits(3L);

		RecentVisitAsset recentVisitAsset3 = new RecentVisitAsset();

		recentVisitAsset3.setAssetId("c99ytfl7");
		recentVisitAsset3.setAssetTitle("Form Title 4");
		recentVisitAsset3.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset3.setDataSourceId(10293L);
		recentVisitAsset3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentVisitAsset3.setGroupId("12345");
		recentVisitAsset3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentVisitAsset3.setUrl("https://www.beryl.com/forms/form-4");
		recentVisitAsset3.setVisits(3L);

		RecentVisitAsset recentVisitAsset4 = new RecentVisitAsset();

		recentVisitAsset4.setAssetId("a37higg1");
		recentVisitAsset4.setAssetTitle("Form Title 2");
		recentVisitAsset4.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset4.setDataSourceId(84756L);
		recentVisitAsset4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -18));
		recentVisitAsset4.setGroupId("67890");
		recentVisitAsset4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -10));
		recentVisitAsset4.setUrl("https://www.beryl.com/forms/form-2");
		recentVisitAsset4.setVisits(2L);

		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.FORM), null,
			null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"lastVisitDate", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitAsset4, recentVisitAsset3, recentVisitAsset2,
				recentVisitAsset1),
			recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.FORM), null,
			null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"lastVisitDate", "desc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitAsset1, recentVisitAsset2, recentVisitAsset3,
				recentVisitAsset4),
			recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentFormsSortByVisits() {
		RecentVisitAsset recentVisitAsset1 = new RecentVisitAsset();

		recentVisitAsset1.setAssetId("e242gdef");
		recentVisitAsset1.setAssetTitle("Form Title 1");
		recentVisitAsset1.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset1.setDataSourceId(10293L);
		recentVisitAsset1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentVisitAsset1.setGroupId("12345");
		recentVisitAsset1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentVisitAsset1.setUrl("https://www.beryl.com/forms/form-1");
		recentVisitAsset1.setVisits(4L);

		RecentVisitAsset recentVisitAsset2 = new RecentVisitAsset();

		recentVisitAsset2.setAssetId("b42spry4");
		recentVisitAsset2.setAssetTitle("Form Title 3");
		recentVisitAsset2.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset2.setDataSourceId(84756L);
		recentVisitAsset2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -27));
		recentVisitAsset2.setGroupId("67890");
		recentVisitAsset2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitAsset2.setUrl("https://www.beryl.com/forms/form-3");
		recentVisitAsset2.setVisits(3L);

		RecentVisitAsset recentVisitAsset3 = new RecentVisitAsset();

		recentVisitAsset3.setAssetId("c99ytfl7");
		recentVisitAsset3.setAssetTitle("Form Title 4");
		recentVisitAsset3.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset3.setDataSourceId(10293L);
		recentVisitAsset3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentVisitAsset3.setGroupId("12345");
		recentVisitAsset3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentVisitAsset3.setUrl("https://www.beryl.com/forms/form-4");
		recentVisitAsset3.setVisits(3L);

		RecentVisitAsset recentVisitAsset4 = new RecentVisitAsset();

		recentVisitAsset4.setAssetId("a37higg1");
		recentVisitAsset4.setAssetTitle("Form Title 2");
		recentVisitAsset4.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset4.setDataSourceId(84756L);
		recentVisitAsset4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -18));
		recentVisitAsset4.setGroupId("67890");
		recentVisitAsset4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -10));
		recentVisitAsset4.setUrl("https://www.beryl.com/forms/form-2");
		recentVisitAsset4.setVisits(2L);

		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.FORM), null,
			null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"visits", "asc", "assetTitle", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitAsset4, recentVisitAsset2, recentVisitAsset3,
				recentVisitAsset1),
			recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.FORM), null,
			null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"visits", "desc", "assetTitle", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitAsset1, recentVisitAsset2, recentVisitAsset3,
				recentVisitAsset4),
			recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentFormsWithDataSourceId() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.FORM),
			84756L, null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[0], TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(2, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> expectedRecentVisitAssets = new ArrayList<>();

		RecentVisitAsset recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("b42spry4");
		recentVisitAsset.setAssetTitle("Form Title 3");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -27));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitAsset.setUrl("https://www.beryl.com/forms/form-3");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("a37higg1");
		recentVisitAsset.setAssetTitle("Form Title 2");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -18));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -10));
		recentVisitAsset.setUrl("https://www.beryl.com/forms/form-2");
		recentVisitAsset.setVisits(2L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		Assertions.assertEquals(
			expectedRecentVisitAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentFormsWithGroupId() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.FORM), null,
			"12345",
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[0], TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(2, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> expectedRecentVisitAssets = new ArrayList<>();

		RecentVisitAsset recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("e242gdef");
		recentVisitAsset.setAssetTitle("Form Title 1");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentVisitAsset.setUrl("https://www.beryl.com/forms/form-1");
		recentVisitAsset.setVisits(4L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("c99ytfl7");
		recentVisitAsset.setAssetTitle("Form Title 4");
		recentVisitAsset.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentVisitAsset.setUrl("https://www.beryl.com/forms/form-4");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		Assertions.assertEquals(
			expectedRecentVisitAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentFormsWithPagination() {
		RecentVisitAsset recentVisitAsset1 = new RecentVisitAsset();

		recentVisitAsset1.setAssetId("e242gdef");
		recentVisitAsset1.setAssetTitle("Form Title 1");
		recentVisitAsset1.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset1.setDataSourceId(10293L);
		recentVisitAsset1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentVisitAsset1.setGroupId("12345");
		recentVisitAsset1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentVisitAsset1.setUrl("https://www.beryl.com/forms/form-1");
		recentVisitAsset1.setVisits(4L);

		RecentVisitAsset recentVisitAsset2 = new RecentVisitAsset();

		recentVisitAsset2.setAssetId("b42spry4");
		recentVisitAsset2.setAssetTitle("Form Title 3");
		recentVisitAsset2.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset2.setDataSourceId(84756L);
		recentVisitAsset2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -27));
		recentVisitAsset2.setGroupId("67890");
		recentVisitAsset2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitAsset2.setUrl("https://www.beryl.com/forms/form-3");
		recentVisitAsset2.setVisits(3L);

		RecentVisitAsset recentVisitAsset3 = new RecentVisitAsset();

		recentVisitAsset3.setAssetId("c99ytfl7");
		recentVisitAsset3.setAssetTitle("Form Title 4");
		recentVisitAsset3.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset3.setDataSourceId(10293L);
		recentVisitAsset3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentVisitAsset3.setGroupId("12345");
		recentVisitAsset3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentVisitAsset3.setUrl("https://www.beryl.com/forms/form-4");
		recentVisitAsset3.setVisits(3L);

		RecentVisitAsset recentVisitAsset4 = new RecentVisitAsset();

		recentVisitAsset4.setAssetId("a37higg1");
		recentVisitAsset4.setAssetTitle("Form Title 2");
		recentVisitAsset4.setContentType(RecentVisitAsset.ContentType.FORM);
		recentVisitAsset4.setDataSourceId(84756L);
		recentVisitAsset4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -18));
		recentVisitAsset4.setGroupId("67890");
		recentVisitAsset4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -10));
		recentVisitAsset4.setUrl("https://www.beryl.com/forms/form-2");
		recentVisitAsset4.setVisits(2L);

		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.FORM), null,
			null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 3, new String[] {"firstVisitDate", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitAsset1, recentVisitAsset3, recentVisitAsset2),
			recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.FORM), null,
			null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			1, 3, new String[] {"firstVisitDate", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Collections.singletonList(recentVisitAsset4),
			recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentFormsWithSuppression() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.FORM), null,
			null,
			"8bb3cd4319c4cc4df1addc31cb0fae500288133b91228a1cacb4ff2802446220",
			0, 10, new String[0], TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(0L, recentAssetPage.getTotalElements());
		Assertions.assertEquals(0L, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> recentVisitAssets = recentAssetPage.getContent();

		Assertions.assertTrue(recentVisitAssets.isEmpty());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_yesterday_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentFormsYesterday() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.FORM), null,
			null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 10, new String[0], TimeRange.YESTERDAY);

		Assertions.assertEquals(3, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> recentVisitAssets = recentAssetPage.getContent();

		Assertions.assertFalse(recentVisitAssets.isEmpty());

		Map<Pair<String, String>, Long> expectedRecentAssetCounts =
			new HashMap<Pair<String, String>, Long>() {
				{
					put(Pair.of("e242gdef", "Form Title 1"), 10L);
					put(Pair.of("a37higg1", "Form Title 2"), 2L);
					put(Pair.of("b42spry4", "Form Title 3"), 2L);
				}
			};

		for (RecentVisitAsset recentVisitAsset : recentVisitAssets) {
			Pair<String, String> pair = Pair.of(
				recentVisitAsset.getAssetId(),
				recentVisitAsset.getAssetTitle());

			Assertions.assertEquals(
				expectedRecentAssetCounts.get(pair),
				recentVisitAsset.getVisits());

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
		Page<RecentVisitPage> recentPagePage = _bqEventDog.getRecentPagePage(
			null, null, null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 7, 10, new String[0]);

		Assertions.assertEquals(2, recentPagePage.getTotalElements());
		Assertions.assertEquals(1, recentPagePage.getTotalPages());

		List<RecentVisitPage> recentVisitPages = recentPagePage.getContent();

		Assertions.assertFalse(recentVisitPages.isEmpty());

		List<RecentVisitPage> expectedRecentVisitPages = new ArrayList<>();

		RecentVisitPage recentVisitPage = new RecentVisitPage();

		recentVisitPage.setDataSourceId(84756L);
		recentVisitPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentVisitPage.setDisplayLanguageId("en-US");
		recentVisitPage.setGroupId("67890");
		recentVisitPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentVisitPage.setUrl("https://www.beryl.com/delivery");
		recentVisitPage.setVisits(2L);

		expectedRecentVisitPages.add(recentVisitPage);

		recentVisitPage = new RecentVisitPage();

		recentVisitPage.setDataSourceId(10293L);
		recentVisitPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentVisitPage.setDisplayLanguageId("pt-BR");
		recentVisitPage.setGroupId("12345");
		recentVisitPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentVisitPage.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentVisitPage.setVisits(1L);

		expectedRecentVisitPages.add(recentVisitPage);

		Assertions.assertEquals(
			expectedRecentVisitPages, recentPagePage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq_last_24_hours.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesLast24Hours() {
		Page<RecentVisitPage> recentPagePage = _bqEventDog.getRecentPagePage(
			null, null, null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 0, 10, new String[0]);

		Assertions.assertEquals(4, recentPagePage.getTotalElements());
		Assertions.assertEquals(1, recentPagePage.getTotalPages());

		List<RecentVisitPage> recentVisitPages = recentPagePage.getContent();

		Assertions.assertFalse(recentVisitPages.isEmpty());

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

		for (RecentVisitPage recentVisitPage : recentVisitPages) {
			Pair<String, String> pair = Pair.of(
				recentVisitPage.getURL(),
				recentVisitPage.getDisplayLanguageId());

			Assertions.assertEquals(
				expectedRecentPageCounts.get(pair),
				recentVisitPage.getVisits());

			expectedRecentPageCounts.remove(pair);
		}

		Assertions.assertTrue(expectedRecentPageCounts.isEmpty());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesLast28Days() {
		Page<RecentVisitPage> recentPagePage = _bqEventDog.getRecentPagePage(
			null, null, null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 28, 10, new String[0]);

		Assertions.assertEquals(4, recentPagePage.getTotalElements());
		Assertions.assertEquals(1, recentPagePage.getTotalPages());

		List<RecentVisitPage> expectedRecentVisitPages = new ArrayList<>();

		RecentVisitPage recentVisitPage = new RecentVisitPage();

		recentVisitPage.setDataSourceId(84756L);
		recentVisitPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -23));
		recentVisitPage.setDisplayLanguageId("en-US");
		recentVisitPage.setGroupId("67890");
		recentVisitPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentVisitPage.setUrl("https://www.beryl.com/delivery");
		recentVisitPage.setVisits(4L);

		expectedRecentVisitPages.add(recentVisitPage);

		recentVisitPage = new RecentVisitPage();

		recentVisitPage.setDataSourceId(10293L);
		recentVisitPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentVisitPage.setDisplayLanguageId("pt-BR");
		recentVisitPage.setGroupId("12345");
		recentVisitPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentVisitPage.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentVisitPage.setVisits(4L);

		expectedRecentVisitPages.add(recentVisitPage);

		recentVisitPage = new RecentVisitPage();

		recentVisitPage.setDataSourceId(84756L);
		recentVisitPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -13));
		recentVisitPage.setDisplayLanguageId("pt-BR");
		recentVisitPage.setGroupId("67890");
		recentVisitPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -11));
		recentVisitPage.setUrl("https://www.beryl.com/delivery");
		recentVisitPage.setVisits(2L);

		expectedRecentVisitPages.add(recentVisitPage);

		recentVisitPage = new RecentVisitPage();

		recentVisitPage.setDataSourceId(10293L);
		recentVisitPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -18));
		recentVisitPage.setDisplayLanguageId("en-US");
		recentVisitPage.setGroupId("12345");
		recentVisitPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -16));
		recentVisitPage.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentVisitPage.setVisits(2L);

		expectedRecentVisitPages.add(recentVisitPage);

		Assertions.assertEquals(
			expectedRecentVisitPages, recentPagePage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesLast30Days() {
		Page<RecentVisitPage> recentPagePage = _bqEventDog.getRecentPagePage(
			null, null, null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10, new String[0]);

		Assertions.assertEquals(4, recentPagePage.getTotalElements());
		Assertions.assertEquals(1, recentPagePage.getTotalPages());

		List<RecentVisitPage> expectedRecentVisitPages = new ArrayList<>();

		RecentVisitPage recentVisitPage = new RecentVisitPage();

		recentVisitPage.setDataSourceId(84756L);
		recentVisitPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentVisitPage.setDisplayLanguageId("en-US");
		recentVisitPage.setGroupId("67890");
		recentVisitPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitPage.setUrl("https://www.beryl.com/delivery");
		recentVisitPage.setVisits(4L);

		expectedRecentVisitPages.add(recentVisitPage);

		recentVisitPage = new RecentVisitPage();

		recentVisitPage.setDataSourceId(10293L);
		recentVisitPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -24));
		recentVisitPage.setDisplayLanguageId("pt-BR");
		recentVisitPage.setGroupId("12345");
		recentVisitPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitPage.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentVisitPage.setVisits(4L);

		expectedRecentVisitPages.add(recentVisitPage);

		recentVisitPage = new RecentVisitPage();

		recentVisitPage.setDataSourceId(84756L);
		recentVisitPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -19));
		recentVisitPage.setDisplayLanguageId("pt-BR");
		recentVisitPage.setGroupId("67890");
		recentVisitPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -17));
		recentVisitPage.setUrl("https://www.beryl.com/delivery");
		recentVisitPage.setVisits(2L);

		expectedRecentVisitPages.add(recentVisitPage);

		recentVisitPage = new RecentVisitPage();

		recentVisitPage.setDataSourceId(10293L);
		recentVisitPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -14));
		recentVisitPage.setDisplayLanguageId("en-US");
		recentVisitPage.setGroupId("12345");
		recentVisitPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -12));
		recentVisitPage.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentVisitPage.setVisits(2L);

		expectedRecentVisitPages.add(recentVisitPage);

		Assertions.assertEquals(
			expectedRecentVisitPages, recentPagePage.getContent());
	}

	@BQSQLResource(
		resourcePath = "test_get_recent_pages_multiple_datasource_ids_bq.sql"
	)
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesMultipleDataSourceIds() {
		Page<RecentVisitPage> recentPagePage = _bqEventDog.getRecentPagePage(
			null, null, null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10, new String[0]);

		Assertions.assertEquals(6, recentPagePage.getTotalElements());
		Assertions.assertEquals(1, recentPagePage.getTotalPages());

		List<RecentVisitPage> expectedRecentVisitPages = new ArrayList<>();

		RecentVisitPage recentVisitPage = new RecentVisitPage();

		recentVisitPage.setDataSourceId(84756L);
		recentVisitPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentVisitPage.setDisplayLanguageId("en-US");
		recentVisitPage.setGroupId("67890");
		recentVisitPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitPage.setUrl("https://www.beryl.com/delivery");
		recentVisitPage.setVisits(4L);

		expectedRecentVisitPages.add(recentVisitPage);

		recentVisitPage = new RecentVisitPage();

		recentVisitPage.setDataSourceId(84756L);
		recentVisitPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -19));
		recentVisitPage.setDisplayLanguageId("pt-BR");
		recentVisitPage.setGroupId("67890");
		recentVisitPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -17));
		recentVisitPage.setUrl("https://www.beryl.com/delivery");
		recentVisitPage.setVisits(2L);

		expectedRecentVisitPages.add(recentVisitPage);

		recentVisitPage = new RecentVisitPage();

		recentVisitPage.setDataSourceId(10293L);
		recentVisitPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -22));
		recentVisitPage.setDisplayLanguageId("pt-BR");
		recentVisitPage.setGroupId("12345");
		recentVisitPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitPage.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentVisitPage.setVisits(2L);

		expectedRecentVisitPages.add(recentVisitPage);

		recentVisitPage = new RecentVisitPage();

		recentVisitPage.setDataSourceId(84756L);
		recentVisitPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -24));
		recentVisitPage.setDisplayLanguageId("pt-BR");
		recentVisitPage.setGroupId("12345");
		recentVisitPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -4));
		recentVisitPage.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentVisitPage.setVisits(2L);

		expectedRecentVisitPages.add(recentVisitPage);

		recentVisitPage = new RecentVisitPage();

		recentVisitPage.setDataSourceId(10293L);
		recentVisitPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -14));
		recentVisitPage.setDisplayLanguageId("en-US");
		recentVisitPage.setGroupId("12345");
		recentVisitPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -14));
		recentVisitPage.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentVisitPage.setVisits(1L);

		expectedRecentVisitPages.add(recentVisitPage);

		recentVisitPage = new RecentVisitPage();

		recentVisitPage.setDataSourceId(84756L);
		recentVisitPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -12));
		recentVisitPage.setDisplayLanguageId("en-US");
		recentVisitPage.setGroupId("12345");
		recentVisitPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -12));
		recentVisitPage.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentVisitPage.setVisits(1L);

		expectedRecentVisitPages.add(recentVisitPage);

		Assertions.assertEquals(
			expectedRecentVisitPages, recentPagePage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesSortByDisplayLanguageId() {
		RecentVisitPage recentVisitPage1 = new RecentVisitPage();

		recentVisitPage1.setDataSourceId(84756L);
		recentVisitPage1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentVisitPage1.setDisplayLanguageId("en-US");
		recentVisitPage1.setGroupId("67890");
		recentVisitPage1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitPage1.setUrl("https://www.beryl.com/delivery");
		recentVisitPage1.setVisits(4L);

		RecentVisitPage recentVisitPage2 = new RecentVisitPage();

		recentVisitPage2.setDataSourceId(10293L);
		recentVisitPage2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -24));
		recentVisitPage2.setDisplayLanguageId("pt-BR");
		recentVisitPage2.setGroupId("12345");
		recentVisitPage2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitPage2.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentVisitPage2.setVisits(4L);

		RecentVisitPage recentVisitPage3 = new RecentVisitPage();

		recentVisitPage3.setDataSourceId(10293L);
		recentVisitPage3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -14));
		recentVisitPage3.setDisplayLanguageId("en-US");
		recentVisitPage3.setGroupId("12345");
		recentVisitPage3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -12));
		recentVisitPage3.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentVisitPage3.setVisits(2L);

		RecentVisitPage recentVisitPage4 = new RecentVisitPage();

		recentVisitPage4.setDataSourceId(84756L);
		recentVisitPage4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -19));
		recentVisitPage4.setDisplayLanguageId("pt-BR");
		recentVisitPage4.setGroupId("67890");
		recentVisitPage4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -17));
		recentVisitPage4.setUrl("https://www.beryl.com/delivery");
		recentVisitPage4.setVisits(2L);

		Page<RecentVisitPage> recentPagePage = _bqEventDog.getRecentPagePage(
			null, null, null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10, new String[] {"displayLanguageId", "asc", "url", "asc"});

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitPage1, recentVisitPage3, recentVisitPage4,
				recentVisitPage2),
			recentPagePage.getContent());

		recentPagePage = _bqEventDog.getRecentPagePage(
			null, null, null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10,
			new String[] {"displayLanguageId", "desc", "url", "asc"});

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitPage4, recentVisitPage2, recentVisitPage1,
				recentVisitPage3),
			recentPagePage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesSortByFirstVisitDate() {
		RecentVisitPage recentVisitPage1 = new RecentVisitPage();

		recentVisitPage1.setDataSourceId(84756L);
		recentVisitPage1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentVisitPage1.setDisplayLanguageId("en-US");
		recentVisitPage1.setGroupId("67890");
		recentVisitPage1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitPage1.setUrl("https://www.beryl.com/delivery");
		recentVisitPage1.setVisits(4L);

		RecentVisitPage recentVisitPage2 = new RecentVisitPage();

		recentVisitPage2.setDataSourceId(10293L);
		recentVisitPage2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -24));
		recentVisitPage2.setDisplayLanguageId("pt-BR");
		recentVisitPage2.setGroupId("12345");
		recentVisitPage2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitPage2.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentVisitPage2.setVisits(4L);

		RecentVisitPage recentVisitPage3 = new RecentVisitPage();

		recentVisitPage3.setDataSourceId(10293L);
		recentVisitPage3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -14));
		recentVisitPage3.setDisplayLanguageId("en-US");
		recentVisitPage3.setGroupId("12345");
		recentVisitPage3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -12));
		recentVisitPage3.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentVisitPage3.setVisits(2L);

		RecentVisitPage recentVisitPage4 = new RecentVisitPage();

		recentVisitPage4.setDataSourceId(84756L);
		recentVisitPage4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -19));
		recentVisitPage4.setDisplayLanguageId("pt-BR");
		recentVisitPage4.setGroupId("67890");
		recentVisitPage4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -17));
		recentVisitPage4.setUrl("https://www.beryl.com/delivery");
		recentVisitPage4.setVisits(2L);

		Page<RecentVisitPage> recentPagePage = _bqEventDog.getRecentPagePage(
			null, null, null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10, new String[] {"firstVisitDate", "asc"});

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitPage1, recentVisitPage2, recentVisitPage4,
				recentVisitPage3),
			recentPagePage.getContent());

		recentPagePage = _bqEventDog.getRecentPagePage(
			null, null, null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10, new String[] {"firstVisitDate", "desc"});

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitPage3, recentVisitPage4, recentVisitPage2,
				recentVisitPage1),
			recentPagePage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesSortByLastVisitDate() {
		RecentVisitPage recentVisitPage1 = new RecentVisitPage();

		recentVisitPage1.setDataSourceId(84756L);
		recentVisitPage1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentVisitPage1.setDisplayLanguageId("en-US");
		recentVisitPage1.setGroupId("67890");
		recentVisitPage1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitPage1.setUrl("https://www.beryl.com/delivery");
		recentVisitPage1.setVisits(4L);

		RecentVisitPage recentVisitPage2 = new RecentVisitPage();

		recentVisitPage2.setDataSourceId(10293L);
		recentVisitPage2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -24));
		recentVisitPage2.setDisplayLanguageId("pt-BR");
		recentVisitPage2.setGroupId("12345");
		recentVisitPage2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitPage2.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentVisitPage2.setVisits(4L);

		RecentVisitPage recentVisitPage3 = new RecentVisitPage();

		recentVisitPage3.setDataSourceId(10293L);
		recentVisitPage3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -14));
		recentVisitPage3.setDisplayLanguageId("en-US");
		recentVisitPage3.setGroupId("12345");
		recentVisitPage3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -12));
		recentVisitPage3.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentVisitPage3.setVisits(2L);

		RecentVisitPage recentVisitPage4 = new RecentVisitPage();

		recentVisitPage4.setDataSourceId(84756L);
		recentVisitPage4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -19));
		recentVisitPage4.setDisplayLanguageId("pt-BR");
		recentVisitPage4.setGroupId("67890");
		recentVisitPage4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -17));
		recentVisitPage4.setUrl("https://www.beryl.com/delivery");
		recentVisitPage4.setVisits(2L);

		Page<RecentVisitPage> recentPagePage = _bqEventDog.getRecentPagePage(
			null, null, null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10, new String[] {"lastVisitDate", "asc"});

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitPage4, recentVisitPage3, recentVisitPage1,
				recentVisitPage2),
			recentPagePage.getContent());

		recentPagePage = _bqEventDog.getRecentPagePage(
			null, null, null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10, new String[] {"lastVisitDate", "desc"});

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitPage2, recentVisitPage1, recentVisitPage3,
				recentVisitPage4),
			recentPagePage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesSortByVisits() {
		RecentVisitPage recentVisitPage1 = new RecentVisitPage();

		recentVisitPage1.setDataSourceId(84756L);
		recentVisitPage1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentVisitPage1.setDisplayLanguageId("en-US");
		recentVisitPage1.setGroupId("67890");
		recentVisitPage1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitPage1.setUrl("https://www.beryl.com/delivery");
		recentVisitPage1.setVisits(4L);

		RecentVisitPage recentVisitPage2 = new RecentVisitPage();

		recentVisitPage2.setDataSourceId(10293L);
		recentVisitPage2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -24));
		recentVisitPage2.setDisplayLanguageId("pt-BR");
		recentVisitPage2.setGroupId("12345");
		recentVisitPage2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitPage2.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentVisitPage2.setVisits(4L);

		RecentVisitPage recentVisitPage3 = new RecentVisitPage();

		recentVisitPage3.setDataSourceId(10293L);
		recentVisitPage3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -14));
		recentVisitPage3.setDisplayLanguageId("en-US");
		recentVisitPage3.setGroupId("12345");
		recentVisitPage3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -12));
		recentVisitPage3.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentVisitPage3.setVisits(2L);

		RecentVisitPage recentVisitPage4 = new RecentVisitPage();

		recentVisitPage4.setDataSourceId(84756L);
		recentVisitPage4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -19));
		recentVisitPage4.setDisplayLanguageId("pt-BR");
		recentVisitPage4.setGroupId("67890");
		recentVisitPage4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -17));
		recentVisitPage4.setUrl("https://www.beryl.com/delivery");
		recentVisitPage4.setVisits(2L);

		Page<RecentVisitPage> recentPagePage = _bqEventDog.getRecentPagePage(
			null, null, null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10, new String[] {"visits", "asc", "url", "asc"});

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitPage4, recentVisitPage3, recentVisitPage1,
				recentVisitPage2),
			recentPagePage.getContent());

		recentPagePage = _bqEventDog.getRecentPagePage(
			null, null, null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10, new String[] {"visits", "desc", "url", "asc"});

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitPage1, recentVisitPage2, recentVisitPage4,
				recentVisitPage3),
			recentPagePage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesWithDataSourceId() {
		Page<RecentVisitPage> recentPagePage = _bqEventDog.getRecentPagePage(
			10293L, null, null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10, new String[0]);

		Assertions.assertEquals(2, recentPagePage.getTotalElements());
		Assertions.assertEquals(1, recentPagePage.getTotalPages());

		List<RecentVisitPage> expectedRecentVisitPages = new ArrayList<>();

		RecentVisitPage recentVisitPage = new RecentVisitPage();

		recentVisitPage.setDataSourceId(10293L);
		recentVisitPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -24));
		recentVisitPage.setDisplayLanguageId("pt-BR");
		recentVisitPage.setGroupId("12345");
		recentVisitPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitPage.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentVisitPage.setVisits(4L);

		expectedRecentVisitPages.add(recentVisitPage);

		recentVisitPage = new RecentVisitPage();

		recentVisitPage.setDataSourceId(10293L);
		recentVisitPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -14));
		recentVisitPage.setDisplayLanguageId("en-US");
		recentVisitPage.setGroupId("12345");
		recentVisitPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -12));
		recentVisitPage.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentVisitPage.setVisits(2L);

		expectedRecentVisitPages.add(recentVisitPage);

		Assertions.assertEquals(
			expectedRecentVisitPages, recentPagePage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesWithDisplayLanguageId() {
		List<RecentVisitPage> expectedRecentVisitPages = new ArrayList<>();

		RecentVisitPage recentVisitPage = new RecentVisitPage();

		recentVisitPage.setDataSourceId(84756L);
		recentVisitPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentVisitPage.setDisplayLanguageId("en-US");
		recentVisitPage.setGroupId("67890");
		recentVisitPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitPage.setUrl("https://www.beryl.com/delivery");
		recentVisitPage.setVisits(4L);

		expectedRecentVisitPages.add(recentVisitPage);

		recentVisitPage = new RecentVisitPage();

		recentVisitPage.setDataSourceId(10293L);
		recentVisitPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -14));
		recentVisitPage.setDisplayLanguageId("en-US");
		recentVisitPage.setGroupId("12345");
		recentVisitPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -12));
		recentVisitPage.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentVisitPage.setVisits(2L);

		expectedRecentVisitPages.add(recentVisitPage);

		Page<RecentVisitPage> recentPagePage = _bqEventDog.getRecentPagePage(
			null, "en-US", null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10, new String[0]);

		Assertions.assertEquals(
			expectedRecentVisitPages, recentPagePage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesWithGroupId() {
		List<RecentVisitPage> expectedRecentVisitPages = new ArrayList<>();

		RecentVisitPage recentVisitPage = new RecentVisitPage();

		recentVisitPage.setDataSourceId(84756L);
		recentVisitPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentVisitPage.setDisplayLanguageId("en-US");
		recentVisitPage.setGroupId("67890");
		recentVisitPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitPage.setUrl("https://www.beryl.com/delivery");
		recentVisitPage.setVisits(4L);

		expectedRecentVisitPages.add(recentVisitPage);

		recentVisitPage = new RecentVisitPage();

		recentVisitPage.setDataSourceId(84756L);
		recentVisitPage.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -19));
		recentVisitPage.setDisplayLanguageId("pt-BR");
		recentVisitPage.setGroupId("67890");
		recentVisitPage.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -17));
		recentVisitPage.setUrl("https://www.beryl.com/delivery");
		recentVisitPage.setVisits(2L);

		expectedRecentVisitPages.add(recentVisitPage);

		Page<RecentVisitPage> recentPagePage = _bqEventDog.getRecentPagePage(
			null, null, "67890",
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10, new String[0]);

		Assertions.assertEquals(
			expectedRecentVisitPages, recentPagePage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesWithPagination() {
		RecentVisitPage recentVisitPage1 = new RecentVisitPage();

		recentVisitPage1.setDataSourceId(84756L);
		recentVisitPage1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentVisitPage1.setDisplayLanguageId("en-US");
		recentVisitPage1.setGroupId("67890");
		recentVisitPage1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitPage1.setUrl("https://www.beryl.com/delivery");
		recentVisitPage1.setVisits(4L);

		RecentVisitPage recentVisitPage2 = new RecentVisitPage();

		recentVisitPage2.setDataSourceId(10293L);
		recentVisitPage2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -24));
		recentVisitPage2.setDisplayLanguageId("pt-BR");
		recentVisitPage2.setGroupId("12345");
		recentVisitPage2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitPage2.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentVisitPage2.setVisits(4L);

		RecentVisitPage recentVisitPage3 = new RecentVisitPage();

		recentVisitPage3.setDataSourceId(10293L);
		recentVisitPage3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -14));
		recentVisitPage3.setDisplayLanguageId("en-US");
		recentVisitPage3.setGroupId("12345");
		recentVisitPage3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -12));
		recentVisitPage3.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");
		recentVisitPage3.setVisits(2L);

		RecentVisitPage recentVisitPage4 = new RecentVisitPage();

		recentVisitPage4.setDataSourceId(84756L);
		recentVisitPage4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -19));
		recentVisitPage4.setDisplayLanguageId("pt-BR");
		recentVisitPage4.setGroupId("67890");
		recentVisitPage4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -17));
		recentVisitPage4.setUrl("https://www.beryl.com/delivery");
		recentVisitPage4.setVisits(2L);

		Page<RecentVisitPage> recentPagePage = _bqEventDog.getRecentPagePage(
			null, null, null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 2, new String[] {"visits", "desc", "url", "asc"});

		Assertions.assertEquals(
			Arrays.asList(recentVisitPage1, recentVisitPage2),
			recentPagePage.getContent());

		recentPagePage = _bqEventDog.getRecentPagePage(
			null, null, null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			1, 30, 2, new String[] {"visits", "desc", "url", "asc"});

		Assertions.assertEquals(
			Arrays.asList(recentVisitPage4, recentVisitPage3),
			recentPagePage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesWithSuppression() {
		Page<RecentVisitPage> recentPagePage = _bqEventDog.getRecentPagePage(
			null, null, null,
			"8bb3cd4319c4cc4df1addc31cb0fae500288133b91228a1cacb4ff2802446220",
			0, 30, 10, new String[] {"counts", "desc", "url", "asc"});

		Assertions.assertEquals(0L, recentPagePage.getTotalElements());
		Assertions.assertEquals(0L, recentPagePage.getTotalPages());

		List<RecentVisitPage> recentVisitPages = recentPagePage.getContent();

		Assertions.assertTrue(recentVisitPages.isEmpty());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_yesterday_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesYesterday() {
		Page<RecentVisitPage> recentPagePage = _bqEventDog.getRecentPagePage(
			null, null, null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 1, 10, new String[0]);

		Assertions.assertEquals(4, recentPagePage.getTotalElements());
		Assertions.assertEquals(1, recentPagePage.getTotalPages());

		List<RecentVisitPage> recentVisitPages = recentPagePage.getContent();

		Assertions.assertFalse(recentVisitPages.isEmpty());

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

		for (RecentVisitPage recentVisitPage : recentVisitPages) {
			Pair<String, String> pair = Pair.of(
				recentVisitPage.getURL(),
				recentVisitPage.getDisplayLanguageId());

			Assertions.assertEquals(
				expectedRecentPageCounts.get(pair),
				recentVisitPage.getVisits());

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
			"United States", DateUtil.newDate(), 10293L, "", "",
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
			"United States", DateUtil.newDate(), 84756L, "", "",
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
			"United States", DateUtil.newDate(), 84756L, "", "",
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
			"United States", DateUtil.newDate(), 84756L, "", "",
			DigestUtils.sha256Hex("test2@liferay.com"), DateUtil.newDate(),
			"pageViewed", "", "analyticsEventId4", "", "en_US", "", "", "", "",
			"", "", "", "http://localhost:8080/search?q=Diamond+Bar",
			"123123-sadf-32423-234afsd", "");

		Page<RecentVisitSite> recentSitePage = _bqEventDog.getRecentSitePage(
			null, DigestUtils.sha256Hex("test2@liferay.com"), 0, 5,
			new String[] {"visits", "desc"}, TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(1, recentSitePage.getTotalElements());

		List<RecentVisitSite> recentVisitSites = recentSitePage.getContent();

		Assertions.assertEquals(1, recentVisitSites.size());

		RecentVisitSite[] recentVisitSitesArray = recentVisitSites.toArray(
			new RecentVisitSite[0]);

		RecentVisitSite recentVisitSite = recentVisitSitesArray[0];

		Assertions.assertEquals(84756L, recentVisitSite.getDataSourceId());
		Assertions.assertEquals("3212", recentVisitSite.getGroupId());
		Assertions.assertEquals(1, recentVisitSite.getVisits());

		recentSitePage = _bqEventDog.getRecentSitePage(
			null, DigestUtils.sha256Hex("test@liferay.com"), 0, 5,
			new String[] {"visits", "desc"}, TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(2, recentSitePage.getTotalElements());

		recentVisitSites = recentSitePage.getContent();

		Assertions.assertEquals(2, recentVisitSites.size());

		recentVisitSitesArray = recentVisitSites.toArray(
			new RecentVisitSite[0]);

		recentVisitSite = recentVisitSitesArray[0];

		Assertions.assertEquals(84756L, recentVisitSite.getDataSourceId());
		Assertions.assertEquals("3212", recentVisitSite.getGroupId());
		Assertions.assertEquals(2, recentVisitSite.getVisits());

		recentVisitSite = recentVisitSitesArray[1];

		Assertions.assertEquals(10293L, recentVisitSite.getDataSourceId());
		Assertions.assertEquals("3213", recentVisitSite.getGroupId());
		Assertions.assertEquals(1, recentVisitSite.getVisits());
	}

	@BQSQLResource(
		resourcePath = "test_get_recent_pages_multiple_datasource_ids_bq.sql"
	)
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentSitesMultipleDataSourceIds() {
		Page<RecentVisitSite> recentSitePage = _bqEventDog.getRecentSitePage(
			null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 5, new String[] {"visits", "desc"}, TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(3, recentSitePage.getTotalElements());
		Assertions.assertEquals(1, recentSitePage.getTotalPages());

		List<RecentVisitSite> expectedRecentVisitSites = new ArrayList<>();

		RecentVisitSite recentVisitSite = new RecentVisitSite();

		recentVisitSite.setDataSourceId(84756L);
		recentVisitSite.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -23));
		recentVisitSite.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentVisitSite.setGroupId("67890");
		recentVisitSite.setVisits(6L);

		expectedRecentVisitSites.add(recentVisitSite);

		recentVisitSite = new RecentVisitSite();

		recentVisitSite.setDataSourceId(10293L);
		recentVisitSite.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -26));
		recentVisitSite.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentVisitSite.setGroupId("12345");
		recentVisitSite.setVisits(3L);

		expectedRecentVisitSites.add(recentVisitSite);

		recentVisitSite = new RecentVisitSite();

		recentVisitSite.setDataSourceId(84756L);
		recentVisitSite.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentVisitSite.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentVisitSite.setGroupId("12345");
		recentVisitSite.setVisits(3L);

		expectedRecentVisitSites.add(recentVisitSite);

		Assertions.assertEquals(
			expectedRecentVisitSites, recentSitePage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentSitesWithDataSourceId() {
		Page<RecentVisitSite> recentSitePage = _bqEventDog.getRecentSitePage(
			10293L,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 5, new String[] {"visits", "desc"}, TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(1, recentSitePage.getTotalElements());
		Assertions.assertEquals(1, recentSitePage.getTotalPages());

		RecentVisitSite recentVisitSite = new RecentVisitSite();

		recentVisitSite.setDataSourceId(10293L);
		recentVisitSite.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentVisitSite.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentVisitSite.setGroupId("12345");
		recentVisitSite.setVisits(6L);

		Assertions.assertEquals(
			Collections.singletonList(recentVisitSite),
			recentSitePage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentWebContentLast7Days() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.WEBCONTENT),
			null, null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 10, new String[0], TimeRange.LAST_7_DAYS);

		Assertions.assertEquals(3, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> expectedRecentVisitAssets = new ArrayList<>();

		RecentVisitAsset recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("a73ihsy9");
		recentVisitAsset.setAssetTitle("WebContent Title 2");
		recentVisitAsset.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitAsset.setUrl("https://www.beryl.com/journals/journal-2");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("b73ihsy9");
		recentVisitAsset.setAssetTitle("WebContent Title 3");
		recentVisitAsset.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitAsset.setUrl("https://www.beryl.com/journals/journal-3");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("c73ihsy9");
		recentVisitAsset.setAssetTitle("WebContent Title 4");
		recentVisitAsset.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -4));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -4));
		recentVisitAsset.setUrl("https://www.beryl.com/journals/journal-4");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		Assertions.assertEquals(
			expectedRecentVisitAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_last_24_hours_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentWebContentLast24Hours() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.WEBCONTENT),
			null, null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[0], TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(3, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> recentVisitAssets = recentAssetPage.getContent();

		Assertions.assertFalse(recentVisitAssets.isEmpty());

		Map<Pair<String, String>, Long> expectedRecentAssetCounts =
			new HashMap<Pair<String, String>, Long>() {
				{
					put(Pair.of("e131fabc", "WebContent Title 1"), 10L);
					put(Pair.of("a73ihsy9", "WebContent Title 2"), 3L);
					put(Pair.of("b73ihsy9", "WebContent Title 3"), 3L);
				}
			};

		for (RecentVisitAsset recentVisitAsset : recentVisitAssets) {
			Pair<String, String> pair = Pair.of(
				recentVisitAsset.getAssetId(),
				recentVisitAsset.getAssetTitle());

			Assertions.assertEquals(
				expectedRecentAssetCounts.get(pair),
				recentVisitAsset.getVisits());

			expectedRecentAssetCounts.remove(pair);
		}

		Assertions.assertTrue(expectedRecentAssetCounts.isEmpty());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentWebContentLast28Days() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.WEBCONTENT),
			null, null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[0], TimeRange.LAST_28_DAYS);

		Assertions.assertEquals(4, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> expectedRecentVisitAssets = new ArrayList<>();

		RecentVisitAsset recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("a73ihsy9");
		recentVisitAsset.setAssetTitle("WebContent Title 2");
		recentVisitAsset.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -26));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentVisitAsset.setUrl("https://www.beryl.com/journals/journal-2");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("b73ihsy9");
		recentVisitAsset.setAssetTitle("WebContent Title 3");
		recentVisitAsset.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -23));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentVisitAsset.setUrl("https://www.beryl.com/journals/journal-3");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("c73ihsy9");
		recentVisitAsset.setAssetTitle("WebContent Title 4");
		recentVisitAsset.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentVisitAsset.setUrl("https://www.beryl.com/journals/journal-4");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("e131fabc");
		recentVisitAsset.setAssetTitle("WebContent Title 1");
		recentVisitAsset.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentVisitAsset.setUrl("https://www.beryl.com/journals/journal-1");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		Assertions.assertEquals(
			expectedRecentVisitAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentWebContentLast30Days() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.WEBCONTENT),
			null, null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 10, new String[0], TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(4, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> expectedRecentVisitAssets = new ArrayList<>();

		RecentVisitAsset recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("a73ihsy9");
		recentVisitAsset.setAssetTitle("WebContent Title 2");
		recentVisitAsset.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -22));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitAsset.setUrl("https://www.beryl.com/journals/journal-2");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("b73ihsy9");
		recentVisitAsset.setAssetTitle("WebContent Title 3");
		recentVisitAsset.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -27));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitAsset.setUrl("https://www.beryl.com/journals/journal-3");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("c73ihsy9");
		recentVisitAsset.setAssetTitle("WebContent Title 4");
		recentVisitAsset.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -24));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -4));
		recentVisitAsset.setUrl("https://www.beryl.com/journals/journal-4");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("e131fabc");
		recentVisitAsset.setAssetTitle("WebContent Title 1");
		recentVisitAsset.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -9));
		recentVisitAsset.setUrl("https://www.beryl.com/journals/journal-1");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		Assertions.assertEquals(
			expectedRecentVisitAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(
		resourcePath = "test_get_recent_assets_multiple_datasource_ids_bq.sql"
	)
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentWebContentMultipleDataSourceIds() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.WEBCONTENT),
			null, null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 10, new String[0], TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(6, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> expectedRecentVisitAssets = new ArrayList<>();

		RecentVisitAsset recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("c73ihsy9");
		recentVisitAsset.setAssetTitle("WebContent Title 4");
		recentVisitAsset.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -24));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -4));
		recentVisitAsset.setUrl("https://www.beryl.com/journals/journal-4");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("e131fabc");
		recentVisitAsset.setAssetTitle("WebContent Title 1");
		recentVisitAsset.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -9));
		recentVisitAsset.setUrl("https://www.beryl.com/journals/journal-1");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("a73ihsy9");
		recentVisitAsset.setAssetTitle("WebContent Title 2");
		recentVisitAsset.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -22));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -14));
		recentVisitAsset.setUrl("https://www.beryl.com/journals/journal-2");
		recentVisitAsset.setVisits(2L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("b73ihsy9");
		recentVisitAsset.setAssetTitle("WebContent Title 3");
		recentVisitAsset.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -27));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -19));
		recentVisitAsset.setUrl("https://www.beryl.com/journals/journal-3");
		recentVisitAsset.setVisits(2L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("a73ihsy9");
		recentVisitAsset.setAssetTitle("WebContent Title 2");
		recentVisitAsset.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitAsset.setUrl("https://www.beryl.com/journals/journal-2");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("b73ihsy9");
		recentVisitAsset.setAssetTitle("WebContent Title 3");
		recentVisitAsset.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitAsset.setUrl("https://www.beryl.com/journals/journal-3");
		recentVisitAsset.setVisits(1L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		Assertions.assertEquals(
			expectedRecentVisitAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentWebContentSortByFirstVisitDate() {
		RecentVisitAsset recentVisitAsset1 = new RecentVisitAsset();

		recentVisitAsset1.setAssetId("a73ihsy9");
		recentVisitAsset1.setAssetTitle("WebContent Title 2");
		recentVisitAsset1.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset1.setDataSourceId(10293L);
		recentVisitAsset1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -26));
		recentVisitAsset1.setGroupId("12345");
		recentVisitAsset1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentVisitAsset1.setUrl("https://www.beryl.com/journals/journal-2");
		recentVisitAsset1.setVisits(3L);

		RecentVisitAsset recentVisitAsset2 = new RecentVisitAsset();

		recentVisitAsset2.setAssetId("b73ihsy9");
		recentVisitAsset2.setAssetTitle("WebContent Title 3");
		recentVisitAsset2.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset2.setDataSourceId(10293L);
		recentVisitAsset2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -30));
		recentVisitAsset2.setGroupId("12345");
		recentVisitAsset2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentVisitAsset2.setUrl("https://www.beryl.com/journals/journal-3");
		recentVisitAsset2.setVisits(4L);

		RecentVisitAsset recentVisitAsset3 = new RecentVisitAsset();

		recentVisitAsset3.setAssetId("c73ihsy9");
		recentVisitAsset3.setAssetTitle("WebContent Title 4");
		recentVisitAsset3.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset3.setDataSourceId(84756L);
		recentVisitAsset3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentVisitAsset3.setGroupId("67890");
		recentVisitAsset3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentVisitAsset3.setUrl("https://www.beryl.com/journals/journal-4");
		recentVisitAsset3.setVisits(3L);

		RecentVisitAsset recentVisitAsset4 = new RecentVisitAsset();

		recentVisitAsset4.setAssetId("e131fabc");
		recentVisitAsset4.setAssetTitle("WebContent Title 1");
		recentVisitAsset4.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset4.setDataSourceId(84756L);
		recentVisitAsset4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentVisitAsset4.setGroupId("67890");
		recentVisitAsset4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentVisitAsset4.setUrl("https://www.beryl.com/journals/journal-1");
		recentVisitAsset4.setVisits(3L);

		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.WEBCONTENT),
			null, null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"firstVisitDate", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitAsset2, recentVisitAsset3, recentVisitAsset1,
				recentVisitAsset4),
			recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.WEBCONTENT),
			null, null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"firstVisitDate", "desc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitAsset4, recentVisitAsset1, recentVisitAsset3,
				recentVisitAsset2),
			recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentWebContentSortByLastVisitDate() {
		RecentVisitAsset recentVisitAsset1 = new RecentVisitAsset();

		recentVisitAsset1.setAssetId("a73ihsy9");
		recentVisitAsset1.setAssetTitle("WebContent Title 2");
		recentVisitAsset1.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset1.setDataSourceId(10293L);
		recentVisitAsset1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -26));
		recentVisitAsset1.setGroupId("12345");
		recentVisitAsset1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentVisitAsset1.setUrl("https://www.beryl.com/journals/journal-2");
		recentVisitAsset1.setVisits(3L);

		RecentVisitAsset recentVisitAsset2 = new RecentVisitAsset();

		recentVisitAsset2.setAssetId("b73ihsy9");
		recentVisitAsset2.setAssetTitle("WebContent Title 3");
		recentVisitAsset2.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset2.setDataSourceId(10293L);
		recentVisitAsset2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -30));
		recentVisitAsset2.setGroupId("12345");
		recentVisitAsset2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentVisitAsset2.setUrl("https://www.beryl.com/journals/journal-3");
		recentVisitAsset2.setVisits(4L);

		RecentVisitAsset recentVisitAsset3 = new RecentVisitAsset();

		recentVisitAsset3.setAssetId("c73ihsy9");
		recentVisitAsset3.setAssetTitle("WebContent Title 4");
		recentVisitAsset3.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset3.setDataSourceId(84756L);
		recentVisitAsset3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentVisitAsset3.setGroupId("67890");
		recentVisitAsset3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentVisitAsset3.setUrl("https://www.beryl.com/journals/journal-4");
		recentVisitAsset3.setVisits(3L);

		RecentVisitAsset recentVisitAsset4 = new RecentVisitAsset();

		recentVisitAsset4.setAssetId("e131fabc");
		recentVisitAsset4.setAssetTitle("WebContent Title 1");
		recentVisitAsset4.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset4.setDataSourceId(84756L);
		recentVisitAsset4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentVisitAsset4.setGroupId("67890");
		recentVisitAsset4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentVisitAsset4.setUrl("https://www.beryl.com/journals/journal-1");
		recentVisitAsset4.setVisits(3L);

		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.WEBCONTENT),
			null, null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"lastVisitDate", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitAsset3, recentVisitAsset1, recentVisitAsset2,
				recentVisitAsset4),
			recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.WEBCONTENT),
			null, null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"lastVisitDate", "desc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitAsset4, recentVisitAsset2, recentVisitAsset1,
				recentVisitAsset3),
			recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentWebContentSortByVisits() {
		RecentVisitAsset recentVisitAsset1 = new RecentVisitAsset();

		recentVisitAsset1.setAssetId("a73ihsy9");
		recentVisitAsset1.setAssetTitle("WebContent Title 2");
		recentVisitAsset1.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset1.setDataSourceId(10293L);
		recentVisitAsset1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -26));
		recentVisitAsset1.setGroupId("12345");
		recentVisitAsset1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentVisitAsset1.setUrl("https://www.beryl.com/journals/journal-2");
		recentVisitAsset1.setVisits(3L);

		RecentVisitAsset recentVisitAsset2 = new RecentVisitAsset();

		recentVisitAsset2.setAssetId("b73ihsy9");
		recentVisitAsset2.setAssetTitle("WebContent Title 3");
		recentVisitAsset2.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset2.setDataSourceId(10293L);
		recentVisitAsset2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -30));
		recentVisitAsset2.setGroupId("12345");
		recentVisitAsset2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentVisitAsset2.setUrl("https://www.beryl.com/journals/journal-3");
		recentVisitAsset2.setVisits(4L);

		RecentVisitAsset recentVisitAsset3 = new RecentVisitAsset();

		recentVisitAsset3.setAssetId("c73ihsy9");
		recentVisitAsset3.setAssetTitle("WebContent Title 4");
		recentVisitAsset3.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset3.setDataSourceId(84756L);
		recentVisitAsset3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentVisitAsset3.setGroupId("67890");
		recentVisitAsset3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentVisitAsset3.setUrl("https://www.beryl.com/journals/journal-4");
		recentVisitAsset3.setVisits(3L);

		RecentVisitAsset recentVisitAsset4 = new RecentVisitAsset();

		recentVisitAsset4.setAssetId("e131fabc");
		recentVisitAsset4.setAssetTitle("WebContent Title 1");
		recentVisitAsset4.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset4.setDataSourceId(84756L);
		recentVisitAsset4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentVisitAsset4.setGroupId("67890");
		recentVisitAsset4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentVisitAsset4.setUrl("https://www.beryl.com/journals/journal-1");
		recentVisitAsset4.setVisits(3L);

		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.WEBCONTENT),
			null, null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"visits", "asc", "assetTitle", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitAsset4, recentVisitAsset1, recentVisitAsset3,
				recentVisitAsset2),
			recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.WEBCONTENT),
			null, null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 10, new String[] {"visits", "desc", "assetTitle", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitAsset2, recentVisitAsset4, recentVisitAsset1,
				recentVisitAsset3),
			recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentWebContentWithDataSourceId() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.WEBCONTENT),
			84756L, null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 10, new String[0], TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(2, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> expectedRecentVisitAssets = new ArrayList<>();

		RecentVisitAsset recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("c73ihsy9");
		recentVisitAsset.setAssetTitle("WebContent Title 4");
		recentVisitAsset.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -24));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -4));
		recentVisitAsset.setUrl("https://www.beryl.com/journals/journal-4");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("e131fabc");
		recentVisitAsset.setAssetTitle("WebContent Title 1");
		recentVisitAsset.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset.setDataSourceId(84756L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentVisitAsset.setGroupId("67890");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -9));
		recentVisitAsset.setUrl("https://www.beryl.com/journals/journal-1");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		Assertions.assertEquals(
			expectedRecentVisitAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentWebContentWithGroupId() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.WEBCONTENT),
			null, "12345",
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 10, new String[0], TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(2, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> expectedRecentVisitAssets = new ArrayList<>();

		RecentVisitAsset recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("a73ihsy9");
		recentVisitAsset.setAssetTitle("WebContent Title 2");
		recentVisitAsset.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -22));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentVisitAsset.setUrl("https://www.beryl.com/journals/journal-2");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		recentVisitAsset = new RecentVisitAsset();

		recentVisitAsset.setAssetId("b73ihsy9");
		recentVisitAsset.setAssetTitle("WebContent Title 3");
		recentVisitAsset.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset.setDataSourceId(10293L);
		recentVisitAsset.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -27));
		recentVisitAsset.setGroupId("12345");
		recentVisitAsset.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentVisitAsset.setUrl("https://www.beryl.com/journals/journal-3");
		recentVisitAsset.setVisits(3L);

		expectedRecentVisitAssets.add(recentVisitAsset);

		Assertions.assertEquals(
			expectedRecentVisitAssets, recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentWebContentWithPagination() {
		RecentVisitAsset recentVisitAsset1 = new RecentVisitAsset();

		recentVisitAsset1.setAssetId("a73ihsy9");
		recentVisitAsset1.setAssetTitle("WebContent Title 2");
		recentVisitAsset1.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset1.setDataSourceId(10293L);
		recentVisitAsset1.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -26));
		recentVisitAsset1.setGroupId("12345");
		recentVisitAsset1.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentVisitAsset1.setUrl("https://www.beryl.com/journals/journal-2");
		recentVisitAsset1.setVisits(3L);

		RecentVisitAsset recentVisitAsset2 = new RecentVisitAsset();

		recentVisitAsset2.setAssetId("b73ihsy9");
		recentVisitAsset2.setAssetTitle("WebContent Title 3");
		recentVisitAsset2.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset2.setDataSourceId(10293L);
		recentVisitAsset2.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -30));
		recentVisitAsset2.setGroupId("12345");
		recentVisitAsset2.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentVisitAsset2.setUrl("https://www.beryl.com/journals/journal-3");
		recentVisitAsset2.setVisits(4L);

		RecentVisitAsset recentVisitAsset3 = new RecentVisitAsset();

		recentVisitAsset3.setAssetId("c73ihsy9");
		recentVisitAsset3.setAssetTitle("WebContent Title 4");
		recentVisitAsset3.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset3.setDataSourceId(84756L);
		recentVisitAsset3.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentVisitAsset3.setGroupId("67890");
		recentVisitAsset3.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -8));
		recentVisitAsset3.setUrl("https://www.beryl.com/journals/journal-4");
		recentVisitAsset3.setVisits(3L);

		RecentVisitAsset recentVisitAsset4 = new RecentVisitAsset();

		recentVisitAsset4.setAssetId("e131fabc");
		recentVisitAsset4.setAssetTitle("WebContent Title 1");
		recentVisitAsset4.setContentType(
			RecentVisitAsset.ContentType.WEBCONTENT);
		recentVisitAsset4.setDataSourceId(84756L);
		recentVisitAsset4.setFirstVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -21));
		recentVisitAsset4.setGroupId("67890");
		recentVisitAsset4.setLastVisitDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentVisitAsset4.setUrl("https://www.beryl.com/journals/journal-1");
		recentVisitAsset4.setVisits(3L);

		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.WEBCONTENT),
			null, null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 3, new String[] {"firstVisitDate", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Arrays.asList(
				recentVisitAsset2, recentVisitAsset3, recentVisitAsset1),
			recentAssetPage.getContent());

		recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.WEBCONTENT),
			null, null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			1, 3, new String[] {"firstVisitDate", "asc"},
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			Collections.singletonList(recentVisitAsset4),
			recentAssetPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentWebContentWithSuppression() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.WEBCONTENT),
			null, null,
			"8bb3cd4319c4cc4df1addc31cb0fae500288133b91228a1cacb4ff2802446220",
			0, 10, new String[0], TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(0L, recentAssetPage.getTotalElements());
		Assertions.assertEquals(0L, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> recentVisitAssets = recentAssetPage.getContent();

		Assertions.assertTrue(recentVisitAssets.isEmpty());
	}

	@BQSQLResource(resourcePath = "test_get_recent_assets_yesterday_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_assets.sql")
	@Test
	public void testGetRecentWebContentYesterday() {
		Page<RecentVisitAsset> recentAssetPage = _bqEventDog.getRecentAssetPage(
			Collections.singletonList(RecentVisitAsset.ContentType.WEBCONTENT),
			null, null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 10, new String[0], TimeRange.YESTERDAY);

		Assertions.assertEquals(3, recentAssetPage.getTotalElements());
		Assertions.assertEquals(1, recentAssetPage.getTotalPages());

		List<RecentVisitAsset> recentVisitAssets = recentAssetPage.getContent();

		Assertions.assertFalse(recentVisitAssets.isEmpty());

		Map<Pair<String, String>, Long> expectedRecentAssetCounts =
			new HashMap<Pair<String, String>, Long>() {
				{
					put(Pair.of("e131fabc", "WebContent Title 1"), 10L);
					put(Pair.of("a73ihsy9", "WebContent Title 2"), 2L);
					put(Pair.of("b73ihsy9", "WebContent Title 3"), 2L);
				}
			};

		for (RecentVisitAsset recentVisitAsset : recentVisitAssets) {
			Pair<String, String> pair = Pair.of(
				recentVisitAsset.getAssetId(),
				recentVisitAsset.getAssetTitle());

			Assertions.assertEquals(
				expectedRecentAssetCounts.get(pair),
				recentVisitAsset.getVisits());

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
			"United States", DateUtil.newDate(), 10293L, "", "", null,
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
			"United States", DateUtil.newDate(), 10293L, "", "", null,
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
			"United States", DateUtil.newDate(), 10293L, "", "", null,
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
			"United States", DateUtil.newDate(), 10293L, "", "", null,
			DateUtil.newDate(), "pageViewed", "", "analyticsEventId4", "",
			"en_US", "", "", "", "", "", "", "",
			"http://localhost:8080/search?q=Diamond+Bar", "userId", "");

		Page<SearchKeyword> searchKeywordPage =
			_bqEventDog.getSearchKeywordPage(
				null, null, null, null, 1, 0, 2,
				new String[] {"counts", "desc"}, null);

		Assertions.assertEquals(3, searchKeywordPage.getTotalElements());

		List<SearchKeyword> searchKeywords = searchKeywordPage.getContent();

		Assertions.assertEquals(2, searchKeywords.size());

		SearchKeyword[] searchKeywordsArray = searchKeywords.toArray(
			new SearchKeyword[0]);

		SearchKeyword searchKeyword = searchKeywordsArray[0];

		Assertions.assertEquals(2, searchKeyword.getCounts());
		Assertions.assertEquals(10293L, searchKeyword.getDataSourceId());
		Assertions.assertEquals("en_US", searchKeyword.getDisplayLanguageId());
		Assertions.assertEquals("3212", searchKeyword.getGroupId());
		Assertions.assertEquals("liferay dxp", searchKeyword.getKeywords());

		searchKeywordPage = _bqEventDog.getSearchKeywordPage(
			null, null, null, null, 0, 0, 1,
			new String[] {"lastmodifieddate", "desc"}, null);

		Assertions.assertEquals(3, searchKeywordPage.getTotalElements());

		searchKeywords = searchKeywordPage.getContent();

		Assertions.assertEquals(1, searchKeywords.size());

		searchKeywordsArray = searchKeywords.toArray(new SearchKeyword[0]);

		searchKeyword = searchKeywordsArray[0];

		Assertions.assertEquals(1, searchKeyword.getCounts());
		Assertions.assertEquals(10293L, searchKeyword.getDataSourceId());
		Assertions.assertEquals("pt_BR", searchKeyword.getDisplayLanguageId());
		Assertions.assertEquals("3212", searchKeyword.getGroupId());
		Assertions.assertEquals("diamond bar", searchKeyword.getKeywords());

		searchKeywordPage = _bqEventDog.getSearchKeywordPage(
			null, null, null, null, 3, 0, 1, new String[] {"counts", "desc"},
			null);

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
			"United States", DateUtil.newDate(), 84756L, "", "",
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
			"United States", DateUtil.newDate(), 84756L, "", "",
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
			"United States", DateUtil.newDate(), 84756L, "", "",
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
			"United States", DateUtil.newDate(), 84756L, "", "",
			DigestUtils.sha256Hex("test2@liferay.com"), DateUtil.newDate(),
			"pageViewed", "", "analyticsEventId4", "", "en_US", "", "", "", "",
			"", "", "", "http://localhost:8080/search?q=Diamond+Bar", "userId",
			"");

		Page<SearchKeyword> searchKeywordPage =
			_bqEventDog.getSearchKeywordPage(
				null, null, null, DigestUtils.sha256Hex("test@liferay.com"), 1,
				0, 2, new String[] {"counts", "desc"}, TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(2, searchKeywordPage.getTotalElements());

		List<SearchKeyword> searchKeywords = searchKeywordPage.getContent();

		Assertions.assertEquals(2, searchKeywords.size());

		SearchKeyword[] searchKeywordsArray = searchKeywords.toArray(
			new SearchKeyword[0]);

		SearchKeyword searchKeyword = searchKeywordsArray[0];

		Assertions.assertEquals(2, searchKeyword.getCounts());
		Assertions.assertEquals(84756L, searchKeyword.getDataSourceId());
		Assertions.assertEquals("en_US", searchKeyword.getDisplayLanguageId());
		Assertions.assertEquals("3212", searchKeyword.getGroupId());
		Assertions.assertEquals("liferay dxp", searchKeyword.getKeywords());

		searchKeyword = searchKeywordsArray[1];

		Assertions.assertEquals(1, searchKeyword.getCounts());
		Assertions.assertEquals(84756L, searchKeyword.getDataSourceId());
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
			"United States", DateUtil.newDate(), 10293L, "", "",
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
			"United States", DateUtil.newDate(), 10293L, "", "",
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
			"United States", DateUtil.newDate(), 10293L, "", "",
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
			"United States", DateUtil.newDate(), 10293L, "", "",
			DigestUtils.sha256Hex("test@liferay.com"), DateUtil.newDate(),
			"pageViewed", "", "analyticsEventId4", "", "en_US", "", "", "", "",
			"", "", "", "http://localhost:8080/search?q=Diamond+Bar",
			"123123-sadf-32423-4245", "");

		Page<SearchKeyword> searchKeywordPage =
			_bqEventDog.getSearchKeywordPage(
				null, "pt_BR", null, DigestUtils.sha256Hex("test@liferay.com"),
				1, 0, 2, new String[] {"counts", "desc"},
				TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(1, searchKeywordPage.getTotalElements());

		List<SearchKeyword> searchKeywords = searchKeywordPage.getContent();

		Assertions.assertEquals(1, searchKeywords.size());

		SearchKeyword[] searchKeywordsArray = searchKeywords.toArray(
			new SearchKeyword[0]);

		SearchKeyword searchKeyword = searchKeywordsArray[0];

		Assertions.assertEquals(1, searchKeyword.getCounts());
		Assertions.assertEquals(10293L, searchKeyword.getDataSourceId());
		Assertions.assertEquals("pt_BR", searchKeyword.getDisplayLanguageId());
		Assertions.assertEquals("3212", searchKeyword.getGroupId());
		Assertions.assertEquals("diamond bar", searchKeyword.getKeywords());

		searchKeywordPage = _bqEventDog.getSearchKeywordPage(
			null, "en_US", null, DigestUtils.sha256Hex("test@liferay.com"), 1,
			0, 2, new String[] {"counts", "desc"}, TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(2, searchKeywordPage.getTotalElements());

		searchKeywords = searchKeywordPage.getContent();

		Assertions.assertEquals(2, searchKeywords.size());

		searchKeywordsArray = searchKeywords.toArray(new SearchKeyword[0]);

		searchKeyword = searchKeywordsArray[0];

		Assertions.assertEquals(2, searchKeyword.getCounts());
		Assertions.assertEquals(10293L, searchKeyword.getDataSourceId());
		Assertions.assertEquals("en_US", searchKeyword.getDisplayLanguageId());
		Assertions.assertEquals("3212", searchKeyword.getGroupId());
		Assertions.assertEquals("liferay dxp", searchKeyword.getKeywords());

		searchKeyword = searchKeywordsArray[1];

		Assertions.assertEquals(1, searchKeyword.getCounts());
		Assertions.assertEquals(10293L, searchKeyword.getDataSourceId());
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
			"United States", DateUtil.newDate(), 84756L, "", "",
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
			"United States", DateUtil.newDate(), 84756L, "", "",
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
			"United States", DateUtil.newDate(), 84756L, "", "",
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
			"United States", DateUtil.newDate(), 84756L, "", "",
			DigestUtils.sha256Hex("test@liferay.com"), DateUtil.newDate(),
			"pageViewed", "", "analyticsEventId4", "", "en_US", "", "", "", "",
			"", "", "", "http://localhost:8080/search?q=Diamond+Bar",
			"123123-sadf-32423-4245", "");

		Page<SearchKeyword> searchKeywordPage =
			_bqEventDog.getSearchKeywordPage(
				null, null, "3213", DigestUtils.sha256Hex("test@liferay.com"),
				1, 0, 2, new String[] {"counts", "desc"},
				TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(1, searchKeywordPage.getTotalElements());

		List<SearchKeyword> searchKeywords = searchKeywordPage.getContent();

		Assertions.assertEquals(1, searchKeywords.size());

		SearchKeyword[] searchKeywordsArray = searchKeywords.toArray(
			new SearchKeyword[0]);

		SearchKeyword searchKeyword = searchKeywordsArray[0];

		Assertions.assertEquals(1, searchKeyword.getCounts());
		Assertions.assertEquals(84756L, searchKeyword.getDataSourceId());
		Assertions.assertEquals("pt_BR", searchKeyword.getDisplayLanguageId());
		Assertions.assertEquals("3213", searchKeyword.getGroupId());
		Assertions.assertEquals("diamond bar", searchKeyword.getKeywords());

		searchKeywordPage = _bqEventDog.getSearchKeywordPage(
			null, null, "3212", DigestUtils.sha256Hex("test@liferay.com"), 1, 0,
			2, new String[] {"counts", "desc"}, TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(2, searchKeywordPage.getTotalElements());

		searchKeywords = searchKeywordPage.getContent();

		Assertions.assertEquals(2, searchKeywords.size());

		searchKeywordsArray = searchKeywords.toArray(new SearchKeyword[0]);

		searchKeyword = searchKeywordsArray[0];

		Assertions.assertEquals(2, searchKeyword.getCounts());
		Assertions.assertEquals(84756L, searchKeyword.getDataSourceId());
		Assertions.assertEquals("en_US", searchKeyword.getDisplayLanguageId());
		Assertions.assertEquals("3212", searchKeyword.getGroupId());
		Assertions.assertEquals("liferay dxp", searchKeyword.getKeywords());

		searchKeyword = searchKeywordsArray[1];

		Assertions.assertEquals(1, searchKeyword.getCounts());
		Assertions.assertEquals(84756L, searchKeyword.getDataSourceId());
		Assertions.assertEquals("en_US", searchKeyword.getDisplayLanguageId());
		Assertions.assertEquals("3212", searchKeyword.getGroupId());
		Assertions.assertEquals("liferay", searchKeyword.getKeywords());
	}

	@BQSQLResource(resourcePath = "test_bq_identity.sql")
	@Test
	public void testGetSearchKeywordsMultipleDataSourceIds() throws Exception {
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
			"United States", DateUtil.newDate(), 10293L, "", "",
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
			"United States", DateUtil.newDate(), 84756L, "", "",
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
			"United States", DateUtil.newDate(), 84756L, "", "",
			DigestUtils.sha256Hex("test@liferay.com"), DateUtil.newDate(),
			"pageViewed", "", "analyticsEventId3", "", "en_US", "", "", "", "",
			"", "", "", "http://localhost:8080/search?q=Liferay+DXP",
			"123123-sadf-32423-4245", "");

		Page<SearchKeyword> searchKeywordPage =
			_bqEventDog.getSearchKeywordPage(
				null, null, "3212", DigestUtils.sha256Hex("test@liferay.com"),
				1, 0, 3, new String[] {"counts", "desc"},
				TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(3, searchKeywordPage.getTotalElements());

		List<SearchKeyword> searchKeywords = searchKeywordPage.getContent();

		Assertions.assertEquals(3, searchKeywords.size());

		SearchKeyword searchKeyword = searchKeywords.get(0);

		Assertions.assertEquals(1, searchKeyword.getCounts());
		Assertions.assertEquals(10293L, searchKeyword.getDataSourceId());
		Assertions.assertEquals("en_US", searchKeyword.getDisplayLanguageId());
		Assertions.assertEquals("3212", searchKeyword.getGroupId());
		Assertions.assertEquals("liferay dxp", searchKeyword.getKeywords());

		searchKeyword = searchKeywords.get(1);

		Assertions.assertEquals(1, searchKeyword.getCounts());
		Assertions.assertEquals(84756L, searchKeyword.getDataSourceId());
		Assertions.assertEquals("en_US", searchKeyword.getDisplayLanguageId());
		Assertions.assertEquals("3212", searchKeyword.getGroupId());
		Assertions.assertEquals("liferay", searchKeyword.getKeywords());

		searchKeyword = searchKeywords.get(2);

		Assertions.assertEquals(1, searchKeyword.getCounts());
		Assertions.assertEquals(84756L, searchKeyword.getDataSourceId());
		Assertions.assertEquals("en_US", searchKeyword.getDisplayLanguageId());
		Assertions.assertEquals("3212", searchKeyword.getGroupId());
		Assertions.assertEquals("liferay dxp", searchKeyword.getKeywords());
	}

	@BQSQLResource(resourcePath = "test_bq_identity.sql")
	@Test
	public void testGetSearchKeywordsWithDataSourceId() throws Exception {
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
			"United States", DateUtil.newDate(), 10293L, "", "",
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
			"United States", DateUtil.newDate(), 84756L, "", "",
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
			"United States", DateUtil.newDate(), 84756L, "", "",
			DigestUtils.sha256Hex("test@liferay.com"), DateUtil.newDate(),
			"pageViewed", "", "analyticsEventId3", "", "en_US", "", "", "", "",
			"", "", "", "http://localhost:8080/search?q=Liferay+DXP",
			"123123-sadf-32423-4245", "");

		Page<SearchKeyword> searchKeywordPage =
			_bqEventDog.getSearchKeywordPage(
				84756L, null, "3212", DigestUtils.sha256Hex("test@liferay.com"),
				1, 0, 2, new String[] {"counts", "desc"},
				TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(2, searchKeywordPage.getTotalElements());

		List<SearchKeyword> searchKeywords = searchKeywordPage.getContent();

		Assertions.assertEquals(2, searchKeywords.size());

		SearchKeyword searchKeyword = searchKeywords.get(0);

		Assertions.assertEquals(1, searchKeyword.getCounts());
		Assertions.assertEquals(84756L, searchKeyword.getDataSourceId());
		Assertions.assertEquals("en_US", searchKeyword.getDisplayLanguageId());
		Assertions.assertEquals("3212", searchKeyword.getGroupId());
		Assertions.assertEquals("liferay", searchKeyword.getKeywords());

		searchKeyword = searchKeywords.get(1);

		Assertions.assertEquals(1, searchKeyword.getCounts());
		Assertions.assertEquals(84756L, searchKeyword.getDataSourceId());
		Assertions.assertEquals("en_US", searchKeyword.getDisplayLanguageId());
		Assertions.assertEquals("3212", searchKeyword.getGroupId());
		Assertions.assertEquals("liferay dxp", searchKeyword.getKeywords());
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