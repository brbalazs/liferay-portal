/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.batch.curator.bot.scheduling;

import com.liferay.osb.asah.batch.curator.bot.nanite.Nanite;
import com.liferay.osb.asah.common.concurrent.BoundedExecutor;
import com.liferay.osb.asah.common.dog.AsahTaskDog;
import com.liferay.osb.asah.common.dog.ProjectDog;
import com.liferay.osb.asah.common.dog.RunLogDog;
import com.liferay.osb.asah.common.entity.AsahTask;
import com.liferay.osb.asah.common.entity.Project;
import com.liferay.osb.asah.common.entity.RunLog;
import com.liferay.osb.asah.common.lock.KeyReentrantLock;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;
import com.liferay.osb.asah.common.wedeploy.data.WeDeployDataService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

/**
 * @author André Miranda
 */
@Component
public class AsahTaskManager {

	public boolean checkNanite(String naniteClassName) {
		RunLog latestRunLog = _runLogDog.fetchLatestRunLog(
			null, naniteClassName, null,
			WeDeployDataService.OSB_ASAH_FARO_INFO);

		if ((latestRunLog != null) &&
			Objects.equals(latestRunLog.getStatus(), "STARTED")) {

			return true;
		}

		return false;
	}

	public void deleteAsahTask(Long asahTaskId) {
		_asahTaskDog.deleteAsahTask(asahTaskId);
	}

	public void executeAsahTask(AsahTask asahTask, boolean force) {
		if (Objects.equals(asahTask.getClassName(), "ClearChannelsNanite") ||
			Objects.equals(asahTask.getClassName(), "DeleteChannelsNanite")) {

			if (checkNanite("DataControlNanite")) {
				if (_log.isDebugEnabled()) {
					_log.debug("Pending running " + asahTask.getClassName());
				}

				return;
			}

			_boundedExecutor.runAsync(
				new AsahTaskRunnable(
					asahTask, _asahTaskDog, force,
					_nanitesMap.get(asahTask.getClassName()), _runLogDog));
		}
		else if (Objects.equals(asahTask.getClassName(), "DataControlNanite")) {
			if (checkNanite("ClearChannelsNanite") ||
				checkNanite("DeleteChannelsNanite")) {

				if (_log.isDebugEnabled()) {
					_log.debug("Pending running DataControlNanite");
				}

				return;
			}

			_boundedExecutor.runAsync(
				new AsahTaskRunnable(
					asahTask, _asahTaskDog, force,
					_nanitesMap.get(asahTask.getClassName()), _runLogDog));
		}
		else if (Objects.equals(
					asahTask.getClassName(), "UpdateMembershipsNanite")) {

			JSONObject contextJSONObject = asahTask.getContextJSONObject();

			if (contextJSONObject != null) {
				JSONObject individualJSONObject =
					contextJSONObject.optJSONObject("individualJSONObject");

				if (individualJSONObject != null) {
					_boundedExecutor.runAsync(
						new AsahTaskRunnable(
							asahTask, _asahTaskDog, false,
							_nanitesMap.get(asahTask.getClassName()),
							_runLogDog));

					return;
				}
			}

			if (_log.isDebugEnabled()) {
				_log.debug(
					String.format(
						"Pending update memberships nanite tasks: %d",
						_updateMembershipsNaniteBoundedExecutor.
							countPendingTasks()));
			}

			_updateMembershipsNaniteBoundedExecutor.runAsync(
				new AsahTaskRunnable(
					asahTask, _asahTaskDog, false,
					_nanitesMap.get(asahTask.getClassName()), _runLogDog),
				KeyReentrantLock.getReentrantLock(
					getClass(), asahTask.getProjectId()));
		}
		else {
			_boundedExecutor.runAsync(
				new AsahTaskRunnable(
					asahTask, _asahTaskDog, force,
					_nanitesMap.get(asahTask.getClassName()), _runLogDog));
		}
	}

	public void executeAsahTask(Long asahTaskId, boolean force) {
		executeAsahTask(_asahTaskDog.getAsahTask(asahTaskId), force);
	}

	public void executeAsahTasks() {
		try {
			int page = 0;

			while (true) {
				List<AsahTask> asahTasks = _asahTaskDog.getImmediateAsahTasks(
					page++, 100);

				if (asahTasks.isEmpty()) {
					break;
				}

				for (AsahTask asahTask : asahTasks) {
					executeAsahTask(asahTask, true);
				}
			}
		}
		catch (Exception exception) {
			_log.error("Unable to run existing tasks on startup", exception);
		}
	}

	public void executeAsahTasks(List<Long> asahTaskIds, boolean force) {
		for (Long asahTaskId : asahTaskIds) {
			executeAsahTask(_asahTaskDog.getAsahTask(asahTaskId), force);
		}
	}

	public Nanite getNanite(String className) {
		return _nanitesMap.get(className);
	}

