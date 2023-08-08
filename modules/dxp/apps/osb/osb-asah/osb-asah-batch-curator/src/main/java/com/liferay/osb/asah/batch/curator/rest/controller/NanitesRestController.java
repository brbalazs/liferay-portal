/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.batch.curator.rest.controller;

import com.liferay.osb.asah.batch.curator.bot.OSBAsahBatchCuratorBot;
import com.liferay.osb.asah.batch.curator.bot.scheduling.AsahTaskManager;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;

import org.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Michael Bowerman
 */
@Profile("!test")
@RequestMapping("/nanites")
@RestController
public class NanitesRestController {

	@PostMapping("/remove-schedule")
	public void removeSchedule() {
		_osbAsahBatchCuratorBot.removeNanitesSchedule(
			ProjectIdThreadLocal.getProjectId());
	}

	@PostMapping("/reschedule")
	public void reschedule() {
		_osbAsahBatchCuratorBot.rescheduleNanites(
			ProjectIdThreadLocal.getProjectId());
	}

	@PostMapping("/run")
	public void run(@RequestBody String json) {
		_asahTaskManager.runNanites(
			JSONUtil.toStringArray(new JSONArray(json)));
	}

	@PostMapping("/schedule/{asahTaskId}")
	public void schedule(@PathVariable Long asahTaskId) {
		_asahTaskManager.scheduleAsahTask(asahTaskId);
	}

	@PostMapping("/unschedule/{asahTaskId}")
	public void unschedule(@PathVariable Long asahTaskId) {
		_asahTaskManager.unscheduleAsahTask(asahTaskId);
	}

	@Autowired
	private AsahTaskManager _asahTaskManager;

	@Autowired
	private OSBAsahBatchCuratorBot _osbAsahBatchCuratorBot;

}