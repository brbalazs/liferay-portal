/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Inácio Nery
 */
public class TimeRangeTest {

	@BeforeEach
	public void setUp() {
		ProjectIdThreadLocal.setProjectId("test");
	}

	@Test
	public void testGetEndLocalDateTime() {
		TimeRange timeRange = TimeRange.LAST_30_DAYS;

		LocalDateTime localDateTime = LocalDateTime.now(ZoneOffset.UTC);

		localDateTime = localDateTime.minusDays(1);

		localDateTime = localDateTime.withHour(23);
		localDateTime = localDateTime.withMinute(59);
		localDateTime = localDateTime.withSecond(59);
		localDateTime = localDateTime.withNano(999999000);

		Assertions.assertEquals(localDateTime, timeRange.getEndLocalDateTime());
	}

	@Test
	public void testGetIncludePreviousTimeRange1() {
		TimeRange timeRange = TimeRange.YESTERDAY.getIncludePreviousTimeRange();

		Duration duration = Duration.between(
			timeRange.getStartLocalDateTime(), timeRange.getEndLocalDateTime());

		Assertions.assertEquals(47, Math.abs(duration.toHours()));
	}

	@Test
	public void testGetIncludePreviousTimeRange2() {
		TimeRange timeRange =
			TimeRange.LAST_7_DAYS.getIncludePreviousTimeRange();

		Duration duration = Duration.between(
			timeRange.getStartLocalDateTime(), timeRange.getEndLocalDateTime());

		Assertions.assertEquals(13, Math.abs(duration.toDays()));
	}

	@Test
	public void testGetIncludePreviousTimeRange3() {
		TimeRange timeRange =
			TimeRange.LAST_28_DAYS.getIncludePreviousTimeRange();

		Duration duration = Duration.between(
			timeRange.getStartLocalDateTime(), timeRange.getEndLocalDateTime());

		Assertions.assertEquals(55, Math.abs(duration.toDays()));
	}

	@Test
	public void testGetIncludePreviousTimeRange4() {
		TimeRange timeRange =
			TimeRange.LAST_30_DAYS.getIncludePreviousTimeRange();

		Duration duration = Duration.between(
			timeRange.getStartLocalDateTime(), timeRange.getEndLocalDateTime());

		Assertions.assertEquals(59, Math.abs(duration.toDays()));
	}

	@Test
	public void testGetIncludePreviousTimeRange5() {
		TimeRange timeRange =
			TimeRange.LAST_90_DAYS.getIncludePreviousTimeRange();

		Duration duration = Duration.between(
			timeRange.getStartLocalDateTime(), timeRange.getEndLocalDateTime());

		Assertions.assertEquals(179, Math.abs(duration.toDays()));
	}

	@Test
	public void testGetIncludePreviousTimeRange6() {
		TimeRange timeRange =
			TimeRange.LAST_180_DAYS.getIncludePreviousTimeRange();

		Duration duration = Duration.between(
			timeRange.getStartLocalDateTime(), timeRange.getEndLocalDateTime());

		Assertions.assertEquals(359, Math.abs(duration.toDays()));
	}

	@Test
	public void testGetIncludePreviousTimeRange7() {
		TimeRange timeRange = TimeRange.YESTERDAY.getIncludePreviousTimeRange();

		Duration duration = Duration.between(
			timeRange.getStartLocalDateTime(), timeRange.getEndLocalDateTime());

		Assertions.assertEquals(47, Math.abs(duration.toHours()));
	}

	@Test
	public void testGetKey1() {
		TimeRange timeRange = TimeRange.LAST_7_DAYS;

		Assertions.assertEquals("last-7-days", timeRange.getKey());
	}

	@Test
	public void testGetKey2() {
		TimeRange timeRange = TimeRange.LAST_24_HOURS;

		Assertions.assertEquals("last-24-hours", timeRange.getKey());
	}

	@Test
	public void testGetKey3() {
		TimeRange timeRange = TimeRange.LAST_28_DAYS;

		Assertions.assertEquals("last-28-days", timeRange.getKey());
	}