	@PostConstruct
	public void init() {
		for (Nanite nanite : _nanites) {
			Class<?> clazz = nanite.getClass();

			_nanitesMap.put(ClassUtils.getShortName(clazz), nanite);
		}
	}

	public void removeAsahTasks() {
		try {
			List<AsahTask> asahTasks = _asahTaskDog.getScheduledAsahTasks();

			for (AsahTask asahTask : asahTasks) {
				unscheduleAsahTask(asahTask.getId());
			}
		}
		catch (Exception exception) {
			_log.error("Unable to unschedule existing tasks", exception);
		}

		_asahTaskDog.deleteAsahTasks();
	}

	public void runDataControlNaniteForAllProjects() {
		for (Project project : _projectDog.getProjects()) {
			try {
				ProjectIdThreadLocal.setProjectId(project.getId());

				if (checkNanite("ClearChannelsNanite") ||
					checkNanite("DeleteChannelsNanite")) {

					if (_log.isDebugEnabled()) {
						_log.debug(
							"Pending data control nanite for " +
								project.getId());
					}

					continue;
				}

				_boundedExecutor.runAsync(
					new AsahTaskRunnable(
						_asahTaskDog, project.getId(), _runLogDog,
						_nanitesMap.get("DataControlNanite")));
			}
			catch (Exception exception) {
				_log.error(
					"Unable to run data control nanite for " + project.getId(),
					exception);
			}
			finally {
				ProjectIdThreadLocal.remove();
			}
		}
	}

	public void runNanites(String... naniteClassNames) {
		Stream<String> stream = Arrays.stream(naniteClassNames);

		Nanite[] nanites = stream.map(
			naniteClassName -> _nanitesMap.get(naniteClassName)
		).filter(
			Objects::nonNull
		).toArray(
			Nanite[]::new
		);

		_boundedExecutor.runAsync(
			new AsahTaskRunnable(
				_asahTaskDog, ProjectIdThreadLocal.getProjectId(), _runLogDog,
				nanites));
	}

	public void runNanitesForAllProjects(String... naniteClassNames) {
		try {
			Stream<String> stream = Arrays.stream(naniteClassNames);

			Nanite[] nanites = stream.map(
				naniteClassName -> _nanitesMap.get(naniteClassName)
			).filter(
				Objects::nonNull
			).toArray(
				Nanite[]::new
			);

			List<Project> projects = _projectDog.getProjects();

			for (Project project : projects) {
				_boundedExecutor.runAsync(
					new AsahTaskRunnable(
						_asahTaskDog, project.getId(), _runLogDog, nanites));
			}
		}
		catch (Exception exception) {
			_log.error("Unable to run nanites for all projects", exception);
		}
	}

	public void scheduleAsahTask(AsahTask asahTask) {
		Nanite nanite = getNanite(asahTask.getClassName());

		if (nanite == null) {
			throw new IllegalArgumentException(
				"Unable to schedule nanite with class name " +
					asahTask.getClassName());
		}

		if (_log.isDebugEnabled()) {
			_log.debug(
				String.format(
					"Scheduling task %s according cron expression %s",
					asahTask.getId(), asahTask.getCronExpression()));
		}

		_asahTaskScheduler.schedule(
			asahTask.getCronExpression(),
			new AsahTaskRunnable(
				asahTask, _asahTaskDog,
				_nanitesMap.get(asahTask.getClassName()), _runLogDog),
			String.valueOf(asahTask.getId()));
	}

	public void scheduleAsahTask(Long asahTaskId) {
		scheduleAsahTask(_asahTaskDog.getAsahTask(asahTaskId));
	}

	public void scheduleAsahTasks() {
		try {
			List<AsahTask> asahTasks = _asahTaskDog.getScheduledAsahTasks();

			for (AsahTask asahTask : asahTasks) {
				scheduleAsahTask(asahTask);
			}
		}
		catch (Exception exception) {
			_log.error(
				"Unable to schedule existing tasks on startup", exception);
		}
	}

	public void unscheduleAsahTask(Long asahTaskId) {
		_asahTaskScheduler.unschedule(String.valueOf(asahTaskId));
	}

	@PreDestroy
	private void _destroy() {
		_boundedExecutor.shutdown();
		_updateMembershipsNaniteBoundedExecutor.shutdown();
	}

	private static final Log _log = LogFactory.getLog(AsahTaskManager.class);

	@Autowired
	private AsahTaskDog _asahTaskDog;

	@Autowired
	private AsahTaskScheduler _asahTaskScheduler;

	private final BoundedExecutor _boundedExecutor =
		BoundedExecutor.newBoundedExecutor(50, 40);

	@Autowired
	private List<Nanite> _nanites;

	private final Map<String, Nanite> _nanitesMap = new HashMap<>();

	@Autowired
	private ProjectDog _projectDog;

	@Autowired
	private RunLogDog _runLogDog;

	private final BoundedExecutor _updateMembershipsNaniteBoundedExecutor =
		BoundedExecutor.newBoundedExecutor(10000, 30);

}