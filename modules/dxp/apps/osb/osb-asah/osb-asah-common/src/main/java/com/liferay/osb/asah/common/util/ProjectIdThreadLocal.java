/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.util;

import com.liferay.osb.asah.common.dog.exception.InvalidProjectIdException;
import com.liferay.osb.asah.common.entity.Project;

import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.slf4j.MDC;

/**
 * @author Shinn Lok
 */
public class ProjectIdThreadLocal {

	public static void forProject(Project project, Runnable runnable) {
		forProjects(Collections.singletonList(project), runnable);
	}

	public static void forProject(String projectId, Runnable runnable) {
		forProject(new Project(projectId), runnable);
	}

	public static void forProjects(List<Project> projects, Runnable runnable) {
		for (Project project : projects) {
			try {
				setProjectId(project.getId());

				runnable.run();
			}
			finally {
				remove();
			}
		}
	}

	public static String getProjectId() {
		if ((_globalContext.get() != null) && _globalContext.get()) {
			return "global";
		}

		String projectId = _projectId.get();

		if (projectId == null) {
			throw new IllegalStateException("Project ID is not set");
		}

		if (_log.isDebugEnabled()) {
			_log.debug("getProjectId " + projectId);
		}

		return projectId;
	}

	public static void remove() {
		_globalContext.remove();
		_projectId.remove();

		MDC.remove("osbAsahProjectId");
	}

	public static void setGlobalContext(boolean globalContext) {
		if (_log.isDebugEnabled()) {
			_log.debug("Setting global context to: " + globalContext);
		}

		_globalContext.set(globalContext);

		if (globalContext) {
			MDC.put("osbAsahProjectId", "global");
		}
		else {
			MDC.put("osbAsahProjectId", _projectId.get());
		}
	}

	public static void setProject(Project project) {
		setProjectId(project.getId());
	}

	public static void setProjectId(String projectId) {
		if (_log.isDebugEnabled()) {
			_log.debug("setProjectId " + projectId);
		}

		if ((projectId == null) || !projectId.matches("^[0-9A-Za-z]+$")) {
			throw new InvalidProjectIdException(projectId);
		}

		_projectId.set(projectId);

		MDC.put("osbAsahProjectId", projectId);
	}

	private static final Log _log = LogFactory.getLog(
		ProjectIdThreadLocal.class);

	private static final ThreadLocal<Boolean> _globalContext =
		new ThreadLocal<>();
	private static final ThreadLocal<String> _projectId = new ThreadLocal<>();

}