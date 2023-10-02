/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.batch.curator.bot;

import com.liferay.osb.asah.batch.curator.bot.scheduling.AsahTaskManager;
import com.liferay.osb.asah.batch.curator.bot.scheduling.AsahTaskScheduler;
import com.liferay.osb.asah.common.concurrent.BoundedExecutor;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.date.dog.TimeZoneDog;
import com.liferay.osb.asah.common.dog.AsahTaskDog;
import com.liferay.osb.asah.common.dog.ProjectDog;
import com.liferay.osb.asah.common.dog.RunLogDog;
import com.liferay.osb.asah.common.entity.AsahTask;
import com.liferay.osb.asah.common.entity.Project;
import com.liferay.osb.asah.common.lock.KeyReentrantLock;
import com.liferay.osb.asah.common.spring.annotation.CacheEvict;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;

import java.util.TimeZone;

import javax.annotation.PreDestroy;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

/**
 * @author Brian Wing Shun Chan
 */
@Component
@EnableScheduling
@Profile("!test")
public class OSBAsahBatchCuratorBot {

	@CacheEvict(
		allProjects = true,
		value = {
			"getActivityTransformations", "getGraphQLExecutionResult",
			"getMembershipChangeTransformations"
		}
	)
	@Scheduled(cron = "0 0 0 * * ?")
	public void clearCache() {
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onStartup() {
		for (Project project : _projectDog.getProjects()) {
			_init(project.getId());
		}
	}

	public void removeNanitesSchedule(String projectId) {
		ProjectIdThreadLocal.forProject(
			projectId,
			() -> {
				_unscheduleNanites();

				_asahTaskManager.removeAsahTasks();
			});
	}

	public void rescheduleNanites(String projectId) {
		ProjectIdThreadLocal.forProject(
			projectId,
			() -> {
				_unscheduleNanites();

				_scheduleNanites();
			});
	}

	@Scheduled(fixedDelay = DateUtil.MINUTE * 5)
	public void runDataControlNanite() {
		_asahTaskManager.runDataControlNaniteForAllProjects();
	}

	@Scheduled(fixedDelay = DateUtil.MINUTE * 5)
	public void runDataExportNanite() {
		_asahTaskManager.runNanitesForAllProjects("DataExportNanite");
	}

	@Scheduled(fixedDelay = DateUtil.MINUTE * 5)
	public void runDXPEntityNanite() {
		_asahTaskManager.runNanitesForAllProjects("DXPEntityNanite");
	}

	@Scheduled(fixedDelay = DateUtil.MINUTE * 5)
	public void runImmediateAsahTask() {
		for (Project project : _projectDog.getProjects()) {
			_boundedExecutor.runAsync(
				() -> {
					try {
						ProjectIdThreadLocal.setProjectId(project.getId());

						for (AsahTask asahTask :
								_asahTaskDog.getImmediateAsahTasks(0, 100)) {

							_asahTaskManager.executeAsahTask(asahTask, true);
						}
					}
					catch (Exception exception) {
						_log.error(
							"Unable to run immediate Asah task", exception);
					}
					finally {
						ProjectIdThreadLocal.remove();
					}
				},
				KeyReentrantLock.getReentrantLock(getClass(), project.getId()));
		}
	}

	@Scheduled(fixedDelay = DateUtil.HOUR * 3)
	public void runUpdateMembershipsNanite() {
		_asahTaskManager.runNanitesForAllProjects("UpdateMembershipsNanite");
	}

	private String _buildCronExpression(int second, int minute) {
		return String.format("%d %d 0 * * ?", second, minute);
	}

	@PreDestroy
	private void _destroy() {
		_boundedExecutor.shutdown();
	}

	private Runnable _getDeleteTempFilesRunnable() {
		return () -> _asahTaskManager.runNanites("DeleteTempFilesNanite");
	}

	private Runnable _getExperimentRunnable() {
		return () -> _asahTaskManager.runNanites("ExperimentNanite");
	}

	private void _init(String projectId) {
		try {
			ProjectIdThreadLocal.setProjectId(projectId);

			// TODO

			_runLogDog.resetRunLogs();

			_asahTaskManager.runNanites("DeleteTempFilesNanite");

			_asahTaskManager.scheduleAsahTasks();

			_scheduleNanites();
		}
		catch (Exception exception) {
			_log.error("Unable to schedule nanites", exception);
		}
		finally {
			ProjectIdThreadLocal.remove();
		}
	}

	private void _scheduleNanite(
		String cronExpression, Runnable runnable, String scheduledTaskId) {

		String projectId = ProjectIdThreadLocal.getProjectId();

		String scopedScheduledTaskId = String.format(
			"%s#%s", projectId, scheduledTaskId);

		String timeZoneId = _timeZoneDog.getTimeZoneId();

		_asahTaskScheduler.schedule(
			new CronTrigger(cronExpression, TimeZone.getTimeZone(timeZoneId)),
			() -> ProjectIdThreadLocal.forProject(projectId, runnable),
			scopedScheduledTaskId);

		if (_log.isDebugEnabled()) {
			_log.debug(
				String.format(
					"%s scheduled to run at %s (%s) for project %s",
					scheduledTaskId, cronExpression, timeZoneId, projectId));
		}

		_scheduledTasks.put(projectId, scopedScheduledTaskId);
	}

	private void _scheduleNanites() {
		_scheduleNanite(
			_buildCronExpression(
				RandomUtils.nextInt(0, 60), RandomUtils.nextInt(0, 16)),
			_getDeleteTempFilesRunnable(), "DeleteTempFilesNanite");
		_scheduleNanite(
			_buildCronExpression(0, 0), _getExperimentRunnable(),
			"ExperimentNanite");
	}

	private void _unscheduleNanites() {
		String projectId = ProjectIdThreadLocal.getProjectId();

		for (String scheduledTask : _scheduledTasks.get(projectId)) {
			if (_log.isDebugEnabled()) {
				_log.debug(scheduledTask + " unscheduled");
			}

			_asahTaskScheduler.unschedule(scheduledTask);
		}

		_scheduledTasks.remove(projectId);
	}

	private static final Log _log = LogFactory.getLog(
		OSBAsahBatchCuratorBot.class);

	@Autowired
	private AsahTaskDog _asahTaskDog;

	@Autowired
	private AsahTaskManager _asahTaskManager;

	@Autowired
	private AsahTaskScheduler _asahTaskScheduler;

	private final BoundedExecutor _boundedExecutor =
		BoundedExecutor.newBoundedExecutor(50, 40);

	@Autowired
	private ProjectDog _projectDog;

	@Autowired
	private RunLogDog _runLogDog;

	private final MultiValuedMap<String, String> _scheduledTasks =
		new HashSetValuedHashMap<>();

	@Autowired
	private TimeZoneDog _timeZoneDog;

}