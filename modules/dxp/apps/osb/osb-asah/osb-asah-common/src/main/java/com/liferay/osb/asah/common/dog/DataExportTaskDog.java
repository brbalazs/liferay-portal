/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.liferay.osb.asah.common.entity.DataExportTask;
import com.liferay.osb.asah.common.repository.DataExportTaskRepository;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahException;
import com.liferay.osb.asah.common.util.TimeOrderedUuidGenerator;

import java.io.File;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FilenameUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @author André Miranda
 * @author Marcellus Tavares
 */
@Component
public class DataExportTaskDog {

	public DataExportTask addDataExportTask(
		Date fromDate, Date toDate, DataExportTask.Type type) {

		DataExportTask dataExportTask = new DataExportTask();

		dataExportTask.setCreateDate(new Date());
		dataExportTask.setFromDate(fromDate);
		dataExportTask.setId(_timeOrderedUuidGenerator.generateIdAsLong());
		dataExportTask.setIsNew(Boolean.TRUE);
		dataExportTask.setStatus(DataExportTask.Status.PENDING);
		dataExportTask.setToDate(toDate);
		dataExportTask.setType(type);

		return _dataExportTaskRepository.save(dataExportTask);
	}

	public DataExportTask fetchLastDataExportTask(DataExportTask.Type type) {
		return _dataExportTaskRepository.findFirstByTypeOrderByIdDesc(type);
	}

	public DataExportTask fetchLastDataExportTaskByRange(
		Date fromDate, Date toDate, DataExportTask.Type type) {

		return _dataExportTaskRepository.
			findFirstByFromDateAndToDateAndTypeOrderByIdDesc(
				fromDate, toDate, type);
	}

	public DataExportTask getDataExportTask(Long dataExportTaskId) {
		Optional<DataExportTask> dataExportTaskOptional =
			_dataExportTaskRepository.findById(dataExportTaskId);

		return dataExportTaskOptional.orElseThrow(
			() -> new OSBAsahException(
				HttpStatus.BAD_REQUEST,
				"There is no data export task with ID " + dataExportTaskId));
	}

	public File getDataExportTaskFile(Long dataExportTaskId) {
		Path path = Paths.get(
			_exportPath, FilenameUtils.getName(dataExportTaskId + ".zip"));

		path = path.normalize();

		if (!path.startsWith(_exportPath)) {
			throw new OSBAsahException(
				HttpStatus.BAD_REQUEST,
				"Invalid file name: " + dataExportTaskId);
		}

		return path.toFile();
	}

	public List<DataExportTask> getDataExportTasks(
		DataExportTask.Status status) {

		return _dataExportTaskRepository.findByStatus(status);
	}

	public DataExportTask updateDataExportTask(
		Long dataExportTaskId, DataExportTask.Status status) {

		DataExportTask dataExportTask = getDataExportTask(dataExportTaskId);

		if (status == DataExportTask.Status.COMPLETED) {
			dataExportTask.setCompletedDate(new Date());
		}
		else if (status == DataExportTask.Status.RUNNING) {
			dataExportTask.setStartedDate(new Date());
		}

		dataExportTask.setStatus(status);

		return _dataExportTaskRepository.save(dataExportTask);
	}

	@Autowired
	private DataExportTaskRepository _dataExportTaskRepository;

	@Value("${osb.asah.batch.curator.data.export.path:/export}")
	private String _exportPath;

	private final TimeOrderedUuidGenerator _timeOrderedUuidGenerator =
		new TimeOrderedUuidGenerator();

}