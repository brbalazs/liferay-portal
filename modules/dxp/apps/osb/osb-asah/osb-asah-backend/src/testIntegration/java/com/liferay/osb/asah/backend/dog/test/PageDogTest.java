/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dog.PageDog;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.model.RecentPage;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.annotation.SQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

/**
 * @author Leslie Wong
 */
public class PageDogTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesLast7Days() {
		Page<RecentPage> recentPagesPage = _pageDog.getRecentPagesPage(
			null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 7, 10, new String[0]);

		Assertions.assertEquals(2, recentPagesPage.getTotalElements());
		Assertions.assertEquals(1, recentPagesPage.getTotalPages());

		List<RecentPage> recentPages = recentPagesPage.getContent();

		Assertions.assertFalse(recentPages.isEmpty());

		List<RecentPage> expectedRecentPages = new ArrayList<>();

		RecentPage recentPage = new RecentPage();

		recentPage.setCounts(2L);
		recentPage.setCreateDate(DateUtil.addDays(DateUtil.newDayDate(), -3));
		recentPage.setDisplayLanguageId("en-US");
		recentPage.setLastModifiedDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentPage.setUrl("https://www.beryl.com/delivery");

		expectedRecentPages.add(recentPage);

		recentPage = new RecentPage();

		recentPage.setCounts(1L);
		recentPage.setCreateDate(DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentPage.setDisplayLanguageId("pt-BR");
		recentPage.setLastModifiedDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentPage.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");

		expectedRecentPages.add(recentPage);

		Assertions.assertEquals(
			expectedRecentPages, recentPagesPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesLast24Hours() {
		Page<RecentPage> recentPagesPage = _pageDog.getRecentPagesPage(
			null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 0, 10, new String[0]);

		Assertions.assertEquals(4, recentPagesPage.getTotalElements());
		Assertions.assertEquals(1, recentPagesPage.getTotalPages());

		List<RecentPage> recentPages = recentPagesPage.getContent();

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
				expectedRecentPageCounts.get(pair), recentPage.getCounts());

			expectedRecentPageCounts.remove(pair);
		}

		Assertions.assertTrue(expectedRecentPageCounts.isEmpty());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesLast28Days() {
		Page<RecentPage> recentPagesPage = _pageDog.getRecentPagesPage(
			null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 28, 10, new String[0]);

		Assertions.assertEquals(4, recentPagesPage.getTotalElements());
		Assertions.assertEquals(1, recentPagesPage.getTotalPages());

		List<RecentPage> expectedRecentPages = new ArrayList<>();

		RecentPage recentPage = new RecentPage();

		recentPage.setCounts(4L);
		recentPage.setCreateDate(DateUtil.addDays(DateUtil.newDayDate(), -23));
		recentPage.setDisplayLanguageId("en-US");
		recentPage.setLastModifiedDate(
			DateUtil.addDays(DateUtil.newDayDate(), -1));
		recentPage.setUrl("https://www.beryl.com/delivery");

		expectedRecentPages.add(recentPage);

		recentPage = new RecentPage();

		recentPage.setCounts(4L);
		recentPage.setCreateDate(DateUtil.addDays(DateUtil.newDayDate(), -28));
		recentPage.setDisplayLanguageId("pt-BR");
		recentPage.setLastModifiedDate(
			DateUtil.addDays(DateUtil.newDayDate(), -6));
		recentPage.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");

		expectedRecentPages.add(recentPage);

		recentPage = new RecentPage();

		recentPage.setCounts(2L);
		recentPage.setCreateDate(DateUtil.addDays(DateUtil.newDayDate(), -18));
		recentPage.setDisplayLanguageId("en-US");
		recentPage.setLastModifiedDate(
			DateUtil.addDays(DateUtil.newDayDate(), -16));
		recentPage.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");

		expectedRecentPages.add(recentPage);

		recentPage = new RecentPage();

		recentPage.setCounts(2L);
		recentPage.setCreateDate(DateUtil.addDays(DateUtil.newDayDate(), -13));
		recentPage.setDisplayLanguageId("pt-BR");
		recentPage.setLastModifiedDate(
			DateUtil.addDays(DateUtil.newDayDate(), -11));
		recentPage.setUrl("https://www.beryl.com/delivery");

		expectedRecentPages.add(recentPage);

		Assertions.assertEquals(
			expectedRecentPages, recentPagesPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesLast30Days() {
		Page<RecentPage> recentPagesPage = _pageDog.getRecentPagesPage(
			null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10, new String[0]);

		Assertions.assertEquals(4, recentPagesPage.getTotalElements());
		Assertions.assertEquals(1, recentPagesPage.getTotalPages());

		List<RecentPage> expectedRecentPages = new ArrayList<>();

		RecentPage recentPage = new RecentPage();

		recentPage.setCounts(4L);
		recentPage.setCreateDate(DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentPage.setDisplayLanguageId("en-US");
		recentPage.setLastModifiedDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentPage.setUrl("https://www.beryl.com/delivery");

		expectedRecentPages.add(recentPage);

		recentPage = new RecentPage();

		recentPage.setCounts(4L);
		recentPage.setCreateDate(DateUtil.addDays(DateUtil.newDayDate(), -24));
		recentPage.setDisplayLanguageId("pt-BR");
		recentPage.setLastModifiedDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentPage.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");

		expectedRecentPages.add(recentPage);

		recentPage = new RecentPage();

		recentPage.setCounts(2L);
		recentPage.setCreateDate(DateUtil.addDays(DateUtil.newDayDate(), -14));
		recentPage.setDisplayLanguageId("en-US");
		recentPage.setLastModifiedDate(
			DateUtil.addDays(DateUtil.newDayDate(), -12));
		recentPage.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");

		expectedRecentPages.add(recentPage);

		recentPage = new RecentPage();

		recentPage.setCounts(2L);
		recentPage.setCreateDate(DateUtil.addDays(DateUtil.newDayDate(), -19));
		recentPage.setDisplayLanguageId("pt-BR");
		recentPage.setLastModifiedDate(
			DateUtil.addDays(DateUtil.newDayDate(), -17));
		recentPage.setUrl("https://www.beryl.com/delivery");

		expectedRecentPages.add(recentPage);

		Assertions.assertEquals(
			expectedRecentPages, recentPagesPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesSortByCounts() {
		RecentPage recentPage1 = new RecentPage();

		recentPage1.setCounts(4L);
		recentPage1.setCreateDate(DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentPage1.setDisplayLanguageId("en-US");
		recentPage1.setLastModifiedDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentPage1.setUrl("https://www.beryl.com/delivery");

		RecentPage recentPage2 = new RecentPage();

		recentPage2.setCounts(4L);
		recentPage2.setCreateDate(DateUtil.addDays(DateUtil.newDayDate(), -24));
		recentPage2.setDisplayLanguageId("pt-BR");
		recentPage2.setLastModifiedDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentPage2.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");

		RecentPage recentPage3 = new RecentPage();

		recentPage3.setCounts(2L);
		recentPage3.setCreateDate(DateUtil.addDays(DateUtil.newDayDate(), -14));
		recentPage3.setDisplayLanguageId("en-US");
		recentPage3.setLastModifiedDate(
			DateUtil.addDays(DateUtil.newDayDate(), -12));
		recentPage3.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");

		RecentPage recentPage4 = new RecentPage();

		recentPage4.setCounts(2L);
		recentPage4.setCreateDate(DateUtil.addDays(DateUtil.newDayDate(), -19));
		recentPage4.setDisplayLanguageId("pt-BR");
		recentPage4.setLastModifiedDate(
			DateUtil.addDays(DateUtil.newDayDate(), -17));
		recentPage4.setUrl("https://www.beryl.com/delivery");

		Page<RecentPage> recentPagesPage = _pageDog.getRecentPagesPage(
			null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10, new String[] {"counts", "asc", "url", "asc"});

		Assertions.assertEquals(
			Arrays.asList(recentPage4, recentPage3, recentPage1, recentPage2),
			recentPagesPage.getContent());

		recentPagesPage = _pageDog.getRecentPagesPage(
			null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10, new String[] {"counts", "desc", "url", "asc"});

		Assertions.assertEquals(
			Arrays.asList(recentPage1, recentPage2, recentPage4, recentPage3),
			recentPagesPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesSortByCreateDate() {
		RecentPage recentPage1 = new RecentPage();

		recentPage1.setCounts(4L);
		recentPage1.setCreateDate(DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentPage1.setDisplayLanguageId("en-US");
		recentPage1.setLastModifiedDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentPage1.setUrl("https://www.beryl.com/delivery");

		RecentPage recentPage2 = new RecentPage();

		recentPage2.setCounts(4L);
		recentPage2.setCreateDate(DateUtil.addDays(DateUtil.newDayDate(), -24));
		recentPage2.setDisplayLanguageId("pt-BR");
		recentPage2.setLastModifiedDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentPage2.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");

		RecentPage recentPage3 = new RecentPage();

		recentPage3.setCounts(2L);
		recentPage3.setCreateDate(DateUtil.addDays(DateUtil.newDayDate(), -14));
		recentPage3.setDisplayLanguageId("en-US");
		recentPage3.setLastModifiedDate(
			DateUtil.addDays(DateUtil.newDayDate(), -12));
		recentPage3.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");

		RecentPage recentPage4 = new RecentPage();

		recentPage4.setCounts(2L);
		recentPage4.setCreateDate(DateUtil.addDays(DateUtil.newDayDate(), -19));
		recentPage4.setDisplayLanguageId("pt-BR");
		recentPage4.setLastModifiedDate(
			DateUtil.addDays(DateUtil.newDayDate(), -17));
		recentPage4.setUrl("https://www.beryl.com/delivery");

		Page<RecentPage> recentPagesPage = _pageDog.getRecentPagesPage(
			null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10, new String[] {"createDate", "asc"});

		Assertions.assertEquals(
			Arrays.asList(recentPage1, recentPage2, recentPage4, recentPage3),
			recentPagesPage.getContent());

		recentPagesPage = _pageDog.getRecentPagesPage(
			null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10, new String[] {"createDate", "desc"});

		Assertions.assertEquals(
			Arrays.asList(recentPage3, recentPage4, recentPage2, recentPage1),
			recentPagesPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesSortByDisplayLanguageId() {
		RecentPage recentPage1 = new RecentPage();

		recentPage1.setCounts(4L);
		recentPage1.setCreateDate(DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentPage1.setDisplayLanguageId("en-US");
		recentPage1.setLastModifiedDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentPage1.setUrl("https://www.beryl.com/delivery");

		RecentPage recentPage2 = new RecentPage();

		recentPage2.setCounts(4L);
		recentPage2.setCreateDate(DateUtil.addDays(DateUtil.newDayDate(), -24));
		recentPage2.setDisplayLanguageId("pt-BR");
		recentPage2.setLastModifiedDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentPage2.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");

		RecentPage recentPage3 = new RecentPage();

		recentPage3.setCounts(2L);
		recentPage3.setCreateDate(DateUtil.addDays(DateUtil.newDayDate(), -14));
		recentPage3.setDisplayLanguageId("en-US");
		recentPage3.setLastModifiedDate(
			DateUtil.addDays(DateUtil.newDayDate(), -12));
		recentPage3.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");

		RecentPage recentPage4 = new RecentPage();

		recentPage4.setCounts(2L);
		recentPage4.setCreateDate(DateUtil.addDays(DateUtil.newDayDate(), -19));
		recentPage4.setDisplayLanguageId("pt-BR");
		recentPage4.setLastModifiedDate(
			DateUtil.addDays(DateUtil.newDayDate(), -17));
		recentPage4.setUrl("https://www.beryl.com/delivery");

		Page<RecentPage> recentPagesPage = _pageDog.getRecentPagesPage(
			null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10, new String[] {"displayLanguageId", "asc", "url", "asc"});

		Assertions.assertEquals(
			Arrays.asList(recentPage1, recentPage3, recentPage4, recentPage2),
			recentPagesPage.getContent());

		recentPagesPage = _pageDog.getRecentPagesPage(
			null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10,
			new String[] {"displayLanguageId", "desc", "url", "asc"});

		Assertions.assertEquals(
			Arrays.asList(recentPage4, recentPage2, recentPage1, recentPage3),
			recentPagesPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesSortByLastModifiedDate() {
		RecentPage recentPage1 = new RecentPage();

		recentPage1.setCounts(4L);
		recentPage1.setCreateDate(DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentPage1.setDisplayLanguageId("en-US");
		recentPage1.setLastModifiedDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentPage1.setUrl("https://www.beryl.com/delivery");

		RecentPage recentPage2 = new RecentPage();

		recentPage2.setCounts(4L);
		recentPage2.setCreateDate(DateUtil.addDays(DateUtil.newDayDate(), -24));
		recentPage2.setDisplayLanguageId("pt-BR");
		recentPage2.setLastModifiedDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentPage2.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");

		RecentPage recentPage3 = new RecentPage();

		recentPage3.setCounts(2L);
		recentPage3.setCreateDate(DateUtil.addDays(DateUtil.newDayDate(), -14));
		recentPage3.setDisplayLanguageId("en-US");
		recentPage3.setLastModifiedDate(
			DateUtil.addDays(DateUtil.newDayDate(), -12));
		recentPage3.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");

		RecentPage recentPage4 = new RecentPage();

		recentPage4.setCounts(2L);
		recentPage4.setCreateDate(DateUtil.addDays(DateUtil.newDayDate(), -19));
		recentPage4.setDisplayLanguageId("pt-BR");
		recentPage4.setLastModifiedDate(
			DateUtil.addDays(DateUtil.newDayDate(), -17));
		recentPage4.setUrl("https://www.beryl.com/delivery");

		Page<RecentPage> recentPagesPage = _pageDog.getRecentPagesPage(
			null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10, new String[] {"lastModifiedDate", "asc"});

		Assertions.assertEquals(
			Arrays.asList(recentPage4, recentPage3, recentPage1, recentPage2),
			recentPagesPage.getContent());

		recentPagesPage = _pageDog.getRecentPagesPage(
			null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10, new String[] {"lastModifiedDate", "desc"});

		Assertions.assertEquals(
			Arrays.asList(recentPage2, recentPage1, recentPage3, recentPage4),
			recentPagesPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesWithDisplayLanguageId() {
		List<RecentPage> expectedRecentPages = new ArrayList<>();

		RecentPage recentPage = new RecentPage();

		recentPage.setCounts(4L);
		recentPage.setCreateDate(DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentPage.setDisplayLanguageId("en-US");
		recentPage.setLastModifiedDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentPage.setUrl("https://www.beryl.com/delivery");

		expectedRecentPages.add(recentPage);

		recentPage = new RecentPage();

		recentPage.setCounts(2L);
		recentPage.setCreateDate(DateUtil.addDays(DateUtil.newDayDate(), -14));
		recentPage.setDisplayLanguageId("en-US");
		recentPage.setLastModifiedDate(
			DateUtil.addDays(DateUtil.newDayDate(), -12));
		recentPage.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");

		expectedRecentPages.add(recentPage);

		Page<RecentPage> recentPagesPage = _pageDog.getRecentPagesPage(
			"en-US",
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 10, new String[0]);

		Assertions.assertEquals(
			expectedRecentPages, recentPagesPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesWithPagination() {
		RecentPage recentPage1 = new RecentPage();

		recentPage1.setCounts(4L);
		recentPage1.setCreateDate(DateUtil.addDays(DateUtil.newDayDate(), -29));
		recentPage1.setDisplayLanguageId("en-US");
		recentPage1.setLastModifiedDate(
			DateUtil.addDays(DateUtil.newDayDate(), -7));
		recentPage1.setUrl("https://www.beryl.com/delivery");

		RecentPage recentPage2 = new RecentPage();

		recentPage2.setCounts(4L);
		recentPage2.setCreateDate(DateUtil.addDays(DateUtil.newDayDate(), -24));
		recentPage2.setDisplayLanguageId("pt-BR");
		recentPage2.setLastModifiedDate(
			DateUtil.addDays(DateUtil.newDayDate(), -2));
		recentPage2.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");

		RecentPage recentPage3 = new RecentPage();

		recentPage3.setCounts(2L);
		recentPage3.setCreateDate(DateUtil.addDays(DateUtil.newDayDate(), -14));
		recentPage3.setDisplayLanguageId("en-US");
		recentPage3.setLastModifiedDate(
			DateUtil.addDays(DateUtil.newDayDate(), -12));
		recentPage3.setUrl(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100");

		RecentPage recentPage4 = new RecentPage();

		recentPage4.setCounts(2L);
		recentPage4.setCreateDate(DateUtil.addDays(DateUtil.newDayDate(), -19));
		recentPage4.setDisplayLanguageId("pt-BR");
		recentPage4.setLastModifiedDate(
			DateUtil.addDays(DateUtil.newDayDate(), -17));
		recentPage4.setUrl("https://www.beryl.com/delivery");

		Page<RecentPage> recentPagesPage = _pageDog.getRecentPagesPage(
			null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			0, 30, 2, new String[] {"counts", "desc", "url", "asc"});

		Assertions.assertEquals(
			Arrays.asList(recentPage1, recentPage2),
			recentPagesPage.getContent());

		recentPagesPage = _pageDog.getRecentPagesPage(
			null,
			"09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f",
			1, 30, 2, new String[] {"counts", "desc", "url", "asc"});

		Assertions.assertEquals(
			Arrays.asList(recentPage4, recentPage3),
			recentPagesPage.getContent());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesWithSuppression() {
		Page<RecentPage> recentPagesPage = _pageDog.getRecentPagesPage(
			null,
			"8bb3cd4319c4cc4df1addc31cb0fae500288133b91228a1cacb4ff2802446220",
			0, 30, 10, new String[] {"counts", "desc", "url", "asc"});

		Assertions.assertEquals(0L, recentPagesPage.getTotalElements());
		Assertions.assertEquals(0L, recentPagesPage.getTotalPages());

		List<RecentPage> recentPages = recentPagesPage.getContent();

		Assertions.assertTrue(recentPages.isEmpty());
	}

	@BQSQLResource(resourcePath = "test_get_recent_pages_yesterday_bq.sql")
	@SQLResource(resourcePath = "test_get_recent_pages.sql")
	@Test
	public void testGetRecentPagesYesterday() {
		Page<RecentPage> recentPagesPage = _pageDog.getRecentPagesPage(
			null,
			"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485",
			0, 1, 10, new String[0]);

		Assertions.assertEquals(4, recentPagesPage.getTotalElements());
		Assertions.assertEquals(1, recentPagesPage.getTotalPages());

		List<RecentPage> recentPages = recentPagesPage.getContent();

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
				expectedRecentPageCounts.get(pair), recentPage.getCounts());

			expectedRecentPageCounts.remove(pair);
		}

		Assertions.assertTrue(expectedRecentPageCounts.isEmpty());
	}

	@Autowired
	private PageDog _pageDog;

}