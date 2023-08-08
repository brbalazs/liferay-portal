/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.spring.scheduling;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * @author Shinn Lok
 */
public abstract class BaseSchedulingConfigurer implements SchedulingConfigurer {

	public BaseSchedulingConfigurer(int poolSize) {
		_poolSize = poolSize;
	}

	@Override
	public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
		scheduledTaskRegistrar.setTaskScheduler(threadPoolTaskScheduler());
	}

	@Bean(destroyMethod = "shutdown")
	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
		ThreadPoolTaskScheduler threadPoolTaskScheduler =
			new ThreadPoolTaskScheduler();

		threadPoolTaskScheduler.setPoolSize(_poolSize);

		threadPoolTaskScheduler.initialize();

		return threadPoolTaskScheduler;
	}

	private final int _poolSize;

}