/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * @author Brian Wing Shun Chan
 */
public class DateUtil {

	public static final long DAY = DateUtil.HOUR * 24;

	public static final long HOUR = DateUtil.MINUTE * 60;

	public static final long MINUTE = DateUtil.SECOND * 60;

	public static final long MONTH = DAY * 30;

	public static final String PATTERN_ISO_8601 =
		"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

	public static final String PATTERN_SHORT = "yyyy-MM-dd";

	public static final long SECOND = 1000;

	public static Date addDays(Date date, int days) {
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));

		calendar.setTime(date);

		calendar.add(Calendar.DATE, days);

		return calendar.getTime();
	}

	public static String addDays(String dateString, int days) {
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));

		calendar.setTime(toUTCDate(dateString));

		calendar.add(Calendar.DATE, days);

		return toUTCString(calendar.getTime());
	}

	public static String addHours(String dateString, int hours) {
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));

		calendar.setTime(toUTCDate(dateString));

		calendar.add(Calendar.HOUR, hours);

		return toUTCString(calendar.getTime());
	}

	public static Date addMinutes(Date date, int minutes) {
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));

		calendar.setTime(date);

		calendar.add(Calendar.MINUTE, minutes);

		return calendar.getTime();
	}

	public static String addMinutes(String dateString, int minutes) {
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));

		calendar.setTime(toUTCDate(dateString));

		calendar.add(Calendar.MINUTE, minutes);

		return toUTCString(calendar.getTime());
	}

	public static String addMonths(String dateString, int months) {
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));

		calendar.setTime(toUTCDate(dateString));

		calendar.add(Calendar.MONTH, months);

		return toUTCString(calendar.getTime());
	}

	public static int getDeltaDays(String dateString) {
		return getDeltaDays(_newDayDateString(dateString), newDayDateString());
	}

	public static int getDeltaDays(String dateString1, String dateString2) {
		long millisecondsBetween = getDeltaMilliseconds(
			_newDayDateString(dateString1), _newDayDateString(dateString2));

		return (int)(millisecondsBetween / DAY);
	}

	public static long getDeltaMilliseconds(Date date1, Date date2) {
		return date2.getTime() - date1.getTime();
	}

	public static long getDeltaMilliseconds(
		String dateString1, String dateString2) {

		Date date1 = toUTCDate(dateString1);
		Date date2 = toUTCDate(dateString2);

		return date2.getTime() - date1.getTime();
	}

	public static int getDeltaMonths(
		LocalDate localDate1, LocalDate localDate2) {

		int deltaMonths = 0;

		int dayOfMonth = localDate1.getDayOfMonth();

		if (dayOfMonth != 1) {
			localDate1 = localDate1.withDayOfMonth(1);
		}

		while (!localDate1.isAfter(localDate2)) {
			localDate1 = localDate1.plusMonths(1);

			deltaMonths++;
		}

		return deltaMonths;
	}

	public static int getDeltaWeeks(
		LocalDate localDate1, LocalDate localDate2) {

		int deltaWeeks = 0;

		DayOfWeek dayOfWeek = localDate1.getDayOfWeek();

		if (dayOfWeek != DayOfWeek.SUNDAY) {
			localDate1 = localDate1.minusDays(dayOfWeek.getValue());
		}

		while (!localDate1.isAfter(localDate2)) {
			localDate1 = localDate1.plusWeeks(1);

			deltaWeeks++;
		}

		return deltaWeeks;
	}

	public static boolean isValidPatternShort(String dateString) {
		if (PATTERN_SHORT.length() != dateString.length()) {
			return false;
		}

		DateFormat dateFormat = new SimpleDateFormat(PATTERN_SHORT);

		dateFormat.setLenient(false);

		try {
			dateFormat.parse(dateString);
		}
		catch (ParseException parseException) {
			return false;
		}

		return true;
	}

	public static String minusMinutes(
		LocalDateTime localDateTime, int minutes) {

		LocalDateTime newLocalDateTime = localDateTime.minusMinutes(minutes);

		return newLocalDateTime.toString();
	}

	public static Date newBeginningOfDayDate(Date date) {
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));

		calendar.setTime(date);

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		return calendar.getTime();
	}

	public static String newBeginningOfDayDateString(String dateString) {
		return toUTCString(newBeginningOfDayDate(toUTCDate(dateString)));
	}

	public static Date newDate() {
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));

		calendar.setTime(new Date());

		return calendar.getTime();
	}

	public static String newDateString() {
		return toUTCString(new Date());
	}

	public static Date newDayDate() {
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));

		calendar.setTime(new Date());

		calendar.set(Calendar.AM_PM, Calendar.AM);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		return calendar.getTime();
	}

	public static String newDayDateString() {
		return toUTCString(newDayDate());
	}

	public static LocalDateTime newDayLocalDateTime(ZoneId zoneId) {
		return LocalDateTime.of(LocalDate.now(zoneId), LocalTime.MIDNIGHT);
	}

	public static String newDayLocalDateTimeString(
		LocalDateTime localDateTime) {

		LocalDateTime newLocalDateTime = localDateTime.with(LocalTime.MIDNIGHT);

		return newLocalDateTime.toString();
	}

	public static String newDayLocalDateTimeString(ZoneId zoneId) {
		LocalDateTime newDayLocalDateTime = newDayLocalDateTime(zoneId);

		return newDayLocalDateTime.toString();
	}

	public static Date newEndOfDayDate(Date date) {
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));

		calendar.setTime(date);

		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MILLISECOND, 999);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);

		return calendar.getTime();
	}

	public static String newEndOfDayDateString(String dateString) {
		return toUTCString(newEndOfDayDate(toUTCDate(dateString)));
	}

	public static Date newEndOfMonthDate(String dateString) {
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));

		calendar.setTime(toUTCDate(dateString));

		calendar.add(Calendar.MONTH, 1);

		calendar.set(Calendar.DAY_OF_MONTH, 1);

		calendar.add(Calendar.DATE, -1);

		return newEndOfDayDate(calendar.getTime());
	}

	public static String newEndOfMonthDateString(String dateString) {
		return toUTCString(newEndOfMonthDate(dateString));
	}

	public static Date newEpochDate() {
		return Date.from(Instant.EPOCH);
	}

	public static String newEpochDateString() {
		return toUTCString(newEpochDate());
	}

	public static LocalDateTime newLocalDateTime(ZoneId zoneId) {
		return LocalDateTime.now(zoneId);
	}

	public static String newMonthDateString() {
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));

		calendar.setTime(new Date());

		calendar.set(Calendar.AM_PM, Calendar.AM);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		return toUTCString(calendar.getTime());
	}

	public static Date toDate(LocalDateTime localDateTime, ZoneId zoneId) {
		ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);

		return Date.from(zonedDateTime.toInstant());
	}

	public static LocalDateTime toLocalDateTime(Date date, ZoneId zoneId) {
		Instant instant = date.toInstant();

		ZonedDateTime zonedDateTime = instant.atZone(zoneId);

		return zonedDateTime.toLocalDateTime();
	}

	public static String toString(Date date) {
		DateFormat dateFormat = _newISODateFormat();

		return dateFormat.format(date);
	}

	public static String toString(String dateString) {
		DateFormat dateFormat = _newISODateFormat();

		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		return dateFormat.format(new Date(Long.parseLong(dateString)));
	}

	public static Date toUTCDate(LocalDateTime localDateTime) {
		ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneOffset.UTC);

		return Date.from(zonedDateTime.toInstant());
	}

	public static Date toUTCDate(String dateString) {
		return toUTCDate(LocalDateTime.parse(dateString, _dateTimeFormatter));
	}

	public static LocalDate toUTCLocalDate(
		LocalDateTime localDateTime, ZoneId zoneId) {

		ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneOffset.UTC);

		zonedDateTime = zonedDateTime.withZoneSameInstant(zoneId);

		return zonedDateTime.toLocalDate();
	}

	public static LocalDate toUTCLocalDate(
		String dateString, String pattern, ZoneId zoneId) {

		LocalDate localDate = LocalDate.parse(
			dateString, DateTimeFormatter.ofPattern(pattern));

		LocalDateTime localDateTime = toUTCLocalDateTime(
			localDate.atStartOfDay(), zoneId);

		return localDateTime.toLocalDate();
	}

	public static LocalDateTime toUTCLocalDateTime(
		LocalDateTime localDateTime, ZoneId zoneId) {

		ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);

		zonedDateTime = zonedDateTime.withZoneSameInstant(ZoneOffset.UTC);

		return zonedDateTime.toLocalDateTime();
	}

	public static String toUTCString(Date date) {
		return toUTCString(date, _newISODateFormat());
	}

	public static String toUTCString(Date date, DateFormat dateFormat) {
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		return dateFormat.format(date);
	}

	public static String toUTCString(Date date, String pattern) {
		return toUTCString(date, new SimpleDateFormat(pattern));
	}

	public static String toUTCString(LocalDateTime localDateTime) {
		ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneOffset.UTC);

		return zonedDateTime.format(_dateTimeFormatter);
	}

	private static String _newDayDateString(String dateString) {
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));

		calendar.setTime(toUTCDate(dateString));

		calendar.set(Calendar.AM_PM, Calendar.AM);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		return toUTCString(calendar.getTime());
	}

	private static DateFormat _newISODateFormat() {
		return new SimpleDateFormat(PATTERN_ISO_8601);
	}

	private static final DateTimeFormatter _dateTimeFormatter =
		DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm[:ss.SSS'Z']");

}