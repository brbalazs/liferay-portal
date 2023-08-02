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

import com.google.cloud.bigquery.BigQuery;

import com.liferay.osb.asah.batch.curator.bot.nanite.data.exporter.BigQueryDataExporter;
import com.liferay.osb.asah.batch.curator.bot.nanite.data.exporter.DataExporter;
import com.liferay.osb.asah.batch.curator.bot.nanite.data.exporter.PostgreSQLDataExporter;
import com.liferay.osb.asah.common.dog.DataExportTaskDog;
import com.liferay.osb.asah.common.entity.DataExportTask;

import java.io.File;
import java.io.FileOutputStream;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
public class DataExportNanite extends BaseNanite {

	@Autowired
	public DataExportNanite(
		BigQuery bigQuery, DataExportTaskDog dataExportTaskDog,
		DSLContext dslContext) {

		_bigQuery = bigQuery;
		_dataExportTaskDog = dataExportTaskDog;
		_dslContext = dslContext;
	}

	@Override
	public boolean isLogRunEnabled() {
		return true;
	}

	@Override
	public void run(JSONObject contextJSONObject) throws Exception {
		List<DataExportTask> dataExportTasks =
			_dataExportTaskDog.getDataExportTasks(
				DataExportTask.Status.PENDING);

		dataExportTasks.forEach(this::_runDataExportTask);
	}

	@Override
	protected Log getLog() {
		return _log;
	}

	private void _runBigQueryDataExportTask(DataExportTask dataExportTask)
		throws Exception {

		DataExporter dataExporter = null;

		if (dataExportTask.getType() == DataExportTask.Type.EVENT) {
			dataExporter = new BigQueryDataExporter(
				_bigQuery, dataExportTask, "eventDate", _dslContext,
				_exportPath, "Event");
		}
		else if (dataExportTask.getType() == DataExportTask.Type.IDENTITY) {
			Condition condition = DSL.field(
				"individualId"
			).notIn(
				_dslContext.select(
					DSL.field("TO_HEX(SHA256(emailAddress))")
				).from(
					"Suppression"
				)
			);

			dataExporter = new BigQueryDataExporter(
				_bigQuery, Collections.singletonList(condition),
				dataExportTask, "createDate", _dslContext, _exportPath,
				Collections.emptyList(), "Identity");
		}
		else if (dataExportTask.getType() == DataExportTask.Type.INDIVIDUAL) {
			Condition condition = DSL.or(
				DSL.field(
					"suppressed"
				).isNull(),
				DSL.field(
					"suppressed", Boolean.class
				).eq(
					false
				));

			dataExporter = new BigQueryDataExporter(
				_bigQuery, Collections.singletonList(condition), dataExportTask,
				"createDate", _dslContext, _exportPath, Collections.emptyList(),
				"Individual");
		}
		else if (dataExportTask.getType() == DataExportTask.Type.MEMBERSHIP) {
			dataExporter = new BigQueryDataExporter(
				_bigQuery, dataExportTask, "createDate", _dslContext,
				_exportPath, "Membership");
		}
		else if (dataExportTask.getType() == DataExportTask.Type.PAGE) {
			dataExporter = new BigQueryDataExporter(
				_bigQuery, dataExportTask, "eventDate", _dslContext,
				_exportPath, "PageDaily");
		}
		else {
			throw new IllegalArgumentException(
				"Invalid data export task type: " + dataExportTask.getType());
		}

		dataExporter.export();
	}

	private void _runDataExportTask(DataExportTask dataExportTask) {
		_dataExportTaskDog.updateDataExportTask(
			dataExportTask.getId(), DataExportTask.Status.RUNNING);

		try {
			if (dataExportTask.getType() == DataExportTask.Type.SEGMENT) {
				_runSegmentDataExportTask(dataExportTask);
			}
			else {
				_runBigQueryDataExportTask(dataExportTask);
			}

			_dataExportTaskDog.updateDataExportTask(
				dataExportTask.getId(), DataExportTask.Status.COMPLETED);
		}
		catch (Exception exception) {
			_log.error(
				"Unable to run data export on task " + dataExportTask.getId(),
				exception);

			_dataExportTaskDog.updateDataExportTask(
				dataExportTask.getId(), DataExportTask.Status.ERROR);
		}
	}

	private void _runSegmentDataExportTask(DataExportTask dataExportTask)
		throws Exception {

		Path path = Paths.get(
			_exportPath,
			FilenameUtils.getName(dataExportTask.getId() + ".zip"));

		ZipOutputStream zipOutputStream = new ZipOutputStream(
			new FileOutputStream(path.toFile()));

		File file = new File("data.json");

		zipOutputStream.putNextEntry(new ZipEntry(file.getName()));

		DataExporter dataExporter = new PostgreSQLDataExporter(
			dataExportTask, "createDate", _dslContext, _jsonFactory,
			zipOutputStream, "segment");

		dataExporter.export();

		zipOutputStream.close();
	}

	private static final Log _log = LogFactory.getLog(DataExportNanite.class);

	private final BigQuery _bigQuery;
	private final DataExportTaskDog _dataExportTaskDog;
	private final DSLContext _dslContext;

	@Value("${osb.asah.batch.curator.data.export.path:/export}")
	private String _exportPath;

	private final JsonFactory _jsonFactory = new JsonFactory();

}