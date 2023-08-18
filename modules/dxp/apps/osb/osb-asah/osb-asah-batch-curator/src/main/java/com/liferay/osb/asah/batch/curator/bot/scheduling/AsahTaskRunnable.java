/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.batch.curator.bot.scheduling;

import com.liferay.osb.asah.batch.curator.bot.nanite.Nanite;
import com.liferay.osb.asah.common.dog.AsahTaskDog;
import com.liferay.osb.asah.common.dog.RunLogDog;
import com.liferay.osb.asah.common.entity.AsahTask;
import com.liferay.osb.asah.common.entity.RunLog;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;
import com.liferay.osb.asah.common.wedeploy.data.WeDeployDataService;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONObject;

/**
 * @author Michael Bowerman
 * @author Leslie Wong
 * @author André Miranda
 */
public class AsahTaskRunnable implements Runnable {

	public AsahTaskRunnable(
		AsahTask asahTask, AsahTaskDog asahTaskDog, boolean force,
		Nanite nanite, RunLogDog runLogDog) {

		_asahTaskDog = asahTaskDog;
		_force = force;

		_asahTaskId = asahTask.getId();
		_contextJSONObject = asahTask.getContextJSONObject();
		_nanites = new Nanite[] {nanite};

		_projectId = asahTask.getProjectId();

		if (StringUtils.isBlank(_projectId)) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Defaulting to project ID " +
						ProjectIdThreadLocal.getProjectId());
			}

			_projectId = ProjectIdThreadLocal.getProjectId();
		}

		_runLogDog = runLogDog;
	}

	public AsahTaskRunnable(
		AsahTask asahTask, AsahTaskDog asahTaskDog, Nanite nanite,
		RunLogDog runLogDog) {

		this(asahTask, asahTaskDog, false, nanite, runLogDog);
	}

	public AsahTaskRunnable(
		AsahTaskDog asahTaskDog, String projectId, RunLogDog runLogDog,
		Nanite... nanites) {

		_asahTaskDog = asahTaskDog;
		_projectId = projectId;
		_runLogDog = runLogDog;
		_nanites = nanites;

		_contextJSONObject = null;
		_force = false;
		_asahTaskId = null;
	}

	public Long getAsahTaskId() {
		return _asahTaskId;
	}

	public String[] getNaniteClassNames() {
		Stream<Nanite> stream = Arrays.stream(_nanites);

		return stream.map(
			nanite -> {
				Class<?> clazz = nanite.getClass();

				return clazz.getSimpleName();
			}
		).toArray(
			String[]::new
		);
	}

	public String getProjectId() {
		return _projectId;
	}

	public boolean isForce() {
		return _force;
	}

	@Override
	public void run() {
		try {
			ProjectIdThreadLocal.forProject(_projectId, this::_run);
		}
		catch (Exception exception) {
			_log.error(
				"Unable to run nanites " +
					Arrays.toString(getNaniteClassNames()),
				exception);
		}
	}

	private boolean _checkNanite(String naniteClassName) {
		RunLog latestRunLog = _runLogDog.fetchLatestRunLog(
			null, naniteClassName, null,
			WeDeployDataService.OSB_ASAH_FARO_INFO);

		if ((latestRunLog != null) &&
			Objects.equals(latestRunLog.getStatus(), "STARTED")) {

			_log.error(
				"Nanite is already running: " +
					latestRunLog.getNaniteClassName());

			return true;
		}

		return false;
	}

	private void _deleteAsahTask() {
		if (_asahTaskId != null) {
			_asahTaskDog.deleteAsahTask(_asahTaskId);
		}
	}

	private void _run() {
		for (Nanite nanite : _nanites) {
			if (Objects.isNull(nanite)) {
				continue;
			}

			Class<?> clazz = nanite.getClass();

			String naniteClassName = clazz.getSimpleName();

			if (nanite.isLogRunEnabled() && _checkNanite(naniteClassName)) {
				continue;
			}

			long start = System.currentTimeMillis();

			try {
				nanite.logStart(_contextJSONObject);

				nanite.run(_contextJSONObject);

				nanite.logCompleted(
					String.valueOf(_asahTaskId), _contextJSONObject,
					System.currentTimeMillis() - start);
			}
			catch (Exception exception) {
				_log.error(
					"Unable to run nanite with class name " + naniteClassName,
					exception);

				nanite.logFailed(
					String.valueOf(_asahTaskId), _contextJSONObject,
					System.currentTimeMillis() - start, exception);
			}
		}

		_deleteAsahTask();
	}

	private static final Log _log = LogFactory.getLog(AsahTaskRunnable.class);

	private final AsahTaskDog _asahTaskDog;
	private final Long _asahTaskId;
	private final JSONObject _contextJSONObject;
	private final boolean _force;
	private final Nanite[] _nanites;
	private String _projectId;
	private final RunLogDog _runLogDog;

}