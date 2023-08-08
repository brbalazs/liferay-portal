/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.repository.BQEventPropertyRepository;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.annotation.SQLResource;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.TreeMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

/**
 * @author Leslie Wong
 */
@Import(JDBCTestConfiguration.class)
public class BQEventPropertyRepositoryTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BQSQLResource(resourcePath = "test_bq_event_properties_bq.sql")
	@SQLResource(resourcePath = "test_bq_event_properties.sql")
	@Test
	public void testFindBQEventPropertyValuesByEventAttributeDefinitionId() {
		Date date = DateUtil.newDate();

		Assertions.assertEquals(
			new TreeMap<String, Date>() {
				{
					put("Windshield Wipers", _getExpectedDate(date, -1));

					put("Wheels", _getExpectedDate(date, -2));

					put("Plates", _getExpectedDate(date, -3));

					put("Apples", _getExpectedDate(date, -4));

					put("Books", _getExpectedDate(date, -6));
				}
			},
			_bqEventPropertyRepository.
				findBQEventPropertyValuesByEventAttributeDefinitionName(
					"itemName", 5));
	}

	@BQSQLResource(resourcePath = "test_bq_event_properties_1_bq.sql")
	@SQLResource(resourcePath = "test_bq_event_properties_1.sql")
	@Test
	public void testFindBQEventPropertyValuesByEventAttributeDefinitionIdNoMatchingValues() {
		Assertions.assertEquals(
			Collections.emptyMap(),
			_bqEventPropertyRepository.
				findBQEventPropertyValuesByEventAttributeDefinitionName(
					"itemName", 10));
	}

	@BQSQLResource(resourcePath = "test_bq_event_properties_2_bq.sql")
	@SQLResource(resourcePath = "test_bq_event_properties_2.sql")
	@Test
	public void testSearchValues() {
		List<String> values = _bqEventPropertyRepository.searchValues(
			1L, "test", "test", "Attribute Value", PageRequest.of(0, 100));

		Assertions.assertEquals(4, values.size());

		for (String value :
				Arrays.asList(
					"event attribute value 4", "event attribute value 3",
					"event attribute value 2", "event attribute value 1")) {

			Assertions.assertTrue(values.contains(value));
		}

		values = _bqEventPropertyRepository.searchValues(
			1L, "pageTitle", "test", "Test", PageRequest.of(0, 100));

		Assertions.assertEquals(1, values.size());

		values = _bqEventPropertyRepository.searchValues(
			1L, "test", "test", "Attribute Value", PageRequest.of(0, 3));

		Assertions.assertEquals(3, values.size());

		values = _bqEventPropertyRepository.searchValues(
			1L, "test", "test", "Attribute Value", PageRequest.of(1, 3));

		Assertions.assertEquals(1, values.size());

		Assertions.assertEquals(
			4,
			_bqEventPropertyRepository.countValues(
				1L, "test", "test", "Attribute Value"));
	}

	private Date _getExpectedDate(Date date, int deltaDays) {
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));

		calendar.setTime(date);

		calendar.add(Calendar.DATE, deltaDays);

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		return new Date(calendar.getTimeInMillis());
	}

	@Autowired
	private BQEventPropertyRepository _bqEventPropertyRepository;

}