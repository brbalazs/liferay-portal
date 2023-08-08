/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.test;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.DataExportTask;
import com.liferay.osb.asah.common.repository.DataExportTaskRepository;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;

import java.util.Date;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Inácio Nery
 */
@Import(JDBCTestConfiguration.class)
public class DataExportTaskRepositoryTest
	extends BaseRepositoryTestCase<DataExportTask, Long> {

	@BeforeEach
	public void setUp() {
		DataExportTask dataExportTask1 = new DataExportTask();

		dataExportTask1.setCompletedDate(
			DateUtil.toUTCDate("2022-03-03T00:00:00.000Z"));
		dataExportTask1.setCreateDate(
			DateUtil.toUTCDate("2022-03-01T00:00:00.000Z"));
		dataExportTask1.setFromDate(
			DateUtil.toUTCDate("2022-02-01T12:00:00.000Z"));
		dataExportTask1.setStartedDate(
			DateUtil.toUTCDate("2022-03-02T12:00:00.000Z"));
		dataExportTask1.setStatus(DataExportTask.Status.COMPLETED);
		dataExportTask1.setToDate(
			DateUtil.toUTCDate("2022-02-28T12:00:00.000Z"));
		dataExportTask1.setType(DataExportTask.Type.PAGE);

		DataExportTask dataExportTask2 = new DataExportTask();

		dataExportTask2.setCompletedDate(
			DateUtil.toUTCDate("2022-04-03T00:00:00.000Z"));
		dataExportTask2.setCreateDate(
			DateUtil.toUTCDate("2022-04-01T00:00:00.000Z"));
		dataExportTask2.setFromDate(
			DateUtil.toUTCDate("2022-03-01T12:00:00.000Z"));
		dataExportTask2.setStartedDate(
			DateUtil.toUTCDate("2022-04-02T12:00:00.000Z"));
		dataExportTask2.setStatus(DataExportTask.Status.COMPLETED);
		dataExportTask2.setToDate(
			DateUtil.toUTCDate("2022-03-31T12:00:00.000Z"));
		dataExportTask2.setType(DataExportTask.Type.PAGE);

		DataExportTask dataExportTask3 = new DataExportTask();

		dataExportTask3.setCompletedDate(new Date());
		dataExportTask3.setCreateDate(new Date());
		dataExportTask3.setFromDate(new Date());
		dataExportTask3.setStartedDate(new Date());
		dataExportTask3.setStatus(DataExportTask.Status.COMPLETED);
		dataExportTask3.setToDate(new Date());
		dataExportTask3.setType(DataExportTask.Type.PAGE);

		DataExportTask dataExportTask4 = new DataExportTask();

		dataExportTask4.setCompletedDate(new Date());
		dataExportTask4.setCreateDate(new Date());
		dataExportTask4.setFromDate(new Date());
		dataExportTask4.setStartedDate(new Date());
		dataExportTask4.setStatus(DataExportTask.Status.ERROR);
		dataExportTask4.setToDate(new Date());
		dataExportTask4.setType(DataExportTask.Type.PAGE);

		setUpRepository(
			dataExportTask1, dataExportTask2, dataExportTask3, dataExportTask4);
	}

	@Test
	public void testFindByStatus() {
		List<DataExportTask> dataExportTasks =
			_dataExportTaskRepository.findByStatus(
				DataExportTask.Status.COMPLETED);

		Assertions.assertEquals(
			3, dataExportTasks.size(), dataExportTasks.toString());

		MatcherAssert.assertThat(
			dataExportTasks,
			Matchers.containsInAnyOrder(
				entityModels.get(0), entityModels.get(1), entityModels.get(2)));
	}

	@Test
	public void testFindFirstByFromDateAndToDateAndTypeOrderByIdDesc() {
		DataExportTask dataExportTask =
			_dataExportTaskRepository.
				findFirstByFromDateAndToDateAndTypeOrderByIdDesc(
					DateUtil.toUTCDate("2022-03-01T12:00:00.000Z"),
					DateUtil.toUTCDate("2022-03-31T12:00:00.000Z"),
					DataExportTask.Type.PAGE);

		Assertions.assertEquals(entityModels.get(1), dataExportTask);
	}

	@Test
	public void testFindFirstByTypeOrderByIdDesc() {
		DataExportTask dataExportTask =
			_dataExportTaskRepository.findFirstByTypeOrderByIdDesc(
				DataExportTask.Type.PAGE);

		Assertions.assertEquals(entityModels.get(3), dataExportTask);
	}

	@Override
	protected PagingAndSortingRepository<DataExportTask, Long>
		getPagingAndSortingRepository() {

		return _dataExportTaskRepository;
	}

	@Autowired
	private DataExportTaskRepository _dataExportTaskRepository;

}