/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.batch.curator.bot.scheduling.test;

import com.liferay.osb.asah.batch.curator.OSBAsahBatchCuratorSpringTestContext;
import com.liferay.osb.asah.batch.curator.bot.scheduling.AsahTaskScheduler;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

/**
 * @author André Miranda
 */
public class AsahTaskSchedulerTest
	implements OSBAsahBatchCuratorSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BeforeEach
	public void setUp() {
		Mockito.when(
			_threadPoolTaskScheduler.schedule(
				ArgumentMatchers.any(Runnable.class),
				ArgumentMatchers.any(Trigger.class))
		).thenReturn(
			Mockito.mock(ScheduledFuture.class)
		);
	}

	@Test
	public void testSchedule() {
		Runnable runnable = () -> {
		};

		_asahTaskScheduler.schedule(
			"0 0 0 * * ?", runnable, "450553576847486528");

		Mockito.verify(
			_threadPoolTaskScheduler, Mockito.times(1)
		).schedule(
			ArgumentMatchers.eq(runnable),
			ArgumentMatchers.any(CronTrigger.class)
		);

		Map<String, ScheduledFuture<?>> scheduledFuturesMap =
			_asahTaskScheduler.getScheduledFuturesMap();

		Assertions.assertEquals(
			1, scheduledFuturesMap.size(), scheduledFuturesMap.toString());
		Assertions.assertTrue(
			scheduledFuturesMap.containsKey("450553576847486528"));
	}

	@Test
	public void testScheduleFail() {
		Assertions.assertThrows(
			IllegalArgumentException.class,
			() -> _asahTaskScheduler.schedule(
				"",
				() -> {
				},
				null));
	}

	@Test
	public void testUnschedule() {
		_asahTaskScheduler.schedule(
			"0 0 0 * * ?",
			() -> {
			},
			"450553576847486528");

		Map<String, ScheduledFuture<?>> scheduledFuturesMap =
			_asahTaskScheduler.getScheduledFuturesMap();

		ScheduledFuture<?> scheduledFuture = scheduledFuturesMap.get(
			"450553576847486528");

		_asahTaskScheduler.unschedule("450553576847486528");

		Assertions.assertEquals(
			0, scheduledFuturesMap.size(), scheduledFuturesMap.toString());

		Mockito.verify(
			scheduledFuture, Mockito.times(1)
		).cancel(
			ArgumentMatchers.eq(false)
		);
	}

	@Test
	public void testUnscheduleFail() {
		Assertions.assertThrows(
			IllegalArgumentException.class,
			() -> _asahTaskScheduler.unschedule(null));
	}

	@Autowired
	private AsahTaskScheduler _asahTaskScheduler;

	@MockBean
	private ThreadPoolTaskScheduler _threadPoolTaskScheduler;

}