/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.stream.curator.bot;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.stream.curator.bot.nanite.Nanite;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.concurrent.ScheduledExecutorFactoryBean;
import org.springframework.scheduling.concurrent.ScheduledExecutorTask;
import org.springframework.stereotype.Component;

/**
 * @author Brian Wing Shun Chan
 */
@Component
@ConditionalOnProperty(
	matchIfMissing = true, value = "osb.asah.enable.scheduling"
)
@Profile("!test")
public class OSBAsahStreamCuratorBot {

	@PreDestroy
	public void destroy() {
		_scheduledExecutorFactoryBeans.forEach(
			ScheduledExecutorFactoryBean::destroy);
	}

	@PostConstruct
	public void init() {
		Stream<Nanite> stream = _nanites.stream();

		stream.collect(
			Collectors.groupingBy(
				nanite -> {
					Class<?> clazz = nanite.getClass();

					return clazz.getName();
				})
		).forEach(
			(name, nanites) -> _addScheduledExecutorFactoryBeans(
				name, nanites.get(0))
		);
	}

	private void _addScheduledExecutorFactoryBeans(String name, Nanite nanite) {
		ScheduledExecutorFactoryBean scheduledExecutorFactoryBean =
			new ScheduledExecutorFactoryBean();

		scheduledExecutorFactoryBean.
			setContinueScheduledExecutionAfterException(true);
		scheduledExecutorFactoryBean.setScheduledExecutorTasks(
			new ScheduledExecutorTask(
				nanite, DateUtil.SECOND * 5, nanite.getInterval(), false));
		scheduledExecutorFactoryBean.setThreadNamePrefix(
			String.format("osb-asah-stream-curator-bot[%s]", name));

		scheduledExecutorFactoryBean.initialize();

		_scheduledExecutorFactoryBeans.add(scheduledExecutorFactoryBean);
	}

	@Autowired
	private List<Nanite> _nanites;

	private final List<ScheduledExecutorFactoryBean>
		_scheduledExecutorFactoryBeans = new ArrayList<>();

}