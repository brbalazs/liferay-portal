/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.emulator.bot;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.dog.ProjectDog;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;
import com.liferay.osb.asah.dataflow.emulator.bot.nanite.AnalyticsEventsIngestionNanite;
import com.liferay.osb.asah.dataflow.emulator.bot.nanite.DXPEntitiesIngestionNanite;
import com.liferay.osb.asah.dataflow.emulator.bot.nanite.IdentityNanite;
import com.liferay.osb.asah.dataflow.emulator.bot.nanite.IndividualNanite;
import com.liferay.osb.asah.dataflow.emulator.bot.nanite.InterestScoreIngestionNanite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
@EnableScheduling
@Profile("!test")
public class OSBAsahDataflowEmulatorCuratorBot {

	@Scheduled(fixedDelay = DateUtil.MINUTE)
	public void checkIngestionNaniteWatermark() {
		_analyticsEventsIngestionNanite.checkWatermark();
	}

	@Scheduled(fixedDelay = DateUtil.MINUTE)
	public void closeIngestionNaniteOpenSessions() {
		_analyticsEventsIngestionNanite.closeOpenSessions();
	}

	@Scheduled(fixedDelay = 10 * DateUtil.SECOND)
	public void runAnalyticsEventsIngestionNanite() {
		try {
			_analyticsEventsIngestionNanite.run();
		}
		catch (Exception exception) {
			_log.error(exception, exception);
		}
	}

	@Scheduled(fixedDelay = 30 * DateUtil.SECOND)
	public void runDXPEntitiesIngestionNanite() {
		try {
			_dxpEntitiesIngestionNanite.run();
		}
		catch (Exception exception) {
			_log.error(exception, exception);
		}
	}

	@Scheduled(fixedDelay = 10 * DateUtil.SECOND)
	public void runIdentityNanite() {
		try {
			_identityNanite.run();
		}
		catch (Exception exception) {
			_log.error(exception, exception);
		}
	}

	@Scheduled(fixedDelay = 1 * DateUtil.MINUTE)
	public void runIndividualNanite() {
		ProjectIdThreadLocal.forProjects(
			_projectDog.getProjects(), _individualNanite::run);
	}

	@Scheduled(fixedDelay = 10 * DateUtil.MINUTE)
	public void runInterestScoreIngestionNanite() {
		try {
			ProjectIdThreadLocal.forProjects(
				_projectDog.getProjects(), _interestScoreIngestionNanite::run);
		}
		catch (Exception exception) {
			_log.error(exception, exception);
		}
	}

	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
		ThreadPoolTaskScheduler threadPoolTaskScheduler =
			new ThreadPoolTaskScheduler();

		threadPoolTaskScheduler.setPoolSize(2);

		return threadPoolTaskScheduler;
	}

	private static final Log _log = LogFactory.getLog(
		OSBAsahDataflowEmulatorCuratorBot.class);

	@Autowired
	private AnalyticsEventsIngestionNanite _analyticsEventsIngestionNanite;

	@Autowired
	private DXPEntitiesIngestionNanite _dxpEntitiesIngestionNanite;

	@Autowired
	private IdentityNanite _identityNanite;

	@Autowired
	private IndividualNanite _individualNanite;

	@Autowired
	private InterestScoreIngestionNanite _interestScoreIngestionNanite;

	@Autowired
	private ProjectDog _projectDog;

}