	@Test
	public void testGetKey4() {
		TimeRange timeRange = TimeRange.LAST_30_DAYS;

		Assertions.assertEquals("last-30-days", timeRange.getKey());
	}

	@Test
	public void testGetKey5() {
		TimeRange timeRange = TimeRange.LAST_90_DAYS;

		Assertions.assertEquals("last-90-days", timeRange.getKey());
	}

	@Test
	public void testGetKey6() {
		TimeRange timeRange = TimeRange.YESTERDAY;

		Assertions.assertEquals("yesterday", timeRange.getKey());
	}

	@Test
	public void testGetKey7() {
		TimeRange timeRange = TimeRange.LAST_180_DAYS;

		Assertions.assertEquals("last-180-days", timeRange.getKey());
	}

	@Test
	public void testGetKey8() {
		TimeRange timeRange = TimeRange.LAST_YEAR;

		Assertions.assertEquals("last-year", timeRange.getKey());
	}

	@Test
	public void testGetPreviousTimeRange1() {
		TimeRange timeRange = TimeRange.LAST_24_HOURS;

		TimeRange previousTimeRange = timeRange.getPreviousTimeRange();

		Duration duration = Duration.between(
			previousTimeRange.getStartLocalDateTime(),
			previousTimeRange.getEndLocalDateTime());

		Assertions.assertEquals(23, Math.abs(duration.toHours()));

		duration = Duration.between(
			timeRange.getStartLocalDateTime(),
			previousTimeRange.getStartLocalDateTime());

		Assertions.assertEquals(24, Math.abs(duration.toHours()));
	}

	@Test
	public void testGetPreviousTimeRange2() {
		TimeRange timeRange = TimeRange.LAST_7_DAYS;

		TimeRange previousTimeRange = timeRange.getPreviousTimeRange();

		Duration duration = Duration.between(
			previousTimeRange.getStartLocalDateTime(),
			previousTimeRange.getEndLocalDateTime());

		Assertions.assertEquals(6, Math.abs(duration.toDays()));

		duration = Duration.between(
			timeRange.getStartLocalDateTime(),
			previousTimeRange.getStartLocalDateTime());

		Assertions.assertEquals(7, Math.abs(duration.toDays()));
	}

	@Test
	public void testGetPreviousTimeRange3() {
		TimeRange timeRange = TimeRange.LAST_28_DAYS;

		TimeRange previousTimeRange = timeRange.getPreviousTimeRange();

		Duration duration = Duration.between(
			previousTimeRange.getStartLocalDateTime(),
			previousTimeRange.getEndLocalDateTime());

		Assertions.assertEquals(27, Math.abs(duration.toDays()));

		duration = Duration.between(
			timeRange.getStartLocalDateTime(),
			previousTimeRange.getStartLocalDateTime());

		Assertions.assertEquals(28, Math.abs(duration.toDays()));
	}

	@Test
	public void testGetPreviousTimeRange4() {
		TimeRange timeRange = TimeRange.LAST_30_DAYS;

		TimeRange previousTimeRange = timeRange.getPreviousTimeRange();

		Duration duration = Duration.between(
			previousTimeRange.getStartLocalDateTime(),
			previousTimeRange.getEndLocalDateTime());

		Assertions.assertEquals(29, Math.abs(duration.toDays()));

		duration = Duration.between(
			timeRange.getStartLocalDateTime(),
			previousTimeRange.getStartLocalDateTime());

		Assertions.assertEquals(30, Math.abs(duration.toDays()));
	}

	@Test
	public void testGetPreviousTimeRange5() {
		TimeRange timeRange = TimeRange.LAST_90_DAYS;

		TimeRange previousTimeRange = timeRange.getPreviousTimeRange();

		Duration duration = Duration.between(
			previousTimeRange.getStartLocalDateTime(),
			previousTimeRange.getEndLocalDateTime());

		Assertions.assertEquals(89, Math.abs(duration.toDays()));

		duration = Duration.between(
			timeRange.getStartLocalDateTime(),
			previousTimeRange.getStartLocalDateTime());

		Assertions.assertEquals(90, Math.abs(duration.toDays()));
	}

