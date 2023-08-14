/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.data.exporter;

import com.google.api.gax.paging.Page;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import com.liferay.osb.asah.common.entity.DataControlTask;
import com.liferay.osb.asah.common.entity.DataExportTask;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SelectSelectStep;
import org.jooq.impl.DSL;

/**
 * @author Marcellus Tavares
 */
public class BigQueryDataExporter implements DataExporter {

	public BigQueryDataExporter(
		BigQuery bigQuery, DataControlTask dataControlTask,
		DSLContext dslContext, String exportPath, List<String> tableNames) {

		_bigQuery = bigQuery;
		_dataControlTask = dataControlTask;
		_dslContext = dslContext;
		_exportPath = exportPath;
		_tableNames = tableNames;

		_bigQueryOptions = bigQuery.getOptions();

		StorageOptions storageOptions = StorageOptions.getDefaultInstance();

		_storage = storageOptions.getService();
	}

	public BigQueryDataExporter(
		BigQuery bigQuery, DataExportTask dataExportTask, String dateFieldName,
		DSLContext dslContext, String exportPath, String tableName) {

		this(
			bigQuery, Collections.emptyList(), dataExportTask, dateFieldName,
			dslContext, exportPath, Collections.emptyList(), tableName);
	}

	public BigQueryDataExporter(
		BigQuery bigQuery, List<Condition> conditions,
		DataExportTask dataExportTask, String dateFieldName,
		DSLContext dslContext, String exportPath,
		List<String> selectedFieldNames, String tableName) {

		_bigQuery = bigQuery;
		_conditions = conditions;
		_dataExportTask = dataExportTask;
		_dateFieldName = dateFieldName;
		_dslContext = dslContext;
		_exportPath = exportPath;
		_selectedFieldNames = selectedFieldNames;
		_tableName = tableName;

		_bigQueryOptions = bigQuery.getOptions();

		StorageOptions storageOptions = StorageOptions.getDefaultInstance();

		_storage = storageOptions.getService();
	}

	@Override
	public void export() throws Exception {
		if (_dataControlTask != null) {
			_exportDataControlTask();
		}
		else if (_dataExportTask != null) {
			_exportDataExportTask();
		}
	}

	private void _createDataControlZipFile(
			String exportBucket, String exportBucketFolder)
		throws Exception {

		Page<Blob> blobs = _storage.list(
			exportBucket, Storage.BlobListOption.prefix(exportBucketFolder));

		Path path = Paths.get(
			_exportPath,
			FilenameUtils.getName(_dataControlTask.getId() + ".zip"));

		File dataExportZipFile = path.toFile();

		ZipOutputStream zipOutputStream = new ZipOutputStream(
			new FileOutputStream(dataExportZipFile));

		for (Blob blob : blobs.iterateAll()) {
			String blobName = blob.getName();

			if (!blobName.endsWith(".csv")) {
				continue;
			}

			File file = new File(blobName);

			zipOutputStream.putNextEntry(new ZipEntry(file.getName()));

			try {
				byte[] bytes = blob.getContent();

				zipOutputStream.write(bytes, 0, bytes.length);
			}
			catch (IOException ioException) {
				_log.error(ioException.getMessage(), ioException);
			}
		}

		zipOutputStream.closeEntry();

		zipOutputStream.close();
	}

	private void _createDataExportZipFile(
			String exportBucket, String exportBucketFolder)
		throws Exception {

		Page<Blob> blobs = _storage.list(
			exportBucket, Storage.BlobListOption.prefix(exportBucketFolder));

		Path path = Paths.get(
			_exportPath,
			FilenameUtils.getName(_dataExportTask.getId() + ".zip"));

		File dataExportZipFile = path.toFile();

		ZipOutputStream zipOutputStream = new ZipOutputStream(
			new FileOutputStream(dataExportZipFile));

		File file = new File("data.json");

		zipOutputStream.putNextEntry(new ZipEntry(file.getName()));

		for (Blob blob : blobs.iterateAll()) {
			String blobName = blob.getName();

			if (!blobName.endsWith(".json")) {
				continue;
			}

			try {
				byte[] bytes = blob.getContent();

				zipOutputStream.write(bytes, 0, bytes.length);
			}
			catch (IOException ioException) {
				_log.error(ioException.getMessage(), ioException);
			}
		}

		zipOutputStream.closeEntry();

		zipOutputStream.close();
	}

	private void _exportDataControlTask() throws Exception {
		String exportBucket = StringUtils.replace(
			_DATA_EXPORTER_BUCKET_TEMPLATE, "{googleProjectId}",
			_bigQueryOptions.getProjectId());

		String exportBucketFolder =
			ProjectIdThreadLocal.getProjectId() + "/" +
				_dataControlTask.getId();

		for (String tableName : _tableNames) {
			_runBigQueryDataControlExportJob(
				exportBucket, exportBucketFolder, tableName);
		}

		_createDataControlZipFile(exportBucket, exportBucketFolder);
	}

	private void _exportDataExportTask() throws Exception {
		String exportBucket = StringUtils.replace(
			_DATA_EXPORTER_BUCKET_TEMPLATE, "{googleProjectId}",
			_bigQueryOptions.getProjectId());

		String exportBucketFolder =
			ProjectIdThreadLocal.getProjectId() + "/" + _dataExportTask.getId();

		_runBigQueryExportJob(exportBucket, exportBucketFolder);

		_createDataExportZipFile(exportBucket, exportBucketFolder);
	}

