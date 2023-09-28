/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.batch.curator.bot.nanite;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.batch.curator.bot.scheduling.AsahTaskRunnable;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.dog.AsahTaskDog;
import com.liferay.osb.asah.common.dog.AuditEventDog;
import com.liferay.osb.asah.common.dog.DataControlTaskDog;
import com.liferay.osb.asah.common.dog.RunLogDog;
import com.liferay.osb.asah.common.entity.DataControlTask;
import com.liferay.osb.asah.common.http.EmailHttp;
import com.liferay.osb.asah.common.model.DataControlTaskStatus;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;
import com.liferay.osb.asah.common.util.SetUtil;
import com.liferay.osb.asah.common.zip.ZipFileBuilder;

import java.io.File;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Matthew Kong
 */
@Component
public class DataControlNanite extends BaseNanite {

	@Override
	public boolean isLogRunEnabled() {
		return true;
	}

	@Override
	public void run(JSONObject contextJSONObject) {
		List<DataControlTask> pendingDataControlTasks =
			_dataControlTaskDog.getPrioritizedPendingDataControlTasks();

		for (DataControlTask pendingDataControlTask : pendingDataControlTasks) {
			_runDataControlTask(pendingDataControlTask);
		}

		List<DataControlTask> completedDataControlTasks =
			_dataControlTaskDog.getPrioritizedDataControlTasks(
				DateUtil.addDays(DateUtil.newDate(), -30),
				Arrays.asList(DataControlTaskStatus.COMPLETED.toString()),
				Arrays.asList(DataControlTask.Type.ACCESS));

		for (DataControlTask completedDataControlTask :
				completedDataControlTasks) {

			_expireDataControlTask(completedDataControlTask);
		}

		Set<DataControlTask.Type> types = SetUtil.map(
			pendingDataControlTasks, DataControlTask::getType);

		if (types.contains(DataControlTask.Type.SUPPRESS) ||
			types.contains(DataControlTask.Type.UNSUPPRESS)) {

			AsahTaskRunnable asahTaskRunnable = new AsahTaskRunnable(
				_asahTaskDog, ProjectIdThreadLocal.getProjectId(), _runLogDog,
				new Nanite[] {_updateMembershipsNanite});

			asahTaskRunnable.run();
		}
	}

	@Override
	protected Log getLog() {
		return LogFactory.getLog(DataControlNanite.class);
	}

	private void _expireDataControlTask(DataControlTask dataControlTask) {
		try {
			Path zipFilePath = Paths.get(
				_exportPathName, dataControlTask.getId() + ".zip");

			File file = zipFilePath.toFile();

			if (file.exists() && !file.delete()) {
				_log.error("Unable to delete file " + file.getAbsolutePath());
			}

			dataControlTask.setStatus(DataControlTaskStatus.EXPIRED.toString());

			_dataControlTaskDog.updateDataControlTask(dataControlTask);
		}
		catch (Exception exception) {
			_log.error(exception, exception);
		}
	}

	private void _exportDataControlTask(
			DataControlTask dataControlTask, ZipFileBuilder zipFileBuilder)
		throws Exception {

		zipFileBuilder.addToZip(
			"data-control-tasks.json",
			zipOutputStream -> {
				JSONObject dataControlTaskJSONObject =
					_objectMapper.convertValue(
						dataControlTask, JSONObject.class);

				String dataControlTaskJSON = dataControlTaskJSONObject.toString(
					2);

				zipOutputStream.write(
					dataControlTaskJSON.getBytes(StandardCharsets.UTF_8));
			});

		zipFileBuilder.build();
	}

	private void _runDataControlTask(DataControlTask dataControlTask) {
		try {
			_dataControlTaskDog.run(dataControlTask);

			DataControlTask.Type type = dataControlTask.getType();

			if (type != DataControlTask.Type.ACCESS) {
				_exportDataControlTask(
					dataControlTask,
					new ZipFileBuilder(
						_exportPathName + "/" + dataControlTask.getId() +
							".zip"));
			}

			_auditEventDog.addAuditEvent(
				String.format(
					"Request created for %s",
					dataControlTask.getEmailAddress()),
				type.getAuditEventType(), dataControlTask.getUserId(),
				dataControlTask.getUserName());

			_sendEmail(dataControlTask);
		}
		catch (Exception exception) {
			_log.error(exception, exception);
		}
	}

	private void _sendEmail(DataControlTask dataControlTask) {
		if (!_dataControlTaskDog.existsDataControlTask(
				dataControlTask.getBatchId(),
				Arrays.asList(
					DataControlTaskStatus.PENDING.toString(),
					DataControlTaskStatus.RUNNING.toString()))) {

			_emailHttp.sendEmail(
				_objectMapper.convertValue(dataControlTask, JSONObject.class));
		}
	}

	private static final Log _log = LogFactory.getLog(DataControlNanite.class);

	@Autowired
	private AsahTaskDog _asahTaskDog;

	@Autowired
	private AuditEventDog _auditEventDog;

	@Autowired
	private DataControlTaskDog _dataControlTaskDog;

	@Autowired
	private EmailHttp _emailHttp;

	@Value("${osb.asah.batch.curator.data.export.path:/export}")
	private String _exportPathName;

	@Autowired
	private ObjectMapper _objectMapper;

	@Autowired
	private RunLogDog _runLogDog;

	@Autowired
	private UpdateMembershipsNanite _updateMembershipsNanite;

}