	@Test
	public void testGetPreviousTimeRange6() {
		TimeRange timeRange = TimeRange.LAST_180_DAYS;

		TimeRange previousTimeRange = timeRange.getPreviousTimeRange();

		Duration duration = Duration.between(
			previousTimeRange.getStartLocalDateTime(),
			previousTimeRange.getEndLocalDateTime());

		Assertions.assertEquals(179, Math.abs(duration.toDays()));

		duration = Duration.between(
			timeRange.getStartLocalDateTime(),
			previousTimeRange.getStartLocalDateTime());

		Assertions.assertEquals(180, Math.abs(duration.toDays()));
	}

	@Test
	public void testGetPreviousTimeRange7() {
		TimeRange timeRange = TimeRange.YESTERDAY;

		TimeRange previousTimeRange = timeRange.getPreviousTimeRange();

		Duration duration = Duration.between(
			previousTimeRange.getStartLocalDateTime(),
			previousTimeRange.getEndLocalDateTime());

		Assertions.assertEquals(23, Math.abs(duration.toHours()));

		duration = Duration.between(
			timeRange.getStartLocalDateTime(),
			previousTimeRange.getStartLocalDateTime());

		Assertions.assertEquals(24, Math.abs(duration.toHours()));
	}

	@Test
	public void testGetPreviousTimeRange8() {
		TimeRange timeRange = TimeRange.of(
			LocalDateTime.of(2020, 10, 24, 0, 0),
			LocalDateTime.of(2020, 4, 20, 0, 0));

		TimeRange previousTimeRange = timeRange.getPreviousTimeRange();

		Duration duration = Duration.between(
			previousTimeRange.getStartLocalDateTime(),
			previousTimeRange.getEndLocalDateTime());

		Assertions.assertEquals(187, Math.abs(duration.toDays()));

		duration = Duration.between(
			timeRange.getStartLocalDateTime(),
			previousTimeRange.getStartLocalDateTime());

		Assertions.assertEquals(188, Math.abs(duration.toDays()));
	}

	@Test
	public void testGetPreviousTimeRange9() {
		TimeRange timeRange = TimeRange.of(
			LocalDateTime.of(2022, 9, 21, 23, 59),
			LocalDateTime.of(2022, 9, 20, 0, 0));

		Duration duration = Duration.between(
			timeRange.getStartLocalDateTime(), timeRange.getEndLocalDateTime());

		Assertions.assertEquals(1, Math.abs(duration.toDays()));

		TimeRange includePreviousTimeRange =
			timeRange.getIncludePreviousTimeRange();

		duration = Duration.between(
			includePreviousTimeRange.getStartLocalDateTime(),
			includePreviousTimeRange.getEndLocalDateTime());

		Assertions.assertEquals(2, Math.abs(duration.toDays()));

		timeRange = TimeRange.of(
			LocalDateTime.of(2022, 9, 21, 23, 59),
			LocalDateTime.of(2022, 8, 20, 0, 0));

		duration = Duration.between(
			timeRange.getStartLocalDateTime(), timeRange.getEndLocalDateTime());

		Assertions.assertEquals(32, Math.abs(duration.toDays()));

		includePreviousTimeRange = timeRange.getIncludePreviousTimeRange();

		duration = Duration.between(
			includePreviousTimeRange.getStartLocalDateTime(),
			includePreviousTimeRange.getEndLocalDateTime());

		Assertions.assertEquals(64, Math.abs(duration.toDays()));
	}

	@Test
	public void testGetRangeKey1() {
		TimeRange timeRange = TimeRange.LAST_7_DAYS;

		Assertions.assertEquals(7, timeRange.getRangeKey());
	}

	@Test
	public void testGetRangeKey2() {
		TimeRange timeRange = TimeRange.LAST_24_HOURS;

		Assertions.assertEquals(0, timeRange.getRangeKey());
	}