	private String _getBigQueryTableName(String tableName) {
		return "`" + _bigQueryOptions.getProjectId() + "." +
			ProjectIdThreadLocal.getProjectId() + "." +
				StringUtils.lowerCase(tableName + "`");
	}

	private List<Condition> _getConditions() {
		List<Condition> conditions = new ArrayList<>();

		if (_dataExportTask != null) {
			if (_dataExportTask.getFromDate() != null) {
				conditions.add(
					DSL.field(
						_dateFieldName, Date.class
					).greaterOrEqual(
						_dataExportTask.getFromDate()
					));
			}

			if (_dataExportTask.getToDate() != null) {
				conditions.add(
					DSL.field(
						_dateFieldName, Date.class
					).lessOrEqual(
						_dataExportTask.getToDate()
					));
			}
		}

		if (!_conditions.isEmpty()) {
			conditions.addAll(_conditions);
		}

		return conditions;
	}

	private SelectSelectStep<Record> _getSelectSelectStep() {
		if (_selectedFieldNames.isEmpty()) {
			return _dslContext.select();
		}

		List<Field> fields = new ArrayList<>();

		for (String selectedFieldName : _selectedFieldNames) {
			fields.add(DSL.field(selectedFieldName));
		}

		return _dslContext.select(fields);
	}

	private void _runBigQueryDataControlExportJob(
			String exportBucket, String exportBucketFolder, String tableName)
		throws Exception {

		String emailAddress = StringUtils.lowerCase(
			_dataControlTask.getEmailAddress());
		String query = null;

		if (tableName.equalsIgnoreCase("event")) {
			query = _dslContext.select(
			).from(
				_getBigQueryTableName("event")
			).where(
				DSL.field(
					"emailAddressHashed"
				).eq(
					DigestUtils.sha256Hex(emailAddress)
				)
			).toString();
		}
		else if (tableName.equalsIgnoreCase("expandovalue")) {
			query = _dslContext.select(
				DSL.field("user.emailAddress"), DSL.field("expandoValue.*")
			).from(
				DSL.table(
					_getBigQueryTableName("expandovalue")
				).as(
					"expandoValue"
				)
			).innerJoin(
				DSL.table(
					_getBigQueryTableName("user")
				).as(
					"user"
				)
			).on(
				DSL.field(
					"expandoValue.classPK"
				).eq(
					DSL.field("CAST(user.dxpUserId AS string)")
				)
			).where(
				DSL.field(
					"user.emailAddress"
				).eq(
					emailAddress
				)
			).toString();
		}
		else if (tableName.equalsIgnoreCase("user")) {
			query = String.join(
				"", "SELECT * EXCEPT(fields), (SELECT '{' || STRING_AGG(",
				"format('\"%s\": \"%s\"', name, value)) || '}' FROM UNNEST(",
				"fields)) AS fields FROM ", _getBigQueryTableName("user"),
				" WHERE emailAddress = '", emailAddress, "'");
		}

		QueryJobConfiguration queryJobConfiguration =
			QueryJobConfiguration.newBuilder(
				String.format(
					_EXPORT_DATA_CSV_QUERY_TEMPLATE, exportBucket,
					exportBucketFolder, tableName, query)
			).build();

		_bigQuery.query(queryJobConfiguration);
	}

	private void _runBigQueryExportJob(
			String exportBucket, String exportBucketFolder)
		throws Exception {

		SelectSelectStep<Record> selectSelectStep = _getSelectSelectStep();

		QueryJobConfiguration queryJobConfiguration =
			QueryJobConfiguration.newBuilder(
				String.format(
					_EXPORT_DATA_JSON_QUERY_TEMPLATE, exportBucket,
					exportBucketFolder,
					selectSelectStep.from(
						_getBigQueryTableName(_tableName)
					).where(
						_getConditions()
					))
			).build();

		_bigQuery.query(queryJobConfiguration);
	}

	private static final String _DATA_EXPORTER_BUCKET_TEMPLATE =
		"{googleProjectId}-data-exporter";

	private static final String _EXPORT_DATA_CSV_QUERY_TEMPLATE =
		"EXPORT DATA OPTIONS(field_delimiter=',', format='CSV', header=true, " +
			"overwrite=true, uri='gs://%s/%s/%s-*.csv') AS %s";

	private static final String _EXPORT_DATA_JSON_QUERY_TEMPLATE =
		"EXPORT DATA OPTIONS(format='JSON', overwrite=true, " +
			"uri='gs://%s/%s/*.json') AS %s";

	private static final Log _log = LogFactory.getLog(
		BigQueryDataExporter.class);

	private final BigQuery _bigQuery;
	private final BigQueryOptions _bigQueryOptions;
	private List<Condition> _conditions;
	private DataControlTask _dataControlTask;
	private DataExportTask _dataExportTask;
	private String _dateFieldName;
	private final DSLContext _dslContext;
	private final String _exportPath;
	private List<String> _selectedFieldNames;
	private final Storage _storage;
	private String _tableName;
	private List<String> _tableNames;

}