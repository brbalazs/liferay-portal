/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.date;

import java.time.LocalDate;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Michael Bowerman
 */
public class DateUtilTest {

	@Test
	public void testAddDaysForNegativeDays() {
		Assertions.assertEquals(
			"2013-08-03T14:02:46.984Z",
			DateUtil.addDays("2014-01-12T14:02:46.984Z", -162));
	}

	@Test
	public void testAddDaysForPositiveDays() {
		Assertions.assertEquals(
			"2021-03-09T15:15:16.012Z",
			DateUtil.addDays("2019-03-31T15:15:16.012Z", 709));
	}

	@Test
	public void testAddDaysForZeroDays() {
		Assertions.assertEquals(
			"2020-02-17T15:25:51.433Z",
			DateUtil.addDays("2020-02-17T15:25:51.433Z", 0));
	}

	@Test
	public void testAddMonthsClampsToValidDate() {
		Assertions.assertEquals(
			"2016-02-29T22:48:42.105Z",
			DateUtil.addMonths("2016-05-31T22:48:42.105Z", -3));
	}

	@Test
	public void testAddMonthsForNegativeMonths() {
		Assertions.assertEquals(
			"2016-02-26T00:47:58.314Z",
			DateUtil.addMonths("2019-08-26T00:47:58.314Z", -42));
	}

	@Test
	public void testAddMonthsForPositiveMonths() {
		Assertions.assertEquals(
			"2022-06-30T00:46:32.089Z",
			DateUtil.addMonths("2020-10-30T00:46:32.089Z", 20));
	}

	@Test
	public void testAddMonthsForZeroMonths() {
		Assertions.assertEquals(
			"2011-06-04T07:17:50.706Z",
			DateUtil.addMonths("2011-06-04T07:17:50.706Z", 0));
	}

	@Test
	public void testGetDeltaDaysForNewDayDate() {
		String newDayDateString = DateUtil.newDayDateString();

		Assertions.assertEquals(
			-1, DateUtil.getDeltaDays(DateUtil.addDays(newDayDateString, 1)));
		Assertions.assertEquals(0, DateUtil.getDeltaDays(newDayDateString));
		Assertions.assertEquals(
			1, DateUtil.getDeltaDays(DateUtil.addDays(newDayDateString, -1)));
	}

	@Test
	public void testGetDeltaDaysString() {
		Assertions.assertEquals(
			-1,
			DateUtil.getDeltaDays(
				"2011-06-04T07:17:50.706Z", "2011-06-03T23:59:59.999Z"));
		Assertions.assertEquals(
			0,
			DateUtil.getDeltaDays(
				"2011-06-04T07:17:50.706Z", "2011-06-04T07:17:50.706Z"));
		Assertions.assertEquals(
			0,
			DateUtil.getDeltaDays(
				"2011-06-04T07:17:50.706Z", "2011-06-04T23:59:59.999Z"));
		Assertions.assertEquals(
			1,
			DateUtil.getDeltaDays(
				"2011-06-04T07:17:50.706Z", "2011-06-05T00:00:00.000Z"));
	}

	@Test
	public void testGetDeltaMillisecondsForEarlierDate() {
		Assertions.assertEquals(
			-243634878350L,
			DateUtil.getDeltaMilliseconds(
				"2025-04-10T21:43:45.843Z", "2017-07-22T01:22:27.493Z"));
	}

	@Test
	public void testGetDeltaMillisecondsForEqualDate() {
		Assertions.assertEquals(
			0,
			DateUtil.getDeltaMilliseconds(
				"2024-06-30T06:37:50.417Z", "2024-06-30T06:37:50.417Z"));
	}

	@Test
	public void testGetDeltaMillisecondsForLaterDate() {
		Assertions.assertEquals(
			122202552140L,
			DateUtil.getDeltaMilliseconds(
				"2017-01-07T22:19:40.162Z", "2020-11-22T07:28:52.302Z"));
	}

	@Test
	public void testGetDeltaMonths() {
		Assertions.assertEquals(
			3,
			DateUtil.getDeltaMonths(
				LocalDate.of(2018, 4, 13), LocalDate.of(2018, 6, 2)));
		Assertions.assertEquals(
			4,
			DateUtil.getDeltaMonths(
				LocalDate.of(2018, 12, 31), LocalDate.of(2019, 3, 1)));
	}

	@Test
	public void testGetDeltaWeeks() {
		Assertions.assertEquals(
			13,
			DateUtil.getDeltaWeeks(
				LocalDate.of(2018, 9, 23), LocalDate.of(2018, 12, 21)));
		Assertions.assertEquals(
			14,
			DateUtil.getDeltaWeeks(
				LocalDate.of(2019, 12, 3), LocalDate.of(2020, 3, 1)));
	}

