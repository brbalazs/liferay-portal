/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog.test;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.dog.DataExportTaskDog;
import com.liferay.osb.asah.common.entity.DataExportTask;
import com.liferay.osb.asah.common.faro.info.dog.test.BaseFaroInfoDogTestCase;
import com.liferay.osb.asah.test.util.annotation.SQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.Date;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Alejo Ceballos
 */
public class DataExportTaskDogTest
	extends BaseFaroInfoDogTestCase
	implements OSBAsahTestExecutionListenersContext {

	@Test
	public void testAddDataExportTask() {
		Date todayDate = DateUtil.newDayDate();

		Date tomorrowDate = DateUtil.addDays(todayDate, 1);

		DataExportTask dataExportTask1 = _dataExportTaskDog.addDataExportTask(
			todayDate, tomorrowDate, DataExportTask.Type.PAGE);

		MatcherAssert.assertThat(
			dataExportTask1.getCreateDate(),
			Matchers.lessThanOrEqualTo(new Date()));
		Assertions.assertEquals(todayDate, dataExportTask1.getFromDate());
		Assertions.assertEquals(
			DataExportTask.Status.PENDING, dataExportTask1.getStatus());
		Assertions.assertEquals(tomorrowDate, dataExportTask1.getToDate());
		Assertions.assertEquals(
			DataExportTask.Type.PAGE, dataExportTask1.getType());

		DataExportTask dataExportTask2 = _dataExportTaskDog.getDataExportTask(
			dataExportTask1.getId());

		Assertions.assertNotNull(dataExportTask2);

		Assertions.assertEquals(dataExportTask1, dataExportTask2);
	}

	@SQLResource(resourcePath = "test_fetch_last_data_export_task_by_range.sql")
	@Test
	public void testFetchLastDataExportTaskByRange() {
		DataExportTask dataExportTask =
			_dataExportTaskDog.fetchLastDataExportTaskByRange(
				DateUtil.toUTCDate("2022-03-03T12:00:00.000Z"),
				DateUtil.toUTCDate("2022-04-02T12:00:00.000Z"),
				DataExportTask.Type.PAGE);

		Assertions.assertNotNull(dataExportTask);
		Assertions.assertEquals(1002, dataExportTask.getId());
	}

	@Autowired
	private DataExportTaskDog _dataExportTaskDog;

}