/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.batch.curator.bot.scheduling;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

/**
 * @author André Miranda
 */
@Component
public class AsahTaskScheduler {

	public Map<String, ScheduledFuture<?>> getScheduledFuturesMap() {
		return Collections.unmodifiableMap(_scheduledFuturesMap);
	}

	public void schedule(
		CronTrigger cronTrigger, Runnable runnable, String scheduledTaskId) {

		if (scheduledTaskId == null) {
			throw new IllegalArgumentException("scheduledTaskId is null");
		}

		ScheduledFuture<?> scheduledFuture = _threadPoolTaskScheduler.schedule(
			runnable, cronTrigger);

		_scheduledFuturesMap.put(scheduledTaskId, scheduledFuture);
	}

	public void schedule(
		String cronExpression, Runnable runnable, String scheduledTaskId) {

		schedule(new CronTrigger(cronExpression), runnable, scheduledTaskId);
	}

	public void unschedule(String scheduledAsahTaskId) {
		ScheduledFuture<?> scheduledFuture = _scheduledFuturesMap.remove(
			scheduledAsahTaskId);

		if (scheduledFuture == null) {
			throw new IllegalArgumentException(
				"Unable to unschedule task " + scheduledAsahTaskId);
		}

		scheduledFuture.cancel(false);
	}

	@PreDestroy
	private void _destroy() {
		_threadPoolTaskScheduler.setAwaitTerminationSeconds(60);

		_threadPoolTaskScheduler.shutdown();
	}

	private final Map<String, ScheduledFuture<?>> _scheduledFuturesMap =
		new HashMap<>();

	@Autowired
	private ThreadPoolTaskScheduler _threadPoolTaskScheduler;

}