	@Test
	public void testGetRangeKey3() {
		TimeRange timeRange = TimeRange.LAST_28_DAYS;

		Assertions.assertEquals(28, timeRange.getRangeKey());
	}

	@Test
	public void testGetRangeKey4() {
		TimeRange timeRange = TimeRange.LAST_30_DAYS;

		Assertions.assertEquals(30, timeRange.getRangeKey());
	}

	@Test
	public void testGetRangeKey5() {
		TimeRange timeRange = TimeRange.LAST_90_DAYS;

		Assertions.assertEquals(90, timeRange.getRangeKey());
	}

	@Test
	public void testGetRangeKey6() {
		TimeRange timeRange = TimeRange.YESTERDAY;

		Assertions.assertEquals(1, timeRange.getRangeKey());
	}

	@Test
	public void testGetRangeKey7() {
		TimeRange timeRange = TimeRange.LAST_180_DAYS;

		Assertions.assertEquals(180, timeRange.getRangeKey());
	}

	@Test
	public void testGetRangeKey8() {
		TimeRange timeRange = TimeRange.LAST_YEAR;

		Assertions.assertEquals(365, timeRange.getRangeKey());
	}

	@Test
	public void testGetStartLocalDateTime() {
		TimeRange timeRange = TimeRange.LAST_30_DAYS;

		LocalDateTime localDateTime = LocalDateTime.of(
			LocalDate.now(ZoneOffset.UTC), LocalTime.MIN);

		Assertions.assertEquals(
			localDateTime.minusDays(30), timeRange.getStartLocalDateTime());
	}

	@Test
	public void testTimeRangeCustom() {
		TimeRange timeRange = TimeRange.of(1);

		Assertions.assertEquals(TimeRange.YESTERDAY, timeRange);
	}

	@Test
	public void testTimeRangeOf1() {
		TimeRange timeRange = TimeRange.of(7);

		Assertions.assertEquals(TimeRange.LAST_7_DAYS, timeRange);
	}

	@Test
	public void testTimeRangeOf2() {
		TimeRange timeRange = TimeRange.of(0);

		Assertions.assertEquals(TimeRange.LAST_24_HOURS, timeRange);
	}

	@Test
	public void testTimeRangeOf3() {
		TimeRange timeRange = TimeRange.of(28);

		Assertions.assertEquals(TimeRange.LAST_28_DAYS, timeRange);
	}

	@Test
	public void testTimeRangeOf4() {
		TimeRange timeRange = TimeRange.of(30);

		Assertions.assertEquals(TimeRange.LAST_30_DAYS, timeRange);
	}

	@Test
	public void testTimeRangeOf5() {
		TimeRange timeRange = TimeRange.of(90);

		Assertions.assertEquals(TimeRange.LAST_90_DAYS, timeRange);
	}

	@Test
	public void testTimeRangeOf6() {
		LocalDateTime endLocalDateTime = LocalDateTime.now();

		LocalDateTime startLocalDateTime = endLocalDateTime.minusDays(2);

		TimeRange timeRange = TimeRange.of(
			endLocalDateTime, startLocalDateTime);

		Assertions.assertEquals("custom", timeRange.getKey());
		Assertions.assertEquals(
			endLocalDateTime, timeRange.getEndLocalDateTime());
		Assertions.assertEquals(3, timeRange.getRangeKey());
		Assertions.assertEquals(
			startLocalDateTime, timeRange.getStartLocalDateTime());
	}

	@Test
	public void testTimeRangeOf7() {
		Assertions.assertThrows(
			IllegalArgumentException.class, () -> TimeRange.of(14));
	}

	@Test
	public void testTimeRangeOf8() {
		TimeRange timeRange = TimeRange.of(180);

		Assertions.assertEquals(TimeRange.LAST_180_DAYS, timeRange);
	}

	@Test
	public void testTimeRangeOf9() {
		TimeRange timeRange = TimeRange.of(365);

		Assertions.assertEquals(TimeRange.LAST_YEAR, timeRange);
	}

}