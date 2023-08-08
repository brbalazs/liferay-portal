/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.batch.curator.bot.scheduling;

import com.liferay.osb.asah.batch.curator.bot.nanite.Nanite;
import com.liferay.osb.asah.common.entity.AsahTask;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;

import java.util.Arrays;

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
		AsahTask asahTask, AsahTaskManager asahTaskManager) {

		this(asahTask, asahTaskManager, false);
	}

	public AsahTaskRunnable(
		AsahTask asahTask, AsahTaskManager asahTaskManager, boolean force) {

		_asahTaskManager = asahTaskManager;
		_force = force;

		_asahTaskId = asahTask.getId();
		_contextJSONObject = asahTask.getContextJSONObject();
		_naniteClassNames = new String[] {asahTask.getClassName()};

		_projectId = asahTask.getProjectId();

		if (StringUtils.isBlank(_projectId)) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Defaulting to project ID " +
						ProjectIdThreadLocal.getProjectId());
			}

			_projectId = ProjectIdThreadLocal.getProjectId();
		}
	}

	public AsahTaskRunnable(
		AsahTaskManager asahTaskManager, String projectId,
		String... naniteClassNames) {

		_asahTaskManager = asahTaskManager;
		_projectId = projectId;
		_naniteClassNames = naniteClassNames;

		_contextJSONObject = null;
		_force = false;
		_asahTaskId = null;
	}

	public Long getAsahTaskId() {
		return _asahTaskId;
	}

	public String[] getNaniteClassNames() {
		return Arrays.copyOf(_naniteClassNames, _naniteClassNames.length);
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
				"Unable to run nanites " + Arrays.toString(_naniteClassNames),
				exception);
		}
	}

	private void _deleteAsahTask() {
		if (_asahTaskId != null) {
			_asahTaskManager.deleteAsahTask(_asahTaskId);
		}
	}

	private void _run() {
		for (String naniteClassName : _naniteClassNames) {
			Nanite nanite = _asahTaskManager.getNanite(naniteClassName);

			if (nanite == null) {
				_log.error(
					"Unable to get nanite with class name " + naniteClassName);

				continue;
			}

			if (nanite.isLogRunEnabled() &&
				_asahTaskManager.checkNanite(naniteClassName)) {

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

	private final Long _asahTaskId;
	private final AsahTaskManager _asahTaskManager;
	private final JSONObject _contextJSONObject;
	private final boolean _force;
	private final String[] _naniteClassNames;
	private String _projectId;

}