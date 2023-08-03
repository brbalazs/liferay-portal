/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.asah.batch.curator.bot.nanite;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.batch.curator.bot.nanite.data.exporter.DXPEntityDataExporter;
import com.liferay.osb.asah.batch.curator.bot.nanite.data.exporter.DataExporter;
import com.liferay.osb.asah.batch.curator.bot.nanite.data.exporter.RawDataExporter;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.dog.AuditEventDog;
import com.liferay.osb.asah.common.dog.BQCSVUserDog;
import com.liferay.osb.asah.common.dog.BQUserDog;
import com.liferay.osb.asah.common.dog.DataControlTaskDog;
import com.liferay.osb.asah.common.dog.DataSourceDog;
import com.liferay.osb.asah.common.dog.SuppressionDog;
import com.liferay.osb.asah.common.entity.DataControlTask;
import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.http.EmailHttp;
import com.liferay.osb.asah.common.model.DataControlTaskStatus;
import com.liferay.osb.asah.common.model.Individual;
import com.liferay.osb.asah.common.zip.ZipFileBuilder;

import java.io.File;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;
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
	public void run(JSONObject contextJSONObject) throws Exception {
		List<DataControlTask> pendingDataControlTasks =
			_dataControlTaskDog.getDataControlTasks(
				null, Arrays.asList(DataControlTaskStatus.PENDING.toString()),
				null);

		Stream<DataControlTask> pendingDataControlTasksStream =
			pendingDataControlTasks.stream();

		pendingDataControlTasksStream.forEach(this::_runDataControlTask);

		List<DataControlTask> completedDataControlTasks =
			_dataControlTaskDog.getDataControlTasks(
				DateUtil.addDays(DateUtil.newDate(), -30),
				Arrays.asList(DataControlTaskStatus.COMPLETED.toString()),
				Arrays.asList(DataControlTask.Type.ACCESS.toString()));

		Stream<DataControlTask> completedDataControlTasksStream =
			completedDataControlTasks.stream();

		completedDataControlTasksStream.forEach(this::_expireDataControlTask);
	}

	@Override
	protected Log getLog() {
		return LogFactory.getLog(DataControlNanite.class);
	}

	private void _addSuppression(
		DataControlTask dataControlTask, String emailAddress) {

		emailAddress = StringUtils.lowerCase(emailAddress);

		if (_dataControlTaskDog.existsCompletedDataControlTask(
				emailAddress, DataControlTask.Type.SUPPRESS)) {

			return;
		}

		_suppressionDog.addSuppression(
			dataControlTask.getBatchId(), dataControlTask.getCreateDate(),
			emailAddress);
	}

	private void _deleteData(String emailAddress) {

		// TODO Fetch Individual by emailAddress

		Map<Long, List<String>> dataSourceIdUsersPKs =
			_getBQDataSourceIdUserPKs("CSV", new Individual());

		if (!dataSourceIdUsersPKs.isEmpty()) {
			for (Map.Entry<Long, List<String>> entry :
					dataSourceIdUsersPKs.entrySet()) {

				_bqCSVUserDog.deleteBQCSVUsers(
					entry.getKey(), entry.getValue());
			}
		}
	}

	private void _deleteSuppression(String emailAddress) {
		_suppressionDog.deleteByEmailAddress(emailAddress);
	}

	private void _expireDataControlTask(DataControlTask dataControlTask) {
		try {
			Path zipFilePath = Paths.get(
				_exportPathName, dataControlTask.getId() + ".zip");

			File file = zipFilePath.toFile();

			if (file.exists() && !file.delete()) {
				_log.error("Unable to delete file " + file.getAbsolutePath());
			}

			_updateDataControlTaskStatus(
				dataControlTask, DataControlTaskStatus.EXPIRED);
		}
		catch (Exception exception) {
			_log.error(exception, exception);
		}
	}

	private void _exportData(
			DataControlTask dataControlTask, String emailAddress)
		throws Exception {

		// TODO Implement Export Data

		ZipFileBuilder zipFileBuilder = new ZipFileBuilder(
			_exportPathName + "/" + dataControlTask.getId() + ".zip");

		zipFileBuilder.addToZip(
			"dxp_users.json",
			zipOutputStream -> _writeUsersToZip(
				"emailAddress", emailAddress, zipOutputStream));
		zipFileBuilder.addToZip(
			"individuals.json",
			zipOutputStream -> _writeToZip("individuals", zipOutputStream));

		// TODO Fetch Individual by emailAddress

		zipFileBuilder.addToZip(
			"csv-individuals.json",
			zipOutputStream -> _writeToZip("csv-individuals", zipOutputStream));
		zipFileBuilder.addToZip(
			"blogs.json",
			zipOutputStream -> _writeToZip("blogs", zipOutputStream));
		zipFileBuilder.addToZip(
			"document-libraries.json",
			zipOutputStream -> _writeToZip(
				"document-libraries", zipOutputStream));
		zipFileBuilder.addToZip(
			"forms.json",
			zipOutputStream -> _writeToZip("forms", zipOutputStream));
		zipFileBuilder.addToZip(
			"journals.json",
			zipOutputStream -> _writeToZip("journals", zipOutputStream));
		zipFileBuilder.addToZip(
			"page-referrers.json",
			zipOutputStream -> _writeToZip("page-referrers", zipOutputStream));
		zipFileBuilder.addToZip(
			"pages.json",
			zipOutputStream -> _writeToZip("pages", zipOutputStream));
		zipFileBuilder.addToZip(
			"user-sessions.json",
			zipOutputStream -> _writeToZip("user-sessions", zipOutputStream));

		_exportDataControlTask(dataControlTask, zipFileBuilder);
	}

	private void _exportDataControlTask(
			DataControlTask dataControlTask, ZipFileBuilder zipFileBuilder)
		throws Exception {

		zipFileBuilder.addToZip(
			"data-control-tasks.json",
			zipOutputStream -> {
				_updateDataControlTaskStatus(
					dataControlTask, DataControlTaskStatus.COMPLETED);

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

	private Map<Long, List<String>> _getBQDataSourceIdUserPKs(
		String dataSourceType, Individual individual) {

		Map<Long, List<String>> dataSourceIdUserPKs = new HashMap<>();

		for (Individual.DataSourceUserPK dataSourceUserPK :
				individual.getDataSourceUserPKs()) {

			DataSource dataSource = _dataSourceDog.getDataSource(
				dataSourceUserPK.getDataSourceId());

			if (!StringUtils.equals(
					dataSource.getProviderType(), dataSourceType)) {

				continue;
			}

			List<String> userPKs = dataSourceIdUserPKs.computeIfAbsent(
				dataSource.getId(), id -> new ArrayList<>());

			userPKs.addAll(dataSourceUserPK.getUserPKs());
		}

		return dataSourceIdUserPKs;
	}

	private void _runDataControlTask(DataControlTask dataControlTask) {
		try {
			_updateDataControlTaskStatus(
				dataControlTask, DataControlTaskStatus.RUNNING);

			String emailAddress = dataControlTask.getEmailAddress();
			DataControlTask.Type type = dataControlTask.getType();

			try {
				if (type == DataControlTask.Type.ACCESS) {
					_exportData(dataControlTask, emailAddress);
				}
				else if (type == DataControlTask.Type.DELETE) {
					_deleteData(emailAddress);
				}
				else if (type == DataControlTask.Type.SUPPRESS) {
					_addSuppression(dataControlTask, emailAddress);
				}
				else if (type == DataControlTask.Type.UNSUPPRESS) {
					_deleteSuppression(emailAddress);
				}

				if (type == DataControlTask.Type.ACCESS) {
					_exportDataControlTask(
						dataControlTask,
						new ZipFileBuilder(
							_exportPathName + "/" + dataControlTask.getId() +
								".zip"));
				}
			}
			catch (Exception exception) {
				_log.error(exception, exception);

				_updateDataControlTaskStatus(
					dataControlTask, DataControlTaskStatus.ERROR);
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

	private void _updateDataControlTaskStatus(
		DataControlTask dataControlTask,
		DataControlTaskStatus dataControlTaskStatus) {

		if (dataControlTaskStatus == DataControlTaskStatus.COMPLETED) {
			dataControlTask.setCompleteDate(DateUtil.newDate());
		}
		else if (dataControlTaskStatus == DataControlTaskStatus.RUNNING) {
			dataControlTask.setStartDate(DateUtil.newDate());
		}

		dataControlTask.setStatus(dataControlTaskStatus.toString());

		_dataControlTaskDog.updateDataControlTask(dataControlTask);
	}

	private void _writeToZip(
			String collectionName, ZipOutputStream zipOutputStream)
		throws Exception {

		DataExporter dataExporter = new RawDataExporter(
			collectionName, _jsonFactory, zipOutputStream);

		dataExporter.export();
	}

	private void _writeUsersToZip(
			String fieldName, String fieldValue,
			ZipOutputStream zipOutputStream)
		throws Exception {

		DataExporter dataExporter = new DXPEntityDataExporter(
			_bqUserDog, fieldName, fieldValue, _jsonFactory, _objectMapper,
			zipOutputStream);

		dataExporter.export();
	}

	private static final Log _log = LogFactory.getLog(DataControlNanite.class);

	@Autowired
	private AuditEventDog _auditEventDog;

	@Autowired
	private BQCSVUserDog _bqCSVUserDog;

	@Autowired
	private BQUserDog _bqUserDog;

	@Autowired
	private DataControlTaskDog _dataControlTaskDog;

	@Autowired
	private DataSourceDog _dataSourceDog;

	@Autowired
	private EmailHttp _emailHttp;

	@Value("${osb.asah.batch.curator.data.export.path:/export}")
	private String _exportPathName;

	private final JsonFactory _jsonFactory = new JsonFactory() {
		{
			disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
		}
	};

	@Autowired
	private ObjectMapper _objectMapper;

	@Autowired
	private SuppressionDog _suppressionDog;

}