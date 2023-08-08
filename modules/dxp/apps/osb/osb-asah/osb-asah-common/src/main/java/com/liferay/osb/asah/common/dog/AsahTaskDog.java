/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.liferay.osb.asah.common.entity.AsahTask;
import com.liferay.osb.asah.common.http.NanitesHttp;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.repository.AsahTaskRepository;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahException;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;

import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.IterableUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @author André Miranda
 */
@Component
public class AsahTaskDog {

	public void deleteAsahTask(Long asahTaskId) {
		_asahTaskRepository.deleteById(asahTaskId);
	}

	public void deleteAsahTasks() {
		_asahTaskRepository.deleteAll();
	}

	public AsahTask getAsahTask(Long asahTaskId) {
		Optional<AsahTask> asahTaskOptional = _asahTaskRepository.findById(
			asahTaskId);

		return asahTaskOptional.orElseThrow(
			() -> new OSBAsahException(
				HttpStatus.BAD_REQUEST,
				"There is no Asah task with ID " + asahTaskId));
	}

	public List<AsahTask> getAsahTasks() {
		return IterableUtils.toList(_asahTaskRepository.findAll());
	}

	public List<AsahTask> getAsahTasks(String className) {
		return _asahTaskRepository.findByClassName(className);
	}

	public List<AsahTask> getImmediateAsahTasks(int page, int size) {
		return _asahTaskRepository.findByCronExpressionIsNull(
			PageRequest.of(page, size));
	}

	public List<AsahTask> getScheduledAsahTasks() {
		return _asahTaskRepository.findByCronExpressionIsNotNull();
	}

	public AsahTask scheduleAsahTask(AsahTask asahTask) {
		asahTask = _asahTaskRepository.save(asahTask);

		if (asahTask.getCronExpression() != null) {
			_nanitesHttp.scheduleAsahTask(asahTask.getId());
		}

		return asahTask;
	}

	public void scheduleAsahTask(String className, JSONArray contextJSONArray) {
		try {
			_asahTaskRepository.saveAll(
				JSONUtil.toList(
					contextJSONArray,
					contextJSONObject -> new AsahTask(
						className, contextJSONObject,
						ProjectIdThreadLocal.getProjectId())));
		}
		catch (Exception exception) {
			throw new RuntimeException(
				"Unable to schedule Asah task", exception);
		}
	}

	public AsahTask scheduleAsahTask(
		String className, JSONObject contextJSONObject) {

		return scheduleAsahTask(
			new AsahTask(
				className, contextJSONObject,
				ProjectIdThreadLocal.getProjectId()));
	}

	public AsahTask scheduleAsahTask(
		String className, JSONObject contextJSONObject, String cronExpression) {

		return scheduleAsahTask(
			new AsahTask(
				className, contextJSONObject, cronExpression,
				ProjectIdThreadLocal.getProjectId()));
	}

	public void unscheduleAsahTask(Long asahTaskId) {
		_nanitesHttp.unscheduleAsahTask(asahTaskId);

		deleteAsahTask(asahTaskId);
	}

	@Autowired
	private AsahTaskRepository _asahTaskRepository;

	@Autowired
	private NanitesHttp _nanitesHttp;

}