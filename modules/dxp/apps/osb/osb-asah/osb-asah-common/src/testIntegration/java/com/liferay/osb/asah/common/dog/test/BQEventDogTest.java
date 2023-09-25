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
import com.liferay.osb.asah.common.model.SearchKeyword;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.annotation.SQLResource;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;

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