	@Test
	public void testIsValidPatternShort() {
		Assertions.assertFalse(
			DateUtil.isValidPatternShort("January 1st 2023"),
			"January 1st 2023 is a valid date pattern");
		Assertions.assertFalse(
			DateUtil.isValidPatternShort("20231209"),
			"20231209 is a valid date pattern");
		Assertions.assertFalse(
			DateUtil.isValidPatternShort("2019-05-31T14:23:31.309Z"),
			"2023-12-09 is a valid date pattern");
		Assertions.assertTrue(
			DateUtil.isValidPatternShort("2023-12-09"),
			"2023-12-09 is not a valid date pattern");
	}

	@Test
	public void testNewBeginningOfDayDate() {
		Assertions.assertEquals(
			"2019-05-31T00:00:00.000Z",
			DateUtil.newBeginningOfDayDateString("2019-05-31T14:23:31.309Z"));
	}

	@Test
	public void testNewDayDateString() {
		String day = DateUtil.newDayDateString();

		Assertions.assertTrue(
			day.endsWith("T00:00:00.000Z"),
			"New day date string did not end with \"T00:00:00.000Z\"");
	}

	@Test
	public void testNewEndOfDayDate() {
		Assertions.assertEquals(
			"2019-05-31T23:59:59.999Z",
			DateUtil.newEndOfDayDateString("2019-05-31T14:23:31.309Z"));
	}

	@Test
	public void testNewEndOfMonthDate() {
		Assertions.assertEquals(
			_toDate(2020, 12, 31, 23, 59, 59, 999),
			DateUtil.newEndOfMonthDate("2020-12-20T04:07:43.642Z"));
	}

	@Test
	public void testNewEndOfMonthDateHandlesLeapYears() {
		Assertions.assertEquals(
			_toDate(2024, 2, 29, 23, 59, 59, 999),
			DateUtil.newEndOfMonthDate("2024-02-08T20:53:29.533Z"));
	}

	@Test
	public void testNewEndOfMonthDateString() {
		Assertions.assertEquals(
			"2019-05-31T23:59:59.999Z",
			DateUtil.newEndOfMonthDateString("2019-05-26T07:23:31.309Z"));
	}

	@Test
	public void testNewEndOfMonthDateStringHandlesLeapYears() {
		Assertions.assertEquals(
			"2012-02-29T23:59:59.999Z",
			DateUtil.newEndOfMonthDateString("2012-02-13T08:35:53.954Z"));
	}

	@Test
	public void testNewEpochDate() {
		Assertions.assertEquals(
			"1970-01-01T00:00:00.000Z",
			DateUtil.toUTCString(DateUtil.newEpochDate()));
	}

	@Test
	public void testNewEpochDateString() {
		Assertions.assertEquals(
			"1970-01-01T00:00:00.000Z", DateUtil.newEpochDateString());
	}

	@Test
	public void testNewMonthDateString() {
		String month = DateUtil.newMonthDateString();

		Assertions.assertTrue(
			month.endsWith("-01T00:00:00.000Z"),
			"New day date string did not end with \"-01T00:00:00.000Z\"");
	}

	@Test
	public void testNewUTCDateString() {
		String utcString = DateUtil.toUTCString(new Date());

		String utcDateString = DateUtil.newDateString();

		Assertions.assertTrue(
			utcDateString.startsWith(
				utcString.substring(0, utcString.indexOf("T"))),
			"New UTC date string did not start with " +
				utcString.substring(0, utcString.indexOf("T")));
	}

	@Test
	public void testToString() {
		Assertions.assertEquals(
			"2013-10-20T03:04:09.360Z",
			DateUtil.toUTCString(_toDate(2013, 10, 20, 3, 4, 9, 360)));
		Assertions.assertEquals(
			"2013-10-20",
			DateUtil.toUTCString(
				_toDate(2013, 10, 20, 3, 4, 9, 360), "yyyy-MM-dd"));
	}

	@Test
	public void testToUTCDate1() {
		Assertions.assertEquals(
			_toDate(2018, 9, 15, 0, 40, 48, 502),
			DateUtil.toUTCDate("2018-09-15T00:40:48.502Z"));
	}

	@Test
	public void testToUTCDate2() {
		Assertions.assertEquals(
			_toDate(2021, 3, 15, 0, 0, 0, 0),
			DateUtil.toUTCDate("2021-03-15T00:00"));
	}

	private Date _toDate(
		int year, int month, int day, int hour, int minute, int second,
		int millisecond) {

		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));

		calendar.set(Calendar.AM_PM, hour / 12);
		calendar.set(Calendar.DATE, day);
		calendar.set(Calendar.HOUR, hour % 12);
		calendar.set(Calendar.MILLISECOND, millisecond);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.SECOND, second);
		calendar.set(Calendar.YEAR, year);

		return calendar.getTime();
	}

}