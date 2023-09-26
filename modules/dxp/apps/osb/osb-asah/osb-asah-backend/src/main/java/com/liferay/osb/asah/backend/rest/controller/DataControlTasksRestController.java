/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.common.antivirus.ClamAVScanner;
import com.liferay.osb.asah.common.dog.DataControlTaskDog;
import com.liferay.osb.asah.common.entity.DataControlTask;
import com.liferay.osb.asah.common.model.DataControlTaskStatus;
import com.liferay.osb.asah.common.util.CSVUtil;
import com.liferay.osb.asah.common.zip.ZipFileBuilder;

import java.io.File;
import java.io.FileInputStream;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Matthew Kong
 */
@RequestMapping("/data-control-tasks")
@RestController
public class DataControlTasksRestController extends BaseRestController {

	@GetMapping("/{id}")
	public ResponseEntity download(@PathVariable String id) {
		DataControlTask dataControlTask =
			_dataControlTaskDog.fetchDataControlTask(
				Long.valueOf(id), DataControlTaskStatus.COMPLETED.toString());

		if (dataControlTask == null) {
			return toNotFoundResponse();
		}

		File file = new File(
			_exportPath + "/" + dataControlTask.getId() + ".zip");

		if (!file.exists()) {
			return toNotFoundResponse();
		}

		return toDownloadResponse(
			file, _getExportFileName(dataControlTask, file));
	}

	@GetMapping
	public ResponseEntity downloadBatch(
			@RequestParam(required = false) Long batchId,
			@RequestParam(required = false) Long[] ids)
		throws Exception {

		File file = File.createTempFile(
			"data-control-export-", ".zip", new File(_tempPath));

		file.deleteOnExit();

		List<DataControlTask> dataControlTasks =
			_dataControlTaskDog.getPrioritizedDataControlTasks(
				batchId, null, ids, DataControlTaskStatus.COMPLETED.toString(),
				null);

		ZipFileBuilder zipFileBuilder = new ZipFileBuilder(file);

		for (DataControlTask dataControlTask : dataControlTasks) {
			File dataControlTaskFile = new File(
				_exportPath + "/" + dataControlTask.getId() + ".zip");

			if (!dataControlTaskFile.exists()) {
				continue;
			}

			zipFileBuilder.addToZip(
				_getExportFileName(dataControlTask, dataControlTaskFile),
				zipOutputStream -> {
					try (FileInputStream fileInputStream = new FileInputStream(
							dataControlTaskFile)) {

						byte[] buffer = new byte[1024];

						int length = fileInputStream.read(buffer);

						while (length > 0) {
							zipOutputStream.write(buffer, 0, length);

							length = fileInputStream.read(buffer);
						}
					}
				});
		}

		zipFileBuilder.build();

		return toDownloadResponse(file, file.getName());
	}

	@GetMapping("/logs")
	public ResponseEntity downloadLogs(
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam Date
				fromDate,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam Date
				toDate)
		throws Exception {

		List<DataControlTask> dataControlTasks =
			_dataControlTaskDog.getPrioritizedDataControlTasks(
				null, fromDate, null, null, toDate);

		Stream<DataControlTask> stream = dataControlTasks.stream();

		File file = CSVUtil.createCSVFile(
			_fieldNames, "data-control-task-logs-", new File(_tempPath),
			stream.map(
				dataControlTask -> _objectMapper.convertValue(
					dataControlTask, JSONObject.class)
			).collect(
				Collectors.toList()
			));

		return toDownloadResponse(file, "data-control-task-logs.csv");
	}

	@PostMapping
	public String upload(@RequestParam MultipartFile multipartFile)
		throws Exception {

		if (_clamAVScanner != null) {
			_clamAVScanner.scan(multipartFile.getInputStream());
		}

		File file = new File(
			_tempPath + "/data-control-task-" + System.currentTimeMillis() +
				".csv");

		file.deleteOnExit();

		multipartFile.transferTo(file);

		return file.getName();
	}

	private String _getExportFileName(
		DataControlTask dataControlTask, File file) {

		return dataControlTask.getEmailAddress() + "-" + file.getName();
	}

	private static final Map<String, String> _fieldNames =
		new LinkedHashMap<String, String>() {
			{
				put("batchId", "Request ID");
				put("completeDate", "Complete Date");
				put("createDate", "Request Date");
				put("emailAddress", "Email");
				put("startDate", "Process Date");
				put("status", "Request Status");
				put("type", "Request Type");
			}
		};

	@Autowired(required = false)
	private ClamAVScanner _clamAVScanner;

	@Autowired
	private DataControlTaskDog _dataControlTaskDog;

	@Value("${osb.asah.batch.curator.data.export.path:/export}")
	private String _exportPath;

	@Autowired
	private ObjectMapper _objectMapper;

	@Value("${osb.asah.backend.temp.path:/temp}")
	private String _tempPath;

}