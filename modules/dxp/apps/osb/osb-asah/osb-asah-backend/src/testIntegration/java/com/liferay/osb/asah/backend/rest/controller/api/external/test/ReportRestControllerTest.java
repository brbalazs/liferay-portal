/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.api.external.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dto.DataExportTaskDTO;
import com.liferay.osb.asah.backend.rest.controller.api.external.ReportRestController;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.dog.DataExportTaskDog;
import com.liferay.osb.asah.common.entity.DataExportTask;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.spring.resource.ResourceUtil;
import com.liferay.osb.asah.test.util.annotation.SQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.skyscreamer.jsonassert.JSONAssert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author Marcellus Tavares
 */
public class ReportRestControllerTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@SQLResource(
		resourcePath = "osbasahfaroinfo/test_report_rest_controller_data_export_task_2.sql"
	)
	@Test
	public void testGetDataExportTaskFile() throws Exception {
		ClassPathResource classPathResource = new ClassPathResource(
			"dependencies", getClass());

		String originalExportPath = (String)ReflectionTestUtils.getField(
			_dataExportTaskDog, "_exportPath");

		ReflectionTestUtils.setField(
			_dataExportTaskDog, "_exportPath", classPathResource.getPath());

		try {
			ResponseEntity<FileSystemResource> responseEntity =
				_reportRestController.getDataExportTaskFile(
					DateUtil.toUTCString(_fromDate),
					DateUtil.toUTCString(_toDate), "page");

			Assertions.assertNotNull(responseEntity);

			JSONAssert.assertEquals(
				ResourceUtil.readResourceToJSONObject(
					"dependencies/osbasahfaroinfo/1003.json", this),
				JSONUtil.put(
					"result",
					"com.liferay.osb.asah.backend.rest.controller.api." +
						"external.test.ReportRestControllerTest::" +
							"testGetDataExportTaskFile"),
				false);
		}
		finally {
			ReflectionTestUtils.setField(
				_dataExportTaskDog, "_exportPath", originalExportPath);
		}
	}

	@Test
	public void testGetDataExportTaskFileWithNoFromDate() {
		Exception exception = Assertions.assertThrows(
			IllegalArgumentException.class,
			() -> _reportRestController.getDataExportTaskFile(
				null, DateUtil.toUTCString(DateUtil.newDayDate()), "page"));

		Assertions.assertEquals(
			"Date range is mandatory", exception.getMessage());
	}

	@Test
	public void testGetDataExportTaskFileWithNoPreviousTask() {
		Date date = DateUtil.newDayDate();

		Date toDate = DateUtil.addDays(date, -1);

		Date fromDate = DateUtil.addDays(toDate, -1);

		ResponseEntity<FileSystemResource> responseEntity =
			_reportRestController.getDataExportTaskFile(
				DateUtil.toUTCString(fromDate), DateUtil.toUTCString(toDate),
				"page");

		Assertions.assertNotNull(responseEntity);
		Assertions.assertEquals(
			HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}

	@Test
	public void testGetDataExportTaskFileWithNoToDate() {
		Exception exception = Assertions.assertThrows(
			IllegalArgumentException.class,
			() -> _reportRestController.getDataExportTaskFile(
				DateUtil.toUTCString(DateUtil.newDayDate()), null, "page"));

		Assertions.assertEquals(
			"Date range is mandatory", exception.getMessage());
	}

	@Test
	public void testGetDataExportTaskFileWithToDateLesserThanFromDate() {
		Exception exception = Assertions.assertThrows(
			IllegalArgumentException.class,
			() -> _reportRestController.getDataExportTaskFile(
				DateUtil.toUTCString(DateUtil.newDayDate()),
				DateUtil.toUTCString(
					DateUtil.addDays(DateUtil.newDayDate(), -1)),
				"page"));

		Assertions.assertEquals(
			"From date is after to date", exception.getMessage());
	}

	@Test
	public void testGetDataExportTaskWithNoFromDate() {
		Exception exception = Assertions.assertThrows(
			IllegalArgumentException.class,
			() -> _reportRestController.getDataExportTask(
				null, DateUtil.toUTCString(DateUtil.newDayDate()), "page"));

		Assertions.assertEquals(
			"Date range is mandatory", exception.getMessage());
	}

	@Test
	public void testGetDataExportTaskWithNoPreviousTask() {
		Date date = DateUtil.newDayDate();

		Date toDate = DateUtil.addDays(date, -1);

		Date fromDate = DateUtil.addDays(toDate, -1);

		ResponseEntity<DataExportTaskDTO> responseEntity =
			_reportRestController.getDataExportTask(
				DateUtil.toUTCString(fromDate), DateUtil.toUTCString(toDate),
				"page");

		Assertions.assertNotNull(responseEntity);

		_assertDataExportTaskDTO(
			null, date, responseEntity.getBody(), fromDate, null, null, null,
			DataExportTask.Status.PENDING, toDate, DataExportTask.Type.PAGE);
	}

	@Test
	public void testGetDataExportTaskWithNoToDate() {
		Exception exception = Assertions.assertThrows(
			IllegalArgumentException.class,
			() -> _reportRestController.getDataExportTask(
				DateUtil.toUTCString(DateUtil.newDayDate()), null, "page"));

		Assertions.assertEquals(
			"Date range is mandatory", exception.getMessage());
	}

	@Test
	public void testGetDataExportTaskWithToDateLesserThanFromDate() {
		Exception exception = Assertions.assertThrows(
			IllegalArgumentException.class,
			() -> _reportRestController.getDataExportTask(
				DateUtil.toUTCString(DateUtil.newDayDate()),
				DateUtil.toUTCString(
					DateUtil.addDays(DateUtil.newDayDate(), -1)),
				"page"));

		Assertions.assertEquals(
			"From date is after to date", exception.getMessage());
	}

	@SQLResource(
		resourcePath = "osbasahfaroinfo/test_report_rest_controller_data_export_task_1.sql"
	)
	@Test
	public void testNoPreviousExportProcessForTheSameTypeAndDateRange() {
		ResponseEntity<DataExportTaskDTO> responseEntity =
			_reportRestController.getDataExportTask(
				DateUtil.toUTCString(_fromDate), DateUtil.toUTCString(_toDate),
				"page");

		Assertions.assertNotNull(responseEntity);

		_assertDataExportTaskDTO(
			null, DateUtil.newDayDate(), responseEntity.getBody(), _fromDate,
			null, null, null, DataExportTask.Status.PENDING, _toDate,
			DataExportTask.Type.PAGE);
	}

	@SQLResource(
		resourcePath = "osbasahfaroinfo/test_report_rest_controller_data_export_task_2.sql"
	)
	@Test
	public void testThereIsPreviousExportProcessForTheSameTypeAndDateRangeAndIsCompleted() {
		ResponseEntity<DataExportTaskDTO> responseEntity =
			_reportRestController.getDataExportTask(
				DateUtil.toUTCString(_fromDate), DateUtil.toUTCString(_toDate),
				"page");

		Assertions.assertNotNull(responseEntity);

		_assertDataExportTaskDTO(
			DateUtil.newBeginningOfDayDate(new Date()), _createDate,
			responseEntity.getBody(), _fromDate, "1003", null, _startedDate,
			DataExportTask.Status.COMPLETED, _toDate, DataExportTask.Type.PAGE);
	}

	@SQLResource(
		resourcePath = "osbasahfaroinfo/test_report_rest_controller_data_export_task_3.sql"
	)
	@Test
	public void testThereIsPreviousExportProcessForTheSameTypeAndDateRangeButIsPending() {
		ResponseEntity<DataExportTaskDTO> responseEntity =
			_reportRestController.getDataExportTask(
				DateUtil.toUTCString(_fromDate), DateUtil.toUTCString(_toDate),
				"page");

		Assertions.assertNotNull(responseEntity);

		_assertDataExportTaskDTO(
			null, _createDate, responseEntity.getBody(), _fromDate, "1000",
			DataExportTask.Status.PENDING, null, DataExportTask.Status.PENDING,
			_toDate, DataExportTask.Type.PAGE);
	}

	@SQLResource(
		resourcePath = "osbasahfaroinfo/test_report_rest_controller_data_export_task_4.sql"
	)
	@Test
	public void testThereIsPreviousExportProcessForTheSameTypeAndDateRangeButIsRunning() {
		ResponseEntity<DataExportTaskDTO> responseEntity =
			_reportRestController.getDataExportTask(
				DateUtil.toUTCString(_fromDate), DateUtil.toUTCString(_toDate),
				"page");

		Assertions.assertNotNull(responseEntity);

		_assertDataExportTaskDTO(
			null, _createDate, responseEntity.getBody(), _fromDate, "1002",
			null, _startedDate, DataExportTask.Status.RUNNING, _toDate,
			DataExportTask.Type.PAGE);
	}

	@SQLResource(
		resourcePath = "osbasahfaroinfo/test_report_rest_controller_data_export_task_5.sql"
	)
	@Test
	public void testThereIsPreviousExportProcessForTheSameTypeAndDateRangeButResultedInError() {
		ResponseEntity<DataExportTaskDTO> responseEntity =
			_reportRestController.getDataExportTask(
				DateUtil.toUTCString(_fromDate), DateUtil.toUTCString(_toDate),
				"page");

		Assertions.assertNotNull(responseEntity);

		_assertDataExportTaskDTO(
			null, DateUtil.newDayDate(), responseEntity.getBody(), _fromDate,
			null, DataExportTask.Status.ERROR, null,
			DataExportTask.Status.PENDING, _toDate, DataExportTask.Type.PAGE);
	}

	private void _assertDataExportTaskDTO(
		Date completedDate, Date createDate,
		DataExportTaskDTO dataExportTaskDTO, Date fromDate, String id,
		DataExportTask.Status previousStatus, Date startedDate,
		DataExportTask.Status status, Date toDate, DataExportTask.Type type) {

		Assertions.assertNotNull(dataExportTaskDTO);

		if (completedDate == null) {
			Assertions.assertNull(dataExportTaskDTO.getCompletedDate());
		}
		else {
			_assertEqualsTruncatedDates(
				dataExportTaskDTO.getCompletedDate(), completedDate);
		}

		_assertEqualsTruncatedDates(
			dataExportTaskDTO.getCreateDate(), createDate);

		Assertions.assertEquals(fromDate, dataExportTaskDTO.getFromDate());

		if (id != null) {
			Assertions.assertEquals(id, dataExportTaskDTO.getId());
		}

		if (previousStatus == null) {
			Assertions.assertNull(dataExportTaskDTO.getPreviousStatus());
		}
		else {
			Assertions.assertEquals(
				previousStatus.toString(),
				dataExportTaskDTO.getPreviousStatus());
		}

		if (startedDate == null) {
			Assertions.assertNull(dataExportTaskDTO.getStartedDate());
		}
		else {
			_assertEqualsTruncatedDates(
				dataExportTaskDTO.getStartedDate(), startedDate);
		}

		Assertions.assertEquals(
			status.toString(), dataExportTaskDTO.getStatus());
		Assertions.assertEquals(toDate, dataExportTaskDTO.getToDate());
		Assertions.assertEquals(type.toString(), dataExportTaskDTO.getType());
	}

	private void _assertEqualsTruncatedDates(
		Date actualDate, Date expectedDate) {

		Assertions.assertEquals(
			DateUtil.newBeginningOfDayDate(expectedDate),
			DateUtil.newBeginningOfDayDate(actualDate));
	}

	private static final Date _createDate = DateUtil.toUTCDate(
		"2022-04-01T12:00:00.000Z");
	private static final Date _fromDate = DateUtil.toUTCDate(
		"2022-03-01T12:00:00.000Z");
	private static final Date _startedDate = DateUtil.toUTCDate(
		"2022-04-02T12:00:00.000Z");
	private static final Date _toDate = DateUtil.toUTCDate(
		"2022-03-31T12:00:00.000Z");

	@Autowired
	private DataExportTaskDog _dataExportTaskDog;

	@Autowired
	private ReportRestController _reportRestController;

}